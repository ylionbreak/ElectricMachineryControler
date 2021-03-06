package hzl.electricmachinerycontroler;



import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ScrollView;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.MaterialEditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import fr.ganfra.materialspinner.MaterialSpinner;


/**
 *
 */
public class ParameterConfigureActivity extends ActionBarActivity {
	Toolbar toolbar;
	ScrollView scrollView;
	MaterialSpinner spinner;
	String[] ITEMS = {"AB", "AABB", "ABZ", "AABBZZ"};
	ArrayAdapter<String> adapter;
	RadioButton can0;
	RadioButton can120;
	Toast toast;
	MaterialEditText xianSuEditText;
	MaterialEditText jianSuBiEditText;
	MaterialEditText eDingDianYaEditText;
	MaterialEditText fengZhiDianYaEditText;
	MaterialEditText eDingDianLiuEditText;
	MaterialEditText fengZhiDianLiuEditText;
	MaterialEditText eDingZhuanSu;
	MaterialEditText zuiDaJiaSuDuEditText;
	ButtonRectangle changeDir;
	ButtonRectangle tryDir;
	Timer timer;
	boolean listening=false;

	Thread lisThread = new Thread(new Runnable() {
		@Override
		public void run() {
			listenForMessages(MainActivity.bluetoothManager.getTransferSocket());
		}
	});
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 12345:
					Bundle bundle=msg.getData();
					byte[] bytes=bundle.getByteArray("bytes");
					try {
						Log.e("hex",String.valueOf((char)bytes[0])+" "+String.valueOf((char)bytes[1])+
								" "+Integer.toHexString(bytes[2])+" "+Integer.toHexString(bytes[3])+
								" "+Integer.toHexString(bytes[4])+" "+Integer.toHexString(bytes[5])+
								" "+Integer.toHexString(bytes[6])+" "+Integer.toHexString(bytes[7]));
						//获取CAN配置：GN(Get Can )
						if(bytes[0]=='g'&&bytes[1]=='n'){
							if(bytes[4]==1){
								can120.setChecked(true);
							}else if(bytes[4]==0){
								can0.setChecked(true);
							}
						}
						//读取编码器：GE(Get Encode)
						if(bytes[0]=='g'&&bytes[1]=='e'){
							spinner.setSelection(bytes[2]);
							int num;
							num=bytes[3];
							num=num<<8;
							num+=bytes[4];
							xianSuEditText.setText(String.valueOf(num));
						}
						//读取减速比：GL(Get scaLe)
						if(bytes[0]=='g'&&bytes[1]=='l'){
							jianSuBiEditText.setText(String.valueOf((bytes[2]*256+bytes[3])/100f));
						}
						//读取电机电压：GP(Get Voltage)
						if(bytes[0]=='g'&&bytes[1]=='p'){
							eDingDianYaEditText.setText(String.valueOf( (bytes[3]*256+bytes[4])/10f ));
							fengZhiDianYaEditText.setText(String.valueOf( (bytes[5]*256+bytes[6])/10f ));
						}
						//读取电机电流：GC(Get Current)
						if(bytes[0]=='g'&&bytes[1]=='c'){
							eDingDianLiuEditText.setText(String.valueOf( (bytes[3]*256+bytes[4])/100f ));
							fengZhiDianLiuEditText.setText(String.valueOf( (bytes[5]*256+bytes[6])/100f ));
						}
						//读取 SD(Set speeD)
						if(bytes[0]=='g'&&bytes[1]=='c'){
							int pwm=bytes[3]<<24+bytes[4]<<16+bytes[5]<<8+bytes[6];
							eDingZhuanSu.setText(String.valueOf( pwm*60/( 4*Integer.valueOf(xianSuEditText.getText().toString()) ) ));
						}
						//读取最大加速度：GA(Get Accelerate)
						if(bytes[0]=='g'&&bytes[1]=='a'){
							zuiDaJiaSuDuEditText.setText(String.valueOf( bytes[2]*256+bytes[3] ));
						}
					}catch (Exception e){
						e.printStackTrace();
					}
					break;
			}
			super.handleMessage(msg);
		}
	};


	private BluetoothSPP.OnDataReceivedListener ParActivityDataRecier = new BluetoothSPP.OnDataReceivedListener() {
		@Override
		public void onDataReceived(byte[] bytes, String s) {
			try {
//				toast.setText("指令为:"+bytes[0]+bytes[1]+bytes[2]+bytes[3]+bytes[4]+bytes[5]+bytes[6]+bytes[7]);
//				toast.show();
				Log.e("hex",String.valueOf((char)bytes[0])+" "+String.valueOf((char)bytes[1])+
						" "+Integer.toHexString(bytes[2])+" "+Integer.toHexString(bytes[3])+
						" "+Integer.toHexString(bytes[4])+" "+Integer.toHexString(bytes[5])+
						" "+Integer.toHexString(bytes[6])+" "+Integer.toHexString(bytes[7]));
				//获取CAN配置：GN(Get Can )
				if(bytes[0]=='g'&&bytes[1]=='n'){
					if(bytes[4]==1){
						can120.setChecked(true);
					}else if(bytes[4]==0){
						can0.setChecked(true);
					}
				}
				//读取编码器：GE(Get Encode)
				if(bytes[0]=='g'&&bytes[1]=='e'){
					spinner.setSelection(bytes[2]);
					int num;
					num=bytes[3];
					num=num<<8;
					num+=bytes[4];
					xianSuEditText.setText(String.valueOf(num));
				}
				//读取减速比：GL(Get scaLe)
				if(bytes[0]=='g'&&bytes[1]=='l'){
					jianSuBiEditText.setText(String.valueOf((bytes[2]*256+bytes[3])/100f));
				}
				//读取电机电压：GP(Get Voltage)
				if(bytes[0]=='g'&&bytes[1]=='p'){
					eDingDianYaEditText.setText(String.valueOf( (bytes[3]*256+bytes[4])/10f ));
					fengZhiDianYaEditText.setText(String.valueOf( (bytes[5]*256+bytes[6])/10f ));
				}
				//读取电机电流：GC(Get Current)
				if(bytes[0]=='g'&&bytes[1]=='c'){
					eDingDianLiuEditText.setText(String.valueOf( (bytes[3]*256+bytes[4])/100f ));
					fengZhiDianLiuEditText.setText(String.valueOf( (bytes[5]*256+bytes[6])/100f ));
				}
				//读取 SD(Set speeD)
				if(bytes[0]=='g'&&bytes[1]=='c'){
					int pwm=bytes[3]<<24+bytes[4]<<16+bytes[5]<<8+bytes[6];
					eDingZhuanSu.setText(String.valueOf( pwm*60/( 4*Integer.valueOf(xianSuEditText.getText().toString()) ) ));
				}
				//读取最大加速度：GA(Get Accelerate)
				if(bytes[0]=='g'&&bytes[1]=='a'){
					zuiDaJiaSuDuEditText.setText(String.valueOf( bytes[2]*256+bytes[3] ));
				}
			}catch (Exception e){
				e.printStackTrace();
				toast.setText("蓝牙无连接");
				toast.show();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("rec","rec");
		setContentView(R.layout.activity_parameter_configure_layout);
		toast=Toast.makeText(this,"异常",Toast.LENGTH_LONG);
//		try{
//			if(MainActivity.bt==null||!MainActivity.bt.isBluetoothEnabled()||!MainActivity.bt.isBluetoothAvailable()||MainActivity.bt.getConnectedDeviceAddress()==null){
//				toast.setText("蓝牙异常");
//				toast.show();
//			}
//		}catch (Exception e){
//			e.printStackTrace();
//		}
		initToolBar();
		initBtn();
		initSpinner();
		initRadioBtn();
		initEditText();
		//MainActivity.bt.setOnDataReceivedListener(ParActivityDataRecier);
		timer = new Timer(true);
		timer.schedule(new java.util.TimerTask() { public void run(){
			listenForMessages(MainActivity.bluetoothManager.getTransferSocket()); }
		}, 0, 100);
		initUI();
		scrollView = (ScrollView)findViewById(R.id.scroll);

	}
	private void initEditText(){
		xianSuEditText=(MaterialEditText)findViewById(R.id.XianSuEditText);
		jianSuBiEditText=(MaterialEditText)findViewById(R.id.JianSuBiEditText);
		eDingDianYaEditText=(MaterialEditText)findViewById(R.id.EDingDianYaEditText);
		fengZhiDianYaEditText=(MaterialEditText)findViewById(R.id.FengZhiDianYaEditText);
		eDingDianLiuEditText =(MaterialEditText)findViewById(R.id.EDingDianLiuEditText);
		fengZhiDianLiuEditText=(MaterialEditText)findViewById(R.id.FengZhiDianLiuEditText);
		eDingZhuanSu =(MaterialEditText)findViewById(R.id.EDingZhuanSu);
		zuiDaJiaSuDuEditText=(MaterialEditText)findViewById(R.id.ZuiDaJiaSuDuEditText);
		xianSuEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					try {
						byte[] sendByte = new byte[]{'S', 'E', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
						int num = Integer.valueOf(xianSuEditText.getText().toString());
						sendByte[2]=(byte)spinner.getSelectedItemPosition();
						sendByte[3]=(byte)(num>>8);
						sendByte[4]=(byte)(num);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bluetoothManager.send(sendByte);
					} catch (Exception e) {
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
		jianSuBiEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					try {
						int num=(int)(Float.valueOf(jianSuBiEditText.getText().toString())*100);
						byte[] sendByte = new byte[]{'S', 'L', (byte)(num>>8), (byte)(num), 0x00, 0x00, 0x00, 0x00};
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bluetoothManager.send(sendByte);
					}catch(Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
		eDingDianYaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					try {
						int num= (int)(Float.valueOf(eDingDianYaEditText.getText().toString()) *10);
						byte[] sendByte = new byte[]{'S', 'P', 0x00, (byte)(num>>8), (byte)(num), 0x00, 0x00, 0x00};
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bluetoothManager.send(sendByte);
					}catch(Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
		fengZhiDianYaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					try {
						int num= (int)(Float.valueOf(fengZhiDianYaEditText.getText().toString()) *10);
						byte[] sendByte = new byte[]{'S', 'P', 0x01, (byte)(num>>8), (byte)(num), 0x00, 0x00, 0x00};
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bluetoothManager.send(sendByte);
					}catch(Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
		eDingDianLiuEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					try {
						int num = (int)(Float.valueOf(eDingDianLiuEditText.getText().toString()) *100);
						byte[] sendByte = new byte[]{'S', 'C', 0x00, (byte)(num>>8), (byte)(num), 0x00, 0x00, 0x00};
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bluetoothManager.send(sendByte);
					}catch(Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
		fengZhiDianLiuEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					try {
						int num= (int)(Float.valueOf(fengZhiDianLiuEditText.getText().toString()) *100);
						byte[] sendByte = new byte[]{'S', 'C', 0x01, (byte)(num>>8), (byte)(num), 0x00, 0x00, 0x00};
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bluetoothManager.send(sendByte);
					}catch(Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
		eDingZhuanSu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					try {
						int num=Integer.valueOf(eDingZhuanSu.getText().toString())*Integer.valueOf(xianSuEditText.getText().toString())*4/60;
						byte[] sendByte = new byte[]{'S', 'D', 0x00, (byte)(num>>24), (byte)(num>>16), (byte)(num>>8), (byte)(num), 0x00};
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bluetoothManager.send(sendByte);
					}catch(Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
		//设置最大加速度：SA(Set Accelerate)
		zuiDaJiaSuDuEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					try {
						int num= (int)( Float.valueOf(zuiDaJiaSuDuEditText.getText().toString())*100 );
						byte[] sendByte = new byte[]{'S', 'A',(byte)(num>>8), (byte)(num), 0x00, 0x00, 0x00, 0x00};
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bluetoothManager.send(sendByte);
					}catch(Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
	}
	/**
	 * 配置CAN阻抗
	 */
	private void initRadioBtn(){
		can0=(RadioButton)findViewById(R.id.CAN0);
		can120=(RadioButton)findViewById(R.id.CAN120);

		can0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					try {
						byte[] sendByte = new byte[] {'S','R',0x00,0x00,0x00,0x00,0x00,(byte)('S'+'R')};
						MainActivity.bluetoothManager.send(sendByte);
					}catch (Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
		can120.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					try {
						byte[] sendByte = new byte[] {'S','R',0x01,0x00,0x00,0x00,0x00,(byte)('S'+'R')};
						MainActivity.bluetoothManager.send(sendByte);
					}catch (Exception e){
						e.printStackTrace();
						toast.setText("蓝牙无连接");
						toast.show();
					}
				}
			}
		});
	}
	private void initSpinner(){
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ITEMS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner = (MaterialSpinner) findViewById(R.id.spinner);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position!=0) {
					if (xianSuEditText.getText().toString().equalsIgnoreCase("") || xianSuEditText.getText().toString() == null) {
						toast.setText("线速未填写");
						toast.show();
					} else if (Integer.valueOf(xianSuEditText.getText().toString()) > 65000) {
						toast.setText("线速超过大小");
						toast.show();
					} else {
						//设置编码器：SE(Set Encode)
						try {
							byte[] sendByte = new byte[]{'S', 'E', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
							int num = Integer.valueOf(xianSuEditText.getText().toString());
							sendByte[2]=(byte)position;
							sendByte[3]=(byte)(num>>8);
							sendByte[4]=(byte)(num);
							for(int i=0;i<7;i++){
								sendByte[7]+=sendByte[i];
							}
							MainActivity.bluetoothManager.send(sendByte);
						} catch (Exception e) {
							e.printStackTrace();
							toast.setText("蓝牙无连接");
							toast.show();
						}
					}
				}

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
	private void initToolBar(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle("参数配置");
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		toolbar.setClickable(false);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent=new Intent(ParameterConfigureActivity.this,MainActivity.class);  //方法1
//				startActivity(intent);
				finish();
			}
		});
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}
	private void initBtn(){
		changeDir=(ButtonRectangle)findViewById(R.id.changeDir);
		tryDir=(ButtonRectangle)findViewById(R.id.tryDir);
		changeDir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					byte[] sendByte = new byte[]{'S', 'D', 0x01, 0x00, 0x00, 0x00, 0x00, 0x00};
					for(int i=0;i<7;i++){
						sendByte[7]+=sendByte[i];
					}
					MainActivity.bluetoothManager.send(sendByte);
				} catch (Exception e) {
					e.printStackTrace();
					toast.setText("蓝牙无连接");
					toast.show();
				}
			}
		});
		tryDir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					byte[] sendByte = new byte[]{'S', 'D', 0x02, 0x00, 0x00, 0x00, 0x00, 0x00};
					for(int i=0;i<7;i++){
						sendByte[7]+=sendByte[i];
					}
					MainActivity.bluetoothManager.send(sendByte);
				} catch (Exception e) {
					e.printStackTrace();
					toast.setText("蓝牙无连接");
					toast.show();
				}
			}
		});
	}
	private void initUI(){
		try {
			byte[] sendByte;
			sendByte = new byte[] {'L','P',0x00,0x00,0x00,0x00,0x00,0x00};
			for(int i=0;i<7;i++){
				sendByte[7]+=sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);
			for(int i=0;i<2000;i++);
			MainActivity.bluetoothManager.send(sendByte);
		}catch (Exception e){
			e.printStackTrace();
			toast.setText("蓝牙无连接");
			toast.show();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_parameter_configure, menu);
		return true;
	}

	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.action_watchPar:
					break;
				case R.id.next_icon:
					Intent intent=new Intent(ParameterConfigureActivity.this,SetPIDActivity.class);  //方法1
					startActivity(intent);
					finish();
					break;
			}
			return true;
		}
	};

	@Override
	protected void onDestroy() {
		try {
			timer.cancel();
			super.onDestroy();
		}catch (Exception e){
			e.printStackTrace();
		}
	}



	private void listenForMessages(BluetoothSocket socket){
		listening = true;
		Message message;
		try {
			InputStream inputStream = socket.getInputStream();
			int bufferSize = 10;
			byte[] bytes = new byte[bufferSize];
			int length = inputStream.read(bytes,0,10);
			while(length!=10){
				length=length+inputStream.read(bytes,length,10-length);
			}
//			if(bytes[0]>122||bytes[0]<97){
//				byte onebtye;
//				do{
//					onebtye = (byte)inputStream.read();
//				}
//				while(onebtye!=(byte)0x0a);
//				onebtye=bytes[0];
//				inputStream.read(bytes,1,9);
//
//			}
			message=new Message();
			message.what=12345;
			Bundle bundle=new Bundle();
			bundle.putByteArray("bytes",bytes);
			message.setData(bundle);
			myHandler.sendMessage(message);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
