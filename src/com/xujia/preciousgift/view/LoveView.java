package com.xujia.preciousgift.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LoveView  extends SurfaceView implements SurfaceHolder.Callback{
    boolean mbloop = false; 
    SurfaceHolder mSurfaceHolder = null; 
    private Canvas canvas; 
   int miCount = 0; 
   int y = 50; 
    public LoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = this.getHolder(); 
        mSurfaceHolder.addCallback(this); 
        this.setKeepScreenOn(true); 
       mbloop = true; 
       setZOrderOnTop(true);
       mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT); 
        // TODO Auto-generated constructor stub
    }


     
          @Override 
          public void surfaceChanged(SurfaceHolder holder, int format, int width, 
                  int height) { 
              // TODO Auto-generated method stub 
    
          } 
      
     
          @Override 
          public void surfaceCreated(SurfaceHolder holder) { 
              // TODO Auto-generated method stub 
             
          } 
    
       public  void start()  {
           new LoveThread().start();
       }
       public  void stop()  {
           mbloop = false;
       }
       public void clear() {
           mbloop = false;
           Paint paint = new Paint();
           paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
           // c.drawPaint(paint);
           Canvas canvas = mSurfaceHolder.lockCanvas();
           canvas.drawPaint(paint);
           mSurfaceHolder.unlockCanvasAndPost(canvas);
           paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
          
       }
  
          @Override 
          public void surfaceDestroyed(SurfaceHolder holder) { 
              // TODO Auto-generated method stub 
              mbloop = false; 
          } 
     
          class LoveThread extends Thread   {
              @Override 
              public void run() { 
                // TODO Auto-generated method stub 
                while (mbloop) { 
                      try { 
                        Thread.sleep(200); 
                     } catch (Exception e) { 
                        // TODO: handle exception 
                      } 
                    synchronized (mSurfaceHolder) { 
                         Draw(); 
                      } 
                  } 
             } 
          
      
             private void Draw() { 
                 // TODO Auto-generated method stub 
                 canvas = mSurfaceHolder.lockCanvas(); 
                 try { 
                     if (mSurfaceHolder == null || canvas == null) { 
                         return; 
                     } 
                     if (miCount < 100) { 
                        miCount++; 
                    } else { 
                         miCount = 0; 
                   } 
                     Paint paint = new Paint(); 
                     paint.setAntiAlias(true); 
                    // paint.setColor(Color.BLACK); 
                     //canvas.drawRect(0, 0, 320, 480, paint); 
                     switch (miCount % 6) { 
                    case 0: 
                         paint.setColor(Color.BLUE); 
                         break; 
                    case 1: 
                        paint.setColor(Color.GREEN); 
                         break; 
                    case 2: 
                        paint.setColor(Color.RED); 
                         break; 
                    case 3: 
                        paint.setColor(Color.YELLOW); 
                       break; 
                  case 4: 
                         paint.setColor(Color.argb(255, 255, 181, 216)); 
                         break; 
                     case 5: 
                         paint.setColor(Color.argb(255, 0, 255, 255)); 
                         break; 
                     default: 
                         paint.setColor(Color.BLACK); 
                        break; 
                     } 
                     int i, j; 
                    double x, y, r; 
        
                     for (i = 0; i<= 135; i++) { 
                         for (j = 0; j <= 135; j++) { 
                            // Thread.sleep(100);
                            r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j)) 
                                    * 20; 
                            x = r * Math.cos(Math.PI / 45 * j) * Math.sin(Math.PI / 45 * i) + 480 / 2; 
                            y = -r * Math.sin(Math.PI / 45 * j) + 854 / 4 -100; 
                          canvas.drawPoint((float) x, (float) y, paint); 
                     } 
                  } 
     
                     mSurfaceHolder.unlockCanvasAndPost(canvas); 
              } catch (Exception e) { 
                } 
       
            } 
          }
    
      

}
