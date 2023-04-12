package com.jhonatan.appreto.presentation.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.jhonatan.appreto.R
import com.jhonatan.appreto.databinding.ActivityMapBinding
import com.jhonatan.appreto.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLoc = LocationServices.getFusedLocationProviderClient(this)

        initMap()
    }

    private var mapFragment: SupportMapFragment? = null
    private fun initMap() {
        val prev = supportFragmentManager.findFragmentByTag("gmap_address")
        if (prev == null) {
            val gmapOptions: GoogleMapOptions = GoogleMapOptions().liteMode(false)
            mapFragment = SupportMapFragment.newInstance(gmapOptions)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.map_address, mapFragment!!, "gmap_address")
                .commit()
        }
        if (mapFragment == null) {
            mapFragment = supportFragmentManager
                .findFragmentByTag("gmap_address") as SupportMapFragment?
        }
        mapFragment?.getMapAsync(this)
    }

    private var mMap: GoogleMap? = null
    @SuppressLint("MissingPermission")
    override fun onMapReady(gMap: GoogleMap) {
        mMap = gMap
        Util.isLocationEnable(this,contentResolver)
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        mMap?.uiSettings?.isRotateGesturesEnabled = true
        mMap?.uiSettings?.isMapToolbarEnabled = true
        locationListener = LocationListener { location ->
            mLastKnownLocation = location
            val latLng = LatLng(
                mLastKnownLocation!!.latitude,
                mLastKnownLocation!!.longitude
            )
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17F))
            setCustomMarker(latLng)
        }
        mMap?.setOnMapLoadedCallback {
            if (Util.isLocationEnable(this, contentResolver)){
                mMap?.isMyLocationEnabled = false
                startLocationUpdates()
            }
        }
    }

    private var locationListener: LocationListener? = null
    private lateinit var fusedLoc: FusedLocationProviderClient
    private var mLastKnownLocation: Location? = null
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        //Setup LocationRequest
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        //Start Updates
        if (locationListener != null){
            fusedLoc.requestLocationUpdates(locationRequest,
                locationListener!!,
                Looper.getMainLooper())
        }
    }

    private var marker: Marker? = null
    private fun setCustomMarker(position: LatLng) {
        val markerOptions = MarkerOptions()
        val drawable = R.drawable.ic_custom_marker
        markerOptions.apply {
            title("Ubicación")
            position(position)
            icon(Util.bitmapDescriptorFromVector(this@MapActivity, drawable))
        }
        marker?.remove()
        marker = mMap?.addMarker(markerOptions)
    }

    override fun onResume() {
        super.onResume()
        if(Util.isLocationEnable(this, contentResolver)){
            startLocationUpdates()
        }
        checkPermissions()
    }

    private fun checkPermissions(){
        if (!Util.isEnableFineLocationPermission(this)){
            startActivity(Intent(this,  LocationPermissionActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        locationListener?.let {
            fusedLoc.removeLocationUpdates(it)
        }
    }
}