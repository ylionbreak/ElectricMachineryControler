package hzl.electricmachinerycontroler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;

/**
 * Created by YLion on 2015/7/17.
 */
public class AboutQudongActivity extends ActionBarActivity {
	Toolbar toolbar;
	ButtonRectangle updataFirmware;
	ButtonRectangle productnNotebook;
	ButtonRectangle orderNotebook;
	ButtonRectangle buyProductn;
	ButtonRectangle parList;
	ButtonRectangle testSoftNotebook;
	ButtonRectangle qaa;
	ButtonRectangle connectUs;
	final static int PRODUCT_NOTEBOOK = 501;
	final static int ORDER_NOTEBOOK = 502;
	final static int TEST_NOTEBOOK = 503;
	final static int QAA_NOTEBOOK = 504;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_qudong_layout);
		initToolBar();
		initBtn();
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
	private void initBtn(){
		updataFirmware=(ButtonRectangle)findViewById(R.id.updataFirmware);
		productnNotebook=(ButtonRectangle)findViewById(R.id.productnNotebook);
		orderNotebook=(ButtonRectangle)findViewById(R.id.orderNotebook);
		buyProductn=(ButtonRectangle)findViewById(R.id.buyProductn);
		parList=(ButtonRectangle)findViewById(R.id.parList);
		testSoftNotebook=(ButtonRectangle)findViewById(R.id.testSoftNotebook);
		qaa=(ButtonRectangle)findViewById(R.id.qaa);
		connectUs=(ButtonRectangle)findViewById(R.id.connectUs);
		updataFirmware.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		productnNotebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutQudongActivity.this, NoteBookActivity.class);  //方法1
				intent.putExtra("type",AboutQudongActivity.PRODUCT_NOTEBOOK);
				startActivity(intent);
				finish();
			}
		});
		orderNotebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutQudongActivity.this, NoteBookActivity.class);  //方法1
				intent.putExtra("type",AboutQudongActivity.ORDER_NOTEBOOK);
				startActivity(intent);
				finish();
			}
		});
		buyProductn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.baidu.com");
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
			}
		});
		parList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		testSoftNotebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutQudongActivity.this, NoteBookActivity.class);  //方法1
				startActivity(intent);
				intent.putExtra("type",AboutQudongActivity.TEST_NOTEBOOK);
				finish();
			}
		});
		qaa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutQudongActivity.this, NoteBookActivity.class);  //方法1
				intent.putExtra("type",AboutQudongActivity.QAA_NOTEBOOK);
				startActivity(intent);
				finish();
			}
		});
		connectUs.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog();
			}
		});

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
	protected void dialog() {
		Dialog alertDialog = new AlertDialog.Builder(this).
				setTitle("联系我们").
				setMessage("电子科大创客空间"+
						'\n'+"刘述亮"+
						'\n'+"手机:18349314729"+
						'\n'+"QQ:1015607727").
				setNegativeButton("确定", null).create();
		alertDialog.show();
	}
}
