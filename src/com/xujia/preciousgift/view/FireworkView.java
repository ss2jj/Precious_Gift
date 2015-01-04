package com.xujia.preciousgift.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xujia.dot.Dot;
import com.xujia.dot.DotFactory;
import com.xujia.dot.LittleDot;
import com.xujia.preciousgift.R;
import com.xujia.preciousgift.utils.Utils;

import java.io.InputStream;
import java.util.Random;
import java.util.Vector;


/**     
 * �����ƣ�FireworkView   
 * ���������̳�android��View��ʵ���̻�Ч��
 * �����ˣ�anan   
 * ����ʱ�䣺2012-12-16 ����1:02:36   
 * �޸��ˣ�anan  
 * �޸�ʱ�䣺2012-12-16 ����1:02:36   
 * �޸ı�ע��   
 * @version        
 * */
public class FireworkView extends View {

	final String LOG_TAG = FireworkView.class.getSimpleName();

	public static final int ID_SOUND_UP = 0;
	public static final int ID_SOUND_BLOW = 1;
	public static final int ID_SOUND_MULTIPLE = 2;
	final static int TIME = 10; // Ȧ��

	/**�����е��̻���*/
	private Vector<Dot> lList = new Vector<Dot>();

	LittleDot[] ld = new LittleDot[200];
	private DotFactory df = null;

	boolean running = false;

	Bitmap backGroundBitmap;

	Context mContext;
private Handler mHander;
	public static SoundPlay soundPlay;
private Thread myThread;
public FireworkView(Context context, AttributeSet attrs) {
    super(context, attrs);
    // TODO �Զ����ɵĹ��캯�����
    df = new DotFactory();
    myThread = new MyThread();
    mContext = context;
//  backGroundBitmap = ReadBitMap(mContext, R.drawable.night);
//  backGroundBitmap = resizeImage(backGroundBitmap, 480, 800);
    initSound(mContext);
    
    Dot tmp = null;
    for(int i = 0;i<10;i++)  {
  int rand = (int) (Math.random() * 99);
  Random random = new Random(System.currentTimeMillis());
  tmp = df.makeDot(mContext, rand, random.nextInt(480),
          50 + random.nextInt(100));
          lList.add(tmp);
    }
}
	public FireworkView(Context context) {
		super(context);
	

	}

	public static void initSound(Context context) {
		soundPlay = new SoundPlay();
		soundPlay.initSounds(context);
		soundPlay.loadSfx(context, R.raw.up, ID_SOUND_UP);
		soundPlay.loadSfx(context, R.raw.blow, ID_SOUND_BLOW);
		soundPlay.loadSfx(context, R.raw.multiple, ID_SOUND_MULTIPLE);
		
	}

	public void startPlay( Handler handler)    {
	    running =  true;
	    myThread.start();
	    soundPlay.play(ID_SOUND_UP, 0);
	    mHander = handler;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//canvas.drawBitmap(backGroundBitmap, 0, 0, null);
		synchronized (lList) {
			for (int i = 0; i < lList.size(); i++) {
			   // soundPlay.play(ID_SOUND_UP, 0);
				lList.get(i).myPaint(canvas, lList);
			}
		}
		invalidate();
	}

	

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Bitmap ReadBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public Bitmap resizeImage(Bitmap mBitmap, int w, int h) {
		Bitmap BitmapOrg = mBitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap tmp = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height,
				matrix, true);
		return tmp;
	}

	/**     
	 * �����ƣ�MyThread   
	 * ���������ػ��߳�
	 * �����ˣ�anan   
	 * ����ʱ�䣺2012-12-16 ����1:06:50   
	 * �޸��ˣ�anan  
	 * �޸�ʱ�䣺2012-12-16 ����1:06:50   
	 * �޸ı�ע��   
	 * @version        
	 * */
	class MyThread extends Thread {
		// ���ڿ����̻��ڿ���������ʱ��
		int times = 0;

		public void run() {
			Dot dot = null;
			while (running) {

				try {
					Thread.sleep(100);
				} catch (Exception e) {
					System.out.println(e);
				}

				synchronized (lList) {
					// ��ֹ������̻���������50��
					while (lList.size() > 50) {
						System.out.println("��ǰ��Ŀ����50");
						for (int i = 0; i < 10; i++) {
							lList.remove(i);
						}
					}
////					// �Զ�����̻�
//					if (lList.size() <= 2) {
//						Dot tmp = null;
//						int rand = (int) (Math.random() * 99);
//						Random random = new Random();
//						tmp = df.makeDot(mContext, rand, random.nextInt(480),
//								50 + random.nextInt(100));
//						lList.add(tmp);
//					}
				}

				for (int i = 0; i < lList.size(); i++) {
					dot = (Dot) lList.get(i);
					if (dot.state == 1 && !dot.whetherBlast()) {
					  
						dot.rise();
					}
					// �����whetherBlast()���ص���true����ô�ͰѸ�dot��state����Ϊ2
					else if (dot.state == 1 && dot.state != 2) {
						dot.state = 2;
						soundPlay.play(ID_SOUND_BLOW, 0);
					} else if (dot.state == 3) {

					}
					// �涨��ÿ����ը�������TIMEȦ�������ͻ���ʧ
					if (dot.circle >= TIME) {
						// �ڿ�������һ�����ʧ
						if (times >= 10) {
							dot.state = 4;
							times = 0;
							lList.remove(i);
						} else {
							times++;
						}
						// dot.state = 4;
					}
				}
				if(lList.size() == 0)   {
				    running = false;
				    mHander.sendMessage(mHander.obtainMessage(Utils.SHOW_YANHUA));
				}
			}
		}
	}
}
