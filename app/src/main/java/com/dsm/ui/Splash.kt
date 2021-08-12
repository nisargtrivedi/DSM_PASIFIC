package com.dsm.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.dsm.R
import com.dsm.ui.util.AppPreferences

class Splash : BaseActivity() {
    lateinit var appPreferences: AppPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        appPreferences= AppPreferences(this)




        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */

            if(!TextUtils.isEmpty(appPreferences.getString("USERNAME"))){
                finish()
                startActivity(
                        Intent(this, SelectPortalActivity::class.java))

            }else {
                finish()
                startActivity(
                        Intent(this, MainActivity::class.java))
            }
            overridePendingTransition(R.anim.slide_right, R.anim.noanim);

        }, 3000)



    }
}