package newproject.fugenx.com.imagemap

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_map_img.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class MapImgActivity : AppCompatActivity() {

    var TAG = "MapImgActivity"
    private var locationManager:LocationManager ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_img)

        /*For Setting email and password data*/

        val name = intent.getStringExtra("key")
        tv_email1.setText(name)
        val id = intent.getStringExtra("key1")
        tv_pass.setText(id)

        /*For Setting Data and Time*/

        val date = System.currentTimeMillis()

        val sdf = SimpleDateFormat("yyyy-MM-dd  hh:mm:ss")
        val dateString = sdf.format(date)
        tv_date.setText(dateString)

        /*For Passing Image*/

        val intent_camera = intent
        val camera_img_bitmap = intent_camera
                .getParcelableExtra<Parcelable>("BitmapImage") as Bitmap
        if (camera_img_bitmap != null) {
            circularImage.setImageBitmap(camera_img_bitmap)
        }
        /*For Static Map*/

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        try {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }


    }
    /*For Static Map*/
       private val locationListener: LocationListener = object : LocationListener {
           override fun onLocationChanged(location: Location) {
               tv_long1.text = location.longitude.toString()
               tv_lat1.text = location.latitude.toString()
               var url = "http://maps.google.com/maps/api/staticmap?center=" +location.latitude + "," + location.longitude +
                       "&zoom=15&size=600x600&sensor=false&markers=color:red|label:|"+location.latitude+","+location.longitude
               var img = findViewById<View>(R.id.map_image1) as ImageView
               Picasso.with(applicationContext).load(url).into(img)
           }
           override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
           override fun onProviderEnabled(provider: String) {}
           override fun onProviderDisabled(provider: String) {}

       }
//    For ScreenShot
    override fun onBackPressed() {
        super.onBackPressed()
        shareScreen()
        var intent = Intent(applicationContext,LoginActivity::class.java)
        startActivity(intent)

    }
    private fun shareScreen() {
        try {
            val cacheDir = File(
                    Environment.getExternalStorageDirectory(),
                    "lakshmi")

            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)

            val path = File(
                    Environment.getExternalStorageDirectory(),
                    "lakshmi").toString() + "/Image-$n.jpg"

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
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun takeScreenShot(activity: Activity): Bitmap {
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

}
