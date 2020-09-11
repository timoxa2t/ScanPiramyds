package com.example.scanpiramyds.UI

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.scanpiramyds.CameraActivity
import com.example.scanpiramyds.R
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.util.*


class CameraService(context: Context, cameraManager: CameraManager, cameraID: String, cameraView: TextureView, callback: CameraActivityCallback) {
    private val mCameraID: String
    private var mCameraDevice: CameraDevice? = null
    private var mCaptureSession: CameraCaptureSession? = null
    private val mCameraManager: CameraManager
    private val mContext: Context
    private val LOG_TAG: String = "My log tag"
    private val mCameraView: TextureView
    private val mDetector:BarcodeDetector
    private val mActivityCallback: CameraActivityCallback


    init {
        mCameraManager = cameraManager
        mCameraID = cameraID
        mContext = context
        mCameraView = cameraView
        mActivityCallback = callback
        mDetector = BarcodeDetector.Builder(mContext)
            .setBarcodeFormats(Barcode.CODE_128 or Barcode.QR_CODE)
            .build()


    }

    val isOpen: Boolean
        get() = mCameraDevice !== null

    fun openCamera() {
        try {
            if (checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mCameraManager.openCamera(mCameraID, mCameraCallback, null)
                Log.i(LOG_TAG, "Open camera  with id:" + mCameraDevice?.id)
            }
        } catch (e: CameraAccessException) {
            Log.i(LOG_TAG, e.message.toString())
        }
    }

    fun closeCamera() {
        if (mCameraDevice !== null) {
            mCameraDevice?.close()
            mCameraDevice = null
        }
    }

    private val mCameraCallback: CameraDevice.StateCallback =
        object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                mCameraDevice = camera
                Log.i(LOG_TAG, "Starting camera callback")
                createCameraPreviewSession()
            }

            override fun onDisconnected(camera: CameraDevice) {
                mCameraDevice?.close()
                Log.i(LOG_TAG, "disconnect camera  with id:" + mCameraDevice?.id)
                mCameraDevice = null
            }

            override fun onError(camera: CameraDevice, error: Int) {
                Log.i(
                    LOG_TAG,
                    "error! camera id:" + camera.id + " error:" + error
                )
            }
        }

    private fun createCameraPreviewSession() {

        val texture: SurfaceTexture? = mCameraView.getSurfaceTexture()
        mCameraView.surfaceTextureListener = textureChangeListener
        // texture.setDefaultBufferSize(1920,1080);
        val surface = Surface(texture)
        try {
            val builder = mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                ?: return

            builder.addTarget(surface)
            mCameraDevice?.createCaptureSession(
                Arrays.asList(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        mCaptureSession = session
                        try {
                            mCaptureSession?.setRepeatingRequest(builder.build(), null, null)
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {}
                }, null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private val cameraCaptureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            super.onCaptureCompleted(session, request, result)

        }
    }

    val textureChangeListener = object: TextureView.SurfaceTextureListener{
        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            TODO("Not yet implemented")
        }

        override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
            if(mCameraView.bitmap == null) return




            val bitmap = mCameraView.bitmap
            val frame = Frame.Builder().setBitmap(bitmap).build()
            val barcodes = mDetector.detect(frame)

            mActivityCallback.refreshCameraLayout(barcodes)
        }

        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
            return true
        }

        override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {

        }

    }

}
