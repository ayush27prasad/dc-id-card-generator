package com.kvidcard.utils

import android.graphics.Bitmap
import android.util.Log
import com.slowmac.autobackgroundremover.BackgroundRemover
import com.slowmac.autobackgroundremover.OnBackgroundChangeListener

class ImageProcessor {
    companion object {
        @JvmStatic
        fun removeBackground(bitmap: Bitmap, callback: BackgroundChangeCallback) {
            BackgroundRemover.bitmapForProcessing(
                bitmap,
                true,
                object: OnBackgroundChangeListener{
                    override fun onSuccess(bitmap: Bitmap) {
                        Log.d("onSuccess : ", "Successfully done image processing : $bitmap");
                        callback.onSuccess(bitmap)

                    }

                    override fun onFailed(exception: Exception) {
                        Log.d("Failure : ","Failure");
                        //Exception
                        callback.onFailure(exception)

                    }
                }
            )

        }
    }
}



interface BackgroundChangeCallback {
    fun onSuccess(bitmap: Bitmap)
    fun onFailure(exception: Exception)
}
