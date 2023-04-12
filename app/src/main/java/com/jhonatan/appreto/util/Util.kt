package com.jhonatan.appreto.util

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task

object Util {

    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100

    fun isEnableFineLocationPermission(context: Context): Boolean = ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED

    fun requestLocationPermissions(activity: Activity) = ActivityCompat.requestPermissions(
        activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    fun isGpsEnable(contentResolver: ContentResolver): Boolean {
            val locationMode: Int
            val gps_enabled: Boolean
            locationMode = try {
                Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            gps_enabled = locationMode != Settings.Secure.LOCATION_MODE_OFF
            return gps_enabled
        }

    var locationRequest: LocationRequest? = null
    var result: Task<LocationSettingsResponse>? = null
    private val REQUEST_CHECK_SETTINGS = 101
    fun isLocationEnable(activity: Activity, contentResolver: ContentResolver): Boolean {
            if (!isGpsEnable(contentResolver)) {
                locationRequest = LocationRequest.create()
                locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest?.interval = 10000
                locationRequest?.fastestInterval = 5000
                val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest!!)
                builder.setAlwaysShow(true)
                val client = LocationServices.getSettingsClient(activity)
                result = client.checkLocationSettings(builder.build())
                result?.addOnFailureListener(activity, OnFailureListener { e ->
                    if (e is ResolvableApiException) {
                        try {
                            e.startResolutionForResult(
                                activity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (sendEx: IntentSender.SendIntentException) {
                            sendEx.printStackTrace()
                        }
                    }
                })
                return false
            }
            return true
        }
}