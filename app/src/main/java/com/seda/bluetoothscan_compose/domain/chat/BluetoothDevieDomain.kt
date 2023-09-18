package com.seda.bluetoothscan_compose.domain.chat

import android.bluetooth.BluetoothDevice

fun BluetoothDevice.toBluetoothDeviceDomain():BluetoothDeviceDomain{
    return BluetoothDeviceDomain(name = name,address)
}