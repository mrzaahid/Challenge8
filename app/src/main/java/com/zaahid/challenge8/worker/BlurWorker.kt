package com.zaahid.challenge8.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

private const val  TAG = "BlurWorker"
class BlurWorker (ctx : Context,params: WorkerParameters):Worker(ctx,params) {
    override fun doWork(): Result {
        val appContext = applicationContext
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification("Blurring Image",appContext)

        sleep()

        return try {
            if (TextUtils.isEmpty(resourceUri)){
                Log.e(TAG,"invalid input uri")
                throw IllegalArgumentException("Invalid resource UI")
            }
//            val picture = BitmapFactory.decodeResource(
//                appContext.resources,
//                R.drawable.android_cupcake
//            )
            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )
            val output = blurBitmap(picture,appContext)

            val outputUri = writeBitmapToFile(appContext,output)

            makeStatusNotification("Output is $outputUri",appContext)
            val outputData= workDataOf( KEY_IMAGE_URI to outputUri.toString())
            Result.success(outputData)
        }catch (throwable:Throwable){
            Log.e(TAG,"error playing blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }
}