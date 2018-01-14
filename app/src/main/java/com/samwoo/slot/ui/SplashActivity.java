package com.samwoo.slot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samwoo.slot.R;
import com.samwoo.slot.base.BaseActivity;
import com.samwoo.slot.common.LoadResource;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/5.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.relative_layout)
    RelativeLayout layout;
    @BindView(R.id.tv)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                LoadResource.loadGameSound(getApplicationContext());
                LoadResource.loadGameImage(getApplicationContext());
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        }, 2000);
    }
}
