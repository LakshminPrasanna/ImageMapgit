package newproject.fugenx.com.imagemap

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() , View.OnClickListener{
    private val CAMERA_REQUEST_CODE = 12345
    private val REQUEST_GALLERY_CAMERA = 54654

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        input_password.setOnEditorActionListener(object: TextView.OnEditorActionListener {

            override fun onEditorAction(textView:TextView, id:Int, keyEvent: KeyEvent):Boolean {
                if (id == R.id.btn_signin || id == EditorInfo.IME_NULL)
                {
                    attemptLogin()
                    return true
                }
                return false
            }
        })

        btn_signin.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v) {
            btn_signin -> {

                attemptLogin()

                if (Build.VERSION.SDK_INT >= 21) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                this,
                                arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_GALLERY_CAMERA)
                    } else {
//                        openCamera()
                    }
                } else {
//                    openCamera()
                }

            }
        }

    }
    private fun attemptLogin()
    {
        input_email.error = null
        input_password.error = null

        val email1 = input_email.getText().toString()
        val pass = input_password.getText().toString()
        var cancel = false
        var focusView:View? = null

        if (TextUtils.isEmpty(pass) && !isPasswordValid(pass))
        {
            input_password.error = getString(R.string.error_invalid_password)
            focusView = input_password
            cancel = true
        }
        if (TextUtils.isEmpty(email1))
        {
            input_email.error = getString(R.string.error_field_required)
            focusView = input_email
            cancel = true
        }
        else if (!isEmailValid(email1))
        {
            input_email.error = getString(R.string.error_invalid_email)
            focusView = input_email
            cancel = true
        }
        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }
        else
        {
            openCamera()
        }
    }

    private fun isEmailValid(input_email:String):Boolean {

        return input_email.contains("@")
    }
    private fun isPasswordValid(input_password:String):Boolean {

        return input_password.length > 4
    }

    private fun openCamera() {

        val intent = Intent(applicationContext,SurfaveViewActivity::class.java)
        intent.putExtra("key", input_email.text.toString() )
        intent.putExtra("key1", input_password.text.toString() )
        if (intent.resolveActivity(packageManager) != null)

        startActivityForResult(intent, CAMERA_REQUEST_CODE)

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_GALLERY_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    if (data != null) {
//                        val photo = data.getExtras().get("data") as Bitmap
                        val IntentCamera = Intent(this, MapImgActivity::class.java)
//                        IntentCamera.putExtra("BitmapImage", photo)
//                        IntentCamera.putExtra("key", input_email.text.toString() )
//                        IntentCamera.putExtra("key1", input_password.text.toString() )
                        startActivity(IntentCamera)
                    }
                }
            }
        }
    }


