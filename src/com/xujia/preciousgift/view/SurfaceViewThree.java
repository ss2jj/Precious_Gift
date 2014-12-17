package com.xujia.preciousgift.view;

import com.xujia.preciousgift.R;
import com.xujia.preciousgift.utils.BitmapCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
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
		// TODO 自动生成的构造函数存根
	}

	public void setParamets(int width,int height,Handler handler)	{
		this.width = width;
		this.height = height;
		this.handler  = handler;
		cache = BitmapCache.getInstance();
		holder = this.getHolder();
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

	public void drawShuTeng()	{
		new ShowShuTeng().start();
	}

	class ShowShuTeng extends Thread	{
		int startX1,startY1,startX2,startY2;
		Bitmap bitmapLeft,bitmapRight;
		int mapWidth,mapHeight;
		int identy = 5;
		public ShowShuTeng()	{
			
			bitmapLeft = cache.getBitmap(R.drawable.shuteng, mContext);
			bitmapRight = cache.getBitmap(R.drawable.shuteng, mContext);
			mapWidth = bitmapLeft.getWidth();
			mapHeight = bitmapLeft.getHeight();
			startX1 = 0;
			startY1 = height;
			startX2 = width - mapWidth;
			startY2 = height;
		}
		
		public void run()	{
			while(identy < mapHeight)	{
				Canvas c = holder.lockCanvas(new Rect(startX1,identy,mapWidth,startY1));
				Bitmap newLeft= Bitmap.createBitmap(bitmapLeft, startX1, identy, mapWidth, identy);
				c.drawBitmap(newLeft, startX1, identy, null);
				c.drawBitmap(newLeft, startX2, identy, null);
				identy +=5;	
				holder.unlockCanvasAndPost(c);
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}
}
