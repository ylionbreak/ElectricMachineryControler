package hzl.electricmachinerycontroler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

/**
 * Created by YLion on 2015/7/16.
 */
public class MainActivity extends ActionBarActivity {
	BluetoothManager bluetoothManager=new BluetoothManager();
	BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	Toolbar toolbar;
	ButtonRectangle quDongSet;
	ButtonRectangle testOrder;
	ButtonRectangle testICircle;
	ButtonRectangle testVCircle;
	ButtonRectangle testPCircle;
	ButtonRectangle aboutQudong;
	ButtonFlat connectBtn;
	BluetoothSPP bt;
	String macAdd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("ParameterConfigureActivity", "onCreate");
		setContentView(R.layout.main_layout);
		//initBlueTooth();
		initToolBar();
		connectBtn=(ButtonFlat)findViewById(R.id.connect);
		quDongSet=(ButtonRectangle)findViewById(R.id.quDongSet);
		testOrder=(ButtonRectangle)findViewById(R.id.testOrder);
		testICircle=(ButtonRectangle)findViewById(R.id.IcircleTest);
		testVCircle=(ButtonRectangle)findViewById(R.id.VcircleTest);
		testPCircle=(ButtonRectangle)findViewById(R.id.PcircleTest);
		aboutQudong=(ButtonRectangle)findViewById(R.id.aboutQudong);
		quDongSet.setOnClickListener(quDongOnClickListener);
		testOrder.setOnClickListener(TestOrderOnClickListener);
		connectBtn.setOnClickListener(connectOnClickListener);
		aboutQudong.setOnClickListener(aboutQudongClickListener);
		testICircle.setOnClickListener(new CircleTestOnClickListener(MainActivity.this,this,TestCircleAcitvity.CURRENT_TEST));
		testVCircle.setOnClickListener(new CircleTestOnClickListener(MainActivity.this,this,TestCircleAcitvity.SPEED_TEST));
		testPCircle.setOnClickListener(new CircleTestOnClickListener(MainActivity.this,this,TestCircleAcitvity.LOCATION_TEST));
	}

	private void initBlueTooth(){
		bt = new BluetoothSPP(MainActivity.this);
		bt.setupService();
		bt.enable();
		bt.startService(BluetoothState.DEVICE_OTHER);
	}

	private void initToolBar(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle("参数配置");
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		toolbar.setClickable(false);
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		//getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.action_watchPar:
					break;
			}
			return true;
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
			if(resultCode == Activity.RESULT_OK){
				//bt.connect(data.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS,"00:00:00:00:00:00"));
				macAdd =data.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS);
				Thread connectThread = new Thread(new Runnable() {
					@Override
					public void run() {
						bt.connect(macAdd);
					}
				});
				connectThread.start();
			}
		} else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
			if(resultCode == Activity.RESULT_OK) {
				bt.setupService();
				bt.startService(BluetoothState.DEVICE_ANDROID);
				Log.e("setup","setup");
//				setup();
			} else {
				// Do something if user doesn't choose any device (Pressed back)
			}
		}
	}

	private View.OnClickListener aboutQudongClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, AboutQudongActivity.class);  //方法1
			startActivity(intent);
		}
	};
	private View.OnClickListener quDongOnClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, ParameterConfigureActivity.class);  //方法1
			startActivity(intent);
		}
	};
	private View.OnClickListener TestOrderOnClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(MainActivity.this,TestOrderActivity.class);  //方法1
			startActivity(intent);
		}
	};
	private View.OnClickListener connectOnClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), DeviceList.class);
			startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
		}
	};
}
