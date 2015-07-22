package hzl.electricmachinerycontroler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import hzl.electricmachinerycontroler.content.DBManager;

/**
 * Created by YLion on 2015/7/21.
 */
public class NoteBookActivity extends ActionBarActivity {
	Toolbar toolbar;
	TextView textView;
	int typeNum=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notebook_layout);
		Intent intent=getIntent();
		typeNum=intent.getIntExtra("type", 100);
		String type="";
		if(typeNum == 501){
			type="product";
		}
		if(typeNum == 502){
			type="order";
		}
		if(typeNum == 503){
			type="test";
		}
		if(typeNum == 504){
			type="qaa";
		}

		initToolBar();
		textView=(TextView)findViewById(R.id.text);
		textView.setText( DBManager.getLatestContent(type) );
		
	}
	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle("关于驱动器");
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		if(typeNum==AboutQudongActivity.PRODUCT_NOTEBOOK){
			toolbar.setSubtitle("产品手册");
		}else if(typeNum==AboutQudongActivity.ORDER_NOTEBOOK){
			toolbar.setSubtitle("指令手册");
		}else if(typeNum==AboutQudongActivity.TEST_NOTEBOOK){
			toolbar.setSubtitle("调试软件手册");
		}else if(typeNum==AboutQudongActivity.QAA_NOTEBOOK){
			toolbar.setSubtitle("常见问题");
		}
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NoteBookActivity.this, AboutQudongActivity.class);  //方法1
				startActivity(intent);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
}
