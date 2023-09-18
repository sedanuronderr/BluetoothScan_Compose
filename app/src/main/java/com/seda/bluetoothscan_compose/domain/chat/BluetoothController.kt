package com.seda.bluetoothscan_compose.domain.chat

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<bluetoothdevice>>
    val pairedDevices: StateFlow<List<bluetoothdevice>>
    fun startDiscovery()
    fun stopDiscovery()

    fun release()
}