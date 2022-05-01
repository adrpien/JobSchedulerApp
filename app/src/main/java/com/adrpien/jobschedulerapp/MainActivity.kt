package com.adrpien.jobschedulerapp

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adrpien.jobschedulerapp.databinding.ActivityMainBinding


/*
Job Scheduler is an API for scheduling various types of jobs
against the framework that will be executed in your application's own process.

Job Scheduler służy do planowania zadań do wykonania w procesie twojej aplikacji.
Aplikacja musi być w ramie systemu, ale nie musi mieć miejsca wprowadzania.

 Jak stworzyć JobScheduler:
 1. Utworzyć klasę rozszerzającą klasę JobService
    - zaimplementować onStartJob -  w niej wykonać coroutine
    - zaimplementować onStopJob - wykonuje się tylko w sytuacji, w której zadanie zostało przerwane w trakcie!
2. Dodać do pliku manifestu dependecję Coroutine
   (JobService wykonuje się w wątku głównym, sami musimy zadbać o to zadbać, by przekazać zadanie do wątku pobocznego)
3. Create Coroutine
4. Create JobScheduler
5. Create ComponentName and Set Job to execute in ComponentName
6. Create JobInfo - set jobId, ComponentName and conditions o job trigerring
7. Schedule your JobScheduler
7. Register your JobScheduler in manifest file

 */


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val jobId = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Object which can start a job
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        binding.startServiceButton.setOnClickListener {

            // Setting Job to execute in ComponentName
            val componentName = ComponentName(this, MyJobService::class.java)

            // Setting conditions of job triggering, id and ComponentName
            /*
            - setPersisted - survive device reboot
            - setPeriodic - minimum time 15 minutes
              (you have no control when Job run, but guarantee, the Job run max once per setted amount of time)
            - setMinimumLatency - delay job by minimum amount of time
             */
            val jobInfo = JobInfo.Builder(jobId, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(1000)
                .build()

            // Run JobScheduler
            jobScheduler.schedule(jobInfo)
        }

        binding.stopServiceButton.setOnClickListener {
            // Cancel JobScheduler
            jobScheduler.cancel(jobId)

        }

    }
}