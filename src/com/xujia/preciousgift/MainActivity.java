package com.xujia.preciousgift;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xujia.preciousgift.adapter.MyPageViewAdapter;
import com.xujia.preciousgift.service.MusicPlayService;
import com.xujia.preciousgift.transformer.AccordionTransformer;
import com.xujia.preciousgift.utils.Utils;
import com.xujia.preciousgift.view.FireworkView;
import com.xujia.preciousgift.view.LoveView;
import com.xujia.preciousgift.view.PhotoView;
import com.xujia.preciousgift.view.SurfaceViewFive;
import com.xujia.preciousgift.view.SurfaceViewFour;
import com.xujia.preciousgift.view.SurfaceViewThree;
import com.xujia.preciousgift.view.SurfaceViewTwo;
import com.xujia.preciousgift.view.WaterView;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnPageChangeListener{
private ViewPager myViewPager;
private ArrayList<View> views = new ArrayList<View>();
private MyPageViewAdapter adapter;

private Animation ani_hotball,ani_hotball2,ani_hotball3,ani_textone,ani_music,ani_texttwo,ani_leftheart,ani_tianshimove,ani_qinglv;
private AnimationDrawable ani_tianshi,ani_qinglvanim;

private ImageView hotBallView,hotBallView2,hotBallView3,textOneView,textTwoView,leftView,tianShiView,qingLvView,photoWallView,chuangTextView;
private boolean isMusicOpend = true;
private ImageButton exit;
private static final String ACTION_SERVICE = "com.xujia.preciousgift.musicplayservice";
private MusicPlayService.MediaPlayerController controller;
private MyServiceConnection serviceConnection;
private SurfaceViewTwo surfaceView2;
private SurfaceViewThree surfaceView3;
private SurfaceViewFour surfaceView4;
private SurfaceViewFive surfaceView5;
private WaterView waterView;
private LoveView loveView;
private FireworkView fireWork;

private boolean DEBUG = false;
private boolean isUnMoveable = false;
private Handler handler = new Handler(){
    public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case Utils.START_HOTBALL:
                hotBallView.startAnimation(ani_hotball);
                hotBallView.setVisibility(View.VISIBLE);
                break;
            case Utils.START_HOTBALL2:
            	 hotBallView2.startAnimation(ani_hotball2);
            	 hotBallView2.setVisibility(View.VISIBLE);
            	break;
            case Utils.START_HOTBALL3:
            	hotBallView3.startAnimation(ani_hotball3);
            	hotBallView3.setVisibility(View.VISIBLE);
            	break;
            	
            case Utils.START_WENZI1:
                textOneView.startAnimation(ani_textone);
                textOneView.setVisibility(View.VISIBLE);
                break;
            case Utils.START_WENZI2:
            	textTwoView.startAnimation(ani_texttwo);
            	textTwoView.setVisibility(View.VISIBLE);
            	break;

            case Utils.START_LEFTHEART:
                leftView.startAnimation(ani_leftheart);
                leftView.setVisibility(View.VISIBLE);
                break;
            case Utils.COMPLETE_PAGEONE:
            	myViewPager.setCurrentItem(1);
            	break;
            case Utils.START_SHOWHEART:
            	isUnMoveable = true;
            	surfaceView2.showHeart();   
            	break;
            case Utils.HEART_COMPLETE:
                surfaceView2.showCandite();   
               break;
            case Utils.CANDITE_COMPLETE:
                isUnMoveable = false;
                qingLvView.startAnimation(ani_qinglv);
                ani_qinglvanim.start();
                qingLvView.setVisibility(View.VISIBLE);
                handler.sendMessageDelayed(handler.obtainMessage(Utils.QINGLV_COMPLETE),3000);
                break;
            case Utils.QINGLV_COMPLETE:
            	photoWallView.startAnimation(ani_textone);
            	photoWallView.setVisibility(View.VISIBLE);
            	handler.sendMessageDelayed(handler.obtainMessage(Utils.RENWU_COMPLETE),3000);
            	break;
            case Utils.RENWU_COMPLETE:
            	chuangTextView.startAnimation(ani_texttwo);
            	chuangTextView.setVisibility(View.VISIBLE);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
            	surfaceView2.showText();
            	break;
            case Utils.COMPLETE_PAGETWO:
            	myViewPager.setCurrentItem(2);
            	break;
            	
            case Utils.SHOW_STAR:
    			surfaceView3.showStar();
            	break;
            case Utils.SHOW_TIANSHI:
            	ani_tianshi.start();
    			tianShiView.startAnimation(ani_tianshimove);
    			tianShiView.setVisibility(View.VISIBLE);
    			handler.sendEmptyMessageDelayed(Utils.SHOW_HUABAN, 3000);
            	break;
            case Utils.SHOW_HUABAN:
            	surfaceView3.showHuaBan();
            	handler.sendEmptyMessageDelayed(Utils.SHOW_POEM, 1000);
            	break;
            case Utils.SHOW_POEM:
            	surfaceView3.showText();
            	break;
            case  Utils.COMPLETE_PAGETHREE:
                myViewPager.setCurrentItem(3);
                break;
            case Utils.SHOW_MAIL:
                surfaceView4.showMail();
                break;
            case Utils.SHOW_BACK:
                surfaceView4.showBack();
                break;
            case Utils.SHOW_PHOTOS:
                surfaceView4.showPhotos();
                break;
            case Utils.COMPLETE_PAGEFOUR:
                myViewPager.setCurrentItem(4);
                break;
            case Utils.SHOW_FIRE:
                fireWork.startPlay(handler);
              //  surfaceView5.showYanHuo();
                break;
            case Utils.SHOW_YANHUA:
                loveView.start();
                surfaceView5.showTextFrame();
             
                break;
            case Utils.COMPLETE_PAGEFIVE:
                exit.setVisibility(View.VISIBLE);
             break;
            default:
                break;
        }
    };
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		myViewPager = (ViewPager)findViewById(R.id.myviewpager);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view1 = (View)inflater.inflate(R.layout.pageview_one, null);
		View view2 = (View)inflater.inflate(R.layout.pageview_two, null);
		View view3 = (View)inflater.inflate(R.layout.pageview_three, null);
		View view4 = (View)inflater.inflate(R.layout.pageview_four, null);
		View view5 =(View)inflater.inflate(R.layout.pageview_five, null);
		Window window = getWindow();
		int width = window.getWindowManager().getDefaultDisplay().getWidth();
		int height = window.getWindowManager().getDefaultDisplay().getHeight();	
		surfaceView2 =  (SurfaceViewTwo)view2.findViewById(R.id.myServiceView);
		
		surfaceView2.setScreen(width, height);
		surfaceView2.setHandler(handler);
		
		surfaceView3 = (SurfaceViewThree)view3.findViewById(R.id.mySurfaceView3);
		surfaceView3.setParamets(width, height, handler);
		waterView = (WaterView)view3.findViewById(R.id.waterView);
		surfaceView4 = (SurfaceViewFour)view4.findViewById(R.id.surfaceView4);
		surfaceView4.setParamets(width, height, handler);
	
		surfaceView5 = (SurfaceViewFive)view5.findViewById(R.id.surfaceView5);
		exit = (ImageButton)view5.findViewById(R.id.exit);
	    surfaceView5.setParamets(width, height, handler);
	    fireWork = (FireworkView)view5.findViewById(R.id.fireWork);
	    loveView =(LoveView) view5.findViewById(R.id.loveView);
		ani_hotball = AnimationUtils.loadAnimation(this, R.anim.hotball_anim);
		ani_hotball2 = AnimationUtils.loadAnimation(this, R.anim.hotball2_anim);
		ani_hotball3 = AnimationUtils.loadAnimation(this, R.anim.hotball3_anim);
		ani_textone = AnimationUtils.loadAnimation(this, R.anim.wenzi_anim);
		//ani_music = AnimationUtils.loadAnimation(this,R.anim.music_anim);
		ani_texttwo = AnimationUtils.loadAnimation(this, R.anim.wenzi2_anim);
		ani_leftheart = AnimationUtils.loadAnimation(this, R.anim.leftheart_anim);
		ani_tianshimove = AnimationUtils.loadAnimation(this, R.anim.tianshi_ani);
		ani_qinglv = AnimationUtils.loadAnimation(this, R.anim.qinglv_anim);
		
		hotBallView =(ImageView)view1.findViewById(R.id.hotball);
		hotBallView2 = (ImageView)view1.findViewById(R.id.hotball2);
		hotBallView3 = (ImageView)view1.findViewById(R.id.hotball3);
		textOneView = (ImageView)view1.findViewById(R.id.wenzi1);
		textTwoView = (ImageView)view1.findViewById(R.id.wenzi2);
		leftView = (ImageView)view1.findViewById(R.id.zuobian);
		qingLvView = (ImageView)view2.findViewById(R.id.qinglv);
		photoWallView = (ImageView)view2.findViewById(R.id.photewall);
		chuangTextView = (ImageView)view2.findViewById(R.id.chuangkouwenzi);
	
		tianShiView = (ImageView)view3.findViewById(R.id.tianshi);
		ani_tianshi = (AnimationDrawable) tianShiView.getBackground();
		ani_qinglvanim = (AnimationDrawable)qingLvView.getBackground();


		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		adapter = new MyPageViewAdapter(views);
		myViewPager.setAdapter(adapter);
		myViewPager.setPageTransformer(true, new AccordionTransformer());
		myViewPager.setKeepScreenOn(true);
		myViewPager.setOnPageChangeListener(this);
		//禁止手动翻页 实现自动翻页
		myViewPager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 自动生成的方法存根
			
				//Toast.makeText(MainActivity.this, "isUnMoveable"+isUnMoveable, 500).show();
				return !DEBUG;
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
		Intent intent = new Intent();
		intent.setAction(ACTION_SERVICE);
		serviceConnection = new MyServiceConnection();
		this.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
		
		//ani_music.start(); 
		if(!DEBUG)    {
		    init();
		}
	}

	private void init()	{
	   
		handler.sendMessage(handler.obtainMessage(Utils.START_HOTBALL));
        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_HOTBALL2),5000);
        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_HOTBALL3),10000);
        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_WENZI1), 15000);
        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_WENZI2), 20000);
        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_LEFTHEART), 25000);
        handler.sendMessageDelayed(handler.obtainMessage(Utils.COMPLETE_PAGEONE), 40000);
      //  controller.play();
	}
	@Override
	    protected void onResume() {
	        // TODO Auto-generated method stub
	        super.onResume();
	       
	       
	      
	    }
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		com.xujia.preciousgift.utils.BitmapCache.getInstance().clearCache();
		this.unbindService(serviceConnection);
	}
	 
	public void dealMusic(View v)	{
		if(isMusicOpend)	{
			isMusicOpend =  false;
			ani_music.cancel();
			controller.pause();
		}else {
			
			isMusicOpend =  true;
			ani_music.start();
			controller.play();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();
		//		if (id == R.id.action_settings) {
		//			return true;
		//		}
		return super.onOptionsItemSelected(item);
	}
	
	class MyServiceConnection implements ServiceConnection {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO 自动生成的方法存根
			controller.release();
			controller = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO 自动生成的方法存根
			controller = (MusicPlayService.MediaPlayerController)service;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	    public void onBackPressed() {
	        // TODO Auto-generated method stub
	        super.onBackPressed();
	        finish();
	    }
	@Override
	public void onPageSelected(int arg0) {
		// TODO 自动生成的方法存根
		//Toast.makeText(this, "arg0"+arg0, 1000).show();
		if(arg0 == 0 && !DEBUG)	{
			init();
		}
		if(arg0 == 1 && !DEBUG)	{
			com.xujia.preciousgift.utils.BitmapCache.getInstance().clearCache();
			handler.sendMessageDelayed(handler.obtainMessage(Utils.START_SHOWHEART),500);
		}if(arg0 == 2 && !DEBUG)	{
			
			com.xujia.preciousgift.utils.BitmapCache.getInstance().clearCache();
			surfaceView2.clear();
			waterView.start();
			handler.sendMessageDelayed(handler.obtainMessage(Utils.SHOW_STAR),500);
			
		}if(arg0 == 3 && !DEBUG)	{
		    com.xujia.preciousgift.utils.BitmapCache.getInstance().clearCache();
		    
		    surfaceView3.clear();
		    waterView.clear();
		    handler.sendMessageDelayed(handler.obtainMessage(Utils.SHOW_MAIL), 1000);
		    
		}if(arg0 == 4)    {
		    com.xujia.preciousgift.utils.BitmapCache.getInstance().clearCache();
            surfaceView4.clear();
         
            //handler.sendMessageDelayed(handler.obtainMessage(Utils.SHOW_MAIL), 1000);
           // surfaceView5.showYanHuo();
            handler.sendMessageDelayed(handler.obtainMessage(Utils.SHOW_FIRE), 1000);
           
		}
	}
}
