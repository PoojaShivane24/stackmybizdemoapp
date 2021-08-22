package com.example.stackmybizexercise

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast

import android.content.Intent
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.findNavController
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private var userPassword: String? = null
    private  var userName: String? = null
    lateinit var tvId : TextView
    private val mHandler = Handler()
    private lateinit var locationManager: LocationManager
    private val db = FirebaseDatabase.getInstance()

    companion object {
        private val PERMISSIONS_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        tvId = findViewById(R.id.tv_id)
        receiveIntent()

        tvId.text = userName

        firebaseAuth = FirebaseAuth.getInstance()



        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        }

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permission == PackageManager.PERMISSION_GRANTED) {
            mRunnable.run()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST)

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            firebaseAuth.signOut()
            logoutEntry()
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutEntry() {
        val ref = db.reference.child("Events")
        val hashMap = HashMap<String,String>()
        hashMap["status"] = "Logged Out"
        hashMap["time"] = System.currentTimeMillis().toString()
        hashMap["user"] = intent.getStringExtra("userName").toString()
        ref.push().setValue(hashMap)
    }

    private val mRunnable = object : Runnable{
        override fun run() {
            getLocation()
            mHandler.postDelayed(this,5000)
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST)
        } else {
            val i = LocationServices.getFusedLocationProviderClient(this).lastLocation
            i.addOnSuccessListener {
                val ref = db.reference.child("Location")
                val hashMap = HashMap<String, String>()
                hashMap["latitude"] = it.latitude.toString()
                hashMap["longitude"] = it.longitude.toString()
                ref.push().setValue(hashMap)

            }
        }
    }

    private fun receiveIntent() {
        if (intent != null && intent.hasExtra("userName")) {
            userName = intent.getStringExtra("userName")
        }
        if (intent != null  && intent.hasExtra("userPassword")) {
            userPassword = intent.getStringExtra("userPassword")
        }
    }

    override fun onBackPressed() {
        mHandler.removeCallbacks(mRunnable)
        super.onBackPressed()
    }

}