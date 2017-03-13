package com.monolith52.component

import javafx.beans.property.*
import java.io.File


class FileTableRecord (argFile: File,
                       argOriginalSize: Long,
                       argCompressedSize: Long,
                       argProgress: Double) {

    private val error: StringProperty
    private val progress: DoubleProperty
    private val compressedSize: LongProperty
    private val originalSize: LongProperty
    private val file: ObjectProperty<File>

    init {
        file            = SimpleObjectProperty<File>(this, "file", argFile)
        originalSize    = SimpleLongProperty(this, "originalSize", argOriginalSize)
        compressedSize  = SimpleLongProperty(this, "compressedSize", argCompressedSize)
        progress        = SimpleDoubleProperty(this, "progress", argProgress)
        error           = SimpleStringProperty(this, "error", null)
    }

    fun setFile(file: File) {
        this.file.set(file)
    }

    fun getFile(): File {
        return this.file.get()
    }

    fun fileProperty(): ObjectProperty<File> {
        return file
    }

    fun setOriginalSize(filesize: Long?) {
        this.originalSize.set(filesize!!)
    }

    fun getOriginalSize(): Long? {
        return this.originalSize.get()
    }

    fun originalSizeProperty(): LongProperty {
        return originalSize
    }

    fun setCompressedSize(filesize: Long?) {
        this.compressedSize.set(filesize!!)
    }

    fun getCompressedSize(): Long? {
        return this.compressedSize.get()
    }

    fun compressedSizeProperty(): LongProperty {
        return compressedSize
    }


    fun setProgress(progress: Double?) {
        this.progress.set(progress!!)
    }

    fun getProgress(): Double? {
        return this.progress.get()
    }

    fun progressProperty(): DoubleProperty {
        return progress
    }

    fun setError(error: String) {
        this.error.set(error)
    }

    fun getError(): String? {
        return error.get()
    }

    fun errorProperty(): StringProperty {
        return error
    }
}