package com.samwoo.slot.common;

import android.content.Context;
import android.media.MediaPlayer;

import com.samwoo.slot.R;
import com.samwoo.slot.widget.GameView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 */

public class GameRule {
    public static MediaPlayer bgSound = null;
    public static boolean isOpenSound = true;

    public static List<Integer> basePosition = new ArrayList<Integer>();
    public static List<Integer> luckPosition = new ArrayList<Integer>();
    public final static int NORMAL = 0;
    public final static int SMALL_FOUR_WINDS = 24;
    //    public final static int BIG_FOUR_WINDS = 25;
    public final static int SMALL_THREE_DRAGONS = 25;
    public final static int BIG_THREE_DRAGONS = 26;
    public final static int GRAND_SLAM = 27;
    public final static int LANDY_FAIRY = 28;
    public final static int NINE_GATES = 29;
    public final static int DRIVE_TRAIN = 30;
    public final static int SPLOT_LIGHT = 31;
    public final static int EAT_ALL = 32;

    /*
     * 赔率：（所有小77、小星星、小西瓜、小铃铛、小木瓜、小橙子）1：2
	 * ·苹果 1：5
	 * ·橙子1：10
	 * ·木瓜 1：15
	 * ·铃铛1：20
	 * ·西瓜 1：20
	 * ·双星 1：30
	 * ·77 1：40
	 * ·大BAR 1：120
	 */
    public static float Apple = 5f;
    public static float Orange = 10f;
    public static float Mango = 15f;
    public static float Bell = 20f;
    public static float Watermelon = 20f;
    public static float Star = 30f;
    public static float Big_77 = 40f;
    public static float Bar = 120f;

    public static float smallApple = 3f;
    public static float smallOrange = 3f;
    public static float smallMango = 3f;
    public static float smallBell = 3f;
    public static float smallWatermelon = 3f;
    public static float smallStar = 3f;
    public static float small_77 = 3f;
    public static float middleBar = 50f;
    public static float smallBar = 25f;

    /**
     * 下注的分数
     */
    public static int pointApple = 0;
    public static int pointOrange = 0;
    public static int pointMango = 0;
    public static int pointBell = 0;
    public static int pointWatermelon = 0;
    public static int pointStar = 0;
    public static int point_77 = 0;
    public static int pointBar = 0;

    /**
     * 总分和单次赢分
     */
    public static int scoreTotal = 0;//总赢分
    public static int scoreWin = 0;//单次中奖分

    static {


        /**
         * bar
         */
        for (int i = 0; i < 4; i++) {
            basePosition.add(2);
            basePosition.add(3);
            basePosition.add(4);
//            basePosition.add(27);
//            basePosition.add(29);
        }

        /**
         *随机数
         */
        for (int i = 0; i < 10; i++) {
            basePosition.add(8);
            basePosition.add(9);
            basePosition.add(10);
            basePosition.add(11);
            basePosition.add(14);
            basePosition.add(16);
            basePosition.add(17);
            basePosition.add(20);
            basePosition.add(21);
            basePosition.add(22);
            basePosition.add(23);
        }

        for (int i = 0; i < 10; i++) {
            basePosition.add(0);
            basePosition.add(1);
            basePosition.add(5);
            basePosition.add(8);
            basePosition.add(10);
            basePosition.add(11);
            basePosition.add(5);
            basePosition.add(23);
            basePosition.add(17);
            basePosition.add(5);
        }

        for (int i = 0; i < 10; i++) {
            basePosition.add(5);
            basePosition.add(9);
            basePosition.add(7);
            basePosition.add(12);
            basePosition.add(13);
            basePosition.add(6);
            basePosition.add(15);
            basePosition.add(18);
            basePosition.add(19);
            basePosition.add(7);
        }
    }

    /**
     * 返回当前中奖位置
     *
     * @return
     */
    public static int getPosition() {
        //乱序基数池
        Collections.shuffle(basePosition);
        return basePosition.get((int) (Math.random() * 320));
    }

    /**
     * 获取幸运位置
     *
     * @return
     */
    public static int getLuckyPosition() {
        return (int) (Math.random() * 24);
    }

    /**
     * 获取比较大小的点数
     *
     * @return
     */
    public static int getPoints() {
        return (int) (Math.random() * 13 + 1);
    }

    static {
        /**
         * 幸运值
         */
        for (int i = 0; i < 20; i++) {
            luckPosition.add(31);
            luckPosition.add(32);
        }
        for (int i = 0; i < 5; i++) {
            luckPosition.add(24);
            luckPosition.add(25);
            luckPosition.add(26);
        }
        for (int i = 0; i < 2; i++) {
            luckPosition.add(27);
            luckPosition.add(29);
        }
        for (int i = 0; i < 3; i++) {
            luckPosition.add(28);
            luckPosition.add(30);
        }

    }

