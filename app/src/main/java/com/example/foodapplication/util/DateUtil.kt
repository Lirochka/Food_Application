package com.example.foodapplication.util

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {

    private val sdf = SimpleDateFormat("MMMMM d, yyyy")

    fun longToDate(long: Long): Date {
        return Date(long)
    }

    fun dateToLong(date: Date): Long {
        return date.time / 1000 // return seconds
    }

    fun createTimestamp(): Date{
        return Date()
    }
}