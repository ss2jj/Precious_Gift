package com.xujia.preciousgift.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.xujia.preciousgift.R;
import com.xujia.preciousgift.utils.BitmapCache;

import java.util.ArrayList;
import java.util.Random;

public class PhotoView extends  View {
    private BitmapCache cache;
    private int oriPhoto[];
    private Bitmap dealPhoto[];
    private final int PHOTO_WIDTH = 200,PHOTO_HEIGHT=200;
    private ArrayList<Photos> photos;
    private boolean running = false;

	public PhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		   cache = BitmapCache.getInstance();
		   oriPhoto = new int []{
		            R.drawable.background1,
		            R.drawable.background2,
		            R.drawable.background3,
		            R.drawable.background4,
		            R.drawable.background4_1,
		            R.drawable.background4_2,
		            
		            
		        };
	    dealPhoto = new Bitmap[oriPhoto.length];
        for(int i =0;i < oriPhoto.length;i++)   {
            Bitmap bitmap = cache.getBitmap(oriPhoto[i], context);
            float x = (float)PHOTO_WIDTH/bitmap.getWidth();
            float y = (float)PHOTO_HEIGHT/bitmap.getHeight();
            Matrix m = new Matrix();
            m.postScale(x, y);
            dealPhoto[i] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
        }
        
        photos =  new ArrayList<Photos>();
            for(int i =0;i<dealPhoto.length;i++)    {
                Random random = new Random(System.currentTimeMillis());
                photos.add(new Photos(dealPhoto[i],random.nextInt(280),random.nextInt(8)));
            }
   
	}

	

	@Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        synchronized (photos) { 
        for(int i = 0;i<photos.size();i++) {
            photos.get(i).myDraw(canvas);
        }
        }
        invalidate();
    }
	public void start()   {
	  running = true;
	  new TranslatePhotos().start();
		
	  }
	
	
	
	class TranslatePhotos extends Thread {
		int j = 0;
		public TranslatePhotos() {

		}

		public void run() {

			while (running) {
					
					if(photos.size() == 0)	{
						running = false;
					}
					try {
						sleep(50);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

					for (int i = 0; i < photos.size(); i++) {
						if(i == 0)	{
							photos.get(i).move();
							
						}else if(photos.get(j).huay < 20)	{
							break;
						}else	{
							photos.get(i).move();
							 j = i;
						}
						
					}

				}

			
		}
	}
	
	 class Photos    {
	      
	   
	        int huax = 0;
	        int basex = 0;
	        
	        int huay = 0;
	        Bitmap hua;
	        int huaw, huah;
	        int hua_add_plus = 2;
	        int huar=0; 
	        int huamax = 360;
	        int huamin = 0;
	        int huaCount = 0;
	        int speed = 0;
	        float sx=1.0f,sy=1.0f,add = -0.1f;
	        private Camera mCamera;
	        //图片旋转时的中心点坐标
	        private int centerX, centerY;
	        //转动的总距离，跟度数比例1:1
	        private int deltaX, deltaY;
	        private Matrix mMatrix = new Matrix();
	        private Paint mPaint = new Paint();
	        private int bWidth, bHeight;
	     
	        Canvas c = null;
	
	        public Photos(Bitmap hua,int x,int speed)    {
	            mCamera = new Camera(); 
	            mPaint.setAntiAlias(true);
	            this.hua = hua;
	            huaw = hua.getWidth();
	            huah = hua.getHeight();
	            bWidth = huaw;
	            bHeight = huah;
	            centerX = bWidth>>1;
	            centerY = bHeight>>1;
	        
	           //huax = basex + r.nextInt()%480;
	           huax = x;
	           this.speed= speed;
	          
	          // this.c = c;
	           mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
	        }
	        void myDraw(Canvas c)  {
	            if(running)    {
	                if(!hua.isRecycled())
	                    c.drawBitmap(hua, mMatrix, mPaint); 
	            }
	          
	        }
	     void move()   {
	         rotate(0, huar);
	         sy += add;
             if(sy <= 0) {
                 add = 0.1f;
                  sy += add;
             }
             if(sy >= 1.0f) {
                 add = -0.1f;
                 sy += add;
             }
            
        
             huar = huar+hua_add_plus;
            
             huay += speed;
             if(huar==360) huar = 0;
         
             if(huay >= 854) {
              //   running = false;
                 photos.remove(hua);
                 hua.recycle();
               
             }
     
	     }
	        void rotate(int degreeX, int degreeY) {
	            deltaX = degreeX;
	            deltaY = degreeY;
	            
	            mCamera.save();
	            mCamera.rotateY(-deltaY);
	            mCamera.rotateX(deltaX);
	            
	            mCamera.getMatrix(mMatrix);
	           
	            mMatrix.postTranslate(huax, huay);
	            mCamera.restore(); 
	            
	            //以图片的中心点为旋转中心,如果不加这两句，就是以（0,0）点为旋转中心
	            mMatrix.preTranslate(-centerX, -centerY);
	            mMatrix.postTranslate(centerX, centerY );  
	            mCamera.save(); 
	         }

	     
	    }


    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO 自动生成的方法存根
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		  int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
		  int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
	      if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)	{
	    	  setMeasuredDimension(480, 854);  
	      }else	{
	    	  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	      }


	}

	
    
}
