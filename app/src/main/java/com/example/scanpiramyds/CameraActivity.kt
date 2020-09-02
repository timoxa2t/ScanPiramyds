package com.example.scanpiramyds



import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.scanpiramyds.UI.CameraService
import kotlinx.android.synthetic.main.activity_camera.*


class CameraActivity : AppCompatActivity() {

    private lateinit var mCameraManager: CameraManager
    private val LOG_TAG: String = "My log tag"
    private var myCameras: MutableList<CameraService> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
        }

        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.i(LOG_TAG, "Starting CameraActivity")

        try {
            for (cameraID in mCameraManager.cameraIdList) {
                Log.i(LOG_TAG, "cameraID: $cameraID")
                val id = cameraID.toInt()

                // создаем обработчик для камеры
                myCameras.add(id, CameraService(applicationContext, mCameraManager, cameraID, camera_texture_view))
            }
        } catch (e: CameraAccessException) {
            Log.e(LOG_TAG, e.message.toString())
            e.printStackTrace()
        }

        myCameras[0].openCamera();

    }



    fun scanBarcode(view: View){
      Log.i(LOG_TAG, "Button pressed")
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Image_Capture_Code) {
//            if (resultCode == Activity.RESULT_OK) {
//                val bp = data?.extras!!["data"] as Bitmap?
////                capturedImage.setImageBitmap(bp)
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
}