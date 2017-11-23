package com.example.elmdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.elmdroid.login.presentation.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        basic.setOnClickListener { startSample(LoginActivity::class.java) }

    }

    private fun startSample(classRef: Class<LoginActivity>) {
        val intent = Intent(this, classRef)
        startActivity(intent)
    }
}
