package com.samwoo.slot;

import android.app.Application;

import com.samwoo.slot.common.LoadResource;
import com.samwoo.slot.database.DatabaseManager;
import com.samwoo.slot.utils.CrashHandler;

/**
 * Created by Administrator on 2018/1/6.
 */

public class SlotApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        DatabaseManager.getInstance().initDatabase(this);
        CrashHandler.getInstance().init(this);
    }
}
