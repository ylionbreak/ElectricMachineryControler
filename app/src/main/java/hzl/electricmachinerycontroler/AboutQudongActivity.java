package hzl.electricmachinerycontroler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
/**
 * Created by YLion on 2015/7/17.
 */
public class AboutQudongActivity extends ActionBarActivity {
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_qudong_layout);
		initToolBar();
	}
	private void initToolBar(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle("关于驱动器");
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		toolbar.setClickable(false);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AboutQudongActivity.this,MainActivity.class);  //方法1
				startActivity(intent);
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
}
