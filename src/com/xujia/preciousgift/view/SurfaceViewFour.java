
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

public class SurfaceViewFour extends SurfaceView implements SurfaceHolder.Callback {
    private int width, height;
    private Handler handler;
    private Context mContext;
    private BitmapCache cache;
    private SurfaceHolder holder;
    private Bitmap background1;
    int colums = 3;
    int rows = 3;
    int w = 0, h = 0;
    static final int RECT_WIDTH = 5, RECT_HEIGHT = 5;
    private Bitmap backgrounds[][] = new Bitmap[rows][colums];
    private Bitmap backs[][] = new Bitmap[rows][colums];

    public SurfaceViewFour(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        holder = this.getHolder();

        cache = BitmapCache.getInstance();
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
     
        background1 = BitmapFactory.decodeResource(getResources(), R.drawable.background4_1);

        // TODO 自动生成的构造函数存根
    }

    private void init() {
        w = (width - (colums + 1) * RECT_WIDTH) / colums;
        h = (height - (rows + 1) * RECT_HEIGHT) / rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                backs[i][j] = Bitmap.createBitmap(background1, j * w, i * h, w, h);
              
            }
        }
        backgrounds =new Bitmap[][] {{BitmapFactory.decodeResource(getResources(), R.drawable.q1),BitmapFactory.decodeResource(getResources(), R.drawable.q2),BitmapFactory.decodeResource(getResources(), R.drawable.q3)},
                {BitmapFactory.decodeResource(getResources(), R.drawable.q4),BitmapFactory.decodeResource(getResources(), R.drawable.q5),BitmapFactory.decodeResource(getResources(), R.drawable.q6)},
                {BitmapFactory.decodeResource(getResources(), R.drawable.q7),BitmapFactory.decodeResource(getResources(), R.drawable.q8),BitmapFactory.decodeResource(getResources(), R.drawable.q9)}};
    }

    public void setParamets(int width, int height, Handler handler) {
        this.width = width;
        this.height = height;
        this.handler = handler;
        init();

    }

    public void showBack() {
         clear();
        // new ShowPhotos().start();
        new ShowBackGround().start();
        
    }
