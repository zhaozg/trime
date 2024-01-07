package com.osfans.trime.ime.lifecycle

import android.inputmethodservice.InputMethodService
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner

open class LifecycleInputMethodService :
    InputMethodService(),
    LifecycleOwner {
    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    override val lifecycle = lifecycleRegistry

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        window.window!!.decorView.setViewTreeLifecycleOwner(this)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
