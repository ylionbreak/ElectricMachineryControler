package hzl.electricmachinerycontroler;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by YLion on 2015/5/6.
 */
public class BluetoothManager {

	BluetoothSocket transferSocket;
	Boolean listening=false;
	String returnMessage="";

	void connectDevice(BluetoothDevice device){
		try{
			UUID uuid =UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
			BluetoothSocket bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
			bluetoothSocket.connect();
			transferSocket=bluetoothSocket;
			//listenForMessages(bluetoothSocket);
		}catch (IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void send(byte[] bytes){
		OutputStream outputStream;
		try {
			outputStream = transferSocket.getOutputStream();
			outputStream.write(bytes);
		}catch (IOException e){
			e.printStackTrace();
		}
	}




	public BluetoothSocket getTransferSocket() {
		return transferSocket;
	}

}



