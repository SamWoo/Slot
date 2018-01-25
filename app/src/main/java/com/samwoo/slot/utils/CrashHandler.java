package com.samwoo.slot.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.samwoo.slot.R;
import com.samwoo.slot.ui.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/24.
 * <p>
 * 描述：崩溃日志采集类
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler sInstance = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler;//系统默认UncaughtExceptionHandler
    private Context context;
    private Map<String, String> infos = new HashMap<String, String>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 构造函数
     */
    private CrashHandler() {
    }

    /**
     * 单例模式获取唯一实例
     *
     * @return
     */
    public static CrashHandler getInstance() {
        if (null == sInstance) {
            synchronized (CrashHandler.class) {
                if (null == sInstance) {
                    sInstance = new CrashHandler();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handlerException(throwable) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            //记录崩溃信息后1s钟重新应用
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("crash", true);
            PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            am.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            System.gc();
        }
    }

    /**
     * handler Exception
     *
     * @param throwable
     * @return
     */
    private boolean handlerException(Throwable throwable) {
        if (null == throwable) {
            return false;
        }
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    ToastUtils.showToastById(context, R.string.crash_msg);
                    Looper.loop();
                }
            }).start();
            getDeviceInfo(context);
            saveCrashInfoToFile(throwable);
            SystemClock.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 采集应用版本信息和设备信息
     *
     * @param context
     */
    private void getDeviceInfo(Context context) {
        //获取APP版本
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (null != info) {
                infos.put("VersionName", info.versionName);
                infos.put("VersionCode", info.versionCode + "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "an error occured when collect package info");
        }
        //获取系统设备相关信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                LogUtils.e(TAG, "an error occured when collect package info");
            }
        }

    }

    /**
     * 把崩溃信息写进文件中
     *
     * @param throwable
     */
    private String saveCrashInfoToFile(Throwable throwable) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            //拼接时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            sb.append("\r\n").append(date).append("\n");
            //拼接版本信息和设备信息
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key).append("=").append(value).append("\n");
            }
            //获取崩溃日志信息
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            //写崩溃信息到文件中
            return writeFile(sb.toString());
        } catch (Exception e) {
            //异常处理
            Log.e(TAG, "an error occured while writing file...");
            sb.append("an error occured while writing file...");
            writeFile(sb.toString());
        }
        return null;
    }

    /**
     * 将字符串写入日志文件并返回文件名
     *
     * @param str
     * @return
     */
    private String writeFile(String str) {
        //文件记录时间
        String time = formatter.format(new Date());
        //文件名
        String fileName = "crash_" + time + ".log";
        //判断存储卡是否可用
        if (hasExternalStorage()) {
            //文件存储的绝对路径
            String path = Environment
                    .getExternalStorageDirectory()
                    .getAbsolutePath()
                    + File.separator
                    + "Crash"
                    + File.separator;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(str.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 判读外部存储空间是否可用
     *
     * @return
     */
    private boolean hasExternalStorage() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
