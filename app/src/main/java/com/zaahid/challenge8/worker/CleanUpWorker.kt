package com.zaahid.challenge8.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File

private const val TAG = "CLeanUpWorker"
class CleanUpWorker(ctx :Context, parameters: WorkerParameters):Worker(ctx,parameters) {
    override fun doWork(): Result {
        makeStatusNotification("Cleaning Up Old Temporary Files",applicationContext)
        sleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()){
                val entries =outputDirectory.listFiles()
                if (entries != null){
                    for (entry in entries){
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")){
                            val deleted = entry.delete()
                            Log.i(TAG,"delete $name - $deleted")
                        }
                    }
                }
            }
            Result.success()
        }catch (exception : Exception){
            exception.printStackTrace()
            Result.failure()
        }
    }

}