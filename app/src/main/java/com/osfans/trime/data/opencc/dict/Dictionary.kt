package com.osfans.trime.data.opencc.dict

import java.io.File

abstract class Dictionary {
    enum class Type(val ext: String) {
        OPENCC("ocd2"),
        Text("txt"),
        ;

        companion object {
            fun fromFileName(name: String): Type? {
                return when {
                    name.endsWith(".ocd2") -> OPENCC
                    name.endsWith(".ocd") -> OPENCC
                    name.endsWith(".txt") -> Text
                    else -> null
                }
            }
        }
    }

    abstract val file: File

    abstract val type: Type

    abstract fun toTextDictionary(dest: File): TextDictionary

    abstract fun toOpenCCDictionary(dest: File): OpenCCDictionary

    open val name: String
        get() = file.nameWithoutExtension

    fun toTextDictionary(): TextDictionary {
        val dest = file.resolveSibling(name + ".${Type.Text.ext}")
        return toTextDictionary(dest)
    }

    fun toOpenCCDictionary(): OpenCCDictionary {
        val dest = file.resolveSibling(name + ".${Type.OPENCC.ext}")
        return toOpenCCDictionary(dest)
    }

    protected fun ensureFileExists() {
        if (!file.exists()) {
            throw IllegalStateException("File ${file.absolutePath} does not exist")
        }
    }

    protected fun ensureTxt(dest: File) {
        if (dest.extension != Type.Text.ext) {
            throw IllegalArgumentException("Dest file name must end with .${Type.Text.ext}")
        }
        dest.delete()
    }

    protected fun ensureBin(dest: File) {
        if (dest.extension != Type.OPENCC.ext) {
            throw IllegalArgumentException("Dest file name must end with .${Type.OPENCC.ext}")
        }
        dest.delete()
    }

    override fun toString(): String = "${javaClass.simpleName}[$name -> ${file.path}]"

    companion object {
        fun new(it: File): Dictionary? =
            when (Type.fromFileName(it.name)) {
                Type.OPENCC -> OpenCCDictionary(it)
                Type.Text -> TextDictionary(it)
                null -> null
            }
    }
}
