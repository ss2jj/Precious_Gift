package com.xujia.preciousgift;

import java.util.ArrayList;

import com.xujia.preciousgift.adapter.MyPageViewAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
private ViewPager myViewPager;
private ArrayList<View> views = new ArrayList<View>();
private MyPageViewAdapter adapter;
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
		views.add(view1);
		views.add(view2);
		views.add(view3);
		adapter = new MyPageViewAdapter(views);
		
		myViewPager.setAdapter(adapter);
		
		
		
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
