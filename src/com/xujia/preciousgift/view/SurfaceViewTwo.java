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
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
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
		// TODO �Զ����ɵĹ��캯�����
	}
	
	public void setScreen(int width,int height)	{
		this.width = width;
		this.height =  height;
	
	}
	

	public void shouHeart()	{
		clear();
		new ShowHertThread("shouheart", holder).start();
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO �Զ����ɵķ������
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
	
	class ShowHertThread extends Thread	{
		private SurfaceHolder holder;
		int istartx,istarty,lovestartx,lovestarty,ustartx,ustarty;
		int yadd_1200 = 100;
		public ShowHertThread(String threadName,SurfaceHolder holder)	{
			this.setName(threadName);
			this.holder =  holder;
		}
		public void run()	{
			//��Ļ����
			istartx = -50 + width / 2;
			istarty = 50;
			
			
			lovestartx = width / 2 - 16;
			lovestarty = height / 2 - 68;
			
			ustartx = -94 + width / 2;
			ustarty = 150 + height / 2;
			
			if(height/2 >180+150+118+20) ustarty = 150 + height / 2 +20;
			if(height/2 >180+150+118+40) ustarty = 150 + height / 2 +40;
			if(height/2 >180+150+118+60) ustarty = 150 + height / 2 +60;
			if(height >= 1200) {
				istarty = istarty+yadd_1200;
				ustarty = ustarty+yadd_1200;
			}
			
			System.out.println("create1");
			this.holder.setKeepScreenOn(true);
		
			run_hua_heart();
			
		}
	
		private void run_hua_heart() {
			// TODO �Զ����ɵķ������
			int startx = width / 2 - 16, starty = height / 2 - 68;
			int maxh = 100;  
			int y_dao = starty;
			double begin = 10; // ��ʼλ��
			Random rm = new Random();
			int old_num = -1;
			float old_xx = 0, old_yy = 0;
			for (int i = 0; i < maxh  && !isallstop; i++) {
				try {
					Thread.sleep(80);
				} catch (InterruptedException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}

				int hua_num = rm.nextInt(18);
				Bitmap bit = bitmapcache
						.getBitmap(heart_all[hua_num], mContext);
				begin = begin + 0.2;  //�ܶ�
				double b = begin / Math.PI;
				double a = 13.5 * (16 * Math.pow(Math.sin(b), 3));  //�����13.5���Կ��ƴ�С
				double d = -13.5
						* (13 * Math.cos(b) - 5 * Math.cos(2 * b) - 2
								* Math.cos(3 * b) - Math.cos(4 * b));
				synchronized (holder) {
					
					try {
						float xx = (float) a;
						float yy = (float) d;
						
						c = holder.lockCanvas(new Rect(
								(int) (startx + xx - 40),
								(int) (starty + yy - 40),
								(int) (startx + xx + 40),
								(int) (starty + yy + 40)));
						Paint p = new Paint(); // ��������
						p.setColor(Color.RED);
						//����һ����Ҫ��Ȼ����˸
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
						// c.drawPoint(startx+xx,starty+yy, p);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// ����������ͼ�����ύ�ı䡣
								
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