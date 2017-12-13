package com.example.elmdroid.counter

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.elmdroid.R
import com.example.elmdroid.common.ViewRenderer
import cz.inventi.elmdroid.ElmRuntime
import kotlinx.android.synthetic.main.activity_counter.*

class CounterActivity : AppCompatActivity() {

    private lateinit var runtime: ElmRuntime<CounterState, CounterMsg, Nothing>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        supportActionBar?.title = getString(R.string.basic_counter)

        runtime = ElmRuntime(CounterComponent())

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
