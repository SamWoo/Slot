package com.samwoo.slot.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.samwoo.slot.R;
import com.samwoo.slot.base.BaseActivity;
import com.samwoo.slot.common.GameRule;

public class MainActivity extends BaseActivity {

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
     * 退出游戏
     *
     * @param view
     */
    public void exitGame(View view) {
        this.finish();
        System.exit(0);
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
}
