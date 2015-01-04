
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
import com.xujia.preciousgift.utils.BitmapCache;
import com.xujia.preciousgift.utils.Utils;
import com.xujia.preciousgift.view.SurfaceViewThree.ShowYanHuo;

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
            while(true) {
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
//                clear();
            }
            if(( yushu = mapHeight % identy)!=0)    {
                Canvas c = holder.lockCanvas(new Rect(startX1, height-mapHeight,mapWidth,height-mapHeight - yushu ));
                Bitmap newLeft= Bitmap.createBitmap(bitmapLeft, startX1,startX1, mapWidth, yushu);
                c.drawBitmap(newLeft, startX1, height-mapHeight, p);
                holder.unlockCanvasAndPost(c);
            }
            identy = 0;
            
            }
            
        }
    
       private void clear()    {
           p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            Canvas c= holder.lockCanvas(new Rect(startX1,height-identy,mapWidth,startY1));
            c.drawPaint(p);
            holder.unlockCanvasAndPost(c);
            p.setXfermode(new PorterDuffXfermode(Mode.SRC));
       }
   }
    public void setParamets(int width, int height, Handler handler) {
        this.width = width;
        this.height = height;
        this.handler = handler;
     
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
