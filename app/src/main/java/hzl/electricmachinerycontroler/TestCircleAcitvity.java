package hzl.electricmachinerycontroler;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.MaterialEditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.Slider;

import java.io.IOException;
import java.io.InputStream;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by YLion on 2015/7/17.
 */
public class TestCircleAcitvity extends ActionBarActivity {
	Toolbar toolbar;
	final static int CURRENT_TEST = 100;
	final static int SPEED_TEST = 101;
	final static int LOCATION_TEST = 102;
	TextView par1;
	TextView par2;
	TextView par3;
	Slider slider;
	MaterialEditText par1Edit;
	MaterialEditText par2Edit;
	MaterialEditText par3Edit;
	MaterialEditText PEdit;
	MaterialEditText IEdit;
	MaterialEditText DEdit;
	Toast toast;
	ButtonRectangle f1;
	ButtonRectangle f5;
	ButtonRectangle f10;
	ButtonRectangle f50;
	ButtonRectangle f100;
	ButtonRectangle z1;
	ButtonRectangle z5;
	ButtonRectangle z10;
	ButtonRectangle z50;
	ButtonRectangle z100;
	ButtonRectangle changeOpsi;
	ButtonRectangle backZero;
	ButtonRectangle send;
	ButtonRectangle pause;
	boolean sendingFlag=false;
	float[] point1=new float[10];
	int pointPointer1=0;
	float[] point2=new float[10];
	int pointPointer2=0;
	int type;
	int ii=0;
	boolean listening=false;
	MyBitmapView linePic;

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
					byte[] bytes = bundle.getByteArray("byte");
					try {
						for(int i=0;i<8;i++){
							bytes[i]=(byte)(bytes[i]&0xff);
							//bytes[i]=(byte)Math.abs(bytes[i]);
						}
						if(bytes[0]=='g'&&bytes[1]=='j'&& (byte)( (bytes[0]+bytes[1]+bytes[2]+bytes[3]+bytes[4]+bytes[5]+bytes[6])&0xff )==bytes[7]){
							if(bytes[2]==0||bytes[2]==3||bytes[2]==6){
								PEdit.setText( String.valueOf( (bytes[3]*256+bytes[4])/100.0) );
							}else if(bytes[2]==1||bytes[2]==4||bytes[2]==7){
								IEdit.setText( String.valueOf( (bytes[3]*256+bytes[4])/100.0) );
							}else if(bytes[2]==2||bytes[2]==5||bytes[2]==8){
								DEdit.setText( String.valueOf( (bytes[3]*256+bytes[4])/100.0) );
							}
						}else if(bytes[0]=='g'&&bytes[1]=='j'){
							if(bytes[2]==0||bytes[2]==1) {
								if(pointPointer1==10) {
									pointPointer1 = 0;
									//linePic.setPoints1(point1);
								}
								point1[pointPointer1] = (bytes[5] * 256 + Math.abs(bytes[6]) );
								pointPointer1++;
							}
							else if(bytes[2]==3||bytes[2]==4) {
								if(pointPointer2==10) {
									pointPointer2 = 0;
									//linePic.setPoints2(point2);
								}
								point2[pointPointer2] = (bytes[5] * 256 + Math.abs(bytes[6]) );
								pointPointer2++;
							}
						}else if(bytes[0]=='r'&&bytes[1]=='c'&& (byte)((bytes[0]+bytes[1]+bytes[2]+bytes[3]+bytes[4]+bytes[5]+bytes[6])&0xff )== bytes[7]  ){
							ii++;
							if(bytes[2]==0) {
								if(pointPointer1==10) {
									pointPointer1 = 0;
									//linePic.setPoints1(point1);
								}
								point1[pointPointer1] = (bytes[5] * 256 + Math.abs(bytes[6]))/100f;
								//Log.e("set1",String.valueOf(point1[pointPointer1]));
								pointPointer1++;
							}
							else if(bytes[2]==1) {
								if(pointPointer2==10) {
									pointPointer2 = 0;
									//linePic.setPoints2(point2);
								}
								point2[pointPointer2] =(bytes[5] * 256 + Math.abs(bytes[6]))/100f;
								pointPointer2++;
							}
						}
//				for(int i=8;i<12;i++){
//					Log.e("error!!!!!!!",String.valueOf(0xff&bytes[i]) );
//				}

						//Log.e("error!!!!!!!",Integer.toHexString(0xff&bytes[8]) );
//				Log.e("hex",String.valueOf((char)bytes[0])+" "+String.valueOf((char)bytes[1])+
//						" "+Integer.toHexString(bytes[2])+" "+Integer.toHexString(bytes[3])+
//						" "+Integer.toHexString(bytes[4])+" "+Integer.toHexString(bytes[5])+
//						" "+Integer.toHexString(bytes[6])+" "+Integer.toHexString(bytes[7]));


					}catch (Exception e){
						try {
							Log.e("hex", Integer.toHexString(0xff & bytes[2]) + " " + Integer.toHexString(0xff & bytes[3]) +
									" " + Integer.toHexString(0xff & bytes[4]) + " " + Integer.toHexString(0xff & bytes[5]) +
									" " + Integer.toHexString(0xff & bytes[6]));
						}catch (Exception e1){
							e.printStackTrace();
						}
						e.printStackTrace();
					}
			}
			super.handleMessage(msg);
		}
	};


	private BluetoothSPP.OnDataReceivedListener ParActivityDataRecier = new BluetoothSPP.OnDataReceivedListener() {
		@Override
		public void onDataReceived(byte[] bytes, String s) {

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_circle_layout);
		toast=Toast.makeText(this,"",Toast.LENGTH_LONG);
		Intent intent=getIntent();
		type=intent.getIntExtra("buttonType", 100);
		initTextView(type);
		slider=(Slider)findViewById(R.id.slider);

		//MainActivity.bt.setOnDataReceivedListener(ParActivityDataRecier);
		initToolBar();
		initBtn();
		initEditText();
		initUI();
		linePic =(MyBitmapView)findViewById(R.id.linePic);
	}
	private void initTextView(int type){
		par1=(TextView)findViewById(R.id.par1);
		par2=(TextView)findViewById(R.id.par2);
		par3=(TextView)findViewById(R.id.par3);
		if(type==TestCircleAcitvity.CURRENT_TEST){
			par1.setText("PWM");
			par2.setText("电流阀");
			par3.setText("电流");
		}else if(type==TestCircleAcitvity.SPEED_TEST){
			par1.setText("加速度");
			par2.setText("速度阀");
			par3.setText("速度");
		}else if(type==TestCircleAcitvity.LOCATION_TEST){
			par1.setText("速度");
			par2.setText("位置阀");
			par3.setText("位置");
		}
	}
	private void initUI(){
		if(type==TestCircleAcitvity.CURRENT_TEST){
			byte[] sendByte = new byte[]{'G', 'J', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
			sendByte[2]=6;
			for(int i=0;i<7;i++){
				sendByte[7]+=sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);

			for(int i=0;i<1000;i++);
			sendByte[7]=0;
			sendByte[2]=7;
			for(int i=0;i<7;i++){
				sendByte[7]+=sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);

			for(int i=0;i<1000;i++);
			sendByte[7]=0;
			sendByte[2]=8;
			for(int i=0;i<7;i++){
				sendByte[7]+=sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);
		}else if(type==TestCircleAcitvity.LOCATION_TEST) {
			byte[] sendByte = new byte[]{'G', 'J', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
			sendByte[2] = 0;
			for (int i = 0; i < 7; i++) {
				sendByte[7] += sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);

			for(int i=0;i<1000;i++);
			sendByte[7]=0;
			sendByte[2]=1;
			for (int i = 0; i < 7; i++) {
				sendByte[7] += sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);

			for(int i=0;i<1000;i++);
			sendByte[7]=0;
			sendByte[2] = 2;
			for (int i = 0; i < 7; i++) {
				sendByte[7] += sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);
		}else if(type==TestCircleAcitvity.SPEED_TEST) {
			byte[] sendByte = new byte[]{'G', 'J', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
			sendByte[2] = 3;
			for (int i = 0; i < 7; i++) {
				sendByte[7] += sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);

			for(int i=0;i<1000;i++);
			sendByte[7]=0;
			sendByte[2] = 4;
			for (int i = 0; i < 7; i++) {
				sendByte[7] += sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);

			for(int i=0;i<1000;i++);
			sendByte[7]=0;
			sendByte[2] = 5;
			for (int i = 0; i < 7; i++) {
				sendByte[7] += sendByte[i];
			}
			MainActivity.bluetoothManager.send(sendByte);
		}
	}
	private void initEditText(){
		PEdit=(MaterialEditText)findViewById(R.id.PEdit);
		IEdit=(MaterialEditText)findViewById(R.id.IEdit);
		DEdit=(MaterialEditText)findViewById(R.id.DEdit);
		PEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					byte[] sendByte = new byte[]{'S', 'J', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
					int num = (int) (Float.valueOf(PEdit.getText().toString()) * 100);
					sendByte[3] = (byte) (num >> 8);
					sendByte[4] = (byte) (num);
					if (type == TestCircleAcitvity.CURRENT_TEST) {
						sendByte[2] = 6;
					} else if (type == TestCircleAcitvity.SPEED_TEST) {
						sendByte[2] = 3;
					} else if (type == TestCircleAcitvity.LOCATION_TEST) {
						sendByte[2] = 0;
					}
					for (int i = 0; i < 7; i++) {
						sendByte[7] += sendByte[i];
					}
					if (sendingFlag)
						MainActivity.bluetoothManager.send(sendByte);
				}
			}
		});
		IEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					byte[] sendByte = new byte[]{'S', 'J', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
					int num = (int) (Float.valueOf(IEdit.getText().toString()) * 100);
					sendByte[3] = (byte) (num >> 8);
					sendByte[4] = (byte) (num);
					if (type == TestCircleAcitvity.CURRENT_TEST) {
						sendByte[2] = 7;
					} else if (type == TestCircleAcitvity.SPEED_TEST) {
						sendByte[2] = 4;
					} else if (type == TestCircleAcitvity.LOCATION_TEST) {
						sendByte[2] = 1;
					}
					for (int i = 0; i < 7; i++) {
						sendByte[7] += sendByte[i];
					}
					if (sendingFlag)
						MainActivity.bluetoothManager.send(sendByte);
				}
			}
		});
		DEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					byte[] sendByte = new byte[]{'S', 'J', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
					int num = (int) (Float.valueOf(DEdit.getText().toString()) * 100);
					sendByte[3] = (byte) (num >> 8);
					sendByte[4] = (byte) (num);
					if (type == TestCircleAcitvity.CURRENT_TEST) {
						sendByte[2] = 8;
					} else if (type == TestCircleAcitvity.SPEED_TEST) {
						sendByte[2] = 5;
					} else if (type == TestCircleAcitvity.LOCATION_TEST) {
						sendByte[2] = 2;
					}
					for (int i = 0; i < 7; i++) {
						sendByte[7] += sendByte[i];
					}
					if (sendingFlag)
						MainActivity.bluetoothManager.send(sendByte);
				}
			}
		});
		par1Edit=(MaterialEditText)findViewById(R.id.par1Edit);
		par2Edit=(MaterialEditText)findViewById(R.id.par2Edit);
		par3Edit=(MaterialEditText)findViewById(R.id.par3Edit);
		par1Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				sendPar1(sendingFlag);
			}
		});
		par2Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				try{
					if(!hasFocus){
						int newPar2=Integer.valueOf(par2Edit.getText().toString());
						int nowValue=Math.abs(Integer.valueOf(par2Edit.getText().toString()));
						slider.setMax(newPar2);
						slider.setMin(-newPar2);
						if(slider.getValue()>=nowValue||Integer.valueOf( par3Edit.getText().toString() )==0) {
							slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2);
						}
						if(nowValue>newPar2){
							par3Edit.setText(String.valueOf(Integer.valueOf(par2Edit.getText().toString())/2));
						}
						sendPar3(sendingFlag);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		par3Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				try{
					if(!hasFocus) {
						float nowPar3 = Float.valueOf(par3Edit.getText().toString());
						slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)nowPar3/2);
						sendPar3(sendingFlag);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		slider.setOnValueChangedListener(new Slider.OnValueChangedListener() {
             @Override
             public void onValueChanged(int i) {
	             par3Edit.setText(String.valueOf(i));
             }
         });
		slider.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				sendingFlag=!hasFocus;
				sendPar3(sendingFlag);
			}
		});

	}
	private void initBtn(){
		changeOpsi=(ButtonRectangle)findViewById(R.id.changeOpsi);
		backZero=(ButtonRectangle)findViewById(R.id.backZero);
		send=(ButtonRectangle)findViewById(R.id.send);
		pause=(ButtonRectangle)findViewById(R.id.pause);
		f1=(ButtonRectangle)findViewById(R.id.f1);
		f5=(ButtonRectangle)findViewById(R.id.f5);
		f10=(ButtonRectangle)findViewById(R.id.f10);
		f50=(ButtonRectangle)findViewById(R.id.f50);
		f100=(ButtonRectangle)findViewById(R.id.f100);
		z1=(ButtonRectangle)findViewById(R.id.z1);
		z5=(ButtonRectangle)findViewById(R.id.z5);
		z10=(ButtonRectangle)findViewById(R.id.z10);
		z50=(ButtonRectangle)findViewById(R.id.z50);
		z100=(ButtonRectangle)findViewById(R.id.z100);
		changeOpsi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float nowPar3 = Float.valueOf(par3Edit.getText().toString())*-1;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)nowPar3/2);
				sendPar3(sendingFlag);
			}
		});
		backZero.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				par3Edit.setText("0");
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2);
				sendPar3(sendingFlag);
			}
		});
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(sendingFlag){
					byte[] sendByte = new byte[]{'R', 'S', 0x00, 0x00, 0x00, 0x00, 0x00, (byte)('R'+'S')};
					MainActivity.bluetoothManager.send(sendByte);
					sendByte = new byte[] {'S','S',0x00,0x00,0x00,0x00,0x00,(byte)('S'+'S')};
					MainActivity.bluetoothManager.send(sendByte);
					sendingFlag=false;
					send.setBackgroundColor(getResources().getColor(R.color.primary_blue));
				}else{
					sendingFlag=true;
					send.setBackgroundColor(getResources().getColor(R.color.sendingColor));
					boolean error=false;
					if(sendingFlag) {
						byte[] sendByte = new byte[]{'E', 'P', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
						if (type == TestCircleAcitvity.CURRENT_TEST) {
							sendByte[0] = 'R';
							sendByte[1] = 'C';
							sendByte[2] = 2;
							int num=500;
							sendByte[3]=(byte)(num>>8);
							sendByte[4]=(byte)(num);
						} else if (type == TestCircleAcitvity.SPEED_TEST) {


						} else if (type == TestCircleAcitvity.LOCATION_TEST) {


						}
						for (int i = 0; i < 7; i++) {
							sendByte[7] += sendByte[i];
						}
						if (!error)
							MainActivity.bluetoothManager.send(sendByte);
					}
				}
			}
		});
		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] sendByte = new byte[]{'R', 'S', 0x00, 0x00, 0x00, 0x00, 0x00, (byte)('R'+'S')};
				MainActivity.bluetoothManager.send(sendByte);
				sendByte = new byte[] {'S','S',0x00,0x00,0x00,0x00,0x00,(byte)('S'+'S')};
				MainActivity.bluetoothManager.send(sendByte);
				sendingFlag=false;
				send.setBackgroundColor(getResources().getColor(R.color.primary_blue));
			}
		});
		f1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())-Float.valueOf(par2Edit.getText().toString())*0.01f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		f5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())-Float.valueOf(par2Edit.getText().toString())*0.05f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		f10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())-Float.valueOf(par2Edit.getText().toString())*0.10f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		f50.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())-Float.valueOf(par2Edit.getText().toString())*0.50f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		f100.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())-Float.valueOf(par2Edit.getText().toString())*1f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		z1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())+Float.valueOf(par2Edit.getText().toString())*0.01f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		z5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())+Float.valueOf(par2Edit.getText().toString())*0.05f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		z10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())+Float.valueOf(par2Edit.getText().toString())*0.10f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		z50.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())+Float.valueOf(par2Edit.getText().toString())*0.50f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
		z100.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float newPar3=Float.valueOf(par3Edit.getText().toString())+Float.valueOf(par2Edit.getText().toString())*1f;
				slider.setValue(Integer.valueOf(par2Edit.getText().toString())/2+(int)newPar3/2);
				par3Edit.setText(String.valueOf(newPar3));
				sendPar3(sendingFlag);
			}
		});
	}
	private void initToolBar(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		toolbar.setSubtitle("电流环");
		toolbar.setClickable(false);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent=new Intent(TestCircleAcitvity.this,MainActivity.class);  //方法1
//				startActivity(intent);
				finish();
			}
		});
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
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
	void sendPar3(boolean sendFlag){
		try{
			if(sendFlag){
				byte[] sendByte = new byte[] {'E','P',0x00,0x00,0x00,0x00,0x00,0x00};
				if(type==TestCircleAcitvity.CURRENT_TEST){
					int num=(int)((float)(Math.round(Float.valueOf(par3Edit.getText().toString())*10)/10)*10);
					sendByte[3]=(byte)(num>>8);
					sendByte[4]=(byte)(num);
					sendByte[2]=1;
				}else if(type==TestCircleAcitvity.SPEED_TEST){
					int num=Integer.valueOf(par3Edit.getText().toString());
					sendByte[3]=(byte)(num>>8);
					sendByte[4]=(byte)(num);
					sendByte[2]=2;
				}else if(type==TestCircleAcitvity.LOCATION_TEST){
					int num=Integer.valueOf(par3Edit.getText().toString());
					sendByte[3]=(byte)(num>>24);
					sendByte[4]=(byte)(num>>16);
					sendByte[5]=(byte)(num>>8);
					sendByte[6]=(byte)(num);
					sendByte[2]=3;
				}
				for(int i=0;i<7;i++){
					sendByte[7]+=sendByte[i];
				}
				MainActivity.bluetoothManager.send(sendByte);
			}
		}catch (Exception e){
			e.printStackTrace();
			toast.setText("蓝牙异常");
			toast.show();
		}
	}
	void sendPar1(boolean sendFlag){
		try{
			boolean error=false;
			if(sendFlag){
				byte[] sendByte = new byte[] {'E','P',0x00,0x00,0x00,0x00,0x00,0x00};
				if(type==TestCircleAcitvity.CURRENT_TEST){
					int num=Integer.valueOf(par1Edit.getText().toString());
					sendByte[2]=4;
					sendByte[3]=(byte)(num);
					if(num>100){
						error=true;
						toast.setText("PWM超过大小");
						toast.show();
					}
				}else if(type==TestCircleAcitvity.SPEED_TEST){
					int num=Integer.valueOf(par1Edit.getText().toString());
					sendByte[3]=(byte)(num>>8);
					sendByte[4]=(byte)(num);
					sendByte[2]=5;
				}else if(type==TestCircleAcitvity.LOCATION_TEST){
					int num=Integer.valueOf(par1Edit.getText().toString());
					sendByte[3]=(byte)(num>>8);
					sendByte[4]=(byte)(num);
					sendByte[2]=6;
				}
				for(int i=0;i<7;i++){
					sendByte[7]+=sendByte[i];
				}
				if(!error)
					MainActivity.bluetoothManager.send(sendByte);
				Log.e("meHex",String.valueOf((char)sendByte[0])+" "+String.valueOf((char)sendByte[1])+
						" "+Integer.toHexString(sendByte[2])+" "+Integer.toHexString(sendByte[3])+
						" "+Integer.toHexString(sendByte[4])+" "+Integer.toHexString(sendByte[5])+
						" "+Integer.toHexString(sendByte[6])+" "+Integer.toHexString(sendByte[7]));
			}
		}catch (Exception e){
			e.printStackTrace();
			toast.setText("蓝牙异常");
			toast.show();
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
//			Intent intent=new Intent(SetPIDActivity.this,ParameterConfigureActivity.class);  //方法1
//			startActivity(intent);
			finish();
		}
		return false;
	}


	private void listenForMessages(BluetoothSocket socket){
		listening = true;
		int bufferSize =20;
		byte[] buffer = new byte[bufferSize];
		int pointer=0;
		try {
			InputStream inputStream = socket.getInputStream();
			while (listening){
				buffer[pointer]=(byte)inputStream.read();
				pointer++;
				if(buffer[pointer]==0x0a||pointer==9){
					pointer=0;
					Message message=new Message();
					message.what=12345;
					Bundle bundle=new Bundle();
					bundle.putByteArray("bytes",buffer);
					message.setData(bundle);
					myHandler.sendMessage(message);
				}
			}

		}catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lisThread.interrupt();
	}
}
