package com.example.jfinal_blog.view;

import android.content.Context;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class LeftAndRightView extends RelativeLayout implements OnTouchListener {
	/**
	 * 滚动显示和隐藏左侧布局时，手指滑动需要达到的速度。
	 */
	public static final int SNAP_VELOCITY = 200;

	/**
	 * 滑动状态的一种，表示未进行任何滑动。
	 */
	public static final int DO_NOTHING = 0;

	/**
	 * 滑动状态的一种，表示正在滑出左侧菜单。
	 */
	public static final int SHOW_LEFT_MENU = 1;

	/**
	 * 滑动状态的一种，表示正在滑出右侧菜单。
	 */
	public static final int SHOW_RIGHT_MENU = 2;

	/**
	 * 滑动状态的一种，表示正在隐藏左侧菜单。
	 */
	public static final int HIDE_LEFT_MENU = 3;

	/**
	 * 滑动状态的一种，表示正在隐藏右侧菜单。
	 */
	public static final int HIDE_RIGHT_MENU = 4;

	/**
	 * 记录当前的滑动状态
	 */
	private int slideState;

	/**
	 * 屏幕宽度值。
	 */
	private int screenWidth;

	/**
	 * 在被判定为滚动之前用户手指可以移动的最大值。
	 */
	private int touchSlop;

	/**
	 * 记录手指按下时的横坐标。
	 */
	private float xDown;

	/**
	 * 记录手指按下时的纵坐标。
	 */
	private float yDown;

	/**
	 * 记录手指移动时的横坐标。
	 */
	private float xMove;

	/**
	 * 记录手指移动时的纵坐标。
	 */
	private float yMove;

	/**
	 * 记录手机抬起时的横坐标。
	 */
	private float xUp;

	/**
	 * 左侧菜单当前是显示还是隐藏。只有完全显示或隐藏时才会更改此值，滑动过程中此值无效。
	 */
	private boolean isLeftMenuVisible;

	/**
	 * 右侧菜单当前是显示还是隐藏。只有完全显示或隐藏时才会更改此值，滑动过程中此值无效。
	 */
	private boolean isRightMenuVisible;

	/**
	 * 是否正在滑动。
	 */
	private boolean isSliding;

	/**
	 * 左侧菜单布局对象。
	 */
	private View leftMenuLayout;

	/**
	 * 右侧菜单布局对象。
	 */
	private View rightMenuLayout;

	/**
	 * 内容布局对象。
	 */
	private View contentLayout;

	/**
	 * 用于监听滑动事件的View。
	 */
	private View mBindView;

	/**
	 * 左侧菜单布局的参数。
	 */
	private MarginLayoutParams leftMenuLayoutParams;

	/**
	 * 右侧菜单布局的参数。
	 */
	private MarginLayoutParams rightMenuLayoutParams;

	/**
	 * 内容布局的参数。
	 */
	private RelativeLayout.LayoutParams contentLayoutParams;

	/**
	 * 用于计算手指滑动的速度。
	 */
	private VelocityTracker mVelocityTracker;

	/**
	 * 重写BidirSlidingLayout的构造函数，其中获取了屏幕的宽度和touchSlop的值。
	 */
	public LeftAndRightView(Context context) {
		super(context);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = manager.getDefaultDisplay().getWidth();
		touchSlop = ViewConfiguration.getTouchSlop();
	}

	/**
	 * 
	 * 
	 * 绑定滚动事件的view
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
			// 获取左侧menu
			leftMenuLayout = getChildAt(0);
			leftMenuLayoutParams = (MarginLayoutParams) leftMenuLayout
					.getLayoutParams();

			// 获取右侧menu
			rightMenuLayout = getChildAt(1);
			rightMenuLayoutParams = (MarginLayoutParams) rightMenuLayout
					.getLayoutParams();

			// 内容布局
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
			// 计算移动距离
			int moveDistanceX = (int) (xMove - xDown);
			int moveDistanceY = (int) (yMove - yDown);
			// 根据移动距离检查当前的移动状态
			checkSlideState(moveDistanceX, moveDistanceY);
			// 根据当前状态决定如何偏移内容布局
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
				// 手指抬起时，进行判断当前手势的意图
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
				// 当左侧菜单显示时，如果用户点击一下内容部分，则直接滚动到内容界面
				scrollToContentFromLeftMenu();
			} else if (upDistanceX < touchSlop && isRightMenuVisible) {
				// 当右侧菜单显示时，如果用户点击一下内容部分，则直接滚动到内容界面
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

	// 根据xy轴上移动的距离判断当前移动状态,判断意图
	private void checkSlideState(int moveDistanceX, int moveDistanceY) {

		if (isLeftMenuVisible) {
			// 当前显示左侧menu
			if (!isSliding && Math.abs(moveDistanceX) > touchSlop
					&& moveDistanceX < 0) {
				// 向左移动,隐藏左侧menu
				isSliding = true;
				slideState = HIDE_LEFT_MENU;
			}
		} else if (isRightMenuVisible) {
			// 当前显示右侧布局
			if (!isSliding && Math.abs(moveDistanceX) > touchSlop
					&& moveDistanceX > 0) {
				// 向右移动，隐藏右侧menu
				isSliding = true;
				slideState = HIDE_RIGHT_MENU;
			}
		} else {
			// 当前显示content
			if (!isSliding && Math.abs(moveDistanceX) >= touchSlop
					&& moveDistanceX > 0 && Math.abs(moveDistanceY) < 0) {
				// 确认Y方向不移动，X方向向右
				isSliding = true;
				slideState = SHOW_LEFT_MENU;
				contentLayoutParams
						.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				contentLayout.setLayoutParams(contentLayoutParams);
				// 如果用户意图显示左侧menu,将左侧menu显示，右侧menu隐藏
				leftMenuLayout.setVisibility(View.VISIBLE);
				rightMenuLayout.setVisibility(View.GONE);
			} else if (!isSliding && Math.abs(moveDistanceX) >= 0
					&& moveDistanceX < 0 && Math.abs(moveDistanceY) < 0) {
				// 确认Y方向不移动，X方向向左
				isSliding = true;
				slideState = SHOW_RIGHT_MENU;
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
						0);
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				contentLayout.setLayoutParams(contentLayoutParams);
				// 如果用户意图显示右侧menu,将左侧menu隐藏，右侧menu显示
				leftMenuLayout.setVisibility(View.GONE);
				rightMenuLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	class LeftMenuScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int rightMargin = contentLayoutParams.rightMargin;
			// 根据传入的速度来滚动界面，当滚动到达边界值时，跳出循环。
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
				// 为了要有滚动效果产生，每次循环使线程睡眠一段时间，这样肉眼才能够看到滚动动画。
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
			// 根据传入的速度来滚动界面，当滚动到达边界值时，跳出循环。
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
				// 为了要有滚动效果产生，每次循环使线程睡眠一段时间，这样肉眼才能够看到滚动动画。
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
	 * 使用可以获得焦点的控件在滑动的时候失去焦点。
	 */
	private void unFocusBindView() {
		if (mBindView != null) {
			mBindView.setPressed(false);
			mBindView.setFocusable(false);
			mBindView.setFocusableInTouchMode(false);
		}
	}

	/**
	 * 使当前线程睡眠指定的毫秒数。
	 * 
	 * @param millis
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
