package com.monolith52.logic

import com.monolith52.util.ResourceUtil
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.io.IOUtils
import java.io.*
import java.io.File


class ZipAdjuster (val inputFile: File) {

    var adjuster = Adjuster()
    var errorHandler: ((inputFile: File, msg: String) -> Unit)? = null
    var successHandler: ((inputFile: File, outputFilesize: Long) -> Unit)? = null
    var progressHandler: ((inputFile: File, progress: Progress) -> Unit)? = null
    data class Progress(var length: Int = 0, var current: Int = 0) {
        override fun toString(): String {
            if (length == 0) return "0%"
            return String.format("%.2f%% (%d/%d)", current.toDouble() * 100.0 / length.toDouble(), current, length)
        }
    }

    @Throws(IOException::class)
    private fun scan(input: InputStream): Int {
        var count = 0
        ZipArchiveInputStream(input).use{ zipIn ->
            while (zipIn.getNextZipEntry() != null) count++
            return count
        }
    }

    fun process(): Boolean {
        return process(getOutputFile())
    }

    fun process(outputFile: File): Boolean {
        var success = true
        try {
            FileOutputStream(outputFile).use { output ->
                success = process(output)

                val inputFilesize = inputFile.length()
                val outputFilesize = outputFile.length()

                if (ResourceUtil.getBoolean("rename.effectiveOriginalFile", false)) {
                    if (inputFilesize > outputFilesize) {
                        val renameFilename = inputFile.absoluteFile.parent + File.separator + ResourceUtil.getString("rename.prefix", "") + inputFile.name
                        inputFile.renameTo(File(renameFilename))
                        println("original file renamed to ${renameFilename}")
                    }
                }

                successHandler?.invoke(inputFile, outputFilesize)
                println("archive complete")
            }
        } catch (e: IOException) {
            errorHandler?.invoke(inputFile, e.toString())
            success = false
        }

        if (!success && ResourceUtil.getBoolean("remove.incompleteFile", true)) {
            outputFile.delete()
        }

        return success
    }

    private fun process(output: OutputStream): Boolean {
        if (!inputFile.exists()) {
            val e = IOException("入力ファイルがみつかりません。")
            errorHandler?.invoke(inputFile, e.toString())
            return false
        }

        try {
            val progress = Progress()
            FileInputStream(inputFile).use { scanInput ->
                progress.length = scan(scanInput)
            }

                ZipArchiveInputStream(FileInputStream(inputFile)).use { zipIn ->
                    ZipArchiveOutputStream(output).use { zipOut ->

                        while (true) {
                            val entry = zipIn.nextZipEntry
                            if (entry == null) break
                            progress.current += 1

                            val entryName = adjuster.getTargetFilename(entry.name)
                            val newEntry = ZipArchiveEntry(entryName)

                            zipOut.putArchiveEntry(newEntry)
                            if (!entry.isDirectory) adjuster.adjust(entryName, zipIn, zipOut)
                            zipOut.closeArchiveEntry()

                            progressHandler?.invoke(inputFile, progress)
                            println("archive: " + entry.name)
                        }
                        zipIn.close()
                        zipOut.close()
                    }
                }
        } catch (e: IOException) {
            errorHandler?.invoke(inputFile, e.toString())
            return false
        } catch (e: RuntimeException) {
            errorHandler?.invoke(inputFile, e.toString())
            return false
        }

        return true
    }

    fun getOutputFile(): File {
        val prefix = ResourceUtil.getString("savefile.prefix", "")
        val filename = inputFile.absoluteFile.parent + File.separator + prefix + inputFile.name
        return File(filename)
    }
}

