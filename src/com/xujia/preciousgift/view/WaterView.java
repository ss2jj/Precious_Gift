package com.xujia.preciousgift.view;

import com.xujia.preciousgift.R;
import com.xujia.preciousgift.utils.BitmapCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class WaterView extends  SurfaceView implements SurfaceHolder.Callback{
	
	private static int backWidth;

	private static int backHeight;
	/**
	 * buf1 �� buf2�ǲ��ܻ��������ֱ������ÿ�����ǰһʱ�̺ͺ�һʱ�̵Ĳ�������
	 */
	private static short[] buf1;
	private short[] buf2;

	private int[] bitmap1;
	private int[] bitmap2;
	private BitmapCache cache;
	private Bitmap bgImage = null;

	private boolean firstLoad = false;

	WavingThread wavingThread = new WavingThread();

	SurfaceHolder mSurfaceHolder = null;

	private int doubleWidth;

	private int fiveWidth;

	private int loopTime;
	private Context mContext;
	private int bitmapLen;
	public WaterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		cache = BitmapCache.getInstance();
		bgImage =cache.getBitmap(R.drawable.background4_2, mContext);
        bgImage = Bitmap.createBitmap(bgImage, 0, bgImage.getHeight()-400, bgImage.getWidth(), 400);
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT); 
	}

	
	public void clear() {
	    boolean retry = true;
        wavingThread.setRunning(false);
        // �Ǳ����ر��̣߳�ֱ���˴θ��߳����н���֮ǰ�����߳�ֹͣ���У��Է�ֹSurface�����¼���
        while (retry) {
            try {
                wavingThread.join();       //����current Thread(��ǰִ���߳�)ֱ���������߳�(thread)��ɡ�
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        //c.drawPaint(paint);
        Canvas canvas= mSurfaceHolder.lockCanvas();
        canvas.drawPaint(paint);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        if(!bgImage.isRecycled())   {
            bgImage.recycle();
        }
    
}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO �Զ����ɵķ������
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		  int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
		  int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
	      if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)	{
	    	  setMeasuredDimension(bgImage.getWidth(), bgImage.getHeight());  
	      }else	{
	    	  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	      }


	}
	public void start()    {
	    wavingThread.setRunning(true);
        wavingThread.start();
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
	    boolean retry = true;
        wavingThread.setRunning(false);
        // �Ǳ����ر��̣߳�ֱ���˴θ��߳����н���֮ǰ�����߳�ֹͣ���У��Է�ֹSurface�����¼���
        while (retry) {
            try {
                wavingThread.join();       //����current Thread(��ǰִ���߳�)ֱ���������߳�(thread)��ɡ�
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!firstLoad) {
            bgImage = Bitmap.createScaledBitmap(bgImage, w, h, false);// ���Ŷ���
           
            backWidth = bgImage.getWidth();
            backHeight = bgImage.getHeight();

            buf2 = new short[backWidth * backHeight];
            buf1 = new short[backWidth * backHeight];

            bitmap2 = new int[backWidth * backHeight];
            bitmap1 = new int[backWidth * backHeight];

            // ��bgImage�����ؿ�����bitmap1�����У�������Ⱦ������
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
                c = mSurfaceHolder.lockCanvas(new Rect(0,0,backWidth,backHeight));
                makeRipple();
                doDraw(c);
                mSurfaceHolder.unlockCanvasAndPost(c);
            }
        }
    }

    /*******************************************************
     *���㲨�����ݻ�����
     *******************************************************/
    private void makeRipple() {
        int k = fiveWidth;
        int xoff = 0, yoff = 0;
        int cp = 0;
        
        int tarClr = 0;
        int i = fiveWidth;
        while(i < loopTime){
            
            // ������ɢ
            buf2[k] = (short) (((buf1[k - 2] + buf1[k + 2]
                    + buf1[k - doubleWidth] + buf1[k + doubleWidth]) >> 1) - buf2[k]);
            
            // ����˥��
            buf2[k] = (short)(buf2[k] - (buf2[k] >> 5));
            
            // ����õ�����ϵ��Ǹ���xoff,yoff
            cp = k - doubleWidth - 2;

            xoff = buf2[cp - 2] - buf2[cp + 2];
            yoff = buf2[cp - doubleWidth] - buf2[k - 2];

            tarClr = k + yoff * doubleWidth + xoff;
            if(tarClr > bitmapLen || tarClr < 0){
                k += 2;
                continue;
            }
            // ��������
            bitmap2[k] = bitmap1[tarClr];
            k += 2;
            ++i;
        }

        short[] tmpBuf = buf2;
        buf2 = buf1;
        buf1 = tmpBuf;

    }

    /*****************************************************
     * ���Ӳ�Դ x���� y���� ��Դ�뾶 ��Դ����
     *****************************************************/
    public static void touchWater(int x, int y, int stonesize, int stoneweight) {
        // �ж������Ƿ�����Ļ��Χ��
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
        // ������Դ�����ǰ�����ܻ����
        Log.d("XUJIA", "touchWater----------");
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
     * ���Ӳ�Դ x���� y���� ��Դ�뾶 ��Դ����
     *****************************************************/
    private void trickWater(int x, int y, int stonesize, int stoneweight) {
        // �ж������Ƿ�����Ļ��Χ��
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
        Log.d("XUJIA", "trickWater----------");
        // ������Դ�����ǰ�����ܻ����
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
        Log.d("XUJIA", "onTouchEvent---------------------y"+(int) (event.getY()-( 854-backHeight)));
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchWater((int) event.getX(), (int) (event.getY()), 4, 160);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            trickWater((int) event.getX(), (int) (event.getY()), 2, 64);
        }
        return false;
    }

    protected void doDraw(Canvas canvas) {
    	if(canvas != null)	{
        canvas.drawBitmap(bitmap2, 0, backWidth, 0, 0, backWidth, backHeight,
                false, null);
    	}

    }
    
}
