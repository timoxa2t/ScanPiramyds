package com.example.scanpiramyds



import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scanpiramyds.UI.CameraActivityCallback
import com.example.scanpiramyds.UI.CameraService
import com.example.scanpiramyds.database.Pyramid
import com.example.scanpiramyds.database.PiramydViewModel
import com.example.scanpiramyds.database.ViewModelFactory
import com.google.android.gms.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_camera.*


class CameraActivity : AppCompatActivity(), CameraActivityCallback {

    private lateinit var piramydViewModel: PiramydViewModel
    private var mPyramidsList: List<Pyramid>? = null
    private lateinit var mCameraManager: CameraManager
    private val LOG_TAG: String = "My log tag"
    private var myCameras: MutableList<CameraService> = mutableListOf()
    private var mCurrentPyramid: Pyramid? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
        }

        piramydViewModel = ViewModelProvider(this, ViewModelFactory(application)).get(PiramydViewModel::class.java)
        piramydViewModel.allPyramids.observe(this, Observer { piramyds -> piramyds?.let {mPyramidsList = it}})


        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.i(LOG_TAG, "Starting CameraActivity")

        try {
            for (cameraID in mCameraManager.cameraIdList) {
                Log.i(LOG_TAG, "cameraID: $cameraID")
                val id = cameraID.toInt()


                myCameras.add(id, CameraService(this , mCameraManager, cameraID, camera_texture_view, this))
            }
        } catch (e: CameraAccessException) {
            Log.e(LOG_TAG, e.message.toString())
            e.printStackTrace()
        }

        myCameras[0].openCamera();

    }



    fun writeBarcode(view: View){
        if(mCurrentPyramid != null) {
            mCurrentPyramid?.checked = true
        }

    }



    override fun refreshCameraLayout(barcodes: SparseArray<Barcode>) {
        if(barcodes.size() > 0) {

            val barcode =  barcodes.valueAt(0).rawValue
            mCurrentPyramid = mPyramidsList?.find { piramyd -> barcode.equals("996" + piramyd.code) }
            name_view.text = mCurrentPyramid?.name ?: resources.getText(R.string.piramyd_not_in_database_label)

            barcode_view.text = barcode

            if(mCurrentPyramid != null) {
                    if(mCurrentPyramid?.checked == true) {
                        piramyd_received_view.text = "Отримано"
                    }else{
                        piramyd_received_view.text = "Не отримано"
                    }
                    button_save_barcode.visibility = View.VISIBLE
            }
        }
        else{
            name_view.text = getString(R.string.label_scan_barcode)
            barcode_view.text = ""
            piramyd_received_view.text = ""
            button_save_barcode.visibility = View.INVISIBLE
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {

        syncronizeDataWithDatabase()
        super.onPause()
    }

    fun syncronizeDataWithDatabase(){
        piramydViewModel.updateAll()
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