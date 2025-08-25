package com.danzucker.notemark.core.data.networkchecker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.danzucker.notemark.core.domain.networkchecker.NetworkChecker
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DeviceNetworkChecker(
    private val context: Context
) : NetworkChecker  {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isDeviceConnected(): Flow<Boolean> {
        return callbackFlow {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: android.net.Network) {
                    trySend(true)
                }

                override fun onLost(network: android.net.Network) {
                    trySend(false)
                }
            }

            connectivityManager.requestNetwork(networkRequest, networkCallback)

            // Send initial state
            trySend(isCurrentlyConnected())
            awaitClose {
                println("Flow was closed.")
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }
    }

    override fun isCurrentlyConnected(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}