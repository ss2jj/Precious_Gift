
package com.xujia.preciousgift.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.xujia.preciousgift.R;
import com.xujia.preciousgift.dot.Dot;
import com.xujia.preciousgift.dot.DotFactory;
import com.xujia.preciousgift.dot.LittleDot;
import com.xujia.preciousgift.utils.BitmapCache;
import com.xujia.preciousgift.utils.Utils;
import com.xujia.preciousgift.view.FireworkView.MyThread;
import com.xujia.preciousgift.view.SurfaceViewThree.ShowYanHuo;

import java.util.Random;
import java.util.Vector;

public class SurfaceViewFive extends SurfaceView implements SurfaceHolder.Callback {
    private int width, height;
    private Handler handler;
    private Context mContext;
    private BitmapCache cache;
    private SurfaceHolder holder;
    private int oriPhoto[];
    private Bitmap dealPhoto[];
    private final int PHOTO_WIDTH = 200,PHOTO_HEIGHT=200;
    public SurfaceViewFive(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        holder = this.getHolder();

        cache = BitmapCache.getInstance();
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
        oriPhoto = new int []{
        	R.drawable.background1,
        	R.drawable.background2,
        	R.drawable.background3,
        	R.drawable.background4,
        	R.drawable.background4_1,
        	R.drawable.background4_2,
        	
        	
        };
        dealPhoto = new Bitmap[oriPhoto.length];
        for(int i =0;i < oriPhoto.length;i++)	{
        	Bitmap bitmap = cache.getBitmap(oriPhoto[i], context);
        	float x = (float)PHOTO_WIDTH/bitmap.getWidth();
        	float y = (float)PHOTO_HEIGHT/bitmap.getHeight();
        	Matrix m = new Matrix();
        	m.postScale(x, y);
        	dealPhoto[i] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
        }
       
    }
    public void showYanHuo() {
      
    //	new ShowHuaBian(dealPhoto[0]).start();
    	try {
    		for(int i =0;i<dealPhoto.length;i++)	{
    			new ShowHuaBian(dealPhoto[i]).start();
    			Thread.sleep(3000);
			
    		}
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	
    }
    
 
    public void setParamets(int width, int height, Handler handler) {
        this.width = width;
        this.height = height;
        this.handler = handler;
     
    }
public void showTextFrame() {
    new ShowTextFrame().start();
}
class ShowTextFrame extends Thread  {
    Paint p = new Paint();
    int startX,startY;
    Rect rect;
    private String[] texts;
    private String text;
    private int size =25;
    FontMetricsInt fontMetrics;
    public ShowTextFrame()  {
        startX = 0;
        startY = height - 350;
       // rect = new Rect(startX, startY, startX+width, startY+200);
        p.setStyle(Style.FILL);
       // p.setAlpha((int)(255*0.8));
        p.setColor(0x88F5F5F5);
        texts = getResources().getString(R.string.poem).split("\n");
        fontMetrics  = p.getFontMetricsInt();  
      
    }
    public void run()   {
        Canvas c = holder.lockCanvas(new Rect(startX, startY, startX+width, startY+300));
        c.drawRect(startX, startY, startX+width, startY+300, p);
        holder.unlockCanvasAndPost(c);
        p.setTextSize(size);
        p.setColor(Color.BLACK);
        for(int i = 0; i< texts.length;i++) {
            text = texts[i];
            for(int count=0;count<text.length();count++)    {
            try {
                this.sleep(300);
            } catch (InterruptedException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
            String tm = String.valueOf(text.charAt(count));
            rect = new Rect(startX+count*size,startY+size*i,startX+(count+1)*size,startY+size*(i+1));
            c= holder.lockCanvas(new Rect(startX+count*size,startY+size*i,startX+(count+1)*size,startY+size*(i+1)));
             int baseline = rect.top + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
             c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.OVERLAY);  
             c.drawText(tm, startX+count*size,baseline, p);
             holder.unlockCanvasAndPost(c);         
        }
    }
}
   }
    class ShowHuaBian extends Thread   {
        Paint p = new Paint();
   
        int huax = 0;
        int basex = width/2;
        
        int huay = 0;
        Bitmap hua;
        int huaw, huah;
        int hua_add_plus = 2;
        int huar=0; 
        int huamax = 360;
        int huamin = 0;
        int huaCount = 0;
        float sx=1.0f,sy=1.0f,add = -0.1f;
        private Camera mCamera;
        //图片旋转时的中心点坐标
        private int centerX, centerY;
        //转动的总距离，跟度数比例1:1
        private int deltaX, deltaY;
        private Matrix mMatrix = new Matrix();
        private Paint mPaint = new Paint();
        private int bWidth, bHeight;
        boolean running = true;
        Canvas c = null;
        int beforex = 0,beforey=0;
        public ShowHuaBian(Bitmap hua)    {
            mCamera = new Camera(); 
            mPaint.setAntiAlias(true);
            this.hua = hua;
            huaw = hua.getWidth();
            huah = hua.getHeight();
            bWidth = huaw;
            bHeight = huah;
            centerX = bWidth>>1;
            centerY = bHeight>>1;
           Random r = new Random(System.currentTimeMillis());
           huax = basex + r.nextInt()%50;
           beforex =huax;
           beforey = huay;
           mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        }
        public void run()   {
            while(running) {
            	synchronized (holder) {
					
				try {
                    sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
              
				   
			       
			        // c.drawPaint(paint);
			        c = holder.lockCanvas(new Rect(beforex,beforey,beforex+huaw,beforey+huah));
			        c.drawPaint(mPaint);
			        holder.unlockCanvasAndPost(c);
                    rotate(0 , huar);

                    c = holder.lockCanvas(new Rect(huax,huay,huax+huaw,huay+huah));
                   // c.drawColor(Color.TRANSPARENT,Mode.SRC);
                   c.drawBitmap(hua, mMatrix, null);
                   holder.unlockCanvasAndPost(c);
   //                c = holder.lockCanvas();
//                   c.drawColor(Color.TRANSPARENT,Mode.SRC);
//                  c.drawBitmap(hua, mMatrix, mPaint);
//                  holder.unlockCanvasAndPost(c);
                   //c.drawBitmap(hua, huax, huay, null);
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
                    beforey = huay;
                    huay += huaw;
                    if(huar==360) huar = 0;
                
                    if(huay >= height) {
                    	running = false;
                    	hua.recycle();
                    }
            
        }
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
    public void clear() {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        // c.drawPaint(paint);
        Canvas canvas = holder.lockCanvas();
        canvas.drawPaint(paint);
        holder.unlockCanvasAndPost(canvas);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
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
