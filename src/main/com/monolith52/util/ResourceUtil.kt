package com.monolith52.util

import java.io.IOException
import java.util.*

class ResourceUtil() {

    companion object {
        val target = "Message"
        val bundle = ResourceBundle.getBundle(target, Locale.getDefault()) ?:
                ResourceBundle.getBundle(target)
        fun getString(key: String): String? {return bundle.getString(key)}
        fun getString(key: String, value: String): String {return getString(key) ?: value}
        fun getFloat(key: String): Float? {return bundle.getString(key)?.toFloat()}
        fun getFloat(key: String, value: Float): Float {return getFloat(key) ?: value}
        fun getBoolean(key: String): Boolean? {return bundle.getString(key)?.toBoolean()}
        fun getBoolean(key: String, value: Boolean): Boolean {return getBoolean(key) ?: value}
    }
}