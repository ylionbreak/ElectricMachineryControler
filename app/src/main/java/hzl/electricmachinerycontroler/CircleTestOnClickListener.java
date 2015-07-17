package hzl.electricmachinerycontroler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.gc.materialdesign.views.ButtonRectangle;

import app.akexorcist.bluetotohspp.library.DeviceList;

/**
 * Created by YLion on 2015/7/17.
 */
public class CircleTestOnClickListener implements View.OnClickListener  {
	Context context;
	ActionBarActivity actionBarActivity;
	final int buttonType;
	public CircleTestOnClickListener(Context context,ActionBarActivity actionBarActivity,int buttonType) {
		this.context=context;
		this.actionBarActivity=actionBarActivity;
		this.buttonType=buttonType;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, TestCircleAcitvity.class);
		intent.putExtra("buttonType",buttonType);
		actionBarActivity.startActivity(intent);
	}
}
