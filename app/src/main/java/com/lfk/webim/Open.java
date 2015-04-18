package com.lfk.webim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by Administrator on 2015/4/11.
 */
public class Open extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.open, null);
        setContentView(view);
        //渐变展示启动屏
        AlphaAnimation start = new AlphaAnimation(0.3f,1.0f);
        start.setDuration(2000);
        view.startAnimation(start);
        start.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0) {
                Log.e("linc", "---start!");
                try{
                    Intent intent = new Intent();
                    intent.setClass(Open.this,MainActivity.class);
                    Open.this.startActivity(intent);
                    Open.this.finish();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
        });


    }
}

