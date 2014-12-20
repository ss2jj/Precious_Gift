package com.xujia.preciousgift.view;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.xujia.preciousgift.R;
import com.xujia.preciousgift.utils.BitmapCache;
import com.xujia.preciousgift.utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewThree extends SurfaceView implements SurfaceHolder.Callback{
private int width,height;
private Handler handler;
private Context mContext;
private BitmapCache cache;
private SurfaceHolder holder;

	public SurfaceViewThree(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		holder = this.getHolder();
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
		
	}

	
	public void showStar()	{
		new Timer().schedule(new ShowStar(), 300, 1000);
	}
	public void drawShuTeng()	{
		new ShowShuTeng().start();
	}
	class ShowStar extends TimerTask	{
		Bitmap start1,start2,start3,start4;
		int startX = 0,startY,baseY=600;
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
			Canvas canvas= holder.lockCanvas(new Rect(startX,baseY,startX+480,baseY+100));
			canvas.drawPaint(p);
			holder.unlockCanvasAndPost(canvas);
			//p.setXfermode(new PorterDuffXfermode(Mode.SRC));
			r = new Random(System.currentTimeMillis());
			startX = r.nextInt(400);
			startY = baseY + r.nextInt(50);
			Canvas c = holder.lockCanvas(new Rect(startX, startY,startX+start1.getWidth(),startY+start1.getHeight()));
			c.drawBitmap(start1, startX, startY, null);
			holder.unlockCanvasAndPost(c);
			startX = r.nextInt(400);
			startY = baseY + r.nextInt(50);
			c = holder.lockCanvas(new Rect(startX, startY,startX+start2.getWidth(),startY+start2.getHeight()));
			c.drawBitmap(start2, startX, startY, null);
			holder.unlockCanvasAndPost(c);
			startX = r.nextInt(400);
			startY = baseY + r.nextInt(50);
			c = holder.lockCanvas(new Rect(startX, startY,startX+start3.getWidth(),startY+start3.getHeight()));
			c.drawBitmap(start3, startX, startY, null);
			holder.unlockCanvasAndPost(c);
			startX = r.nextInt(400);
			startY = baseY + r.nextInt(50);
			c = holder.lockCanvas(new Rect(startX, startY,startX+start4.getWidth(),startY+start4.getHeight()));
			c.drawBitmap(start4, startX, startY, null);
			holder.unlockCanvasAndPost(c);
		
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
