package com.xujia.preciousgift.view;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;










import com.xujia.preciousgift.R;
import com.xujia.preciousgift.utils.BitmapCache;
import com.xujia.preciousgift.utils.Utils;
import com.xujia.preciousgift.view.SurfaceViewTwo.ShowTextThread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.Xfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class SurfaceViewFour extends SurfaceView implements SurfaceHolder.Callback{
private int width,height;
private Handler handler;
private Context mContext;
private BitmapCache cache;
private SurfaceHolder holder;
private Bitmap background1,background2;
int colums = 3;
int rows = 2;
int w ,h;
private Bitmap backgrounds[][] =  new Bitmap[rows][colums];
	public SurfaceViewFour(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		holder = this.getHolder();
		
		cache = BitmapCache.getInstance();
		setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSPARENT); 
		background2 = BitmapFactory.decodeResource(getResources(), R.drawable.background4_2);
		background1 = BitmapFactory.decodeResource(getResources(), R.drawable.background4_1);
	   w = background2.getWidth() / colums;
	   h = background2.getHeight() / rows;
	
	   for(int i = 0; i < rows;i++)  {
           for(int j = 0;j<colums;j++ )  {
               backgrounds[i][j] = Bitmap.createBitmap(background2, j*w, i*h, w, h);
           }
       }
		// TODO 自动生成的构造函数存根
	}

	public void setParamets(int width,int height,Handler handler)	{
		this.width = width;
		this.height = height;
		this.handler  = handler;
		
	}
	public void showBack() {
	    clear();
	    new ShowBackground().start();
	   
	}
	public  void clear()   {
	      Paint paint = new Paint();
	        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
	        //c.drawPaint(paint);
	        Canvas canvas= holder.lockCanvas();
	        canvas.drawPaint(paint);
	        holder.unlockCanvasAndPost(canvas);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
	}
	class ShowBackground extends Thread    {
	    Bitmap map;
        Canvas c;
        Paint p = new Paint();
       
        public ShowBackground() {
            p.setXfermode( new PorterDuffXfermode(Mode.SRC));
        }
	    public void run()  {
	        for(int i = 0; i < rows;i++)  {
	            for(int j = 0;j<colums;j++ )  {
	                try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
	            
	                map = backgrounds[i][j];
	               Log.i("XUJIA","j*w"+j*w+" i*h"+ i*h+" w"+w+" h"+h+" map"+map);
	               c = holder.lockCanvas(new Rect( j*w, i*h, (j+1)*w, (i+1)*h));
	               
	               for(float k =0;k<=1.0f;k+=0.1) {
	                   Matrix matrix = new Matrix();
	                   matrix.setScale(k,1);
	                   c.setMatrix(matrix);
	                   try {
	                        sleep(100);
	                    } catch (InterruptedException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                   c.drawBitmap(map, j*w, i*h, p);
	               }
	             
	               holder.unlockCanvasAndPost(c);
	            }
	        }
	   
	    
	    }
	}
	public void showBackground()   {
	    Bitmap map;
	    Canvas c;
	    Paint p = new Paint();
	    p.setXfermode( new PorterDuffXfermode(Mode.SRC));
	    for(int i = 0; i < rows;i++)  {
            for(int j = 0;j<colums;j++ )  {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                map = backgrounds[i][j];
               Log.i("XUJIA","j*w"+j*w+" i*h"+ i*h+" w"+w+" h"+h+" map"+map);
               c = holder.lockCanvas(new Rect( j*w, i*h, w, h));
              // Matrix matrix = new Matrix();
               c.drawBitmap(map, j*w, i*h, p);
               holder.unlockCanvasAndPost(c);
            }
        }
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO 自动生成的方法存根
	 
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	
	}

	
}
