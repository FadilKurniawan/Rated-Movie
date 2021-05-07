package com.devfk.ratedmovie.data.api

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor as HttpLoggingInterceptor1
class LoggingInterceptor  {
    companion object{
        private var SERVICE_TAG = "apiServices"

        private val loggingInterceptor = HttpLoggingInterceptor1(object : HttpLoggingInterceptor1.Logger {
            override fun log(message: String) {
                Log.d(SERVICE_TAG, message)
            }
        })

        fun getLoggingInterceptor() : HttpLoggingInterceptor1 {
            loggingInterceptor.level = HttpLoggingInterceptor1.Level.BODY
            return loggingInterceptor
        }
    }
}