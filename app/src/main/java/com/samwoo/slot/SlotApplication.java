package com.samwoo.slot;

import android.app.Application;

import com.samwoo.slot.common.LoadResource;

/**
 * Created by Administrator on 2018/1/6.
 */

public class SlotApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        init();
    }
    private void init(){
        LoadResource.loadGameImage(this);
        LoadResource.loadGameSound(this);
    }
}
