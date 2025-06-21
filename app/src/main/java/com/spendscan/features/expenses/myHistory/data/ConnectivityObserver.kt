package com.spendscan.features.expenses.myHistory.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


enum class NetworkStatus {
    Available, // Сеть доступна
    Unavailable, // Сеть недоступна
    Losing, // Сеть теряется
    Lost // Сеть потеряна
}

// ConnectivityObserver: Отслеживает изменения в состоянии сетевого подключения.
// Предоставляет Flow<NetworkStatus> для наблюдения за статусом сети.
class ConnectivityObserver(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observe(): Flow<NetworkStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Отправляем статус "Доступна"
                launch { send(NetworkStatus.Available) }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                // Отправляем статус "Теряется"
                launch { send(NetworkStatus.Losing) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // Отправляем статус "Потеряна"
                launch { send(NetworkStatus.Lost) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                // Отправляем статус "Недоступна"
                launch { send(NetworkStatus.Unavailable) }
            }
        }


        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)


        val initialStatus = if (isNetworkAvailable()) NetworkStatus.Available else NetworkStatus.Unavailable
        launch { send(initialStatus) }


        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    // Вспомогательная функция для проверки наличия активного соединения (для начального состояния)
    private fun isNetworkAvailable(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}