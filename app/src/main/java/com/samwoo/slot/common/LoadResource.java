package com.samwoo.slot.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import com.samwoo.slot.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 */

public class LoadResource {
    public static List<Bitmap> digtalList = new ArrayList<Bitmap>();
    public static List<Bitmap> bgList = new ArrayList<Bitmap>();
    public static List<Bitmap> luckyPictureList = new ArrayList<Bitmap>();

    public static SoundPool soundPool;
    public static int btn_sound_bar;
    public static int btn_sound_77;
    public static int btn_sound_star;
    public static int btn_sound_watermelon;
    public static int btn_sound_bell;
    public static int btn_sound_mango;
    public static int btn_sound_orange;
    public static int btn_sound_apple;
    public static int btn_sound_start;//开始按钮
    public static int btn_sound_all;//All按钮

    public static int sound_bar;
    public static int sound_77;
    public static int sound_star;
    public static int sound_watermelon;
    public static int sound_bell;
    public static int sound_mango;
    public static int sound_orange;
    public static int sound_apple;

    public static int sound_small_bar;
    public static int sound_small_77;
    public static int sound_small_star;
    public static int sound_small_watermelon;
    public static int sound_small_bell;
    public static int sound_small_mango;
    public static int sound_small_orange;
    public static int sound_small_apple;

    public static int sound_small_four_winds;//小四喜
    public static int sound_big_four_winds;//大四喜
    public static int sound_small_three_dragons;//小三元
    public static int sound_big_three_dragons;//大三元
    public static int sound_ladyfairy;//仙女散花
    public static int sound_bg_ladyfairy;//仙女散花中奖音效
    public static int sound_drive_train;//开火车
    public static int sound_bg_drive_train;//开火车背景音效
    public static int sound_grand_slam;//大满贯
    public static int sound_nine_gates;//九莲宝灯
    public static int sound_eat_all;//吃掉
    public static int sound_splotlight;//射灯

    public static int sound_turn_around;//转圈音效
    public static int sound_select;//随机选灯
    public static int sound_compare_big_small;//比大小



    /**
     * 加载图片资源文件
     *
     * @param context
     */
    public static void loadGameImage(Context context) {
        try {
            for (int i = 0; i < 10; i++) {
                Bitmap bmp = BitmapFactory.decodeStream(context.getAssets().open("digtal/digtal_" + i + ".png"));
                digtalList.add(bmp);
            }

            for (int i = 1; i < 7; i++) {
                Bitmap bmp = BitmapFactory.decodeStream(context.getAssets().open("pic/pic_" + i + ".png"));
                luckyPictureList.add(bmp);
            }

            Bitmap bmp = BitmapFactory.decodeStream(context.getAssets().open("machine_bg" + ".png"));
            bgList.add(bmp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载游戏音效资源
     *
     * @param context
     */
    public static void loadGameSound(Context context) {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        sound_bar = soundPool.load(context, R.raw.sound_bar, 0);
        sound_small_bar = soundPool.load(context, R.raw.sound_small_bar, 0);
        sound_77 = soundPool.load(context, R.raw.sound_77, 0);
        sound_small_77 = soundPool.load(context, R.raw.sound_small_77, 0);
        sound_star = soundPool.load(context, R.raw.sound_star, 0);
        sound_small_star = soundPool.load(context, R.raw.sound_small_star, 0);
        sound_watermelon = soundPool.load(context, R.raw.sound_watermelon, 0);
        sound_small_watermelon = soundPool.load(context, R.raw.sound_small_watermelon, 0);
        sound_bell = soundPool.load(context, R.raw.sound_bell, 0);
        sound_small_bell = soundPool.load(context, R.raw.sound_small_bell, 0);
        sound_mango = soundPool.load(context, R.raw.sound_mango, 0);
        sound_small_mango = soundPool.load(context, R.raw.sound_small_mango, 0);
        sound_orange = soundPool.load(context, R.raw.sound_orange, 0);
        sound_small_orange = soundPool.load(context, R.raw.sound_small_orange, 0);
        sound_apple = soundPool.load(context, R.raw.sound_apple, 0);
        sound_small_apple = soundPool.load(context, R.raw.sound_small_apple, 0);

        btn_sound_bar = soundPool.load(context, R.raw.anjian1, 0);
        btn_sound_77 = soundPool.load(context, R.raw.anjian2, 0);
        btn_sound_star = soundPool.load(context, R.raw.anjian3, 0);
        btn_sound_watermelon = soundPool.load(context, R.raw.anjian4, 0);
        btn_sound_bell = soundPool.load(context, R.raw.anjian5, 0);
        btn_sound_mango = soundPool.load(context, R.raw.anjian6, 0);
        btn_sound_orange = soundPool.load(context, R.raw.anjian7, 0);
        btn_sound_apple = soundPool.load(context, R.raw.anjian8, 0);
        btn_sound_start = soundPool.load(context, R.raw.toudan1, 0);
        btn_sound_all = soundPool.load(context, R.raw.toudan2, 0);

        sound_small_four_winds = soundPool.load(context, R.raw.sound_xiaosixi, 0);
        sound_big_four_winds = soundPool.load(context, R.raw.sound_dasixi, 0);
        sound_small_three_dragons = soundPool.load(context, R.raw.xiaosanyuanbaoyin, 0);
        sound_big_three_dragons = soundPool.load(context, R.raw.sound_dasanyuan, 0);
        sound_ladyfairy = soundPool.load(context, R.raw.tiannvshanhua, 0);
        sound_bg_ladyfairy = soundPool.load(context, R.raw.sanhua, 0);
        sound_drive_train = soundPool.load(context, R.raw.sound_kaihuoche, 0);
        sound_bg_drive_train = soundPool.load(context, R.raw.train, 0);
        sound_grand_slam = soundPool.load(context, R.raw.sound_damanguan, 0);
        sound_nine_gates = soundPool.load(context, R.raw.sound_jiulianbaodeng, 0);
        sound_eat_all = soundPool.load(context, R.raw.chidiao, 0);
        sound_compare_big_small = soundPool.load(context, R.raw.big_small, 0);
        sound_turn_around = soundPool.load(context, R.raw.turn_around, 0);
        sound_splotlight = soundPool.load(context, R.raw.zha, 0);
        sound_select=soundPool.load(context,R.raw.zhadengxuandeng,0);
    }
}

