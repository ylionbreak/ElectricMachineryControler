package hzl.electricmachinerycontroler;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;


public class SetPIDActivity extends ActionBarActivity {
	TabHost tabHost=null;      //选项卡控制器
	TabHost.TabSpec tabSpecA,tabSpecB,tabSpecC=null;   //选项卡,这里选项卡最好不用混用，有几个选项卡就设置几个对象
	ButtonRectangle enterTestOrderButton;
	ButtonRectangle comeBackParConfiButton;
	Toolbar toolbar;

	ButtonRectangle backHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pid_layout);
		Log.e("SetPIDActivity","onCreate");
		initToolBar();



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
			}
		});
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
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
					break;

			}

			if(!msg.equals("")) {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	};
}

