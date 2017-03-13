package com.monolith52.util

import java.io.IOException
import java.util.*

class ResourceUtil() {

    object control : ResourceBundle.Control() {
        val format = "properties"
        val props = Properties()

        override fun getFormats(basename: String?): MutableList<String> {
            return mutableListOf(format)
        }

        override fun newBundle(basename: String?, locale: Locale?, format: String?, loader: ClassLoader?, reload: Boolean): ResourceBundle? {

            try {
                val url = loader?.getResource(toResourceName(basename, format))
                props.load(url?.openStream())
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
        fun getString(key: String): String? {return bundle.getString(key)}
        fun getString(key: String, value: String): String {return getString(key) ?: value}
        fun getFloat(key: String): Float? {return bundle.getString(key)?.toFloat()}
        fun getFloat(key: String, value: Float): Float {return getFloat(key) ?: value}
        fun getBoolean(key: String): Boolean? {return bundle.getString(key)?.toBoolean()}
        fun getBoolean(key: String, value: Boolean): Boolean {return getBoolean(key) ?: value}
    }
}