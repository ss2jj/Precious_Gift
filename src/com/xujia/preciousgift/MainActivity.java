package com.xujia.preciousgift;

import java.util.ArrayList;

import com.xujia.preciousgift.adapter.MyPageViewAdapter;
import com.xujia.preciousgift.service.MusicPlayService;
import com.xujia.preciousgift.transformer.*;
import com.xujia.preciousgift.utils.Utils;

import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
private ViewPager myViewPager;
private ArrayList<View> views = new ArrayList<View>();
private MyPageViewAdapter adapter;
private Animation ani_hotball,ani_hotball2,ani_hotball3,ani_textone,ani_music,ani_texttwo,ani_leftheart;
private ImageButton musicButton;
private ImageView hotBallView,hotBallView2,hotBallView3,textOneView,textTwoView,leftView;
private boolean isMusicOpend = true;
private static final String ACTION_SERVICE = "com.xujia.preciousgift.musicplayservice";
private MusicPlayService.MediaPlayerController controller;
private MyServiceConnection serviceConnection;


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
		
		ani_hotball = AnimationUtils.loadAnimation(this, R.anim.hotball_anim);
		ani_hotball2 = AnimationUtils.loadAnimation(this, R.anim.hotball2_anim);
		ani_hotball3 = AnimationUtils.loadAnimation(this, R.anim.hotball3_anim);
		ani_textone = AnimationUtils.loadAnimation(this, R.anim.wenzi_anim);
		ani_music = AnimationUtils.loadAnimation(this,R.anim.music_anim);
		ani_texttwo = AnimationUtils.loadAnimation(this, R.anim.wenzi2_anim);
		ani_leftheart = AnimationUtils.loadAnimation(this, R.anim.leftheart_anim);
		
		hotBallView =(ImageView)view1.findViewById(R.id.hotball);
		hotBallView2 = (ImageView)view1.findViewById(R.id.hotball2);
		hotBallView3 = (ImageView)view1.findViewById(R.id.hotball3);
		textOneView = (ImageView)view1.findViewById(R.id.wenzi1);
		textTwoView = (ImageView)view1.findViewById(R.id.wenzi2);
		leftView = (ImageView)view1.findViewById(R.id.zuobian);
		musicButton= (ImageButton)findViewById(R.id.music);
	
		
		musicButton.setAnimation(ani_music);

		views.add(view1);
		views.add(view2);
		views.add(view3);
		
		adapter = new MyPageViewAdapter(views);
		myViewPager.setAdapter(adapter);
		myViewPager.setPageTransformer(true, new AccordionTransformer());
		myViewPager.setKeepScreenOn(true);
		Intent intent = new Intent();
		intent.setAction(ACTION_SERVICE);
		serviceConnection = new MyServiceConnection();
		this.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
		
		
	}

	@Override
	    protected void onResume() {
	        // TODO Auto-generated method stub
	        super.onResume();
	       
	        ani_music.start(); 
	        handler.sendMessage(handler.obtainMessage(Utils.START_HOTBALL));
	        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_HOTBALL2),5000);
	        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_HOTBALL3),10000);
	        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_WENZI1), 15000);
	        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_WENZI2), 20000);
	        handler.sendMessageDelayed(handler.obtainMessage(Utils.START_LEFTHEART), 25000);
	    }
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
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
}
