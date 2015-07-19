package hzl.electricmachinerycontroler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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
	@Override
	protected void onDraw(Canvas canvas){
			super.onDraw(canvas);
			canvas.drawBitmap(myBitmap,0,0,null);
		}

	public MyBitmapView(Context context){
		super(context);
		//setMeasuredDimension(100,100);
		myBitmap=Bitmap.createBitmap(300,300,Bitmap.Config.ARGB_8888);
		drawLines();
	}

	public MyBitmapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		myBitmap=Bitmap.createBitmap(300,300,Bitmap.Config.ARGB_8888);
		drawLines();
	}

	public MyBitmapView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		myBitmap=Bitmap.createBitmap(300,300,Bitmap.Config.ARGB_8888);
		drawLines();
	}
	public MyBitmapView(Context context, AttributeSet attrs, int defStyleAttr,float[] linePointNative1,float[] linePointNative2) {
		super(context, attrs, defStyleAttr);
		myBitmap=Bitmap.createBitmap(300,300,Bitmap.Config.ARGB_8888);
		this.linePointNative1=linePointNative1;
		this.linePointNative2=linePointNative2;
		drawLines();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int w=measure(widthMeasureSpec);
		int h=measure(heightMeasureSpec);
		setMeasuredDimension(w, h);

	}
	public void setPoints1(float[] linePointNative1){
		this.linePointNative1=linePointNative1;
		setOneFlag=true;
		if(setTwoFlag){
			drawLines();
		}
	}
	public void setPoints2(float[] linePointNative2){
		this.linePointNative2=linePointNative2;
		setTwoFlag=true;
		if(setOneFlag){
			drawLines();
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

	public void buildPoints(){

		mPts1=new float[10000];
		mPts1[0]=0;
		for(int i=0;i<5000;i++){
			mPts1[i*2] = mPts1[i*2-2]+0.01f;
			mPts1[i*2+1] = linePointNative1[i];
		}
		mPts2=new float[10000];
		mPts2[0]=0;
		for(int i=0;i<5000;i++){
			mPts2[i*2] = mPts2[i*2-2]+0.01f;
			mPts2[i*2+1] = linePointNative2[i];
		}
	}

	public void drawLines(){
		buildPoints();
		Canvas canvas =new Canvas(myBitmap);
		canvas.translate(200,200);
		canvas.drawColor(Color.TRANSPARENT);
		Paint paint=new Paint();
		//画线1
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(1);
		canvas.drawLines(mPts1, 0, mPts1.length, paint);
		canvas.drawLines(mPts1, 2, mPts1.length-2, paint);
		//画线2
		paint.setColor(Color.RED);
		paint.setStrokeWidth(1);
		canvas.drawLines(mPts2, 0, mPts2.length, paint);
		canvas.drawLines(mPts2, 2, mPts2.length-2, paint);
	}
}