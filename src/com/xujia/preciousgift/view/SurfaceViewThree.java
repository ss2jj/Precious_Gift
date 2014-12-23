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
private int backWidth;

private int backHeight;

/**
 * buf1 和 buf2是波能缓冲区，分别代表了每个点的前一时刻和后一时刻的波幅数据
 */
private short[] buf1;
private short[] buf2;

private int[] bitmap1;
private int[] bitmap2;

private Bitmap bgImage = null;

private boolean firstLoad = false;

WavingThread wavingThread = new WavingThread();

SurfaceHolder mSurfaceHolder = null;

private int doubleWidth;

private int fiveWidth;

private int loopTime;

private int bitmapLen;
	public SurfaceViewThree(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		holder = this.getHolder();
		mSurfaceHolder = holder;
		mSurfaceHolder.addCallback(this);
		cache = BitmapCache.getInstance();
		setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSPARENT); 
		
		// TODO 自动生成的构造函数存根
	}

	public void setParamets(int width,int height,Handler handler)	{
		this.width = width;
		this.height = height;
		this.handler  = handler;
		
	}
	
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!firstLoad) {
            bgImage =cache.getBitmap(R.drawable.background4, mContext);
           // bgImage = Bitmap.createScaledBitmap(bgImage, w, h, false);// 缩放而已
            bgImage = Bitmap.createBitmap(bgImage, 0, bgImage.getHeight()-200, bgImage.getWidth(), 200);
            backWidth = bgImage.getWidth();
            backHeight = bgImage.getHeight();

            buf2 = new short[backWidth * backHeight];
            buf1 = new short[backWidth * backHeight];

            bitmap2 = new int[backWidth * backHeight];
            bitmap1 = new int[backWidth * backHeight];

            // 将bgImage的像素拷贝到bitmap1数组中，用于渲染。。。
            bgImage.getPixels(bitmap1, 0, backWidth, 0, 0, backWidth,
                    backHeight);
            bgImage.getPixels(bitmap2, 0, backWidth, 0, 0, backWidth,
                    backHeight);

            for (int i = 0; i < backWidth * backHeight; ++i) {
                buf2[i] = 0;
                buf1[i] = 0;
            }
            doubleWidth = backWidth << 1;
            
            fiveWidth = 5 * backWidth;
            
            loopTime = ((backHeight - 4) * backWidth) >> 1;
            
            bitmapLen = backWidth * backHeight - 1;
            
            firstLoad = true;
        }

    }

    class WavingThread extends Thread {
        boolean running = true;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas c = null;
            while (running) {
                c = mSurfaceHolder.lockCanvas(new Rect(0, height-backHeight, backWidth, backHeight));
                makeRipple();
                doDraw(c);
                mSurfaceHolder.unlockCanvasAndPost(c);
            }
        }
    }

    /*******************************************************
     *计算波能数据缓冲区
     *******************************************************/
    private void makeRipple() {
        int k = fiveWidth;
        int xoff = 0, yoff = 0;
        int cp = 0;
        
        int tarClr = 0;
        int i = fiveWidth;
        while(i < loopTime){
            
            // 波能扩散
            buf2[k] = (short) (((buf1[k - 2] + buf1[k + 2]
                    + buf1[k - doubleWidth] + buf1[k + doubleWidth]) >> 1) - buf2[k]);
            
            // 波能衰减
            buf2[k] = (short)(buf2[k] - (buf2[k] >> 5));
            
            // 求出该点的左上的那个点xoff,yoff
            cp = k - doubleWidth - 2;

            xoff = buf2[cp - 2] - buf2[cp + 2];
            yoff = buf2[cp - doubleWidth] - buf2[k - 2];

            tarClr = k + yoff * doubleWidth + xoff;
            if(tarClr > bitmapLen || tarClr < 0){
                k += 2;
                continue;
            }
            // 复制象素
            bitmap2[k] = bitmap1[tarClr];
            k += 2;
            ++i;
        }

        short[] tmpBuf = buf2;
        buf2 = buf1;
        buf1 = tmpBuf;

    }

    /*****************************************************
     * 增加波源 x坐标 y坐标 波源半径 波源能量
     *****************************************************/
    private void touchWater(int x, int y, int stonesize, int stoneweight) {
        // 判断坐标是否在屏幕范围内
        if (x + stonesize > backWidth) {
            return;
        }
        if (y + stonesize > backHeight) {
            return;
        }
        if (x - stonesize < 0) {
            return;
        }
        if (y - stonesize < 0) {
            return;
        }
        // 产生波源，填充前导波能缓冲池
        int endStoneX = x + stonesize;
        int endStoneY = y + stonesize;
        int squaSize = stonesize * stonesize;
        int posy = y - stonesize;
        int posx = x - stonesize;
        for (posy = y - stonesize; posy < endStoneY; ++posy) {
            for (posx = x - stonesize; posx < endStoneX; ++posx) {
                if ((posx - x) * (posx - x) + (posy - y) * (posy - y) < squaSize) {
                    buf1[backWidth * posy + posx] = (short) -stoneweight;
                }
            }
        }

    }

    /*****************************************************
     * 增加波源 x坐标 y坐标 波源半径 波源能量
     *****************************************************/
    private void trickWater(int x, int y, int stonesize, int stoneweight) {
        // 判断坐标是否在屏幕范围内
        if (x + stonesize > backWidth) {
            return;
        }
        if (y + stonesize > backHeight) {
            return;
        }
        if (x - stonesize < 0) {
            return;
        }
        if (y - stonesize < 0) {
            return;
        }

        // 产生波源，填充前导波能缓冲池
        int endStoneX = x + stonesize;
        int endStoneY = y + stonesize;
        int posy = y - stonesize;
        int posx = x - stonesize;
        for (posy = y - stonesize; posy < endStoneY; ++posy) {
            for (posx = x - stonesize; posx < endStoneX; ++posx) {
                if (posy >= 0 && posy < backHeight && posx >= 0
                        && posx < backWidth) {
                    buf1[backWidth * posy + posx] = (short) -stoneweight;
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("XUJIA", "onTouchEvent---------------------");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchWater((int) event.getX(), (int) event.getY(), 4, 160);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            trickWater((int) event.getX(), (int) event.getY(), 2, 64);
        }
        return true;
    }

    protected void doDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap2, 0, backWidth, 0, height-backHeight, backWidth, backHeight,
                false, null);

    }
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO 自动生成的方法存根
	    wavingThread.setRunning(true);
        wavingThread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
	    boolean retry = true;
        wavingThread.setRunning(false);
        // 非暴力关闭线程，直到此次该线程运行结束之前，主线程停止运行，以防止Surface被重新激活
        while (retry) {
            try {
                wavingThread.join();       //阻塞current Thread(当前执行线程)直到被调用线程(thread)完成。
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}

	public void showYanHuo() {
        new ShowYanHuo().start();
    }
	
	public void showStar()	{
		new Timer().schedule(new ShowStar(), 300, 3000);
		showText();
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
        public ShowTextThread(String threadName,SurfaceHolder holder)   {
            this.setName(threadName);
            this.holder = holder;
            p = new Paint();
            p.setColor(Color.RED);
            p.setTextSize(size);
            p.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
            p.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "bylt.ttf"));
            texts = getResources().getString(R.string.poem2).split("\n");
            
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
                
                c = holder.lockCanvas(new Rect(startX-size*(i+1)+5*i, startY+size*count+5*i-20,
                        startX+size*(i+1)+10*i,   startY+size*(count+1)+10*i));
                c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.OVERLAY);  
                    String tm = String.valueOf(text.charAt(count));
                    Log.d("XUJIA","text is "+tm);
                 c.drawText(tm, startX+size*i+5*i,startY+size*count+5*i, p);
                
                holder.unlockCanvasAndPost(c);
                c = holder.lockCanvas(new Rect(startX-size*(i+1)+5*i, startY+size*count+5*i-20,
                        startX+size*(i+1)+10*i,   startY+size*(count+1)+10*i));
                 tm = String.valueOf(text.charAt(count));
                 c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.OVERLAY);  
               // c.drawText(tm_old, startX+size*i+5*i,startY, p);
             c.drawText(tm, startX+size*i+5*i,startY+size*count+5*i, p);
            
            holder.unlockCanvasAndPost(c);
            }
        }
       }
    }
    
	class ShowHuaBian extends Thread   {
	    Paint p = new Paint();
	    Bitmap hua = cache.getBitmap(R.drawable.hua, mContext);
        int huax = 150;
        int huay = 160;
        int huaw = hua.getWidth();
        int huah = hua.getHeight();
        int hua_add_plus = 1;
        int huar=0; 
        int huamax = 360;
        int huamin = 0;
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
                   // p.setAlpha(255-Math.abs(huar));
                    b2 = Bitmap.createBitmap(
                                hua, 0, 0, huaw,huah,m,true); 
                    c = holder.lockCanvas(new Rect(huax,huay,huax+b2.getWidth(),
                            huay+b2.getHeight()));
                   
                    c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
                    c.drawBitmap(b2, huax,huay, p);
                    Log.d("XUJIA", "huay="+huay+" b2.getHeight()="+ b2.getHeight());
                    //c.drawBitmap(big, dest_x, dest_y, p);
                    holder.unlockCanvasAndPost(c);
                    huar = huar+hua_add_plus;
                    huay += 1;
                    if(huar==huamax) huar = 0;
                    if(huay == height - 50) {
                        Log.d("XUJIA", "final ma huay="+huay+" b2.getHeight()="+ b2.getHeight());
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
			Log.d("XUJIA", "1111111111111");
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
			Log.d("XUJIA","startX1"+startX+"startY1"+startY);
			holder.unlockCanvasAndPost(c);
			startX = 300;
			startY = baseY + r.nextInt(50);
			c = holder.lockCanvas(new Rect(startX, startY,startX+start1.getWidth(),startY+start1.getHeight()));
			c.drawBitmap(start1, startX, startY, p);
			 Log.d("XUJIA","startX2"+startX+"startY2"+startY);
			holder.unlockCanvasAndPost(c);
			startX = 450;
			startY = baseY + r.nextInt(50);
			c = holder.lockCanvas(new Rect(startX, startY,startX+start4.getWidth(),startY+start4.getHeight()));
			c.drawBitmap(start4, startX, startY, p);
			 Log.d("XUJIA","startX3"+startX+"startY3"+startY);
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
