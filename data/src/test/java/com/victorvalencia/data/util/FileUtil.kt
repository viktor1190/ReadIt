package com.victorvalencia.data.util

import com.squareup.moshi.Moshi
import com.victorvalencia.data.model.response.RedditTopPostsResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Utility class for loading json files for unit testing
 * Based on this doc: https://medium.com/mobile-app-development-publication/android-reading-a-text-file-during-test-2815671e8b3b
 * source code: https://github.com/elye/demo_android_mock_web_service/blob/master/app/src/test/java/com/example/mockserverexperiment/ChatTest.kt
 */
object FileUtil {
    @Throws(IOException::class)
    fun readFileWithoutNewLineFromResources(fileName: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream = getInputStreamFromResource(fileName)
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream))

            var str: String? = reader.readLine()
            while (str != null) {
                builder.append(str)
                str = reader.readLine()
            }
            return builder.toString()
        } finally {
            inputStream?.close()
        }
    }

    private fun getInputStreamFromResource(fileName: String)
            = javaClass.classLoader?.getResourceAsStream(fileName)
}

internal fun String.asRedditTopPostsResponse(): RedditTopPostsResponse {
    val moshi = Moshi.Builder().build()
    return moshi.adapter(RedditTopPostsResponse::class.java).fromJson(this)!!
}