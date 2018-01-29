package com.samwoo.slot.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.samwoo.slot.R;
import com.samwoo.slot.base.BaseActivity;
import com.samwoo.slot.common.GameRule;
import com.samwoo.slot.database.DatabaseManager;
import com.samwoo.slot.database.Rank;
import com.samwoo.slot.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity {

    private long lastTime = 0;
    private long currentTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 游戏开始按钮
     *
     * @param view
     */
    public void startGame(View view) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
    }

    /**
     * 获取金币
     *
     * @param view
     */
    public void getCoin(View view) {
        if ((GameRule.scoreTotal + GameRule.scoreWin) <= 10) {
            GameRule.scoreTotal += 100;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.NoBackGroundDialog);
            View v = LayoutInflater.from(this).inflate(R.layout.layout_coin_msg, null);
            Button btnOK = v.findViewById(R.id.btn_ok);

            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setContentView(v);
            dialog.setCanceledOnTouchOutside(false);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }


    public void historyRank(View view) {
        Intent intent = new Intent(this, RankActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    /**
     * 获取帮组
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getHelp(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.NoBackGroundDialog);
        View v = LayoutInflater.from(this).inflate(R.layout.layout_help_msg, null);
        Button btnOK = v.findViewById(R.id.btn_ok);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 返回按键按下动作处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            lastTime = currentTime;
            currentTime = System.currentTimeMillis();
            if (currentTime - lastTime <= 2 * 1000) {
                currentTime = 0;
                lastTime = 0;
                MainActivity.this.finish();
            } else {
                ToastUtils.showToastById(getApplicationContext(), R.string.exit_msg);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出游戏对话框
     */
    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.NoBackGroundDialog);
        View v = LayoutInflater.from(this).inflate(R.layout.layout_exit_msg, null);
        Button btnOK = v.findViewById(R.id.btn_ok);
        Button btnCancle = v.findViewById(R.id.btn_cancle);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
                System.exit(0);
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 退出游戏
     *
     * @param view
     */
    public void exitGame(View view) {
        exitDialog();
    }

    /**
     * 关于游戏
     *
     * @param view
     */
    public void aboutGame(View view) {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = df.format(new Date());
        Rank rank = new Rank(null, "Sam", GameRule.scoreTotal, time);
        DatabaseManager.getInstance().addRank(rank);
    }
}
