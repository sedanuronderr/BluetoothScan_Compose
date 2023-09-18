package com.seda.bluetoothscan_compose.presentation

import com.seda.bluetoothscan_compose.domain.chat.bluetoothdevice

data class BluetoothUiState(
    val scannedDevices: List<bluetoothdevice> = emptyList(),
    val pairedDevices: List<bluetoothdevice> = emptyList(),
)