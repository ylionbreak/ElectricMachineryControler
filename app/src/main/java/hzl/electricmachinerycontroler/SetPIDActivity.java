package hzl.electricmachinerycontroler;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.MaterialEditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.views.ProgressBarIndeterminate;
import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


public class SetPIDActivity extends ActionBarActivity {
	Toolbar toolbar;
	ButtonRectangle selfTunning;
	ButtonRectangle test;
	ButtonFlat speedCircle;
	ButtonFlat currentCircle;
	ButtonFlat locationCircle;
	MaterialEditText IPEdit;
	MaterialEditText IIEdit;
	MaterialEditText IDEdit;
	MaterialEditText VPEdit;
	MaterialEditText VIEdit;
	MaterialEditText VDEdit;
	MaterialEditText PPEdit;
	MaterialEditText PIEdit;
	MaterialEditText PDEdit;
	ProgressBarIndeterminate progressBarIndeterminate;
	int progress=1;
	int testSta=1;
	Toast toast;
	MyBitmapView linePic;
	float[] point1=new float[10];
	int pointPointer1=0;
	float[] point2=new float[10];
	int pointPointer2=0;
	private BluetoothSPP.OnDataReceivedListener ParActivityDataRecier = new BluetoothSPP.OnDataReceivedListener() {
		@Override
		public void onDataReceived(byte[] bytes, String s) {
			try {
				if(bytes[0]=='g' && bytes[1]=='j'){
					progress++;
					if(bytes[2]==0){
						PPEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}else if(bytes[2]==1){
						PIEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}else if(bytes[2]==2){
						PDEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}
					if(bytes[2]==3){
						IPEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}else if(bytes[2]==4){
						IIEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}else if(bytes[2]==5){
						IDEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}
					if(bytes[2]==6){
						VPEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}else if(bytes[2]==7){
						VIEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}else if(bytes[2]==8){
						VDEdit.setText(String.valueOf( (bytes[3]*256+bytes[4])/100.0));
					}
					progressBarIndeterminate.setProgress(progress*10);
					if(progress==10){
						progressBarIndeterminate.setProgress(0);
						progressBarIndeterminate.setVisibility(View.GONE);
						toast.setText("自整定完成");
						toast.show();
					}
				}else if(bytes[0]=='g'&&bytes[1]=='j'){
					if(bytes[2]==0||bytes[2]==1) {
						if(pointPointer1==10) {
							pointPointer1 = 0;
							linePic.setPoints1(point1);
						}
						point1[pointPointer1] = (bytes[5] * 256 + bytes[6]);
						pointPointer1++;
					}
					else if(bytes[2]==3||bytes[2]==4) {
						if(pointPointer2==10) {
							pointPointer2 = 0;
							linePic.setPoints2(point2);
						}
						point2[pointPointer2] = (bytes[5] * 256 + bytes[6]);
						pointPointer2++;
					}
				}else if(bytes[0]=='r'&&bytes[1]=='c'){
					if(bytes[2]==0) {
						if(pointPointer1==10) {
							pointPointer1 = 0;
							linePic.setPoints1(point1);
						}
						point1[pointPointer1] = (bytes[5] * 256 + bytes[6]);
						pointPointer1++;
					}
					else if(bytes[2]==1) {
						if(pointPointer2==10) {
							pointPointer2 = 0;
							linePic.setPoints2(point2);
						}
						point2[pointPointer2] = (bytes[5] * 256 + bytes[6]);
						pointPointer2++;
					}
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pid_layout);
		progressBarIndeterminate=(ProgressBarIndeterminate)findViewById(R.id.progressBarIndeterminate);
		toast=Toast.makeText(this,"异常",Toast.LENGTH_LONG);
		MainActivity.bt.setOnDataReceivedListener(ParActivityDataRecier);
		initToolBar();
		initBtn();
		initEditText();
		linePic =(MyBitmapView)findViewById(R.id.linePic);
	}

	private void initToolBar(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.showOverflowMenu();
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SetPIDActivity.this,ParameterConfigureActivity.class);  //方法1
				startActivity(intent);
				finish();
			}
		});
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}
	private void initEditText(){
		IPEdit=(MaterialEditText)findViewById(R.id.IPEdit);
		IIEdit=(MaterialEditText)findViewById(R.id.IIEdit);
		IDEdit=(MaterialEditText)findViewById(R.id.IDEdit);
		VPEdit=(MaterialEditText)findViewById(R.id.VPEdit);
		VIEdit=(MaterialEditText)findViewById(R.id.VIEdit);
		VDEdit=(MaterialEditText)findViewById(R.id.VDEdit);
		PPEdit=(MaterialEditText)findViewById(R.id.PPEdit);
		PIEdit=(MaterialEditText)findViewById(R.id.PIEdit);
		PDEdit=(MaterialEditText)findViewById(R.id.PDEdit);

	}
	private void initBtn(){
		speedCircle=(ButtonFlat)findViewById(R.id.speedCircle);
		currentCircle=(ButtonFlat)findViewById(R.id.currentCircle);
		locationCircle=(ButtonFlat)findViewById(R.id.locationCircle);
		speedCircle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testSta=1;
				speedCircle.setBackgroundColor(getResources().getColor(R.color.primary_blue));
				currentCircle.setBackgroundColor(getResources().getColor(R.color.divid_color));
				locationCircle.setBackgroundColor(getResources().getColor(R.color.divid_color));
			}
		});
		currentCircle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testSta=2;
				currentCircle.setBackgroundColor(getResources().getColor(R.color.primary_blue));
				speedCircle.setBackgroundColor(getResources().getColor(R.color.divid_color));
				locationCircle.setBackgroundColor(getResources().getColor(R.color.divid_color));
			}
		});
		locationCircle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testSta=3;
				locationCircle.setBackgroundColor(getResources().getColor(R.color.primary_blue));
				currentCircle.setBackgroundColor(getResources().getColor(R.color.divid_color));
				speedCircle.setBackgroundColor(getResources().getColor(R.color.divid_color));
			}
		});
		selfTunning=(ButtonRectangle)findViewById(R.id.selfTunning);
		test=(ButtonRectangle)findViewById(R.id.test);
		selfTunning.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{

					byte[] sendByte = new byte[]{'A', 'T', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
					for(int i=0;i<7;i++){
						sendByte[7]+=sendByte[i];
					}
					MainActivity.bt.send(sendByte, false);
					progressBarIndeterminate.setVisibility(View.VISIBLE);
					progressBarIndeterminate.setMax(100);
					progressBarIndeterminate.setMin(0);
					progressBarIndeterminate.setProgress(0);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		test.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					byte[] sendByte = new byte[]{'T', 'P', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
					int para=0;
					if(testSta==1){
						para = (int)(Float.valueOf(VPEdit.getText().toString())*100);
						sendByte[2]=3;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
						para = (int)(Float.valueOf(VIEdit.getText().toString())*100);
						sendByte[2]=4;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
						para = (int)(Float.valueOf(VDEdit.getText().toString())*100);
						sendByte[2]=5;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);

						sendByte = new byte[]{'T', 'P', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
						para = (Integer.valueOf(VDEdit.getText().toString()));
						sendByte[2]=2;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
					}else if(testSta==2){
						para = (int)(Float.valueOf(IPEdit.getText().toString())*100);
						sendByte[2]=6;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
						para = (int)(Float.valueOf(IIEdit.getText().toString())*100);
						sendByte[2]=7;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
						para = (int)(Float.valueOf(IDEdit.getText().toString())*100);
						sendByte[2]=8;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);

						sendByte = new byte[]{'T', 'P', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
						para = (int)(Float.valueOf(VDEdit.getText().toString())*10);
						sendByte[2]=1;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
					}else if(testSta==3){
						para = (int)(Float.valueOf(PPEdit.getText().toString())*100);
						sendByte[2]=0;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
						para = (int)(Float.valueOf(PIEdit.getText().toString())*100);
						sendByte[2]=1;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
						para = (int)(Float.valueOf(PDEdit.getText().toString())*100);
						sendByte[2]=2;
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);

						sendByte = new byte[]{'T', 'P', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
						para = (Integer.valueOf(VDEdit.getText().toString()));
						sendByte[2]=3;
						sendByte[3]=(byte)(para>>24);
						sendByte[4]=(byte)(para>>16);
						sendByte[3]=(byte)(para>>8);
						sendByte[4]=(byte)(para);
						for(int i=0;i<7;i++){
							sendByte[7]+=sendByte[i];
						}
						MainActivity.bt.send(sendByte, false);
					}
				}catch (Exception e){
					e.printStackTrace();
					toast.setText("蓝牙异常");
					toast.show();
				}
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_set_pid_menu, menu);
		//getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			String msg = "";
			switch (menuItem.getItemId()) {
				case R.id.action_watchPar:
					msg += "Click edit";
					break;
				case R.id.home:
					Intent intent=new Intent(SetPIDActivity.this,MainActivity.class);  //方法1
					startActivity(intent);
					finish();
					break;
			}
			if(!msg.equals("")) {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent=new Intent(SetPIDActivity.this,ParameterConfigureActivity.class);  //方法1
			startActivity(intent);
			finish();
		}
		return false;
	}
}

