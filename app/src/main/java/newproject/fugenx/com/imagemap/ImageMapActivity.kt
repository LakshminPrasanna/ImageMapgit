package newproject.fugenx.com.imagemap


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_image_map.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageMapActivity : AppCompatActivity()
        /*,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener*/ {

   /* private var service: LocationManager? = null
    private var enabled: Boolean? = null
    private var mLocationRequest: LocationRequest?=null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private lateinit var mMap: GoogleMap
    private var REQUEST_LOCATION_CODE = 101*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_map)
        /* service = this.getSystemService(LOCATION_SERVICE) as LocationManager
        enabled = service!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var getBundle: Bundle? = null
        getBundle = this.intent.extras
        val name = getBundle.getString("key")
        tv_email1.setText(name)
        val id = getBundle.getString("key1")
        tv_pass.setText(id)

        val date = System.currentTimeMillis()

        val sdf = SimpleDateFormat("yyyy-MM-dd  hh:mm:ss")
        val dateString = sdf.format(date)
        tv_date.setText(dateString)

        val intent_camera = intent
        val camera_img_bitmap = intent_camera
                .getParcelableExtra<Parcelable>("BitmapImage") as Bitmap
        if (camera_img_bitmap != null) {
            imageView.setImageBitmap(camera_img_bitmap)
        }
        *//*val bitmap = loadBitmapFromView(findViewById(R.id.activity_image_map), 350, 450)
        saveImage(bitmap)*//*

    }


    override fun onBackPressed() {
        super.onBackPressed()
        shareScreen()
    }
        *//*fun saveImage(bitmap: Bitmap) {
            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File(root + "/req_images")
            myDir.mkdir()
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)
            val fname = "Image-$n.jpg"
            val file = File(myDir, fname)
            //  Log.i(TAG, "" + file);
            if (file.exists())
                file.delete()
            try {
                file.createNewFile()
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(0, 0, v.layoutParams.width, v.layoutParams.height)
            v.draw(c)
            return b
        }
*//*
        fun takeScreenShot(activity: Activity): Bitmap {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val b1 = view.drawingCache
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top


            val width = activity.windowManager.defaultDisplay.width
            val height = activity.windowManager.defaultDisplay.height


            val b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight)
            view.destroyDrawingCache()
            return b
        }
    private fun shareScreen() {
        try {
            val cacheDir = File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "devdeeds")

            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)

            val path = File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "devdeeds").toString() + "/Image-$n.jpg"

            savePic(takeScreenShot(this), path)

            Toast.makeText(applicationContext, "Screenshot Saved", Toast.LENGTH_SHORT).show()


        } catch (ignored: NullPointerException) {
            ignored.printStackTrace()
        }

    }

    fun savePic(b: Bitmap, strFileName: String) {
        val fos: FileOutputStream
        try {

            fos = FileOutputStream(strFileName)
            b.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.flush()
            fos.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }




    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                mMap.isMyLocationEnabled = true
            } else {
                checkLocationPermission()
            }
        } else {
            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true
        }
    }
    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient!!.connect()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
    override fun onLocationChanged(location: Location?) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        val latLng = LatLng(location!!.latitude, location.longitude)
        tv_lat1!!.text = location.latitude.toString()
        tv_long1!!.text = location.longitude.toString()
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        mCurrLocationMarker = mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }
    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        // Check if enabled and if not send user to the GPS settings
        if (!enabled!!) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // Check if permission is granted or not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }
    }
    override fun onConnectionSuspended(p0: Int) {
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
                        })
                        .create()
                        .show()
            } else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
        }
    }*/
    }
}