    /**
     * Luck三选一情况
     * 1：吃掉
     * 2：射灯
     * 3：开火车
     */
    public static int getLuck() {
        //乱序基数池
        Collections.shuffle(luckPosition);
        return luckPosition.get((int) (Math.random() * 65));
    }

    /**
     * 获取赢分
     */
    public static int getWinScore(int position) {
        int winScore = 0;
        switch (position) {
            case 0:
                winScore += Orange * pointOrange;
                break;
            case 1:
                winScore += Bell * pointBell;
                break;
            case 2:
                winScore += middleBar * pointBar;
                break;
            case 3:
                winScore += Bar * pointBar;
                break;
            case 4:
                winScore += smallBar * pointBar;
                break;
            case 5:
                winScore += Apple * pointApple;
                break;
            case 6:
                winScore += Mango * pointMango;
                break;
            case 7:
                winScore += Watermelon * pointWatermelon;
                break;
            case 8:
                winScore += smallWatermelon * pointWatermelon;
                break;
            case 9:
                break;
            case 10:
                winScore += Apple * pointApple;
                break;
            case 11:
                winScore += smallOrange * pointOrange;
                break;
            case 12:
                winScore += Orange * pointOrange;
                break;
            case 13:
                winScore += Bell * pointBell;
                break;
            case 14:
                winScore += small_77 * point_77;
                break;
            case 15:
                winScore += Big_77 * point_77;
                break;
            case 16:
                winScore += Apple * pointApple;
                break;
            case 17:
                winScore += smallMango * pointMango;
                break;
            case 18:
                winScore += Mango * pointMango;
                break;
            case 19:
                winScore += Star * pointStar;
                break;
            case 20:
                winScore += smallStar * pointStar;
                break;
            case 21:
                break;
            case 22:
                winScore += Apple * pointApple;
                break;
            case 23:
                winScore += smallBell * pointBell;
                break;
            case SMALL_FOUR_WINDS:
                winScore += getSmallFourWindsScore();
                break;
            case GRAND_SLAM:
                winScore += getGrandSlamScore();
                break;
            case BIG_THREE_DRAGONS:
                winScore += getBigThreeDragonsScore();
                break;
            case SMALL_THREE_DRAGONS:
                winScore += getSmallThreeDragonsScore();
                break;
            case NINE_GATES:
                winScore += getNineGatesScore(GameView.randomPosition);
                break;
            case DRIVE_TRAIN:
                winScore += getDriveTrainScore(GameView.randomPosition);
                break;
            default:
                break;
        }
        return winScore;
    }

    /**
     * 开火车
     *
     * @param startPosition
     * @return
     */
    public static int getDriveTrainScore(int startPosition) {
        int winScore = 0;
        for (int i = 0; i < 6; i++) {
            winScore += getWinScore((startPosition + i) % 24);
        }
        return winScore;
    }

    /**
     * 九莲宝灯
     *
     * @param randomPosition
     * @return
     */
    public static int getNineGatesScore(int randomPosition) {
        int winScore = 0;
        for (int i = 0; i < 9; i++) {
            winScore += getWinScore((randomPosition + i) % 24);
        }
        return winScore;
    }

    /**
     * 小三元
     *
     * @return
     */
    public static int getSmallThreeDragonsScore() {
        return (int) (Orange * pointOrange + Mango * pointMango + Bell * pointBell);
    }

    /**
     * 大三元
     *
     * @return
     */
    public static int getBigThreeDragonsScore() {
        return (int) (Watermelon * pointWatermelon + Star * pointStar + Big_77 * point_77);
    }


    /**
     * 小四喜
     *
     * @return
     */
    public static int getSmallFourWindsScore() {
        return (int) (Apple * pointApple * 4);
    }

    /**
     * 大满贯
     *
     * @return
     */
    public static int getGrandSlamScore() {
        return (int) (smallOrange * pointOrange
                + smallMango * pointMango
                + smallBell * pointBell
                + smallWatermelon * pointWatermelon
                + smallStar * pointStar
                + small_77 * point_77
                + smallApple * pointApple
                + smallBar * pointBar
                + 4 * Apple * pointApple
                + 2 * (Orange * pointOrange + Mango * pointMango + Bell * pointBell)
                + Watermelon * pointWatermelon + Star * pointStar + Big_77 * point_77
                + (Bar + middleBar + smallBar) * pointBar);
    }

    /**
     * 下注的总分数
     *
     * @return
     */
    public static int getBetScore() {
        return pointApple + pointOrange + pointMango + pointBell + pointWatermelon
                + pointStar + point_77 + pointBar;
    }

    /**
     * 初始化下注点数
     */
    public static void initBetPoint() {
        pointApple = 0;
        pointOrange = 0;
        pointMango = 0;
        pointBell = 0;
        pointWatermelon = 0;
        pointStar = 0;
        point_77 = 0;
        pointBar = 0;
    }

