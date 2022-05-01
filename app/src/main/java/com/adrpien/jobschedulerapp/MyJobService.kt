package com.adrpien.jobschedulerapp

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlinx.coroutines.*


/*
JobService is done in main thread unless you use coroutine on your own - need to add depencies
 */
class MyJobService: JobService() {

    private lateinit var job: Job


    override fun onStartJob(params: JobParameters?): Boolean {
    Log.d("ON_START_JOB", "onStartJob started")

        // Create Coroutine
        job = CoroutineScope(Dispatchers.IO).launch {
            for(i in 1..100){
                delay(1000)
            Log.d("COUNT", i.toString())
            }
            // informs system, that job has been done
            jobFinished(params, true)
        }
        /*
         Return true from this method if your job needs to continue running.
         Returning false from this method means your job is already finished.
         */
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
    Log.d("ON_STOP_JOB", "OnStopJob done")
        // remember to stop coroutine!
        job.cancel()
        return true
    }
}