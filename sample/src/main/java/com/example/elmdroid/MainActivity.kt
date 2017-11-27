package com.example.elmdroid

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.elmdroid.login.presentation.LoginActivity
import com.example.elmdroid.login.presentation.LoginVMActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        counter.setOnClickListener { Intent(this, LoginVMActivity::class.java).start() }
        login.setOnClickListener { Intent(this, LoginActivity::class.java).start() }
        loginViewModel.setOnClickListener { Intent(this, LoginVMActivity::class.java).start() }

    }

    private fun Intent.start() = startActivity(this)

}
