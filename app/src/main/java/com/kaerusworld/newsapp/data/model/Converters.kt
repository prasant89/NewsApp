package com.kaerusworld.newsapp.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromSource(source: Source): String {
        // Convert Source object to JSON String
        return gson.toJson(source)
    }

    @TypeConverter
    fun toSource(sourceJson: String): Source {
        val type = object : TypeToken<Source>() {}.type
        // Convert JSON String back to Source object
        return gson.fromJson(sourceJson, type)
    }
}