package hzl.electricmachinerycontroler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by YLion on 2015/7/15.
 */
public class ElectricCircle  extends Fragment {

//	protected void onCreate( Bundle savedInstanceState ) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.speed_circle);
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.speed_circle, container, false);

	}
}
