package com.monolith52.logic

import com.monolith52.logic.Adjuster
import org.junit.Test

import org.junit.Assert.*
import java.io.*

class AdjusterTest {
    val adjuster = Adjuster()

    @Test
    fun adjust() {
        assertEquals(sizeof("test.txt"), getAdjustedByteArray("test.txt").size)
        assertNotEquals(sizeof("test.jpg"), getAdjustedByteArray("test.jpg").size)
        assertNotEquals(sizeof("test.jpeg"), getAdjustedByteArray("test.jpeg").size)
        assertNotEquals(sizeof("test.png"), getAdjustedByteArray("test.png").size)
    }

    fun getAdjustedByteArray(filename: String): ByteArray {
        val output = ByteArrayOutputStream()
        val input = javaClass.getResource(filename).openStream()
        adjuster.adjust(filename, input, output)
        return output.toByteArray()
    }

    fun sizeof(filename: String): Int {
        return javaClass.getResource(filename).openConnection().contentLength
    }

    @Test
    fun isTargetImage() {
        assert(adjuster.isTargetImage("test.png"))
        assert(adjuster.isTargetImage("test.PNG"))
        assert(adjuster.isTargetImage("test.jpg"))
        assert(adjuster.isTargetImage("test.JPG"))
        assert(adjuster.isTargetImage("test.jpeg"))
        assert(adjuster.isTargetImage("test.JPEG"))
        assertFalse(adjuster.isTargetImage("png"))
        assertFalse(adjuster.isTargetImage("test.zip"))
        assertFalse(adjuster.isTargetImage("test"))

        assert(adjuster.isTargetImage("C:\\test.png"))
        assertFalse(adjuster.isTargetImage("C:\\test.zip"))

        assert(adjuster.isTargetImage("/dir/test.png"))
        assertFalse(adjuster.isTargetImage("/dir/test.zip"))
    }

    @Test
    fun getTargetFilename() {
        assertEquals("test.jpg", adjuster.getTargetFilename("test.png"))
        assertEquals("test.jpg", adjuster.getTargetFilename("test.jpg"))
        assertEquals("test.jpeg", adjuster.getTargetFilename("test.jpeg"))
        assertEquals("test.jar", adjuster.getTargetFilename("test.jar"))
        assertEquals("png", adjuster.getTargetFilename("png"))
        assertEquals("jpg", adjuster.getTargetFilename("jpg"))
        assertEquals("test", adjuster.getTargetFilename("test"))
    }

}