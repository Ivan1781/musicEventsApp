package utils

import java.net.URL

fun loadFile(fileName: String): String {
        return getResource(fileName).readText()
    }

fun getResource(fileName: String): URL {
    return Thread.currentThread().contextClassLoader.getResource(fileName)
        ?: error("Resource $fileName not found")
    }
