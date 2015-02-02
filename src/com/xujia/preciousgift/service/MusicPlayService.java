package com.xujia.preciousgift.service;

import java.io.FileDescriptor;


import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class MusicPlayService extends Service {

private	MediaPlayer mediaPlayer = null;
private MediaPlayerController controller = null;
private static final String TAG = "MusicPlayService";
  
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自动生成的方法存根
		Log.d(TAG, "bind service");
		controller = new MediaPlayerController();
		controller.play();
		return controller;
	}
	
	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		Log.d(TAG, "create service");
		mediaPlayer = MediaPlayer.create(this,com.xujia.preciousgift.R.raw.backgroundmusic);
	
		
	}

	@Override
		public void onDestroy() {
			// TODO 自动生成的方法存根
			super.onDestroy();
			if(mediaPlayer != null)	{
				mediaPlayer.release();
				mediaPlayer = null;
			}
			this.stopSelf();
		}
	
	public class MediaPlayerController extends Binder	{
		public void play()	{
			if(mediaPlayer != null && !mediaPlayer.isPlaying())	{
				Log.d(TAG, "start play");
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
				
			}
		}
		
		public void pause()	{
			if(mediaPlayer != null && mediaPlayer.isPlaying())	{
				Log.d(TAG, "pause play");
				mediaPlayer.pause();
			}
		}
		
		public void release()	{
			if(mediaPlayer != null)	{
				Log.d(TAG, "release play");
				mediaPlayer.release();
			}
		}
	}
}
