package hzl.electricmachinerycontroler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class MyBitmapView extends View {
	private Bitmap myBitmap;
	private Bitmap trybit;
	private float[] mPts1;
	private float[] mPts2;
	private float[] linePointNative1;
	private float[] linePointNative2;
	private static final float SIZE = 300;
	private static final int SEGS = 32;
	private static final int X = 0;
	private static final int Y = 1;
	boolean setOneFlag=false;
	boolean setTwoFlag=false;
	boolean firstDraw=false;
	int num=0;
	int xi=0;
	float[] connectPoints1 = new float[4];
	float[] connectPoints2 = new float[4];
	@Override
	protected void onDraw(Canvas canvas){
			super.onDraw(canvas);
			canvas.drawBitmap(myBitmap,0,0,null);
		}

	public boolean isFirstDraw() {
		return firstDraw;
	}

	public void setFirstDraw(boolean firstDraw) {
		this.firstDraw = firstDraw;
	}

	public MyBitmapView(Context context){
		super(context);
		//setMeasuredDimension(100,100);
		myBitmap=Bitmap.createBitmap(1000,500,Bitmap.Config.ARGB_4444);
		myBitmap.eraseColor(Color.WHITE);
		//drawLines();
	}

	public MyBitmapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		myBitmap=Bitmap.createBitmap(1000,500,Bitmap.Config.ARGB_4444);
		myBitmap.eraseColor(Color.WHITE);
		//drawLines();
	}

	public MyBitmapView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		myBitmap=Bitmap.createBitmap(1000,500,Bitmap.Config.ARGB_4444);
		myBitmap.eraseColor(Color.WHITE);
		//drawLines();
	}
	public MyBitmapView(Context context, AttributeSet attrs, int defStyleAttr,float[] linePointNative1,float[] linePointNative2) {
		super(context, attrs, defStyleAttr);
		myBitmap=Bitmap.createBitmap(1000,500,Bitmap.Config.ARGB_4444);
		this.linePointNative1=linePointNative1;
		this.linePointNative2=linePointNative2;
		drawLines();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w=measure(widthMeasureSpec);
		int h=measure(heightMeasureSpec);
		setMeasuredDimension(w, h);

	}
	public void setPoints1(float[] linePointNative1){
		this.linePointNative1=linePointNative1;
		setOneFlag=true;
		if(setTwoFlag){
			drawLines();
			setOneFlag=false;
			setTwoFlag=false;
		}
	}
	public void setPoints2(float[] linePointNative2){
		this.linePointNative2=linePointNative2;
		setTwoFlag=true;
		if(setOneFlag){
			drawLines();
			setOneFlag=false;
			setTwoFlag=false;
		}
	}
	private int measure(int measureSpec){
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if(specMode==MeasureSpec.UNSPECIFIED){
			result = 200;
		}else{
			result = specSize;
		}
		return result;
	}

	public void buildPoints() {
		mPts1 = new float[20];
		mPts2 = new float[20];
		if (setOneFlag && setTwoFlag) {
			mPts1 = new float[20];
			mPts2 = new float[20];
			for (int i = 0; i < 10; i++) {
				mPts1[i * 2] = xi + 8f;
				mPts1[i * 2 + 1] = (linePointNative1[i]-10)*3;
				mPts2[i * 2] = xi+ 8f;
				mPts2[i * 2 + 1] = (linePointNative2[i]-10)*3;
				xi=xi+8;
				num++;
			}
			connectPoints1[2]=mPts1[0];
			connectPoints1[3]=mPts1[1];
			connectPoints2[2]=mPts2[0];
			connectPoints2[3]=mPts2[1];

		}

	}
	public void drawLines(){
		buildPoints();
		Canvas canvas =new Canvas(myBitmap);
		canvas.translate(10,80);
		canvas.drawColor(Color.TRANSPARENT);
		Paint paint=new Paint();
		//画线1
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		canvas.drawLines(mPts1, 0, mPts1.length, paint);
		canvas.drawLines(mPts1, 2, mPts1.length-2, paint);
		//画点
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		canvas.drawPoints(mPts1,paint);
		//画线2
		paint.setColor(Color.RED);
		paint.setStrokeWidth(2);
		canvas.drawLines(mPts2, 0, mPts2.length, paint);
		canvas.drawLines(mPts2, 2, mPts2.length-2, paint);
		//画点
		paint.setColor(Color.RED);
		paint.setStrokeWidth(2);
		canvas.drawPoints(mPts2,paint);
		if(num>15){
			//画线1
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(2);
			canvas.drawLines(connectPoints1, 0, connectPoints1.length, paint);
			//画点
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(2);
			canvas.drawPoints(connectPoints1,paint);
			//画线2
			paint.setColor(Color.RED);
			paint.setStrokeWidth(2);
			canvas.drawLines(connectPoints2, 0, connectPoints2.length, paint);
			//画点
			paint.setColor(Color.RED);
			paint.setStrokeWidth(2);
			canvas.drawPoints(connectPoints2,paint);
		}
		connectPoints1[0]=mPts1[18];
		connectPoints1[1]=mPts1[19];
		connectPoints2[0]=mPts2[18];
		connectPoints2[1]=mPts2[19];
		//Log.e("num", String.valueOf(num) );
		if(num>100) {
			myBitmap.eraseColor(Color.WHITE);
			xi=0;
			num=0;
		}
	}




}