package com.dsm.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dsm.R
import com.dsm.databinding.ActivityLoginBinding

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.btnSignin.setOnClickListener {
            finish()
            startActivity(
                    Intent(this@MainActivity, SelectPortalActivity::class.java)
            )
            overridePendingTransition(R.anim.slide_right, R.anim.noanim);
        }
    }
}