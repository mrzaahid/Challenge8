package com.zaahid.challenge8.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.text.SimpleDateFormat
import java.util.*

private const val TAG ="SaveImageToFileWorker"
@Suppress("DEPRECATION")
class SaveImageToFileWorker(ctx : Context, params : WorkerParameters): Worker(ctx,params) {
    private val title = "Blurred Image"
    private val dateFormater = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss 2",
        Locale.getDefault()
    )
    override fun doWork(): Result {
        makeStatusNotification("Saving Image",applicationContext)
        sleep()

        val resolver = applicationContext.contentResolver
        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val bitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )
            val imageUrl = MediaStore.Images.Media.insertImage(
                resolver,bitmap,title,dateFormater.format(Date())
            )
            if (!imageUrl.isNullOrEmpty()){
                val output = workDataOf(KEY_IMAGE_URI to imageUrl)
                makeStatusNotification("Saving Image success",applicationContext)
                Result.success(output)
            }else{
                Log.e(TAG,"writing to media store failed")
                Result.failure()
            }
        }catch (exception:Exception){
            exception.printStackTrace()
            Result.failure()
        }
    }
}