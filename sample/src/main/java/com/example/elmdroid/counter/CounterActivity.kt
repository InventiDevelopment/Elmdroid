package com.example.elmdroid.counter

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.elmdroid.R
import cz.inventi.elmdroid.ComponentRuntime
import cz.inventi.elmdroid.LogLevel
import cz.inventi.elmdroid.RuntimeFactory
import kotlinx.android.synthetic.main.activity_counter.*

class CounterActivity : AppCompatActivity() {

    private lateinit var runtime: ComponentRuntime<CounterState, CounterMsg>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        supportActionBar?.title = getString(R.string.basic_counter)

        runtime = RuntimeFactory.create(CounterComponent(), LogLevel.FULL)

        runtime.state().observe(this, Observer {
            it?.let { counter.text = "${it.counter}" }
        })

        increment.setOnClickListener { runtime.dispatch(Increment) }
        decrement.setOnClickListener { runtime.dispatch(Decrement) }
    }

    override fun onDestroy() {
        super.onDestroy()
        runtime.clear()
    }
}
