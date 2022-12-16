package com.victorvalencia.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface NetworkHandler {
    val isConnected: Boolean?
}

val Context.connectivityManager: ConnectivityManager
    get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

class NetworkHandlerImpl @Inject constructor(@ApplicationContext private val context: Context) : NetworkHandler {
    override val isConnected get() = networkIsActive()

    private fun networkIsActive(): Boolean? {
        val network = context.connectivityManager.activeNetwork
        val networkCapabilities = context.connectivityManager.getNetworkCapabilities(network)
        return (
                networkCapabilities != null && (
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                        )
                )
    }
}