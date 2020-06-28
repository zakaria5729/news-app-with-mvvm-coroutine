package com.zakariahossain.newsmvvm.data.db

import androidx.room.TypeConverter
import com.zakariahossain.newsmvvm.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String = source.name

    @TypeConverter
    fun toSource(name: String): Source = Source(name, name)
}