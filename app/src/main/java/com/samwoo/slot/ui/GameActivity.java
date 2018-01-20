package com.samwoo.slot.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.samwoo.slot.R;
import com.samwoo.slot.base.BaseActivity;
import com.samwoo.slot.common.GameRule;
import com.samwoo.slot.common.LoadResource;
import com.samwoo.slot.widget.GameView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/10.
 */

public class GameActivity extends BaseActivity {
    @BindView(R.id.btn_77)
    Button mSevenBtn;
    @BindView(R.id.btn_all)
    Button mAllBtn;
    @BindView(R.id.btn_bar)
    Button mBarBtn;
    @BindView(R.id.btn_big)
    Button mBigBtn;
    @BindView(R.id.btn_small)
    Button mSmallBtn;
    @BindView(R.id.btn_orange)
    Button mOrangeBtn;
    @BindView(R.id.btn_star)
    Button mStarBtn;
    @BindView(R.id.btn_mango)
    Button mMangoBtn;
    @BindView(R.id.btn_apple)
    Button mAppleBtn;
    @BindView(R.id.btn_right)
    Button mRightBtn;
    @BindView(R.id.btn_left)
    Button mLeftBtn;
    @BindView(R.id.btn_bell)
    Button mBellBtn;
    @BindView(R.id.btn_watermelon)
    Button mWaterMelonBtn;
    @BindView(R.id.btn_start)
    Button mStartBtn;
    @BindView(R.id.gameView)
    GameView gameView;
    @BindView(R.id.bg)
    RelativeLayout relativeLayout;

    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        sp = getSharedPreferences("history", Context.MODE_PRIVATE);
        GameRule.scoreTotal = sp.getInt("scoreTotal", 100);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sp = getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("scoreTotal", GameRule.scoreTotal);
        editor.commit();
    }

    @OnClick({R.id.btn_start, R.id.btn_star, R.id.btn_watermelon, R.id.btn_bell,
            R.id.btn_left, R.id.btn_right, R.id.btn_apple, R.id.btn_mango, R.id.btn_orange,
            R.id.btn_small, R.id.btn_big, R.id.btn_bar, R.id.btn_all, R.id.btn_77})
    public void onViewClicked(View view) {
        if (GameView.isRunning) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_bar:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_bar, 1, 1, 0, 0, 1);
                initPoint();

                if (GameRule.scoreTotal > 0 && GameRule.pointBar < 99) {
                    GameRule.scoreTotal--;
                    ++GameRule.pointBar;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_77:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_77, 1, 1, 0, 0, 1);
                initPoint();

                if (GameRule.scoreTotal > 0 && GameRule.point_77 < 99) {
                    GameRule.scoreTotal--;
                    ++GameRule.point_77;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_star:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_star, 1, 1, 0, 0, 1);
                initPoint();

                if (GameRule.scoreTotal > 0 && GameRule.pointStar < 99) {
                    GameRule.scoreTotal--;
                    ++GameRule.pointStar;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_watermelon:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_watermelon, 1, 1, 0, 0, 1);
                initPoint();

                if (GameRule.scoreTotal > 0 && GameRule.pointWatermelon < 99) {
                    GameRule.scoreTotal--;
                    ++GameRule.pointWatermelon;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_bell:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_bell, 1, 1, 0, 0, 1);
                initPoint();

                if (GameRule.scoreTotal > 0 && GameRule.pointBell < 99) {
                    GameRule.scoreTotal--;
                    ++GameRule.pointBell;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_mango:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_mango, 1, 1, 0, 0, 1);
                initPoint();

                if (GameRule.scoreTotal > 0 && GameRule.pointMango < 99) {
                    GameRule.scoreTotal--;
                    ++GameRule.pointMango;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_orange:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_orange, 1, 1, 0, 0, 1);
                initPoint();

                if (GameRule.scoreTotal > 0 && GameRule.pointOrange < 99) {
                    GameRule.scoreTotal--;
                    ++GameRule.pointOrange;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_apple:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_apple, 1, 1, 0, 0, 1);
                initPoint();

                if (GameRule.scoreTotal > 0 && GameRule.pointApple < 99) {
                    GameRule.scoreTotal--;
                    ++GameRule.pointApple;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }
                break;

            // 功能键区
            case R.id.btn_start:
                GameRule.stopBgSound();
                LoadResource.soundPool.play(LoadResource.btn_sound_start, 1, 1, 0, 0, 1);
                if (GameRule.getBetScore() > 0) {
                    gameView.startGame();

                } else {
                    Toast.makeText(this, "请先下注再游戏！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_all:
                GameRule.stopBgSound();
                initPoint();
                LoadResource.soundPool.play(LoadResource.btn_sound_all, 1, 1, 0, 0, 1);

                if (GameRule.scoreTotal > 7 && (GameRule.getBetScore() < 8 * 99)) {
                    GameRule.scoreTotal -= 8;
                    ++GameRule.point_77;
                    ++GameRule.pointBar;
                    ++GameRule.pointOrange;
                    ++GameRule.pointMango;
                    ++GameRule.pointApple;
                    ++GameRule.pointBell;
                    ++GameRule.pointWatermelon;
                    ++GameRule.pointStar;
                } else {
                    Toast.makeText(this, "你已超过下注的最大上限！！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_big:
                GameRule.stopBgSound();
                if (GameRule.scoreWin > 0) {
                    int point = GameRule.getPoints();
                    if (point > 6) {
                        GameRule.scoreWin = GameRule.scoreWin * 2;
                        LoadResource.soundPool.play(LoadResource.sound_compare_big_small, 1, 1,
                                0, 0, 1);
                    } else {
                        GameRule.scoreWin = 0;
                        LoadResource.soundPool.play(LoadResource.sound_eat_all, 1, 1,
                                0, 0, 1);
                    }
                }
                break;
            case R.id.btn_small:
                GameRule.stopBgSound();
                if (GameRule.scoreWin > 0) {
                    int point = GameRule.getPoints();
                    if (point < 7) {
                        GameRule.scoreWin = GameRule.scoreWin * 2;
                        LoadResource.soundPool.play(LoadResource.sound_compare_big_small, 1, 1,
                                0, 0, 1);
                    } else {
                        GameRule.scoreWin = 0;
                        LoadResource.soundPool.play(LoadResource.sound_eat_all, 1, 1,
                                0, 0, 1);
                    }
                }
                break;
            case R.id.btn_right:
                GameRule.stopBgSound();
                if (GameRule.scoreTotal > 0) {
                    --GameRule.scoreTotal;
                    ++GameRule.scoreWin;
                    LoadResource.soundPool.play(LoadResource.btn_sound_start, 1, 1, 0, 0,
                            1);
                }
                break;
            case R.id.btn_left:
                GameRule.stopBgSound();
                GameRule.scoreTotal += GameRule.scoreWin;
                GameRule.scoreWin = 0;
                LoadResource.soundPool.play(LoadResource.btn_sound_start, 1, 1, 0, 0, 1);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化下注点数
     */
    private void initPoint() {
        if (GameView.isContinueBet) {
            GameRule.initBetPoint();
            GameView.isContinueBet = false;
        }
    }

    /**
     * 按下返回键结束游戏
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GameView.isGame = !GameView.isGame;
            GameRule.stopBgSound();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 监听屏幕触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                GameRule.stopBgSound();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }
}
