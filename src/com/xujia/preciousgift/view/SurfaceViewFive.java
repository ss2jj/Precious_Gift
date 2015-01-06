
package com.xujia.preciousgift.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
      
    	new ShowHuaBian().start();
    }
    
 
    public void setParamets(int width, int height, Handler handler) {
        this.width = width;
        this.height = height;
        this.handler = handler;
     
    }

//    class ShowHuaBian extends Thread   {
//	    Paint p = new Paint();
//	    Bitmap hua = dealPhoto[0];
//        int huax = 150;
//        int huay = 0;
//        int huaw = hua.getWidth();
//        int huah = hua.getHeight();
//        int hua_add_plus = 1;
//        int huar=0; 
//        int huamax = 360;
//        int huamin = 0;
//        int huaCount = 0;
//        Matrix m = new Matrix();
//        Canvas c = null;
//        Bitmap b2 = null;
//        float sy = 0;
//        public ShowHuaBian()    {
//            p.setXfermode(new PorterDuffXfermode(Mode.CLEAR) );
//        }
//        public void run()   {
//            while(true) {      
//                              
//                   try {
//                    sleep(100);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                   // m.postRotate(180,huaw/2,huah/2);
//                   //p.setAlpha(255-Math.abs(huar));
//                   // m.setScale(1, -1);
//                  //  m.setSkew(0.2f, 0.2f, huax, hua.getHeight()+huay);
//                   
////                    b2 = Bitmap.createBitmap(
////                                hua, 0, 0, huaw,huah); 
//                  //  m.postSkew(0.2f, 0.2f, huax, hua.getHeight()+huay);
////                c = holder.lockCanvas(new Rect(huax-1,huay-1,huax+hua.getWidth(),
////                       height));
////                c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
////                holder.unlockCanvasAndPost(c);
////                p.setXfermode(new PorterDuffXfermode(Mode.SRC));
//              
//                    m.reset();
//                    //m.setTranslate(huax,huay);
//                    c = holder.lockCanvas();
//                  m.setSkew(0.2f, 0.2f);
//                  m.postRotate(sy);
//                   b2 = Bitmap.createBitmap(hua, 0, 0, huaw,huah,m,true); 
//                    c.drawColor(Color.TRANSPARENT,Mode.SRC);
//                    c.drawBitmap(b2, huax,huay, p);
//                    
//                   // c.drawBitmap(b2, m,null);
//                    //c.drawBitmap(big, dest_x, dest_y, p);
//                    holder.unlockCanvasAndPost(c);
//                    huar = huar+hua_add_plus;
//                    huay += 1;
//                    sy += 1;
//                    if(sy ==  360) sy = 0;
//                    if(huay == height) {
////                    	WaterView.touchWater(huax,50,10,200);
//                        p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
//                        c = holder.lockCanvas(new Rect(huax,huay-1,huax+hua.getWidth(),
//                                huay-1+hua.getHeight()));
//                        c.drawPaint(p);
//                        holder.unlockCanvasAndPost(c);
//                        p.setXfermode(new PorterDuffXfermode(Mode.SRC));
//                        if(!hua.isRecycled())    {
//                            hua.recycle();
//                        }
//                        huay = 0;
//                        huar = 0;
//                        huaCount++;
//                        if(huaCount >= dealPhoto.length)	{
//                        	huaCount = 0;
//                        	//handler.sendMessage(handler.obtainMessage(Utils.COMPLETE_PAGETHREE));
//                        	return;
//                        }
//                        hua = dealPhoto[huaCount];
//                        huar = 0;
//                        
//                    }
//            
//        }
//        }
//        
//     
//	}
    class ShowHuaBian extends Thread   {
        Paint p = new Paint();
        Bitmap hua = dealPhoto[0];
        int huax = 150;
        int huay = 0;
        int huaw = hua.getWidth();
        int huah = hua.getHeight();
        int hua_add_plus = 1;
        int huar=0; 
        int huamax = 360;
        int huamin = 0;
        int huaCount = 0;
        public void run()   {
            while(true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e2) {
                    // TODO 自动生成的 catch 块
                    e2.printStackTrace();
                }
                Canvas c = null;
             Bitmap b2 = null;
                
                    //c.drawColor(co);                  
                    Matrix m = new Matrix();
                    m.setRotate(huar,huax+huaw/2,huay+huah/2);
                  //  m.preSkew(0.2f, 0.2f);
                   //p.setAlpha(255-Math.abs(huar));
                    b2 = Bitmap.createBitmap(
                                hua, 0, 0, huaw,huah,m,true); 
                    c = holder.lockCanvas();
                   
                    c.drawColor(Color.TRANSPARENT,Mode.SRC);
                    c.drawBitmap(b2, huax,huay, p);

                    //c.drawBitmap(big, dest_x, dest_y, p);
                    holder.unlockCanvasAndPost(c);
                    huar = huar+hua_add_plus;
                    huay += 2;
                    if(huar==180) hua_add_plus = -2;
                    if(huar == 0) hua_add_plus = 2;
                    if(huay == height - 400) {
                        WaterView.touchWater(huax,50,10,200);
                        p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
                        c = holder.lockCanvas(new Rect(huax,huay-1,huax+b2.getWidth(),
                                huay-1+b2.getHeight()));
                        c.drawPaint(p);
                        holder.unlockCanvasAndPost(c);
                        p.setXfermode(new PorterDuffXfermode(Mode.SRC));
                        if(!b2.isRecycled())    {
                            b2.recycle();
                        }
                        huay = 0;
                        huar = 0;
                        huaCount++;
                        if(huaCount >= dealPhoto.length) {
                            huaCount = 0;
                           // handler.sendMessage(handler.obtainMessage(Utils.COMPLETE_PAGETHREE));
                            return;
                        }
                        hua =dealPhoto[huaCount];
                       
                        
                    }
            
        }
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
