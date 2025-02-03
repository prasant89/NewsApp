package com.kaerusworld.newsapp

import android.app.Application
import android.util.Log
import com.kaerusworld.newsapp.data.db.NewsDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("NewsDatabase", "Database is being initialized")
        NewsDatabase.getDatabase(this)
    }
}