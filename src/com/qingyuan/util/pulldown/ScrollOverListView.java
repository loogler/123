package com.qingyuan.util.pulldown;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

//一个可以监听ListView是否滚动到最顶部或最底部的自定义控件
//只能监听由触摸产生的，如果是ListView本身Flying导致的，则不能监听
//如果加以改进，可以实现监听scroll滚动的具体位置等

public class ScrollOverListView extends ListView {

	private int mLastY;
	private int mBottomPosition;

	public ScrollOverListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ScrollOverListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScrollOverListView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mBottomPosition = 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final int y = (int) ev.getRawY();

		switch (action) {

		case MotionEvent.ACTION_MOVE: {
			final int childCount = getChildCount();
			if (childCount == 0)
				return super.onTouchEvent(ev);

			final int itemCount = getAdapter().getCount() - mBottomPosition;

			final int deltaY = y - mLastY;

			final int lastBottom = getChildAt(childCount - 1).getBottom();
			final int end = getHeight() - getPaddingBottom();

			final int firstVisiblePosition = getFirstVisiblePosition();

			final boolean isHandleMotionMove = mOnScrollOverListener
					.onMotionMove(ev, deltaY);

			if (isHandleMotionMove) {
				mLastY = y;
				return true;
			}

			if (firstVisiblePosition + childCount >= itemCount
					&& lastBottom <= end && deltaY < 0) {
				final boolean isHandleOnListViewBottomAndPullDown;
				isHandleOnListViewBottomAndPullDown = mOnScrollOverListener
						.onListViewBottomAndPullUp(deltaY);
				if (isHandleOnListViewBottomAndPullDown) {
					mLastY = y;
					return true;
				}
			}
			break;
		}

		}

		mLastY = y;
		return super.onTouchEvent(ev);
	}

	/** 空的 */
	private OnScrollOverListener mOnScrollOverListener = new OnScrollOverListener() {

		@Override
		public boolean onListViewBottomAndPullUp(int delta) {
			return false;
		}

		@Override
		public boolean onMotionMove(MotionEvent ev, int delta) {
			return false;
		}

		@Override
		public boolean onListViewTopAndPullDown(int delta) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onMotionDown(MotionEvent ev) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onMotionUp(MotionEvent ev) {
			// TODO Auto-generated method stub
			return false;
		}

	};

	// 可以自定义其中一个条目为头部，头部触发的事件将以这个为准，默认为第一个
	// @param index 正数第几个，必须在条目数范围之内
	public void setTopPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setTopPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Top position must > 0");

		// mTopPosition = index;
	}

	// 可以自定义其中一个条目为尾部，尾部触发的事件将以这个为准，默认为最后一个
	// @param index 倒数第几个，必须在条目数范围之内
	public void setBottomPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setBottonPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Bottom position must > 0");

		mBottomPosition = index;
	}

	// 设置这个Listener可以监听是否到达顶端，或者是否到达低端等事件</br>
	public void setOnScrollOverListener(
			OnScrollOverListener onScrollOverListener) {
		mOnScrollOverListener = onScrollOverListener;
	}

	// 滚动监听接口
	public interface OnScrollOverListener {

		/**
		 * 到达最顶部触发
		 *
		 * @param delta
		 *            手指点击移动产生的偏移量
		 * @return
		 */
		boolean onListViewTopAndPullDown(int delta);

		/**
		 * 到达最底部触发
		 * 
		 * @param delta
		 *            手指点击移动产生的偏移量
		 */
		boolean onListViewBottomAndPullUp(int delta);

		/**
		 * 手指触摸按下触发，相当于{@link MotionEvent#ACTION_DOWN}
		 *
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionDown(MotionEvent ev);

		/**
		 * 手指触摸移动触发，相当于{@link MotionEvent#ACTION_MOVE}
		 * 
		 * @return 返回true表示自己处理
		 */
		boolean onMotionMove(MotionEvent ev, int delta);

		/**
		 * 手指触摸后提起触发，相当于{@link MotionEvent#ACTION_UP}
		 *
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionUp(MotionEvent ev);
	}

}
