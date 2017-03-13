package com.monolith52.logic
import org.apache.commons.io.IOUtils
import com.monolith52.util.ResourceUtil
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam.MODE_EXPLICIT
import javax.imageio.plugins.jpeg.JPEGImageWriteParam

class Adjuster {
    val writeFormatName = "jpg"
    val extensions = arrayOf(".png", ".PNG", ".jpg", ".JPG", ".jpeg", ".JPEG")
    val pngExtensions = arrayOf(".png", ".PNG")
    val defaultQuerity = 0.5f

    @Throws(IOException::class)
    fun adjust(filename: String, `input`: InputStream, output: OutputStream) {
        if (isTargetImage(filename)) {
            val image = ImageIO.read(input)
            val writer = ImageIO.getImageWritersByFormatName(writeFormatName).next()

            val jiparam = JPEGImageWriteParam(Locale.getDefault())
            jiparam.compressionMode = MODE_EXPLICIT
            jiparam.compressionQuality = ResourceUtil.bundle.getString("adjust.compressrate")?.toFloat() ?: defaultQuerity

            writer.output = ImageIO.createImageOutputStream(output)
            writer.write(null, IIOImage(image, null, null), jiparam)
        } else {
            IOUtils.copy(input, output)
        }
    }

    fun isTargetImage(filename: String): Boolean {
        return extensions.any({ filename.endsWith(it) })
    }

    fun getTargetFilename(filename: String): String {
        val ext = "." + writeFormatName
        if (pngExtensions.any({ filename.endsWith(it) })) {
            return filename.replace(Regex("\\.[^.]+$"), ext)
        }
        return filename
    }
}