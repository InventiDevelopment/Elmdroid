package com.example.elmdroid.counter

import android.arch.lifecycle.LifecycleOwner
import android.widget.TextView

interface CounterView : LifecycleOwner {
    fun counter(): TextView
}