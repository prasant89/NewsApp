package com.kaerusworld.newsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaerusworld.newsapp.data.model.Converters
import com.kaerusworld.newsapp.domain.model.NewsArticle

@Database(entities = [NewsArticle::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
