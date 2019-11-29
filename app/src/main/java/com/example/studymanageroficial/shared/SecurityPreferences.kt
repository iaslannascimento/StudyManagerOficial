package com.example.studymanageroficial.shared

import android.content.Context
import android.content.SharedPreferences

class SecurityPreferences (c: Context){
    private val sharedPreferences:SharedPreferences=c.getSharedPreferences("application",Context.MODE_PRIVATE)

    fun setPreferences(key:String, value: String){sharedPreferences.edit().putString(key,value).commit() }

    fun getPreferences(key: String):String{
        return sharedPreferences.getString(key,"").toString()}


}