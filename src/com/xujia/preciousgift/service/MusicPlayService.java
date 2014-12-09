package com.xujia.preciousgift.service;

import java.io.FileDescriptor;

import android.R;
import android.R.raw;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class MusicPlayService extends Service {

private	MediaPlayer mediaPlayer = null;
private MediaPlayerController controller = null;

  
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自动生成的方法存根
		controller = new MediaPlayerController();
		return controller;
	}
	
	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		mediaPlayer = MediaPlayer.create(this,com.xujia.preciousgift.R.raw.backgroundmusic);
		mediaPlayer.reset();
		
	}

	
	public class MediaPlayerController extends Binder	{
		public void play()	{
			if(mediaPlayer != null)	{
				
				mediaPlayer.start();
			}
		}
		
		public void pause()	{
			if(mediaPlayer != null)	{
				mediaPlayer.pause();
			}
		}
	}
}
