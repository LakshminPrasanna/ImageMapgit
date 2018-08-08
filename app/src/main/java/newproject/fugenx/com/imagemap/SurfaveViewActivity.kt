package newproject.fugenx.com.imagemap

import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class SurfaveViewActivity : AppCompatActivity(), SurfaceHolder.Callback {
    internal var testView: TextView? = null
    internal var camera: Camera? = null
    internal var surfaceView: SurfaceView? = null
    internal var surfaceHolder: SurfaceHolder? = null
    internal var rawCallback: android.hardware.Camera.PictureCallback? = null
    internal var shutterCallback: android.hardware.Camera.ShutterCallback? = null
    internal var jpegCallback: android.hardware.Camera.PictureCallback? = null
    lateinit var loginIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surfave_view)
        loginIntent = intent
        //Log.d("Log", "values: " + loginIntent.getStringExtra("key")+loginIntent.getStringExtra("key1"))
        surfaceView = findViewById(R.id.surfaceView) as SurfaceView
        surfaceHolder = surfaceView!!.getHolder()

        surfaceHolder!!.addCallback(this)
        surfaceHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        jpegCallback = object : Camera.PictureCallback {
            override fun onPictureTaken(data: ByteArray, camera: Camera) {
                var outStream: FileOutputStream? = null
                try {
                    outStream = FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()))
                    outStream.write(data)
                    outStream.close()
                    //Log.d("Log", "onPictureTaken - wrote bytes: " + data.size)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                }
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT).show()
        //for sending image
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                var intent = Intent(applicationContext, MapImgActivity::class.java)
                intent.putExtra("BitmapImage", bitmap)

                intent.putExtra("key", loginIntent.getStringExtra("key"))
                intent.putExtra("key1", loginIntent.getStringExtra("key1"))

                startActivity(intent)
                refreshCamera()
            }
        }
    }

    @Throws(IOException::class)
    fun captureImage(v: View) {
        //take the picture
        camera!!.takePicture(null, null, jpegCallback)
    }

    fun refreshCamera() {
        if (surfaceHolder!!.getSurface() == null) {
            // preview surface does not exist
            return
        }
        // stop preview before making changes
        try {
            camera!!.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        try {
            camera!!.setPreviewDisplay(surfaceHolder)
            camera!!.setDisplayOrientation(90)
            camera!!.startPreview()
        } catch (e: Exception) {
        }
    }
    override fun surfaceCreated(holder: SurfaceHolder?) {
        try {
            // open the camera
            camera = Camera.open()
        } catch (e: RuntimeException) {
            System.err.println(e)
            return
        }
        val param = camera!!.getParameters()
        val focusModes = param.getSupportedFocusModes()

        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO)
        }
        param.setJpegQuality(100);
        camera!!.setParameters(param)
        try {
            camera!!.setPreviewDisplay(surfaceHolder)
            camera!!.setDisplayOrientation(90)
            camera!!.startPreview()
        } catch (e: Exception) {

            System.err.println(e)
            return
        }
    }
    override fun surfaceChanged(holder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

        refreshCamera()
    }
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera!!.stopPreview()
        camera!!.release()
        camera = null

    }

}
