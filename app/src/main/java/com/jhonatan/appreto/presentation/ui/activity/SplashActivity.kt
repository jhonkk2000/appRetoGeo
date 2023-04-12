package com.jhonatan.appreto.presentation.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.jhonatan.appreto.databinding.ActivitySplashBinding
import com.jhonatan.appreto.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        delaySplash()
    }

    private fun delaySplash() {
        Handler(Looper.getMainLooper()).postDelayed({
            val isGranted = isEnablePermissions()
            val intent = if (isGranted){
                Intent(this, LoginActivity::class.java)
            }else{
                Intent(this, LocationPermissionActivity::class.java)
            }
            startActivity(intent)
            finish()
        },2000)
    }

    private fun isEnablePermissions(): Boolean{
        return Util.isEnableFineLocationPermission(this)
    }
}