    /**
     * 播放音效
     *
     * @param soundId
     */
    private static void playSound(int soundId) {
        if (isOpenSound) {
            LoadResource.soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }

    /**
     * 播放中奖时的音效
     *
     * @param position
     */
    public static void playWinSound(int position) {
        switch (position) {
            case 0:
                playSound(LoadResource.sound_orange);
                break;
            case 1:
                playSound(LoadResource.sound_bell);
                break;
            case 2:
                playSound(LoadResource.sound_small_bar);
                break;
            case 3:
                playSound(LoadResource.sound_bar);
                break;
            case 4:
                playSound(LoadResource.sound_small_bar);
                break;
            case 5:
                playSound(LoadResource.sound_apple);
                break;
            case 6:
                playSound(LoadResource.sound_mango);
                break;
            case 7:
                playSound(LoadResource.sound_watermelon);
                break;
            case 8:
                playSound(LoadResource.sound_small_watermelon);
                break;
            case 9:
                playSound(LoadResource.sound_select);
                break;
            case 10:
                playSound(LoadResource.sound_apple);
                break;
            case 11:
                playSound(LoadResource.sound_small_orange);
                break;
            case 12:
                playSound(LoadResource.sound_orange);
                break;
            case 13:
                playSound(LoadResource.sound_bell);
                break;
            case 14:
                playSound(LoadResource.sound_small_77);
                break;
            case 15:
                playSound(LoadResource.sound_77);
                break;
            case 16:
                playSound(LoadResource.sound_apple);
                break;
            case 17:
                playSound(LoadResource.sound_small_mango);
                break;
            case 18:
                playSound(LoadResource.sound_mango);
                break;
            case 19:
                playSound(LoadResource.sound_star);
                break;
            case 20:
                playSound(LoadResource.sound_small_star);
                break;
            case 21:
                playSound(LoadResource.sound_select);
                break;
            case 22:
                playSound(LoadResource.sound_apple);
                break;
            case 23:
                playSound(LoadResource.sound_small_bell);
                break;
            case SMALL_FOUR_WINDS:
                playSound(LoadResource.sound_small_four_winds);
                break;
            case SMALL_THREE_DRAGONS:
                playSound(LoadResource.sound_small_three_dragons);
                break;
            case BIG_THREE_DRAGONS:
                playSound(LoadResource.sound_big_three_dragons);
                break;
            case GRAND_SLAM:
                playSound(LoadResource.sound_grand_slam);
                break;
            case LANDY_FAIRY:
                playSound(LoadResource.sound_ladyfairy);
                break;
            case NINE_GATES:
                playSound(LoadResource.sound_nine_gates);
                break;
            case DRIVE_TRAIN:
                playSound(LoadResource.sound_drive_train);
                break;
            case SPLOT_LIGHT:
                playSound(LoadResource.sound_splotlight);
                break;
            case EAT_ALL:
                playSound(LoadResource.sound_eat_all);
                break;
            default:
                break;
        }
    }

    /**
     * 播放中奖时的背景音效
     *
     * @param context
     * @param position
     * @param winScore
     */
    public static void playWinBgSound(Context context, int position, int winScore) {
        if (position <= 23) {
            if (winScore > 0) {
                if (position % 3 == 0) {
                    changeBgSound(context, R.raw.sound0);
                } else if (position % 3 == 1) {
                    changeBgSound(context, R.raw.sound17);
                } else if (position % 3 == 2) {
                    changeBgSound(context, R.raw.sound18);
                }
            }
        } else {
            if (position == SMALL_THREE_DRAGONS) {
                changeBgSound(context, R.raw.sound17);
            } else if (position == SMALL_FOUR_WINDS) {
                changeBgSound(context, R.raw.sound0);
            } else if (position == BIG_THREE_DRAGONS) {
                changeBgSound(context, R.raw.caijin);
            } else if (position == GRAND_SLAM) {
                changeBgSound(context, R.raw.damanguanjieshu);
            } else if (position == NINE_GATES) {
                changeBgSound(context, R.raw.xiaomanguanjieshu);
            } else if (position == SPLOT_LIGHT) {
                changeBgSound(context, R.raw.zhadengbg);
            } else if (position == DRIVE_TRAIN) {
                changeBgSound(context, R.raw.w1);
            } else if (position == LANDY_FAIRY) {
                changeBgSound(context, R.raw.sanhua);
            }
        }
    }

    /**
     * 中奖时改变背景音效
     *
     * @param context
     * @param id
     */
    private static void changeBgSound(Context context, int id) {
        if (isOpenSound) {
            bgSound = MediaPlayer.create(context, id);
            bgSound.start();
        }
    }

    /**
     * 停止播放背景音效
     */
    public static void stopBgSound() {
        if (null != bgSound) {
            if (bgSound.isPlaying()) {
                bgSound.stop();
            }
        }
    }

    /**
     * 转圈时播放的音效
     */
    public static void turnAroundSound() {
        LoadResource.soundPool.play(LoadResource.sound_turn_around, 1, 1, 0, 0, 1);
    }
}