package com.example.elmdroid.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.elmdroid.R
import cz.inventi.elmdroid.ComponentRuntime
import cz.inventi.elmdroid.createRuntimeFor
import kotlinx.android.synthetic.main.activity_counter.*
import net.semanticer.renderit.renderit

class CounterActivity : AppCompatActivity() {

    private lateinit var runtime: ComponentRuntime<CounterState, CounterMsg>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        supportActionBar?.title = getString(R.string.basic_counter)

        runtime = createRuntimeFor(CounterComponent())

        renderit(runtime.state()) { state ->
            state from { it.counter } into { counter.text = "$it" }
        }

        increment.setOnClickListener { runtime.dispatch(Increment) }
        decrement.setOnClickListener { runtime.dispatch(Decrement) }
    }
}
