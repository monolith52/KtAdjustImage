package com.monolith52.logic

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.junit.rules.ExternalResource
import java.io.File
import java.io.FileOutputStream

class TestArchiveResource : ExternalResource() {
    fun copy(fromname: String, toname: String) {
        FileOutputStream(toname).use { output ->
            javaClass.getResource(fromname).openStream().use { input ->
                IOUtils.copy(input, output)
            }
        }
    }

    override fun before() {
        copy("test.org.zip", "test.zip")
        copy("test.org.zip", "test1.zip")
        copy("test.org.zip", "test2.zip")
        copy("test.org.zip", "test3.zip")
    }

    override fun after() {
        val garbage = listOf("test.zip", "test1.zip", "test2.zip", "test3.zip", "out2.zip",
                "!!AdjustImage!!test1.zip",
                "!!AdjustImage!!test2.zip",
                "!!AdjustImage!!test3.zip",
                "__AdjustOrigin__test1.zip",
                "__AdjustOrigin__test2.zip",
                "__AdjustOrigin__test3.zip",
                "!!AdjustImage!!out2.zip")
        garbage.forEach { File(it).delete() }
    }
}
