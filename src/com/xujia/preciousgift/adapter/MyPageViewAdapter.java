package com.xujia.preciousgift.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyPageViewAdapter extends PagerAdapter{
private ArrayList<View> views = new ArrayList<View>();

	public MyPageViewAdapter( ArrayList<View> views)	{
		this.views = views;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return views.size();
	}

	@Override
    public void destroyItem(View container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager)container).removeView(views.get(position));
    }
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO 自动生成的方法存根
		return arg0 == arg1;
	}
	
	 @Override
	  public Object instantiateItem(View arg0, int arg1){
	        ((ViewPager)arg0).addView(views.get(arg1),0);
	        return views.get(arg1);
	         
	    }

}
