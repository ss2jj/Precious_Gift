package com.xujia.preciousgift.view;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.xujia.preciousgift.*;
import com.xujia.preciousgift.utils.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class SurfaceViewTwo extends SurfaceView implements SurfaceHolder.Callback{
private  int width,height;
private SurfaceHolder holder;
private boolean isallstop =  false;
private BitmapCache bitmapcache;
private Context mContext;
private Canvas c = null;
private Handler handler;
int[] heart_all = { R.drawable.a1, R.drawable.a2, R.drawable.a3,
		R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7,
		R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11,
		R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15,
		R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19 };
	public SurfaceViewTwo(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context; 
		this.holder = getHolder();
		this.holder.addCallback(this);
		this.bitmapcache = BitmapCache.getInstance();
		setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSPARENT); 
		// TODO 自动生成的构造函数存根
	}
	
	public void setScreen(int width,int height)	{
		this.width = width;
		this.height =  height;
	
	}
	
	public void setHandler(Handler handler)	{
		this.handler =  handler;
	}

	public void showHeart()	{
		clear();
		new ShowHeartThread("showheart", holder).start();
		
	}
	
	public void showCandite()	{
		new ShowCanditeThread("showcandite",holder).start();
	}
	
	public void showText()	{
		new ShowTextThread("showtext",holder).start();
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
		// TODO 自动生成的方法存根
		//Toast.makeText(mContext, "destoryed", 1000).show();
		//clear(aCanvas);
	}
	
	public void clear() {
	
			Paint paint = new Paint();
			paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			//c.drawPaint(paint);
			Canvas canvas= holder.lockCanvas();
			canvas.drawPaint(paint);
			holder.unlockCanvasAndPost(canvas);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
			
		
	}
	class ShowTextThread extends Thread	{
		private SurfaceHolder holder;
		private int startX = 70;
		private int startY = height/2 + 200;
		private Paint p ;
		private String[] texts;
		private String text;
		private Canvas c;
		private int size =25;
		private Rect rect;
		FontMetricsInt fontMetrics;
		public ShowTextThread(String threadName,SurfaceHolder holder)	{
			this.setName(threadName);
			this.holder = holder;
			p = new Paint();
			p.setColor(Color.BLACK);
			p.setTextSize(size);
			p.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
			p.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "bylt.ttf"));
			texts = getResources().getString(R.string.poem).split("\n");
			fontMetrics  = p.getFontMetricsInt();  
		}
		
		public void run()	{
			
			for(int i = 0; i< texts.length;i++)	{
				text = texts[i];
				for(int count=0;count<text.length();count++)	{
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
			handler.sendMessageDelayed(handler.obtainMessage(Utils.COMPLETE_PAGETWO),10000);
	   }
	}
	class ShowCanditeThread extends Thread	{
		private SurfaceHolder holder;
		Bitmap bitmap;
		int bitmapWidth,bitmapHeight;
		int w,h,w_y,h_y;
		Canvas c = null;
		Paint p = new Paint(); // 创建画笔
		public ShowCanditeThread(String threadName,SurfaceHolder holder)	{
			this.setName(threadName);
			this.holder = holder;
			bitmap = bitmapcache.getBitmap(R.drawable.lazhu,mContext);
			bitmapWidth =  bitmap.getWidth();
			bitmapHeight =  bitmap.getHeight();
			w = width/bitmapWidth;
			w_y =  width%bitmapWidth;
			if(w_y > 5)	{
				w_y = w_y/w;
			}
			h = height/bitmapHeight;
			p.setColor(Color.RED);
			p.setXfermode(new PorterDuffXfermode(Mode.SRC));
		}
		
		
		public void run()	{
			
			//Toast.makeText(mContext,"w: "+w+"  h"+h, 1000).show();
			//Toast.makeText(mContext,"width: "+width+"  h"+h, 1000).show();
			/*Log.d("XUJIA", "width: "+width+"height:"+height+" bitmapWidth"+bitmapWidth+" bitmapGeight"+bitmapHeight+"w_y"+w_y);*/
			
			
			for(int x =0;x<w;x++)	{
				try {
					
					if(x == 0)	{
						c = holder.lockCanvas(new Rect(x*bitmapWidth,0,(x+1)*bitmapWidth,bitmapHeight));
						c.drawBitmap(bitmap, x*bitmapWidth, 0, p);	
					}else if(x == w-1)	{
						c = holder.lockCanvas(new Rect(width-bitmapWidth,0,width,bitmapHeight));
						c.drawBitmap(bitmap, width-bitmapWidth, 0, p);	
					}else{
						c = holder.lockCanvas(new Rect(x*(bitmapWidth+w_y),0,(x+1)*(bitmapWidth+w_y),bitmapHeight));
						c.drawBitmap(bitmap, x*(bitmapWidth+w_y), 0, p);	
					}
					
					holder.unlockCanvasAndPost(c);
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			for(int x =0;x<h-1;x++)	{
				try {
					c = holder.lockCanvas(new Rect(width-bitmapWidth,(x+1)*bitmapHeight,width,(x+2)*bitmapHeight));
					c.drawBitmap(bitmap, width-bitmapWidth, (x+1)*bitmapHeight, p);	
					holder.unlockCanvasAndPost(c);
					Thread.sleep(80);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			for(int x = w-2;x>=0;x--)	{
				try {
					
					if(x == 0)	{
						c = holder.lockCanvas(new Rect((x)*bitmapWidth,(h-1)*bitmapHeight,(x+1)*bitmapWidth,(h)*bitmapHeight));
						c.drawBitmap(bitmap, (x)*bitmapWidth, (h-1)*bitmapHeight, p);	
					}else	{
						c = holder.lockCanvas(new Rect((x)*(bitmapWidth+w_y),(h-1)*bitmapHeight,(x+1)*(bitmapWidth+w_y),(h)*bitmapHeight));
						c.drawBitmap(bitmap, (x)*(bitmapWidth+w_y), (h-1)*bitmapHeight, p);	
					}
					
					
					holder.unlockCanvasAndPost(c);
					Thread.sleep(60);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			for(int x = h-2;x>=1;x--)	{
				try {
					c = holder.lockCanvas(new Rect(0,x*bitmapHeight,bitmapWidth,(x+1)*bitmapHeight));
					c.drawBitmap(bitmap, 0, x*bitmapHeight, p);	
					holder.unlockCanvasAndPost(c);
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			
		handler.sendMessage(handler.obtainMessage(Utils.CANDITE_COMPLETE));
		}
		
	}
	
	class ShowHeartThread extends Thread	{
		private SurfaceHolder holder;
		public ShowHeartThread(String threadName,SurfaceHolder holder)	{
			this.setName(threadName);
			this.holder =  holder;
		}
		public void run()	{
			System.out.println("create1");
			this.holder.setKeepScreenOn(true);
		
			run_hua_heart();
			handler.sendMessage(handler.obtainMessage(Utils.HEART_COMPLETE));
		}
	
		private void run_hua_heart() {
			// TODO 自动生成的方法存根
			//int startx = width / 2 - 16, starty = height / 2 - 68;
			int startx = width / 2 - 10, starty = height / 2 -68;
			int maxh = 100;  
			int y_dao = starty;
			double begin = 10; // 起始位置
			Random rm = new Random();
			int old_num = -1;
			float old_xx = 0, old_yy = 0;
			int hua_num = 0;
			for (int i = 0; i < maxh ; i++) {
				try {
					Thread.sleep(80);
				} catch (InterruptedException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

				
				Bitmap bit = bitmapcache
						.getBitmap(heart_all[hua_num], mContext);
				begin = begin + 0.5;  //密度
				double b = begin / Math.PI;
				double a = 11 * (16 * Math.pow(Math.sin(b), 3));  //这里的11可以控制大小
				double d = -11
						* (13 * Math.cos(b) - 5 * Math.cos(2 * b) - 2
								* Math.cos(3 * b) - Math.cos(4 * b));
				synchronized (holder) {
					
					try {
						float xx = (float) a;
						float yy = (float) d;
						
						/*c = holder.lockCanvas(new Rect(
								(int) (startx + xx - 40),
								(int) (starty + yy - 40),
								(int) (startx + xx + 40),
								(int) (starty + yy + 40)));*/
						c = holder.lockCanvas(new Rect(
								(int) (startx + old_xx),
								(int) (starty + old_yy),
								(int) (startx + old_xx + 25),
								(int) (starty + old_yy + 25)));
						Paint p = new Paint(); // 创建画笔
						p.setColor(Color.RED);
						//画上一个，要不然会闪烁
						if (old_num != -1) {
							Bitmap bb = bitmapcache.getBitmap(
									heart_all[old_num], mContext);
							c.drawBitmap(bb, startx + old_xx, starty + old_yy,
									p);
						}
						c.drawBitmap(bit, startx + xx, starty + yy, p);
						old_num = hua_num;
						old_xx = xx;
						old_yy = yy;
						hua_num++;
						if(hua_num >= heart_all.length)  {
						    hua_num = 0;
						}
						// c.drawPoint(startx+xx,starty+yy, p);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
								
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}

		}
	}

}
