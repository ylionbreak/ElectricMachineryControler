package hzl.electricmachinerycontroler;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import com.gc.materialdesign.views.ButtonRectangle;


public class SetPIDActivity extends ActionBarActivity {
	TabHost tabHost=null;      //选项卡控制器
	TabHost.TabSpec tabSpecA,tabSpecB,tabSpecC=null;   //选项卡,这里选项卡最好不用混用，有几个选项卡就设置几个对象
	ButtonRectangle enterTestOrderButton;
	Toolbar toolbar;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pid_layout);
		enterTestOrderButton=(ButtonRectangle)findViewById(R.id.enterTestOrder);


		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle("参数配置");
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		toolbar.setClickable(false);


		enterTestOrderButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SetPIDActivity.this,TestOrderActivity.class);  //方法1
				startActivity(intent);
			}
		});


	}
}

