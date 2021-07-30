package com.dsm.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import com.dsm.R
import com.dsm.databinding.ActivityLoginBinding
import com.dsm.ui.util.AppPreferences
import com.dsm.ui.util.Cognito
import com.dsm.ui.util.KeyBoardHandling
import com.dsm.ui.util.Util

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var obj : Cognito

    lateinit var appPreferences: AppPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreferences= AppPreferences(this)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_login)
        obj = Cognito(this)

        if(!TextUtils.isEmpty(appPreferences.getString("USERNAME"))){
            startActivity(
                Intent(this, SelectPortalActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        binding.btnSignin.setOnClickListener {

            if(TextUtils.isEmpty(binding.edtUsername.text.toString().trim())){
                Util.showDialog(this, "Please enter email address")
            } else if(!Util.isValidEmail(binding.edtUsername.text.toString().trim())){
                Util.showDialog(this, "Please enter proper email address")
            } else if(TextUtils.isEmpty(binding.edtPassword.text.toString().trim())){
                Util.showDialog(this, "Please enter password")
            }else{
                if(DSM.getInstance().isNetworkAvailable) {
                    KeyBoardHandling.hideSoftKeyboard(this@MainActivity)
                    obj.userLogin(
                        binding.edtUsername.text.toString().trim(),
                        binding.edtPassword.text.toString().trim()
                    )
                }else{
                    Util.showDialog(this, "No Internet Available")
                }
            }

//            finish()
//            startActivity(
//                    Intent(this@MainActivity, SelectPortalActivity::class.java)
//            )
            //overridePendingTransition(R.anim.slide_right, R.anim.noanim);
        }
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
            overridePendingTransition(R.anim.slide_right, R.anim.noanim);
        }
    }
}