package com.example.elmdroid.counter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.elmdroid.R
import com.example.elmdroid.common.ViewRenderer
import cz.inventi.elmdroid.ElmController
import kotlinx.android.synthetic.main.activity_counter.*

class CounterActivity : AppCompatActivity(), CounterView {

    private lateinit var controller: ElmController<CounterState, CounterMsg, CounterCmd>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        supportActionBar?.title = getString(R.string.basic_counter)

        controller = ElmController(CounterComponent())

        controller.state().observe(this, object : ViewRenderer<CounterView, CounterState>(this){
            override fun CounterView.render(state: CounterState) {
                counter().text = "${state.counter}"
            }

        })

        increment.setOnClickListener { controller.dispatch(Increment) }
        decrement.setOnClickListener { controller.dispatch(Decrement) }
    }

    override fun counter(): TextView = counter

    override fun onDestroy() {
        super.onDestroy()
        controller.clear()
    }
}
