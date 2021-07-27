package com.dsm.ui

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dsm.R
import com.dsm.databinding.ActivitySelectPortalBinding

class SelectPortalActivity : BaseActivity() {

    lateinit var binding : ActivitySelectPortalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_portal)
        binding.imgDiamond.setOnClickListener {
            finish()
            startActivity(
                    Intent(this,MainNavigation::class.java)
                        .putExtra("portal","d")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            )
        }
        binding.tvDiamond.setOnClickListener {
            finish()
            startActivity(
                    Intent(this,MainNavigation::class.java)
                        .putExtra("portal","d")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        binding.imgJewellery.setOnClickListener {
            finish()
            startActivity(
                Intent(this,MainNavigation::class.java)
                    .putExtra("portal","j")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            )
        }
        binding.tvJewellery.setOnClickListener {
            //finish()
            startActivity(
                Intent(this,MainNavigation::class.java)
                    .putExtra("portal","j")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }


    }
}