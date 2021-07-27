package com.dsm.ui

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
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            )
        }
    }
}