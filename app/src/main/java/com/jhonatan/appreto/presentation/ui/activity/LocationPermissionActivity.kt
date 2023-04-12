package com.jhonatan.appreto.presentation.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.jhonatan.appreto.BuildConfig
import com.jhonatan.appreto.R
import com.jhonatan.appreto.databinding.ActivityLocationPermissionBinding
import com.jhonatan.appreto.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationPermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enablePermissions()
    }

    private fun enablePermissions(){
        binding.btnEnable.setOnClickListener {
            if (!Util.isEnableFineLocationPermission(this)){
                Util.requestLocationPermissions(this)
            }else{
                startLogin()
            }
        }
    }

    private fun startLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            Util.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty()){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permisos aceptados correctamente", Toast.LENGTH_SHORT).show()
                        startLogin()
                    }else{
                        permissionsDenied()
                    }
                }
            }
        }
    }

    private fun permissionsDenied() {
        MaterialAlertDialogBuilder(this).apply {
            title = "Permisos"
            setMessage("Necesitas los permisos para que el app pueda seguir tu ubicación")
            setPositiveButton("Habilitar") { dialog, value ->
                dialog.dismiss()
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    )
                )
            }
            setNegativeButton("Cancelar") { dialog, value ->
                dialog.dismiss()
            }
        }.show()
    }
}