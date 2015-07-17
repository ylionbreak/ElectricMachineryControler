package hzl.electricmachinerycontroler;



import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import fr.ganfra.materialspinner.MaterialSpinner;


public class ParameterConfigureActivity extends ActionBarActivity {
	Toolbar toolbar;
	ScrollView scrollView;
	MaterialSpinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parameter_configure_layout);
		initToolBar();
		String[] ITEMS = {"AB", "ABZ", "AABB", "AABBZZ"};
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ITEMS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner = (MaterialSpinner) findViewById(R.id.spinner);
		spinner.setAdapter(adapter);
		Log.e("ParameterConfigureActivity", "onCreate");
		scrollView = (ScrollView)findViewById(R.id.scroll);


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
				Intent intent=new Intent(ParameterConfigureActivity.this,MainActivity.class);  //方法1
				startActivity(intent);
			}
		});
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_parameter_configure, menu);
		//getMenuInflater().inflate(R.menu.menu_main, menu);
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
					break;

			}

			return true;
		}
	};

}
