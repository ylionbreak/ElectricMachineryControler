package hzl.electricmachinerycontroler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;

import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import hzl.electricmachinerycontroler.content.DBManager;
import hzl.electricmachinerycontroler.content.TuiContent;
import hzl.electricmachinerycontroler.content.notebookdata;

/**
 * Created by YLion on 2015/7/16.
 */
public class MainActivity extends ActionBarActivity {
	BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	Toolbar toolbar;
	ButtonRectangle quDongSet;
	ButtonRectangle testOrder;
	ButtonRectangle testICircle;
	ButtonRectangle testVCircle;
	ButtonRectangle testPCircle;
	ButtonRectangle aboutQudong;
	ButtonRectangle connectBtn;
	static BluetoothSPP bt;
	String macAdd;
	Toast errorToast;
	boolean connecting=false;
	Toast tuiToast;
	Toast toast;
	String tuiString;
	static private DBManager dataManager;
	static public BluetoothManager bluetoothManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		//toast=Toast.makeText(this,"",Toast.LENGTH_LONG);
		//Bmob.initialize(this,"2fd5105040c345638bf1905310730101");
		//isTui();
		//dataManager = new DBManager(this);
		//updataContent();
//		try{
//				bt = new BluetoothSPP(MainActivity.this);
//				bt.setupService();
//				bt.enable();
//				bt.startService(BluetoothState.DEVICE_OTHER);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
		errorToast=Toast.makeText(this,"",Toast.LENGTH_SHORT);
		initBlueTooth();
		bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
			@Override
			public void onDeviceConnected(String s, String s2) {
				Message message = new Message();
				message.what = 12345;
				myHandler.sendMessage(message);
			}
			@Override
			public void onDeviceDisconnected() {

			}
			@Override
			public void onDeviceConnectionFailed() {
				Message message = new Message();
				message.what = 54321;
				myHandler.sendMessage(message);
			}
		});
		connectBtn=(ButtonRectangle)findViewById(R.id.connect);
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
		initToolBar();
		connectBtn.setText("蓝牙连接(未连接)");
	}

	private void initBlueTooth(){
		try {
			bt = new BluetoothSPP(MainActivity.this);
			bt.setupService();
			bt.enable();
			bt.startService(BluetoothState.DEVICE_OTHER);
		}catch (Exception e){
			errorToast.setText("蓝牙异常或未开启");
			errorToast.show();
			connectBtn.setText("蓝牙连接(未连接)");
			e.printStackTrace();
		}
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
				bt=null;
				bluetoothManager=new BluetoothManager();
				final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAdd);
				connecting=false;
				Thread connectThread = new Thread(new Runnable() {
					@Override
					public void run() {
						Message message = new Message();
						message.what = 11111;
						myHandler.sendMessage(message);
						if(!connecting) {
							bluetoothManager.connectDevice(device);
							//bt.connect(macAdd);
							connecting=true;
						}
						if(bluetoothManager.getTransferSocket()!=null){
							message = new Message();
							message.what = 12345;
							Bundle bundle=new Bundle();
							bundle.putString("name",device.getName());
							myHandler.sendMessage(message);
						}
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
			bt = new BluetoothSPP(MainActivity.this);
			bt.setupService();
			bt.enable();
			bt.startService(BluetoothState.DEVICE_OTHER);
			Intent intent = new Intent(getApplicationContext(), DeviceList.class);
			startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
		}
	};

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 12345:
					errorToast.setText("蓝牙连接成功");
					errorToast.show();
					connectBtn.setText("蓝牙连接"+msg.getData().getString("name"));
					//connectBtn.setText("蓝牙连接"+bt.getConnectedDeviceName());
					break;
				case 54321:
					errorToast.setText("蓝牙连接失败"+'\n'+"请再尝试");
					errorToast.show();
					bt = new BluetoothSPP(MainActivity.this);
					bt.setupService();
					bt.enable();
					bt.startService(BluetoothState.DEVICE_OTHER);
					connectBtn.setText("蓝牙连接(未连接)");
					break;
				case 11111:
					errorToast.setText("连接中");
					errorToast.show();
					break;
			}
			super.handleMessage(msg);
		}
	};
	public void updataContent(){
			BmobQuery<notebookdata> bmobQuery = new BmobQuery<>();
			bmobQuery.setLimit(1000);

			bmobQuery.findObjects(this, new FindListener<notebookdata>() {
				@Override
				public void onSuccess(List<notebookdata> object) {

					if(dataManager.refreshnotebookdata(object)){
						toast=Toast.makeText(MainActivity.this,"自动更新手册成功",Toast.LENGTH_LONG);
					}
				}
				@Override
				public void onError(int code, String msg) {
					toast=Toast.makeText(MainActivity.this,"更新失败",Toast.LENGTH_LONG);
				}
			});
	}
	public void isTui(){

		BmobQuery<TuiContent> bmobQuery = new BmobQuery<>();
		bmobQuery.setLimit(1000);

		bmobQuery.findObjects(this, new FindListener<TuiContent>() {
			@Override
			public void onSuccess(List<TuiContent> object) {
				try {

					if (object.get(0).getContent()!=null&&object.get(0).getNum()==1){
						tuiToast=Toast.makeText(MainActivity.this,tuiString,Toast.LENGTH_LONG);
						tuiToast.setGravity(100,tuiToast.getXOffset(),500);
						tuiToast.show();
					}
				}catch (Exception e){

				}
			}
			@Override
			public void onError(int code, String msg) {
			}
		});

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		dataManager.closeDB();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
