package com.example.scanpiramyds.UI

import android.util.SparseArray
import com.google.android.gms.vision.barcode.Barcode

interface CameraActivityCallback {
    fun refreshCameraLayout(barcodes: SparseArray<Barcode>)
}