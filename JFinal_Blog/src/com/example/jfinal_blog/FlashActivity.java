package com.example.jfinal_blog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 
* 类描述： flashactivity
* 创建者： rain
* 项目名称： Blog
* 创建时间： 2013年12月7日 下午5:46:36
* 版本号： v1.0
 */
public class FlashActivity extends Activity implements AnimationListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.flash_activity_layout);
	Animation startAnimation = AnimationUtils.loadAnimation(this,
		R.anim.flash_animation);
	startAnimation.setFillEnabled(true); // 启动Fill保持
	startAnimation.setFillAfter(true);
	startAnimation.setAnimationListener(this);
	ImageView flashImageView = (ImageView) findViewById(R.id.flashImageView);
	flashImageView.startAnimation(startAnimation);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
	Intent intent = new Intent(FlashActivity.this, MainActivity.class);
	startActivity(intent);
	finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
