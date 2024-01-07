/**
 * Adapted from [fcitx5-android/Logcat.kt](https://github.com/fcitx5-android/fcitx5-android/blob/e44c1c7/app/src/main/java/org/fcitx/fcitx5/android/utils/Logcat.kt)
 */
package com.osfans.trime.util

import android.os.Process
import com.osfans.trime.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class Logcat(val pid: Int? = Process.myPid()) : CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private var process: java.lang.Process? = null
    private var emittingJob: Job? = null

    private val flow: MutableSharedFlow<String> = MutableSharedFlow()

    /**
     * Subscribe to this flow to receive log in app
     * Nothing would be emitted until [initLogFlow] was called
     */
    val logFlow: SharedFlow<String> by lazy { flow.asSharedFlow() }

    /**
     * Get a snapshot of logcat
     */
    fun getLogAsync(): Deferred<Result<List<String>>> =
        async {
            runCatching {
                Runtime.getRuntime()
                    .exec(arrayOf("logcat", pid?.let { "--pid=$it" } ?: "", "-d"))
                    .inputStream
                    .bufferedReader()
                    .readLines()
            }
        }

    /**
     * Clear logcat
     */
    fun clearLog(): Job =
        launch {
            runCatching { Runtime.getRuntime().exec(arrayOf("logcat", "-c")) }
        }

    /**
     * Create a process reading logcat, sending lines to [logFlow]
     */
    fun initLogFlow() =
        if (process != null) {
            errorState(R.string.exception_logcat_created)
        } else {
            launch {
                runCatching {
                    Runtime
                        .getRuntime()
                        .exec(arrayOf("logcat", pid?.let { "--pid=$it" } ?: "", "-v", "time"))
                        .also { process = it }
                        .inputStream
                        .bufferedReader()
                        .lineSequence()
                        .asFlow()
                        .flowOn(Dispatchers.IO)
                        .cancellable()
                        .collect { flow.emit(it) }
                }
            }.also { emittingJob = it }
        }

    /**
     * Destroy the reading process
     */
    fun shutdownLogFlow() {
        process?.destroy()
        emittingJob?.cancel()
    }

    companion object {
        val default by lazy { Logcat() }
    }
}