public void showPhotos()    {
    new ShowPhotos().start();
}
public void showMail()  {
    new ShowMail("show mail",holder).start();
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

    class ShowBackGround extends Thread {
        int startX = 0, startY = 0;
        Canvas c = null;
        Paint p = null;

        public ShowBackGround() {
            p = new Paint();
            p.setFlags(Paint.ANTI_ALIAS_FLAG);
            p.setAntiAlias(true);
            p.setDither(true);
            // 设置光源的方向
            float[] direction = new float[] {
                    1, 1, 1
            };
            // 设置环境光亮度
            float light = 0.4f;
            // 选择要应用的反射等级
            float specular = 6;
            // 向mask应用一定级别的模糊
            float blur = 3.5f;
            p.setMaskFilter(new EmbossMaskFilter(direction, light, specular, blur));
            p.setStyle(Style.STROKE);
            p.setStrokeWidth(10);
            p.setStrokeCap(Cap.ROUND);
            p.setStrokeJoin(Join.ROUND);
            p.setColor(Color.WHITE);
        }

        public void run() {
            drawFrame();
            try {
                sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         //   drawBacks();
            handler.sendMessage(handler.obtainMessage(Utils.SHOW_PHOTOS));
        }

        private void drawFrame() {
            for (int i = 0; i <= rows; i++) {
                c = holder.lockCanvas(new Rect(0, i * (RECT_HEIGHT + h), width, i
                        * (RECT_HEIGHT + h) + RECT_HEIGHT));
                c.drawRect(new Rect(0, i * (RECT_HEIGHT + h), width, i * (RECT_HEIGHT + h)
                        + RECT_HEIGHT), p);
                holder.unlockCanvasAndPost(c);
            }
            for (int j = 0; j <= colums; j++) {
                if (j == colums) {
                    c = holder.lockCanvas(new Rect(j * (RECT_WIDTH + w), 0, width, height));
                    c.drawRect(new Rect(j * (RECT_WIDTH + w), 0, width, height), p);
                    holder.unlockCanvasAndPost(c);
                } else {
                    c = holder.lockCanvas(new Rect(j * (RECT_WIDTH + w), 0, j * (RECT_WIDTH + w)
                            + RECT_WIDTH, height));
                    c.drawRect(new Rect(j * (RECT_WIDTH + w), 0, j * (RECT_WIDTH + w) + RECT_WIDTH,
                            height), p);
                    holder.unlockCanvasAndPost(c);
                }
            }
        }

        private void drawBacks() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < colums; j++) {
                    c = holder.lockCanvas(new Rect(RECT_WIDTH + j * (RECT_WIDTH + w), i
                            * (RECT_HEIGHT + h) + RECT_HEIGHT, RECT_WIDTH + j * (RECT_WIDTH + w)
                            + w, i * (RECT_HEIGHT + h) + RECT_HEIGHT + h));
                    c.drawBitmap(backs[i][j], RECT_WIDTH + j * (RECT_WIDTH + w), i
                            * (RECT_HEIGHT + h) + RECT_HEIGHT, null);
                    holder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    class ShowMail extends Thread   {
        private SurfaceHolder holder;
        private int startX = 70;
        private int startY = 100;
        private Paint p ;
        private String[] texts;
        private String text;
        private Canvas c;
        private int size =25;
        private Rect rect;
        FontMetricsInt fontMetrics;
        public ShowMail(String threadName,SurfaceHolder holder)   {
            this.setName(threadName);
            this.holder = holder;
            p = new Paint();
            p.setColor(Color.CYAN);
            p.setTextSize(size);
            p.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
            p.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "bylt.ttf"));
            texts = getResources().getString(R.string.mail).split("\n");
            fontMetrics  = p.getFontMetricsInt();  
        }
        public void run()   {
            for(int i = 0; i< texts.length;i++) {
                text = texts[i];
                for(int count=0;count<text.length();count++)    {
                try {
                    this.sleep(350);
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
            handler.sendMessageDelayed(handler.obtainMessage(Utils.SHOW_BACK), 5000);
        }
    }
    class ShowPhotos extends Thread {
        Bitmap map;
        Bitmap back;
        Bitmap scaleMap;
        Bitmap scaleBack;
        Canvas c;
        float k = 1.0f;
        Paint p = new Paint();

        public ShowPhotos() {
            p.setXfermode(new PorterDuffXfermode(Mode.SRC));
        }

        public void run() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < colums; j++) {
                    map = backs[i][j];
                    back = backgrounds[i][j];
                    Log.i("XUJIA", "j*w" + j * w + " i*h" + i * h + " w" + w + " h" + h + " map"
                            + map);
                    Matrix matrix = new Matrix();

                    for (float k = 1.0f; k >= 0.01f; k -= 0.02) {
                        matrix.setScale(k, 1, RECT_WIDTH + j * (RECT_WIDTH + w) + w / 2, i
                                * (RECT_HEIGHT + h) + RECT_HEIGHT + h / 2);
                        Log.i("XUJIA", " w" + w + " h" + h);
                        // scaleBack = Bitmap.createBitmap(back, 0, 0, w, h);
                        // Log.i("XUJIA"," w"+scaleBack.getWidth()+" h"+scaleBack.getHeight());
                        c = holder.lockCanvas(new Rect(RECT_WIDTH + j * (RECT_WIDTH + w), i
                                * (RECT_HEIGHT + h) + RECT_HEIGHT, RECT_WIDTH + j
                                * (RECT_WIDTH + w) + w, i * (RECT_HEIGHT + h) + RECT_HEIGHT + h));
                        c.setMatrix(matrix);
                        c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
                        c.drawBitmap(back, RECT_WIDTH + j * (RECT_WIDTH + w), i * (RECT_HEIGHT + h)
                                + RECT_HEIGHT, p);
                        holder.unlockCanvasAndPost(c);
                    }

                    for (float k = 0.01f; k <= 1.0f; k += 0.02) {

                        matrix.setScale(k, 1, RECT_WIDTH + j * (RECT_WIDTH + w) + w / 2, i
                                * (RECT_HEIGHT + h) + RECT_HEIGHT + h / 2);
                        Log.i("XUJIA", " w" + w + " h" + h);
                        // scaleMap = Bitmap.createBitmap(map, 0, 0, w, h);
                        c = holder.lockCanvas(new Rect(RECT_WIDTH + j * (RECT_WIDTH + w), i
                                * (RECT_HEIGHT + h) + RECT_HEIGHT, RECT_WIDTH + j
                                * (RECT_WIDTH + w) + w, i * (RECT_HEIGHT + h) + RECT_HEIGHT + h));
                        c.setMatrix(matrix);
                        c.drawColor(Color.TRANSPARENT);
                        c.drawBitmap(map, RECT_WIDTH + j * (RECT_WIDTH + w), i * (RECT_HEIGHT + h)
                                + RECT_HEIGHT, p);
                        holder.unlockCanvasAndPost(c);

                    }

                }
            }
            handler.sendMessageDelayed(handler.obtainMessage(Utils.COMPLETE_PAGEFOUR),5000);
        }
    }

    public void showBackground() {
        Bitmap map;
        Canvas c;
        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(Mode.SRC));
        float sx = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                map = backgrounds[i][j];
                Log.i("XUJIA", "j*w" + j * w + " i*h" + i * h + " w" + w + " h" + h + " map" + map);
                c = holder.lockCanvas(new Rect(j * w, i * h, w, h));

                c.drawBitmap(map, j * w, i * h, p);
                holder.unlockCanvasAndPost(c);
            }
        }
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
