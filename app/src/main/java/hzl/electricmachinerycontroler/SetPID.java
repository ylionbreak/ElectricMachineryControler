package hzl.electricmachinerycontroler;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TabHost;


public class SetPID extends TabActivity {
	TabHost tabHost=null;      //选项卡控制器
	TabHost.TabSpec tabSpecA,tabSpecB=null;   //选项卡,这里选项卡最好不用混用，有几个选项卡就设置几个对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_pid);
		//获得TabHost实例；
		tabHost = getTabHost();
		//获得TabHost.TabSpec对象实例；
		tabSpecA=tabHost.newTabSpec("One");
		//为TabSpec对象设置指示器
		tabSpecA.setIndicator("TabA",getResources().getDrawable(android.R.drawable.ic_media_play));
		//为选项卡设置内容，这里需要创建一个intent对象
		Intent intentA=new Intent();
		intentA.setClass(this, SpeedCircle.class);
		tabSpecA.setContent(intentA);

		//然后创建第二个选项卡：
		tabSpecB=tabHost.newTabSpec("Two");
		tabSpecB.setIndicator("TabB",getResources().getDrawable(android.R.drawable.ic_media_next));
		Intent intentB=new Intent();
		intentB.setClass(this, SpeedCircle.class);
		tabSpecB.setContent(intentB);

		//最后一步，把两个选项卡TabSpec添加到选项卡控件TabHost中
		tabHost.addTab(tabSpecA);
		tabHost.addTab(tabSpecB);
	}
}

