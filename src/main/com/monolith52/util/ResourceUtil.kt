package com.monolith52.util

import java.io.IOException
import java.util.*

class ResourceUtil() {

    object control : ResourceBundle.Control() {
        val format = "xml"
        val props = Properties()

        override fun getFormats(basename: String?): MutableList<String> {
            return mutableListOf(format)
        }

        override fun newBundle(basename: String?, locale: Locale?, format: String?, loader: ClassLoader?, reload: Boolean): ResourceBundle? {

            try {
                val url = loader?.getResource(toResourceName(basename, format))
                props.loadFromXML(url?.openStream())
            } catch (e: IOException) {
                return null
            }

            return object : ResourceBundle() {
                override fun handleGetObject(key: String): Any {
                    return props.getProperty(key)
                }

                override fun getKeys(): Enumeration<String> {
                    return props.propertyNames() as Enumeration<String>
                }

            }
        }
    }

    companion object {
        val target = "Message"
        val bundle = ResourceBundle.getBundle(target, Locale.getDefault(), control) ?:
                ResourceBundle.getBundle(target, null, control)
    }
}