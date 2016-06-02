package com.blueant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import login.comblueant.teamwork.R;

public class QQListView extends ListView
{

	private static final String TAG = "QQlistView";

	// private static final int VELOCITY_SANP = 200;
	// private VelocityTracker mVelocityTracker;
	/**
	 * �û���������С����
	 */
	private int touchSlop;

	/**
	 * �Ƿ���Ӧ����
	 */
	private boolean isSliding;

	/**
	 * ��ָ����ʱ��x����
	 */
	private int xDown;
	/**
	 * ��ָ����ʱ��y����
	 */
	private int yDown;
	/**
	 * ��ָ�ƶ�ʱ��x����
	 */
	private int xMove;
	/**
	 * ��ָ�ƶ�ʱ��y����
	 */
	private int yMove;

	private LayoutInflater mInflater;

	private PopupWindow mPopupWindow;
	private int mPopupWindowHeight;
	private int mPopupWindowWidth;

	private Button mDelBtn;
	/**
	 * Ϊɾ����ť�ṩһ���ص��ӿ�
	 */
	private DelButtonClickListener mListener;

	/**
	 * ��ǰ��ָ������View
	 */
	private View mCurrentView;

	/**
	 * ��ǰ��ָ������λ��
	 */
	private int mCurrentViewPos;

	/**
	 * ��Ҫ��һЩ��ʼ��
	 * 
	 * @param context
	 * @param attrs
	 */
	public QQListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mInflater = LayoutInflater.from(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		View view = mInflater.inflate(R.layout.delete_btn, null);
		mDelBtn = (Button) view.findViewById(R.id.id_item_btn);
		mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		/**
		 * �ȵ�����measure,�����ò�����͸�
		 */
		mPopupWindow.getContentView().measure(0, 0);
		mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();
		mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (action)
		{

		case MotionEvent.ACTION_DOWN:
			xDown = x;
			yDown = y;
			/**
			 * �����ǰpopupWindow��ʾ����ֱ�����أ�Ȼ������ListView��touch�¼����´�
			 */
			if (mPopupWindow.isShowing())
			{
				dismissPopWindow();
				return false;
			}
			// ��õ�ǰ��ָ����ʱ��item��λ��
			mCurrentViewPos = pointToPosition(xDown, yDown);
			// ��õ�ǰ��ָ����ʱ��item
			View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
			mCurrentView = view;
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = x;
			yMove = y;
			int dx = xMove - xDown;
			int dy = yMove - yDown;
			/**
			 * �ж��Ƿ��Ǵ��ҵ���Ļ���
			 */
			if (xMove < xDown && Math.abs(dx) > touchSlop && Math.abs(dy) < touchSlop)
			{
				// Log.e(TAG, "touchslop = " + touchSlop + " , dx = " + dx +
				// " , dy = " + dy);
				isSliding = true;
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		/**
		 * ����Ǵ��ҵ���Ļ�������Ӧ
		 */
		if (isSliding)
		{
			switch (action)
			{
			case MotionEvent.ACTION_MOVE:

				int[] location = new int[2];
				// ��õ�ǰitem��λ��x��y
				mCurrentView.getLocationOnScreen(location);
				// ����popupWindow�Ķ���
				mPopupWindow.setAnimationStyle(R.style.popwindow_delete_btn_anim_style);
				mPopupWindow.update();
				mPopupWindow.showAtLocation(mCurrentView, Gravity.LEFT | Gravity.TOP,
						location[0] + mCurrentView.getWidth(), location[1] + mCurrentView.getHeight() / 2
								- mPopupWindowHeight / 2);
				// ����ɾ����ť�Ļص�
				mDelBtn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (mListener != null)
						{
							mListener.clickHappend(mCurrentViewPos);
							mPopupWindow.dismiss();
						}
					}
				});
				// Log.e(TAG, "mPopupWindow.getHeight()=" + mPopupWindowHeight);

				break;
			case MotionEvent.ACTION_UP:
				isSliding = false;

			}
			// ��Ӧ�����ڼ���ĻitemClick�¼������ⷢ����ͻ
			return true;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * ����popupWindow
	 */
	private void dismissPopWindow()
	{
		if (mPopupWindow != null && mPopupWindow.isShowing())
		{
			mPopupWindow.dismiss();
		}
	}

	public void setDelButtonClickListener(DelButtonClickListener listener)
	{
		mListener = listener;
	}

	public interface DelButtonClickListener
	{
		public void clickHappend(int position);
	}

}
