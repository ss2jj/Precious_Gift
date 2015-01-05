
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


    public SurfaceViewFive(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        holder = this.getHolder();

        cache = BitmapCache.getInstance();
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
   
       
    }
    public void showYanHuo() {
     new ShowYanHuo().start();
    }
    
 
    public void setParamets(int width, int height, Handler handler) {
        this.width = width;
        this.height = height;
        this.handler = handler;
     
    }

    class ShowYanHuo extends Thread {

        int startX1,startY1;
        Bitmap bitmap;
        int mapWidth,mapHeight;
        Paint p = null;
        int alpha = 100;
        int mesh = 0;
        int WIDTH = 20,HEIGHT = 1;
        private int COUNT = (WIDTH+1)*(HEIGHT+1);
        float verts[] = new float[COUNT*2];
        float orgs[] = new float[COUNT*2];
        boolean runing  = true;
        public ShowYanHuo()    {
            
            bitmap = cache.getBitmap(R.drawable.yanhuo, mContext);
            bitmap =  Bitmap.createBitmap(bitmap, 0, 0, 480, 300);
            mapWidth = bitmap.getWidth();
            mapHeight = bitmap.getHeight();
            startX1 = 0;
            startY1 = 0;
            p = new Paint();
            p.setXfermode(new PorterDuffXfermode(Mode.SRC));
            int index = 0;
            for(int y = 0; y<=HEIGHT;y++)	{
            	float fy = mapHeight*y/HEIGHT;
            	for(int x=0;x<=WIDTH;x++)	{
            		float fx = mapWidth*x/WIDTH;
            		orgs[index*2+0] =  verts[index*2+0] =  fx;
            		orgs[index*2+1] =  verts[index*2+1] = fy;
            		index+=1;
            	}
            	
            }
        }
        
        public void run()   {
        	while(runing)	{
        		alpha+=4;
        		if(alpha >= 255)	runing = false;
        		p.setAlpha(alpha);
        		
        		Canvas c = holder.lockCanvas(new Rect(startX1,startY1,mapWidth,mapHeight));
        		c.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        		//mesh+=1;
        		//c.drawBitmap(bitmap, 0, 0, p);
        		holder.unlockCanvasAndPost(c);
        		wrap();
        		try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
        		
        	}
        	
        	Canvas c = holder.lockCanvas(new Rect(startX1,startY1,mapWidth,mapHeight));
        	c.drawBitmap(bitmap, 0, 0, null);
    		holder.unlockCanvasAndPost(c);
        
        }
    
        public void wrap()	{
        	for(int x =0;x<COUNT*2;x++)	{
        		
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
