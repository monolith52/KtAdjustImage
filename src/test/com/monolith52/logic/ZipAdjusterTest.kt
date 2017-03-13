package com.monolith52.logic

import org.apache.commons.io.FileUtils
import org.junit.*

import org.junit.Assert.*
import java.io.File
import java.io.FileOutputStream

class ZipAdjusterTest {

    @Rule @JvmField val resource = TestArchiveResource()

    @Test
    fun process() {
        val adjuster = ZipAdjuster(File("test1.zip"))
        adjuster.process()
        assert(File("!!AdjustImage!!test1.zip").exists())
        assert(File("!!AdjustImage!!test1.zip").length() < File("test.zip").length())
        assert(File("__AdjustOrigin__test1.zip").exists())
    }

    @Test
    fun process1() {
        val adjuster = ZipAdjuster(File("test2.zip"))
        val outputFile = File("out2.zip")
        adjuster.process(outputFile)
        assert(File("out2.zip").exists())
        assert(File("out2.zip").length() < File("test.zip").length())
        assert(File("__AdjustOrigin__test2.zip").exists())
    }

    @Test
    fun process2() {
        val adjuster = ZipAdjuster(File("test3.zip"))
        var progress: ZipAdjuster.Progress? = null
        adjuster.progressHandler = { inputFile, p ->
            progress = p
        }
        var outputSize: Long? = null
        adjuster.successHandler = { inputFile, size ->
            outputSize = size
        }
        FileOutputStream(adjuster.getOutputFile()).use { out ->
            adjuster.process(out)
            assert(File("!!AdjustImage!!test3.zip").exists())
            assert(File("!!AdjustImage!!test3.zip").length() < File("test.zip").length())
            assert(File("__AdjustOrigin__test3.zip").exists())
        }
        assert(progress != null)
        assertEquals(progress?.current, progress?.length)
        assertEquals(File("!!AdjustImage!!test3.zip").length(), outputSize)

        val adjuster2 = ZipAdjuster(File("none.zip"))
        var errMsg: String? = null
        adjuster2.errorHandler = { inputFile, msg ->
            errMsg = msg
        }

        assertFalse(adjuster2.process())
        assert(errMsg != null)
        assertFalse(File("!!AdjustImage!!none.zip").exists())
    }

    @Test
    fun getOutputFile() {
        assertEquals(File("!!AdjustImage!!test.zip").absoluteFile, ZipAdjuster(File("test.zip")).getOutputFile())
        assertEquals(File("C:\\!!AdjustImage!!test.zip").absoluteFile, ZipAdjuster(File("C:\\test.zip")).getOutputFile())
        assertEquals(File("!!AdjustImage!!test").absoluteFile, ZipAdjuster(File("test")).getOutputFile())
    }
}