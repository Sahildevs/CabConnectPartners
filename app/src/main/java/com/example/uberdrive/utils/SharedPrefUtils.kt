package com.example.uberdrive.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefUtils @Inject constructor(@ApplicationContext private val context: Context) {

    private val SHARED_PREF_FILE = "uberdrivedatastore"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)

    //Using Method overloading concept which allows to define multiple methods with the same name
    // but different parameter types.

    //Add data to shared preference of type string
    fun addData(key: String, value: String) {

        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()

    }


    //Add data to shared preference of type integer
    fun addData(key: String, value: Int) {

        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()

    }


    //Retrieves data from SharedPreferences of type string
    fun getData(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)

    }

    //Retrieves data from SharedPreferences of type string
    fun getData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)

    }


    //Clears user stored data from SharedPreferences
    fun deleteData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

    }


}