package com.zoneol.lovebirds.widget.pulltorefresh;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 滑动scrollView
 * 
 * author sl
 */
public class MyScrollView extends ScrollView {

	onScrollView scrooll;

	private int topHeight = 0;

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScroll(onScrollView scroll) {
		this.scrooll = scroll;
	}

	public void setTopHeight(int height) {
		this.topHeight = height;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		commOnTouchEvent(ev);
		
		boolean flag = super.onTouchEvent(ev);
		//Misc.logd("super.onTouchEvent " + flag);
		return flag;
		//return true;
	}

	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if (getScrollY() > topHeight) {
				scrooll.onScrollListen(0);
			} else {
				scrooll.onScrollListen(1);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (getScrollY() > topHeight) {
				scrooll.onScrollListen(0);
			} else {
				scrooll.onScrollListen(1);
			}
			break;

		default:
			break;
		}
	}

	public interface onScrollView {
		public void onScrollListen(int index);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (oldy > topHeight) {
			scrooll.onScrollListen(0);
		} else {
			scrooll.onScrollListen(1);
		}
	}

}
