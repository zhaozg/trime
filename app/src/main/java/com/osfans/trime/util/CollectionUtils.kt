package com.osfans.trime.util

object CollectionUtils {
    @JvmStatic
    fun <K, V> getOrDefault(
        map: Map<K, V>,
        key: K,
        defaultValue: V,
    ): V = map[key] ?: defaultValue

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun obtainValue(
        map: Map<String, Any?>?,
        vararg: String?,
    ): Any? {
        if (map.isNullOrEmpty() || vararg == null) return null
        val keys = vararg.split('/')
        var v: Any? = map
        for (key in keys) {
            v =
                if (v is Map<*, *> && (v as Map<String?, Any?>).containsKey(key)) {
                    v[key]
                } else {
                    return null
                }
        }
        return v
    }

    @JvmStatic
    fun obtainString(
        map: Map<String, Any?>?,
        key: String,
        defValue: String = "",
    ): String {
        if (map.isNullOrEmpty() || key.isEmpty()) return defValue
        val v = obtainValue(map, key)
        return v?.toString() ?: defValue
    }

    @JvmStatic
    fun obtainInt(
        map: Map<String, Any?>?,
        key: String,
        defValue: Int = 0,
    ): Int {
        if (map.isNullOrEmpty() || key.isEmpty()) return defValue
        val nm = obtainString(map, key)
        return runCatching {
            if (nm.isNotEmpty()) java.lang.Long.decode(nm).toInt() else defValue
        }.getOrDefault(defValue)
    }

    @JvmStatic
    fun obtainFloat(
        map: Map<String, Any?>?,
        key: String,
        defValue: Float = 0f,
    ): Float {
        if (map.isNullOrEmpty() || key.isEmpty()) return defValue
        val s = obtainString(map, key)
        return runCatching {
            if (s.isNotEmpty()) s.toFloat() else defValue
        }.getOrDefault(defValue)
    }

    @JvmStatic
    fun obtainBoolean(
        map: Map<String, Any?>?,
        key: String,
        defValue: Boolean = false,
    ): Boolean {
        return if (map.isNullOrEmpty() || key.isEmpty()) defValue else obtainString(map, key).toBoolean()
    }
}
