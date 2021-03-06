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
            startActivity(
                    Intent(this,MainNavigation::class.java)
                        .putExtra("portal","d")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            )
            overridePendingTransition(R.anim.slide_right, R.anim.noanim);
        }
        binding.tvDiamond.setOnClickListener {

            startActivity(
                    Intent(this,MainNavigation::class.java)
                        .putExtra("portal","d")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            overridePendingTransition(R.anim.slide_right, R.anim.noanim);
        }

        binding.imgJewellery.setOnClickListener {

            startActivity(
                Intent(this,MainNavigation::class.java)
                    .putExtra("portal","j")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            )
            overridePendingTransition(R.anim.slide_right, R.anim.noanim);
        }
        binding.tvJewellery.setOnClickListener {

            startActivity(
                Intent(this,MainNavigation::class.java)
                    .putExtra("portal","j")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            overridePendingTransition(R.anim.slide_right, R.anim.noanim);
        }


    }
}