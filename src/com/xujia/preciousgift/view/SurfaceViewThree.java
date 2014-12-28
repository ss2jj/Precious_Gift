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
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class SurfaceViewThree extends SurfaceView implements SurfaceHolder.Callback{
private int width,height;
private Handler handler;
private Context mContext;
private BitmapCache cache;
private SurfaceHolder holder;
private int huas[];
private Timer timer;
private boolean isRuning = true;
	public SurfaceViewThree(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		holder = this.getHolder();
		
		cache = BitmapCache.getInstance();
		setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSPARENT); 
		 timer =new Timer();
		huas =  new int[]{
				R.drawable.fengye1,
				R.drawable.fengye2,
				R.drawable.fengye3,
				R.drawable.fengye4
		};
		// TODO 自动生成的构造函数存根
	}

	public void setParamets(int width,int height,Handler handler)	{
		this.width = width;
		this.height = height;
		this.handler  = handler;
		
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

	public void showYanHuo() {
        new ShowYanHuo().start();
    }
	public void clear() {
	    isRuning = false;
	    timer.cancel();
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        //c.drawPaint(paint);
        Canvas canvas= holder.lockCanvas();
        canvas.drawPaint(paint);
        holder.unlockCanvasAndPost(canvas);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        
        
}
	public void showStar()	{
	   
	    timer.schedule(new ShowStar(), 300, 3000);
		handler.sendEmptyMessageDelayed(Utils.SHOW_TIANSHI, 1000);
	}
	public void showHuaBan()	{
		new ShowHuaBian().start();
	}
	public void drawShuTeng()	{
		new ShowShuTeng().start();
	}
	public void showText() {
	    new ShowTextThread("showtext",holder).start();
	}
	
	
	
	class ShowTextThread extends Thread    {
        private SurfaceHolder holder;
        private int startX = 300;
        private int startY = height/2 - 300;
        private Paint p ;
        private String[] texts;
        private String text;
        private Canvas c;
        private int size =28;
        private Rect rect;
        FontMetricsInt fontMetrics;
        public ShowTextThread(String threadName,SurfaceHolder holder)   {
            this.setName(threadName);
            this.holder = holder;
            p = new Paint();
            p.setColor(Color.RED);
            p.setTextSize(size);
            p.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
            p.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "bylt.ttf"));
            texts = getResources().getString(R.string.poem2).split("\n");
            fontMetrics  = p.getFontMetricsInt();  
        }
        
        public void run()   {
            
            for(int i = 0; i< texts.length;i++) {
                text = texts[i];
                for(int count=0;count<text.length();count++)  { 
                try {
                    this.sleep(300);
                } catch (InterruptedException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
                String tm = String.valueOf(text.charAt(count));
                rect = new Rect(startX+i*size,startY+size*count,startX+(i+1)*size,startY+size*(count+1));
                c= holder.lockCanvas( new Rect(startX+i*size,startY+size*count,startX+(i+1)*size,startY+size*(count+1)));
                 int baseline = rect.top + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                 c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.OVERLAY);  
                 c.drawText(tm, startX+i*size,baseline, p);
                 holder.unlockCanvasAndPost(c);         
            }
        }
       }
    }
    
	class ShowHuaBian extends Thread   {
	    Paint p = new Paint();
	    Bitmap hua = cache.getBitmap(huas[0], mContext);
        int huax = 150;
        int huay = 160;
        int huaw = hua.getWidth();
        int huah = hua.getHeight();
        int hua_add_plus = 1;
        int huar=0; 
        int huamax = 360;
        int huamin = 0;
        int huaCount = 0;
        public void run()   {
            while(isRuning) {
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
                   //p.setAlpha(255-Math.abs(huar));
                    b2 = Bitmap.createBitmap(
                                hua, 0, 0, huaw,huah,m,true); 
                    c = holder.lockCanvas(new Rect(huax,huay,huax+b2.getWidth(),
                            huay+b2.getHeight()));
                   
                    c.drawColor(Color.TRANSPARENT,Mode.SRC);
                    c.drawBitmap(b2, huax,huay, p);

                    //c.drawBitmap(big, dest_x, dest_y, p);
                    holder.unlockCanvasAndPost(c);
                    huar = huar+hua_add_plus;
                    huay += 1;
                    if(huar==huamax) huar = 0;
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
                        huay = 160;
                        huar = 0;
                        huaCount++;
                        if(huaCount >= huas.length)	{
                        	huaCount = 0;
                        	handler.sendMessage(handler.obtainMessage(Utils.COMPLETE_PAGETHREE));
                        	return;
                        }
                        hua = cache.getBitmap(huas[huaCount], mContext);
                       
                        
                    }
            
        }
        }
        
     
	}
	class ShowYueLiang extends Thread {
	    Bitmap bitmap = null;
	    int statrtX,startY;
	    int degree = 0;
	    int w,h;
	    public ShowYueLiang()  {
	        bitmap = cache.getBitmap(R.drawable.yueliang,mContext);
	        statrtX = width-bitmap.getWidth();
	        startY =  0;
	        w= bitmap.getWidth();
	        h = bitmap.getHeight();
	    }
	    public void run()  {
	        while(true)    {
	            Matrix m = new Matrix();
	            m.setRotate(degree,statrtX+w/2,startY+h/2);
	         
	           Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, w,h); 
	          Canvas c = holder.lockCanvas(new Rect(statrtX,startY,statrtX+b2.getWidth(),
	                  startY+b2.getHeight()));
	          c.setMatrix(m);
               c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
               c.drawBitmap(b2, statrtX,startY, null);
               //c.drawBitmap(big, dest_x, dest_y, p);
               holder.unlockCanvasAndPost(c);
               degree++;
               if(degree ==  360) {
                   degree = 0;
               }
	        }
	      
	    }
	}
	class ShowStar extends TimerTask	{
		Bitmap start1,start2,start3,start4;
		int startX = 0,startY,baseY=0;
		Random r = null;
		Paint p = null;
		public ShowStar(){
			start1 =  cache.getBitmap(R.drawable.star1, mContext);
			start2 =  cache.getBitmap(R.drawable.star2, mContext);
			start3 =  cache.getBitmap(R.drawable.star3, mContext);
			start4 =  cache.getBitmap(R.drawable.star4, mContext);
			p = new Paint();
		
		}
		public void run()	{
			
			p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			startX = 150;
			Canvas c= holder.lockCanvas(new Rect(startX,baseY,startX+480,baseY+100));
			c.drawPaint(p);
			holder.unlockCanvasAndPost(c);
			p.setXfermode(new PorterDuffXfermode(Mode.SRC));
			r = new Random(System.currentTimeMillis());
		/*	startX = 0;
			startY = baseY + r.nextInt(50);
			 c = holder.lockCanvas(new Rect(startX, startY,startX+start1.getWidth(),startY+start1.getHeight()));
			c.drawBitmap(start1, startX, startY, null);
			holder.unlockCanvasAndPost(c);*/
			startX = 150;
			startY = baseY + r.nextInt(50);
			c = holder.lockCanvas(new Rect(startX, startY,startX+start2.getWidth(),startY+start2.getHeight()));
			c.drawBitmap(start2, startX, startY, p);
		
			holder.unlockCanvasAndPost(c);
			startX = 300;
			startY = baseY + r.nextInt(50);
			c = holder.lockCanvas(new Rect(startX, startY,startX+start1.getWidth(),startY+start1.getHeight()));
			c.drawBitmap(start1, startX, startY, p);
			
			holder.unlockCanvasAndPost(c);
			startX = 450;
			startY = baseY + r.nextInt(50);
			c = holder.lockCanvas(new Rect(startX, startY,startX+start4.getWidth(),startY+start4.getHeight()));
			c.drawBitmap(start4, startX, startY, p);
			
			holder.unlockCanvasAndPost(c);
		
		}
	}
	
	   class ShowYanHuo extends Thread {

	        int startX1,startY1;
	        Bitmap bitmapLeft;
	        int mapWidth,mapHeight;
	        int identy = 5;
	        int count = 0;
	        int yushu = 0;
	        Paint p = null;
	        public ShowYanHuo()    {
	            
	            bitmapLeft = cache.getBitmap(R.drawable.yanhuo, mContext);
	            mapWidth = bitmapLeft.getWidth();
	            mapHeight = bitmapLeft.getHeight();
	            startX1 = 0;
	            startY1 = height;
	            count = mapHeight / identy;
	            p = new Paint();
	            p.setXfermode(new PorterDuffXfermode(Mode.SRC));
	        }
	        
	        public void run()   {
	            for (int i = 0;i<count;i++) {
	                Canvas c = holder.lockCanvas(new Rect(startX1, height-identy,mapWidth,startY1));
	                Bitmap newLeft= Bitmap.createBitmap(bitmapLeft, startX1,mapHeight-identy, mapWidth, identy);
	                c.drawBitmap(newLeft, startX1, height-identy, p);
	                holder.unlockCanvasAndPost(c);
	                identy +=5; 
	                try {
	                    sleep(50);
	                } catch (InterruptedException e) {
	                    // TODO 自动生成的 catch 块
	                    e.printStackTrace();
	                }
	                clear();
	            }
	            if(( yushu = mapHeight % identy)!=0)    {
	                Canvas c = holder.lockCanvas(new Rect(startX1, height-mapHeight,mapWidth,height-mapHeight - yushu ));
	                Bitmap newLeft= Bitmap.createBitmap(bitmapLeft, startX1,startX1, mapWidth, yushu);
	                c.drawBitmap(newLeft, startX1, height-mapHeight, p);
	                holder.unlockCanvasAndPost(c);
	            }
	            clear();
	        
	        }
	    
	       private void clear()    {
	           p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
	            Canvas c= holder.lockCanvas(new Rect(startX1,height-identy,mapWidth,startY1));
	            c.drawPaint(p);
	            holder.unlockCanvasAndPost(c);
	            p.setXfermode(new PorterDuffXfermode(Mode.SRC));
	       }
	   }
	class ShowShuTeng extends Thread	{
		int startX1,startY1,startX2,startY2;
		Bitmap bitmapLeft,bitmapRight;
		int mapWidth,mapHeight;
		int identy = 5;
		int count = 0;
		int yushu = 0;
		public ShowShuTeng()	{
			
			bitmapLeft = cache.getBitmap(R.drawable.shuteng, mContext);
			bitmapRight = cache.getBitmap(R.drawable.shuteng, mContext);
			mapWidth = bitmapLeft.getWidth();
			mapHeight = bitmapLeft.getHeight();
			startX1 = 0;
			startY1 = height;
			startX2 = width - mapWidth;
			startY2 = height;
			count = mapHeight / identy;
		}
		
		public void run()	{
			for (int i = 0;i<count;i++)	{
				Canvas c = holder.lockCanvas(new Rect(startX1, height-identy,mapWidth,startY1));
				Bitmap newLeft= Bitmap.createBitmap(bitmapLeft, startX1,mapHeight-identy, mapWidth, identy);
				c.drawBitmap(newLeft, startX1, height-identy, null);
				holder.unlockCanvasAndPost(c);
				c = holder.lockCanvas(new Rect(startX2, height-identy,width,startY1));
				c.drawBitmap(newLeft, startX2, height-identy, null);
				identy +=5;	
				holder.unlockCanvasAndPost(c);
				try {
					sleep(80);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			if(( yushu = mapHeight % identy)!=0)	{
				Canvas c = holder.lockCanvas(new Rect(startX1, height-mapHeight,mapWidth,height-mapHeight - yushu ));
				Bitmap newLeft= Bitmap.createBitmap(bitmapLeft, startX1,startX1, mapWidth, yushu);
				c.drawBitmap(newLeft, startX1, height-mapHeight, null);
				holder.unlockCanvasAndPost(c);
				c = holder.lockCanvas(new Rect(startX2, height-mapHeight,width,height-mapHeight - yushu));
				c.drawBitmap(newLeft, startX2, height-mapHeight, null);
				holder.unlockCanvasAndPost(c);
			}
			
		
		}
	}
}
