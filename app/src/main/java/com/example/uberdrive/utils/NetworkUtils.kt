package com.example.uberdrive.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkUtils(private val networkCallback: NetworkCallback): BroadcastReceiver() {

    fun registerBroadcastReceiver(context: Context, networkUtils: NetworkUtils) {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkUtils, intentFilter)
    }

    fun unRegisterBroadcastReceiver(context: Context, networkUtils: NetworkUtils) {
        context.unregisterReceiver(networkUtils)
    }


    private fun isNetworkAvailable(context: Context?): Boolean {

        val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = manager.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnected
    }


    override fun onReceive(context: Context?, intent: Intent?) {

        val isConnected = isNetworkAvailable(context)
        if (isConnected) {
            networkCallback.networkState(isConnected)
        }
        else {
            networkCallback.networkState(isConnected)
        }
    }


    interface NetworkCallback {
        fun networkState(available : Boolean)
    }

}