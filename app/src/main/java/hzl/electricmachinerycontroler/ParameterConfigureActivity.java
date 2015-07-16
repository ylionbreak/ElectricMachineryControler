package hzl.electricmachinerycontroler;



import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class ParameterConfigureActivity extends ActionBarActivity {
	ButtonRectangle button;
	Toolbar toolbar;
	ScrollView scrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parameter_configure_layout);

		scrollView = (ScrollView)findViewById(R.id.scroll);


		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle("参数配置");
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		toolbar.setClickable(false);

		button=(ButtonRectangle)findViewById(R.id.enterSetPid);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ParameterConfigureActivity.this,SetPIDActivity.class);  //方法1
				startActivity(intent);
			}
		});
	}


}
