package com.samwoo.slot.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/25.
 */

public class ToastUtils {
    public static final String TAG = "ToastUtils";

    /**
     * 通过string直接设置Toast显示的内容
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 通过String文件中的资源ID显示Toast
     *
     * @param context
     * @param resId
     */
    public static void showToastById(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
