/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;

/**
 * This class ensures that the BLE callbacks will be called on the main (UI) thread.
 * Handler parameter was added to {@link android.bluetooth.BluetoothDevice#connectGatt(Context, boolean, BluetoothGattCallback, int, int, Handler)}
 * in Android Oreo, before that the behavior was undefined.
 */
abstract class MainThreadBluetoothGattCallback extends BluetoothGattCallback {
	private Handler mHandler;

	void setHandler(final Handler handler) {
		mHandler = handler;
	}

	private void runOnUiThread(final Runnable runnable) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			mHandler.post(runnable);
		} else {
			runnable.run();
		}
	}

	abstract void onConnectionStateChangeSafe(final BluetoothGatt gatt, final int status, final int newState);
	abstract void onServicesDiscoveredSafe(final BluetoothGatt gatt, final int status);
	abstract void onCharacteristicReadSafe(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status);
	abstract void onCharacteristicWriteSafe(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status);
	abstract void onCharacteristicChangedSafe(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic);
	abstract void onDescriptorReadSafe(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status);
	abstract void onReadRemoteRssiSafe(final BluetoothGatt gatt, final int rssi, final int status);
	abstract void onDescriptorWriteSafe(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status);
	abstract void onReliableWriteCompletedSafe(final BluetoothGatt gatt, final int status);
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	abstract void onMtuChangedSafe(final BluetoothGatt gatt, final int mtu, final int status);
	@RequiresApi(api = Build.VERSION_CODES.O)
	abstract void onPhyReadSafe(final BluetoothGatt gatt, final int txPhy, final int rxPhy, final int status);
	@RequiresApi(api = Build.VERSION_CODES.O)
	abstract void onPhyUpdateSafe(final BluetoothGatt gatt, final int txPhy, final int rxPhy, final int status);
	@RequiresApi(api = Build.VERSION_CODES.O)
	abstract void onConnectionUpdatedSafe(final BluetoothGatt gatt, final int interval, final int latency, final int timeout, final int status);

	@Override
	public final void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
		runOnUiThread(() -> onConnectionStateChangeSafe(gatt, status, newState));
	}

	@Override
	public final void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
		runOnUiThread(() -> onServicesDiscoveredSafe(gatt, status));
	}

	@Override
	public final void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
		runOnUiThread(() -> onCharacteristicReadSafe(gatt, characteristic, status));
	}

	@Override
	public final void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
		runOnUiThread(() -> onCharacteristicWriteSafe(gatt, characteristic, status));
	}

	@Override
	public final void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
		runOnUiThread(() -> onCharacteristicChangedSafe(gatt, characteristic));
	}

	@Override
	public final void onDescriptorRead(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
		runOnUiThread(() -> onDescriptorReadSafe(gatt, descriptor, status));
	}

	@Override
	public final void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
		runOnUiThread(() -> onDescriptorWriteSafe(gatt, descriptor, status));
	}

	@Override
	public final void onReadRemoteRssi(final BluetoothGatt gatt, final int rssi, final int status) {
		runOnUiThread(() -> onReadRemoteRssiSafe(gatt, rssi, status));
	}

	@Override
	public final void onReliableWriteCompleted(final BluetoothGatt gatt, final int status) {
		runOnUiThread(() -> onReliableWriteCompletedSafe(gatt, status));
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	public final void onMtuChanged(final BluetoothGatt gatt, final int mtu, final int status) {
		runOnUiThread(() -> onMtuChangedSafe(gatt, mtu, status));
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public final void onPhyRead(final BluetoothGatt gatt, final int txPhy, final int rxPhy, final int status) {
		runOnUiThread(() -> onPhyReadSafe(gatt, txPhy, rxPhy, status));
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public final void onPhyUpdate(final BluetoothGatt gatt, final int txPhy, final int rxPhy, final int status) {
		runOnUiThread(() -> onPhyUpdateSafe(gatt, txPhy, rxPhy, status));
	}

	// This method is still hidden in Android Oreo
	// @Override
	@RequiresApi(api = Build.VERSION_CODES.O)
	public final void onConnectionUpdated(final BluetoothGatt gatt, final int interval, final int latency, final int timeout, final int status) {
		runOnUiThread(() -> onConnectionUpdatedSafe(gatt, interval, latency, timeout, status));
	}
}