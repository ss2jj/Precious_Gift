package com.xujia.preciousgift;

import java.util.ArrayList;

import com.xujia.preciousgift.adapter.MyPageViewAdapter;
import com.xujia.preciousgift.service.MusicPlayService;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
private Animation ani_hotball,ani_wenzi1,ani_music;
private boolean isMusicOpend = true;
private MusicPlayService.MediaPlayerController controller;
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
		ani_wenzi1 = AnimationUtils.loadAnimation(this, R.anim.wenzi_anim);
		ani_music = AnimationUtils.loadAnimation(this,R.anim.music_anim);
		
		ImageView imageView =(ImageView)view1.findViewById(R.id.hotball);
		ImageView wenzi = (ImageView)view1.findViewById(R.id.wenzi1);
		ImageButton music = (ImageButton)findViewById(R.id.music);
		
		
		imageView.setAnimation(ani_hotball);
		wenzi.setAnimation(ani_wenzi1);
		music.setAnimation(ani_music);
		
		ani_wenzi1.start();
		ani_hotball.start();
		ani_music.start();
		
		views.add(view1);
		views.add(view2);
		views.add(view3);
		
		adapter = new MyPageViewAdapter(views);
		
		myViewPager.setAdapter(adapter);
		Intent intent = new Intent();
		intent.setAction("com.xujia.preciousgift.musicplayservice");
		this.bindService(intent, new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO 自动生成的方法存根
				controller = (MusicPlayService.MediaPlayerController)service;
			}
		}, Service.BIND_AUTO_CREATE);
		
		//controller.play();
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
}
