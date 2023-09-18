package com.seda.bluetoothscan_compose.data.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.seda.bluetoothscan_compose.domain.chat.BluetoothController
import com.seda.bluetoothscan_compose.domain.chat.BluetoothDeviceDomain
import com.seda.bluetoothscan_compose.domain.chat.FoundDeviceReceiver
import com.seda.bluetoothscan_compose.domain.chat.bluetoothdevice
import com.seda.bluetoothscan_compose.domain.chat.toBluetoothDeviceDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AndroidBluetoothController(
  private val context: Context
) :BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }


    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())

    override val scannedDevices: StateFlow<List<bluetoothdevice>>
        get() =_scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())

    override val pairedDevices: StateFlow<List<bluetoothdevice>>
        get() = _pairedDevices.asStateFlow()

private val foundDeviceReceiver=FoundDeviceReceiver{device->

             _scannedDevices.update { devices->
                 val newDevice = device.toBluetoothDeviceDomain()
                 if(newDevice in devices) devices else devices + newDevice

             }
}

    init {
        updatePairedDevices()
    }

    @SuppressLint("MissingPermission")
    override fun startDiscovery() {
        if (!hasPermission(android.Manifest.permission.BLUETOOTH_SCAN)){
              return
        }


        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        updatePairedDevices()
        bluetoothAdapter?.startDiscovery()
    }

    @SuppressLint("MissingPermission")
    override fun stopDiscovery() {


        if(!hasPermission(android.Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }

        bluetoothAdapter?.cancelDiscovery()
    }

    override fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
    }

    @SuppressLint("MissingPermission")
    private fun updatePairedDevices(){
        if (!hasPermission(android.Manifest.permission.BLUETOOTH_CONNECT)){

        }
        bluetoothAdapter
            ?.bondedDevices
            ?.map {it.toBluetoothDeviceDomain()}
            ?.also {_pairedDevices.update { it }  }
    }

    private  fun hasPermission(permission:String):Boolean{
        return context.checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED
    }
}