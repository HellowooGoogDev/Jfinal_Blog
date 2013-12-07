package com.example.jfinal_blog.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class LeftAndRightView extends RelativeLayout implements OnTouchListener {
	/**
	 * ������ʾ��������಼��ʱ����ָ������Ҫ�ﵽ���ٶȡ�
	 */
	public static final int SNAP_VELOCITY = 200;

	/**
	 * ����״̬��һ�֣���ʾδ�����κλ�����
	 */
	public static final int DO_NOTHING = 0;

	/**
	 * ����״̬��һ�֣���ʾ���ڻ������˵���
	 */
	public static final int SHOW_LEFT_MENU = 1;

	/**
	 * ����״̬��һ�֣���ʾ���ڻ����Ҳ�˵���
	 */
	public static final int SHOW_RIGHT_MENU = 2;

	/**
	 * ����״̬��һ�֣���ʾ�����������˵���
	 */
	public static final int HIDE_LEFT_MENU = 3;

	/**
	 * ����״̬��һ�֣���ʾ���������Ҳ�˵���
	 */
	public static final int HIDE_RIGHT_MENU = 4;

	/**
	 * ��¼��ǰ�Ļ���״̬
	 */
	private int slideState;

	/**
	 * ��Ļ���ֵ��
	 */
	private int screenWidth;

	/**
	 * �ڱ��ж�Ϊ����֮ǰ�û���ָ�����ƶ������ֵ��
	 */
	private int touchSlop;

	/**
	 * ��¼��ָ����ʱ�ĺ����ꡣ
	 */
	private float xDown;

	/**
	 * ��¼��ָ����ʱ�������ꡣ
	 */
	private float yDown;

	/**
	 * ��¼��ָ�ƶ�ʱ�ĺ����ꡣ
	 */
	private float xMove;

	/**
	 * ��¼��ָ�ƶ�ʱ�������ꡣ
	 */
	private float yMove;

	/**
	 * ��¼�ֻ�̧��ʱ�ĺ����ꡣ
	 */
	private float xUp;

	/**
	 * ���˵���ǰ����ʾ�������ء�ֻ����ȫ��ʾ������ʱ�Ż���Ĵ�ֵ�����������д�ֵ��Ч��
	 */
	private boolean isLeftMenuVisible;

	/**
	 * �Ҳ�˵���ǰ����ʾ�������ء�ֻ����ȫ��ʾ������ʱ�Ż���Ĵ�ֵ�����������д�ֵ��Ч��
	 */
	private boolean isRightMenuVisible;

	/**
	 * �Ƿ����ڻ�����
	 */
	private boolean isSliding;

	/**
	 * ���˵����ֶ���
	 */
	private View leftMenuLayout;

	/**
	 * �Ҳ�˵����ֶ���
	 */
	private View rightMenuLayout;

	/**
	 * ���ݲ��ֶ���
	 */
	private View contentLayout;

	/**
	 * ���ڼ��������¼���View��
	 */
	private View mBindView;

	/**
	 * ���˵����ֵĲ�����
	 */
	private MarginLayoutParams leftMenuLayoutParams;

	/**
	 * �Ҳ�˵����ֵĲ�����
	 */
	private MarginLayoutParams rightMenuLayoutParams;

	/**
	 * ���ݲ��ֵĲ�����
	 */
	private RelativeLayout.LayoutParams contentLayoutParams;

	/**
	 * ���ڼ�����ָ�������ٶȡ�
	 */
	private VelocityTracker mVelocityTracker;

	/**
	 * ��дBidirSlidingLayout�Ĺ��캯�������л�ȡ����Ļ�Ŀ�Ⱥ�touchSlop��ֵ��
	 */
	public LeftAndRightView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = manager.getDefaultDisplay().getWidth();
		touchSlop = ViewConfiguration.getTouchSlop();
	}
	/**
	 * 
	 * 
	 * �󶨹����¼���view
	 * 
	 * @param bindView
	 */
	public void setScrollEventView(View bindView) {
		mBindView = bindView;
		mBindView.setOnTouchListener(this);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// ��ȡ���menu
			leftMenuLayout = getChildAt(0);
			leftMenuLayoutParams = (MarginLayoutParams) leftMenuLayout
					.getLayoutParams();

			// ��ȡ�Ҳ�menu
			rightMenuLayout = getChildAt(1);
			rightMenuLayoutParams = (MarginLayoutParams) rightMenuLayout
					.getLayoutParams();

			// ���ݲ���
			contentLayout = getChildAt(2);
			contentLayoutParams = (RelativeLayout.LayoutParams) contentLayout
					.getLayoutParams();
			contentLayoutParams.width = screenWidth;
			contentLayout.setLayoutParams(contentLayoutParams);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDown = event.getRawX();
			yDown = event.getRawY();
			slideState = DO_NOTHING;
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = event.getRawX();
			yMove = event.getRawY();
			// �����ƶ�����
			int moveDistanceX = (int) (xMove - xDown);
			int moveDistanceY = (int) (yMove - yDown);
			// �����ƶ������鵱ǰ���ƶ�״̬
			checkSlideState(moveDistanceX, moveDistanceY);
			// ���ݵ�ǰ״̬�������ƫ�����ݲ���
			switch (slideState) {
			case SHOW_LEFT_MENU:
				contentLayoutParams.rightMargin = -moveDistanceX;
				checkLeftMenuBorder();
				contentLayout.setLayoutParams(contentLayoutParams);
				break;
			case HIDE_LEFT_MENU:
				contentLayoutParams.rightMargin = -leftMenuLayoutParams.width
						- moveDistanceX;
				checkLeftMenuBorder();
				contentLayout.setLayoutParams(contentLayoutParams);
				break;
			case SHOW_RIGHT_MENU:
				contentLayoutParams.leftMargin = moveDistanceX;
				checkRightMenuBorder();
				contentLayout.setLayoutParams(contentLayoutParams);
				break;

			case HIDE_RIGHT_MENU:
				contentLayoutParams.leftMargin = -rightMenuLayoutParams.width
						+ moveDistanceX;
				checkRightMenuBorder();
				contentLayout.setLayoutParams(contentLayoutParams);
				break;
			default:
				break;
			}
			break;

		case MotionEvent.ACTION_UP:
			xUp = event.getRawX();
			int upDistanceX = (int) (xUp - xDown);
			if (isSliding) {
				// ��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ
				switch (slideState) {
				case SHOW_LEFT_MENU:
					if (shouldScrollToLeftMenu()) {
						scrollToLeftMenu();
					} else {
						scrollToContentFromLeftMenu();
					}
					break;
				case HIDE_LEFT_MENU:
					if (shouldScrollToContentFromLeftMenu()) {
						scrollToContentFromLeftMenu();
					} else {
						scrollToLeftMenu();
					}
					break;
				case SHOW_RIGHT_MENU:
					if (shouldScrollToRightMenu()) {
						scrollToRightMenu();
					} else {
						scrollToContentFromRightMenu();
					}
					break;
				case HIDE_RIGHT_MENU:
					if (shouldScrollToContentFromRightMenu()) {
						scrollToContentFromRightMenu();
					} else {
						scrollToRightMenu();
					}
					break;
				default:
					break;
				}
			} else if (upDistanceX < touchSlop && isLeftMenuVisible) {
				// �����˵���ʾʱ������û����һ�����ݲ��֣���ֱ�ӹ��������ݽ���
				scrollToContentFromLeftMenu();
			} else if (upDistanceX < touchSlop && isRightMenuVisible) {
				// ���Ҳ�˵���ʾʱ������û����һ�����ݲ��֣���ֱ�ӹ��������ݽ���
				scrollToContentFromRightMenu();
			}
			recycleVelocityTracker();
			break;
		default:
			break;
		}

		return true;
	}

	private void createVelocityTracker(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	private void recycleVelocityTracker() {
		// TODO Auto-generated method stub
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	private boolean shouldScrollToContentFromRightMenu() {
		// TODO Auto-generated method stub
		return xUp - xDown > rightMenuLayoutParams.width / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	private void scrollToContentFromRightMenu() {
		// TODO Auto-generated method stub
		new RightMenuScrollTask().execute(30);
	}

	private void scrollToRightMenu() {
		// TODO Auto-generated method stub
		new RightMenuScrollTask().execute(-30);
	}

	private boolean shouldScrollToRightMenu() {
		// TODO Auto-generated method stub
		return xDown - xUp > rightMenuLayoutParams.width / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	private boolean shouldScrollToContentFromLeftMenu() {
		// TODO Auto-generated method stub
		return xDown - xUp > leftMenuLayoutParams.width / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	private void scrollToContentFromLeftMenu() {
		// TODO Auto-generated method stub
		new LeftMenuScrollTask().execute(30);
	}

	private void scrollToLeftMenu() {
		// TODO Auto-generated method stub
		new LeftMenuScrollTask().execute(-30);
	}

	private boolean shouldScrollToLeftMenu() {
		// TODO Auto-generated method stub
		return xUp - xDown > leftMenuLayoutParams.width / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	private void checkRightMenuBorder() {
		// TODO Auto-generated method stub
		if (contentLayoutParams.leftMargin > 0) {
			contentLayoutParams.leftMargin = 0;
		} else if (contentLayoutParams.leftMargin < -rightMenuLayoutParams.width) {
			contentLayoutParams.leftMargin = -rightMenuLayoutParams.width;
		}
	}

	private void checkLeftMenuBorder() {

		if (contentLayoutParams.rightMargin > 0) {
			contentLayoutParams.rightMargin = 0;
		} else if (contentLayoutParams.rightMargin < -leftMenuLayoutParams.width) {
			contentLayoutParams.rightMargin = -leftMenuLayoutParams.width;
		}
	}

	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	// ����xy�����ƶ��ľ����жϵ�ǰ�ƶ�״̬,�ж���ͼ
	private void checkSlideState(int moveDistanceX, int moveDistanceY) {

		if (isLeftMenuVisible) {
			// ��ǰ��ʾ���menu
			if (!isSliding && Math.abs(moveDistanceX) > touchSlop
					&& moveDistanceX < 0) {
				// �����ƶ�,�������menu
				isSliding = true;
				slideState = HIDE_LEFT_MENU;
			}
		} else if (isRightMenuVisible) {
			// ��ǰ��ʾ�Ҳ಼��
			if (!isSliding && Math.abs(moveDistanceX) > touchSlop
					&& moveDistanceX > 0) {
				// �����ƶ��������Ҳ�menu
				isSliding = true;
				slideState = HIDE_RIGHT_MENU;
			}
		} else {
			// ��ǰ��ʾcontent
			if (!isSliding && Math.abs(moveDistanceX) >= touchSlop
					&& moveDistanceX > 0 && Math.abs(moveDistanceY) < 0) {
				// ȷ��Y�����ƶ���X��������
				isSliding = true;
				slideState = SHOW_LEFT_MENU;
				contentLayoutParams
						.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				contentLayout.setLayoutParams(contentLayoutParams);
				// ����û���ͼ��ʾ���menu,�����menu��ʾ���Ҳ�menu����
				leftMenuLayout.setVisibility(View.VISIBLE);
				rightMenuLayout.setVisibility(View.GONE);
			} else if (!isSliding && Math.abs(moveDistanceX) >= 0
					&& moveDistanceX < 0 && Math.abs(moveDistanceY) < 0) {
				// ȷ��Y�����ƶ���X��������
				isSliding = true;
				slideState = SHOW_RIGHT_MENU;
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
						0);
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				contentLayout.setLayoutParams(contentLayoutParams);
				// ����û���ͼ��ʾ�Ҳ�menu,�����menu���أ��Ҳ�menu��ʾ
				leftMenuLayout.setVisibility(View.GONE);
				rightMenuLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	class LeftMenuScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int rightMargin = contentLayoutParams.rightMargin;
			// ���ݴ�����ٶ����������棬����������߽�ֵʱ������ѭ����
			while (true) {
				rightMargin = rightMargin + speed[0];
				if (rightMargin < -leftMenuLayoutParams.width) {
					rightMargin = -leftMenuLayoutParams.width;
					break;
				}
				if (rightMargin > 0) {
					rightMargin = 0;
					break;
				}
				publishProgress(rightMargin);
				// Ϊ��Ҫ�й���Ч��������ÿ��ѭ��ʹ�߳�˯��һ��ʱ�䣬�������۲��ܹ���������������
				sleep(15);
			}
			if (speed[0] > 0) {
				isLeftMenuVisible = false;
			} else {
				isLeftMenuVisible = true;
			}
			isSliding = false;
			return rightMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... rightMargin) {
			contentLayoutParams.rightMargin = rightMargin[0];
			contentLayout.setLayoutParams(contentLayoutParams);
			unFocusBindView();
		}

		@Override
		protected void onPostExecute(Integer rightMargin) {
			contentLayoutParams.rightMargin = rightMargin;
			contentLayout.setLayoutParams(contentLayoutParams);
		}
	}

	class RightMenuScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = contentLayoutParams.leftMargin;
			// ���ݴ�����ٶ����������棬����������߽�ֵʱ������ѭ����
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin < -rightMenuLayoutParams.width) {
					leftMargin = -rightMenuLayoutParams.width;
					break;
				}
				if (leftMargin > 0) {
					leftMargin = 0;
					break;
				}
				publishProgress(leftMargin);
				// Ϊ��Ҫ�й���Ч��������ÿ��ѭ��ʹ�߳�˯��һ��ʱ�䣬�������۲��ܹ���������������
				sleep(15);
			}
			if (speed[0] > 0) {
				isRightMenuVisible = false;
			} else {
				isRightMenuVisible = true;
			}
			isSliding = false;
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			contentLayoutParams.leftMargin = leftMargin[0];
			contentLayout.setLayoutParams(contentLayoutParams);
			unFocusBindView();
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			contentLayoutParams.leftMargin = leftMargin;
			contentLayout.setLayoutParams(contentLayoutParams);
		}
	}

	/**
	 * ʹ�ÿ��Ի�ý���Ŀؼ��ڻ�����ʱ��ʧȥ���㡣
	 */
	private void unFocusBindView() {
		if (mBindView != null) {
			mBindView.setPressed(false);
			mBindView.setFocusable(false);
			mBindView.setFocusableInTouchMode(false);
		}
	}

	/**
	 * ʹ��ǰ�߳�˯��ָ���ĺ�������
	 * 
	 * @param millis
	 *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
