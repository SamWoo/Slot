package com.samwoo.slot.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.samwoo.slot.common.Configs;
import com.samwoo.slot.common.GameRule;
import com.samwoo.slot.common.LoadResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/8.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private int screenWidth;
    private int screenHeight;
    private Context context;
    private SurfaceHolder holder = null;
    private Canvas mCanvas = null;

    private List<ItemInfo> itemInfos = null;
    private List<RectF> totalScore = new ArrayList<RectF>();
    private List<RectF> winScore = new ArrayList<RectF>();
    private List<RectF> betScore = new ArrayList<RectF>();
    private List<Integer> selectedPointList = new ArrayList<Integer>();
    private List<Integer> betPointList = new ArrayList<Integer>();

    private float itemWidth;
    private float itemHeight;

    private Bitmap bitmap = null;//前景图
    private RectF bgRectF = null;//背景图区
    private RectF showRectF;//中奖图片显示区

    public static boolean isGame = false;//控制画图线程
    public static boolean isRunning = false;//是否正在转圈
    private boolean isFlash = false;//控制闪烁
    private boolean isWorked = false;//是否第一次启动
    public static boolean isContinueBet = false;//是否继续下注
    private boolean isSecond = false;//再次启动
    private boolean isFlag = false;//标识位，射灯、开火车等情况到达目标位置只执行一次播报

    private int count = 1;//亮灯个数
    private int groupCount = 0;
    private int endCount = 0;//控制最后亮灯个数

    private int currentPosition = 0;//当前位置
    private int lastPosition = 0;//上次位置
    private int destPosition = 0;//目标位置
    public static int randomPosition = 0;//随机位置

    private int gameState = 0;
    private static final int IS_LUCK_STATE = 40;
    private static final int DRIVE_TRAIN = 41;
    private static final int LANDY_FAIRY = 42;
    private static final int SPLOT_LIGHT = 43;
    private static final int SMALL_THREE_DRAGONS = 44;
    private static final int BIG_THREE_DRAGONS = 45;
    private static final int GRAND_SLAM = 46;
    private static final int NINE_GATES = 47;
    private static final int SMALL_FOUR_WINDS = 48;

    /**
     * 转盘中每个水果item对象的相关信息
     */
    class ItemInfo {
        public float left;
        public float top;
        public float right;
        public float bottom;
        public Bitmap bmp;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case IS_LUCK_STATE:
                    sleep(200);
                    isLuckState();
                    break;
                case DRIVE_TRAIN:
                    sleep(3 * 1000);
                    for (int i = 0; i < 5; i++) {
                        GameRule.scoreWin += GameRule.getWinScore((randomPosition + i) % 24);
                        GameRule.playWinSound((randomPosition + i) % 24);
                        sleep(1000);
                    }
                    GameRule.playWinBgSound(context, GameRule.DRIVE_TRAIN, GameRule.scoreWin);
                    break;
                case LANDY_FAIRY:
                    sleep(200);
                    for (Integer i : selectedPointList) {
                        GameRule.playWinSound(GameRule.SPLOT_LIGHT);
                        GameRule.playWinSound((randomPosition + i) % 24);
                        GameRule.scoreWin += GameRule.getWinScore((randomPosition + i) % 24);
                        sleep(1000);
                    }
                    GameRule.playWinBgSound(context, GameRule.LANDY_FAIRY, GameRule.scoreWin);
                    break;
                case SPLOT_LIGHT:
                    sleep(200);
                    GameRule.scoreWin = GameRule.getWinScore(randomPosition);
                    GameRule.playWinSound(randomPosition % 24);
                    break;
                case SMALL_FOUR_WINDS:
                    GameRule.scoreWin = GameRule.getWinScore(GameRule.SMALL_FOUR_WINDS);
                    GameRule.playWinBgSound(context, GameRule.SMALL_FOUR_WINDS, GameRule.scoreWin);
                    break;
                case SMALL_THREE_DRAGONS:
                    GameRule.scoreWin = GameRule.getWinScore(GameRule.SMALL_THREE_DRAGONS);
                    GameRule.playWinBgSound(context, GameRule.SMALL_THREE_DRAGONS, GameRule.scoreWin);
                    break;
                case BIG_THREE_DRAGONS:
                    GameRule.scoreWin = GameRule.getWinScore(GameRule.BIG_THREE_DRAGONS);
                    GameRule.playWinBgSound(context, GameRule.BIG_THREE_DRAGONS, GameRule.scoreWin);
                    break;
                case GRAND_SLAM:
                    GameRule.scoreWin = GameRule.getWinScore(GameRule.GRAND_SLAM);
                    GameRule.playWinBgSound(context, GameRule.GRAND_SLAM, GameRule.scoreWin);
                    break;
                case NINE_GATES:
                    GameRule.scoreWin = GameRule.getWinScore(GameRule.NINE_GATES);
                    GameRule.playWinBgSound(context, GameRule.NINE_GATES, GameRule.scoreWin);
                    break;
                default:
                    break;
            }
        }
    };

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        this.context = context;
        this.screenWidth = Configs.getScreenWidth();
        this.screenHeight = Configs.getScreenHeight();

        itemInfos = getItemInfo();
        getFgBitmap();
        drawRectF();

        isGame = true;

        holder = this.getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Thread(this).start();
    }

    /**
     * 画数字显示区和中奖图片显示区
     */
    private void drawRectF() {
        bgRectF = new RectF(0, 0, screenWidth, screenHeight);
        // 显示分数区域
        for (int i = 0; i < 8; i++) {
            RectF score = new RectF(Configs.getRateWidth(420 - i * 23),
                    Configs.getRateHeight(104),
                    Configs.getRateWidth(440 - i * 23),
                    Configs.getRateHeight(132));
            totalScore.add(score);

            RectF win = new RectF(Configs.getRateWidth(200 - i * 23),
                    Configs.getRateHeight(104),
                    Configs.getRateWidth(220 - i * 23),
                    Configs.getRateHeight(132));
            winScore.add(win);
        }

        //下注点数数码管显示区域
        for (int i = 0; i < 23; i++) {
            if (i == 2 || i == 5 || i == 8 || i == 11 || i == 14 || i == 17
                    || i == 20)
                continue;
            RectF xia = new RectF(Configs.getRateWidth((int) (438 - i * 18.5)),
                    Configs.getRateHeight(670),
                    Configs.getRateWidth((int) (454 - i * 18.5)),
                    Configs.getRateHeight(694));
            betScore.add(xia);
        }

        // 中奖后效果区域
        showRectF = new RectF(Configs.getRateWidth(100),
                Configs.getRateHeight(200), Configs.getRateWidth(380),
                Configs.getRateHeight(500));
    }

    /**
     * 获取前景图
     */
    private Bitmap getFgBitmap() {
        if (null == bitmap) {
            bitmap = Bitmap.createBitmap((int) itemWidth, (int) itemHeight, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.argb(150, 255, 0, 0));
        }
        return bitmap;
    }

    /**
     * 获取当前界面上每个图片的位置及图片信息
     *
     * @return
     */
    private List<ItemInfo> getItemInfo() {
        List<ItemInfo> items = null;
        float offsetX = Configs.getRateWidth(36);//第一个item偏移坐标顶点的位置
        float offsetY = Configs.getRateHeight(141);
        itemWidth = Configs.getRateWidth(58);
        itemHeight = Configs.getRateHeight(58);

        if (itemWidth > 0) {
            items = new ArrayList<ItemInfo>();
            for (int i = 0; i < 6; i++) {//上面
                ItemInfo item = new ItemInfo();
                item.left = offsetX + 1 + itemWidth * i;
                item.top = offsetY;
                item.right = item.left + itemWidth;
                item.bottom = item.top + 1 + itemHeight;
                items.add(item);
            }
            for (int i = 0; i < 6; i++) {//右边
                ItemInfo item = new ItemInfo();
                item.left = offsetX + itemWidth * 6;
                item.top = offsetY + itemHeight * i;
                item.right = item.left + itemWidth;
                item.bottom = item.top + itemHeight;
                items.add(item);
            }
            for (int i = 0; i < 6; i++) {//下面
                ItemInfo item = new ItemInfo();
                item.left = offsetX + itemWidth * (6 - i);
                item.top = offsetY - 2 + itemHeight * 6;
                item.right = item.left + itemWidth;
                item.bottom = item.top + itemHeight;
                items.add(item);
            }
            for (int i = 0; i < 6; i++) {//左面
                ItemInfo item = new ItemInfo();
                item.left = offsetX + 2;
                item.top = offsetY + 1 + itemHeight * (6 - i);
                item.right = item.left + itemWidth;
                item.bottom = item.top + itemHeight;
                items.add(item);
            }

            for (int i = 0; i < items.size(); i++) {
                ItemInfo item = items.get(i);
                try {
                    item.bmp = BitmapFactory.decodeStream(context
                            .getAssets().open("item_bg.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//        this.screenHeight = i2;
//        this.screenWidth = i1;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
            bitmap = null;
        }
    }

    /**
     * 点击开始按钮
     */
    public void startGame() {
        if (!isRunning) {
            int betTotal = GameRule.getBetScore();
            selectedPointList.clear();//清除上次中奖的位置信息

            if (GameRule.scoreWin != 0) {
                GameRule.scoreTotal += GameRule.scoreWin;
                GameRule.scoreWin = 0;
                return;
            }

            getGameState();
            if (gameState == GameRule.NORMAL) {
                GameRule.turnAroundSound();
                isRunning = true;
                isWorked = true;
                isFlag = false;
                isSecond = false;
                groupCount = 0;
                endCount = 0;
                currentPosition %= 24;
                lastPosition = currentPosition;
                if (isContinueBet) {
                    GameRule.scoreTotal -= betTotal;
                } else {
                    isContinueBet = !isContinueBet;
                }
            }
        }
    }

    /**
     * 获取游戏状态
     */
    private void getGameState() {
        destPosition = GameRule.getPosition();
//        destPosition = 21;//debug
        gameState = GameRule.NORMAL;
    }

    @Override
    public void run() {
        while (isGame) {
            try {
                mCanvas = holder.lockCanvas();
                if (null != mCanvas) {
                    onDraws(mCanvas);
                }
            } finally {
                if (null != mCanvas) {
                    holder.unlockCanvasAndPost(mCanvas);
                }

            }
        }
    }

    /**
     * 绘图
     *
     * @param canvas
     */
    private void onDraws(Canvas canvas) {
        canvas.drawBitmap(LoadResource.bgList.get(0), null, bgRectF, null);
        if (null != itemInfos) {
            for (int i = 0; i < itemInfos.size(); i++) {
                canvas.drawBitmap(itemInfos.get(i).bmp, null,
                        new RectF(itemInfos.get(i).left,
                                itemInfos.get(i).top,
                                itemInfos.get(i).right,
                                itemInfos.get(i).bottom), null);
            }
            switch (gameState) {
                case GameRule.NORMAL:
                    gameRunAround(canvas);
                    break;
                case GameRule.SMALL_FOUR_WINDS:
                    smallFourWinds(canvas);
                    break;
                case GameRule.SMALL_THREE_DRAGONS:
                    smallThreeDragons(canvas);
                    break;
                case GameRule.BIG_THREE_DRAGONS:
                    bigThreeDragons(canvas);
                    break;
                case GameRule.GRAND_SLAM:
                    grandSlam(canvas);
                    break;
                case GameRule.LANDY_FAIRY:
                    landyFairy(canvas);
                    break;
                case GameRule.NINE_GATES:
                    nineGates(canvas);
                    break;
                case GameRule.DRIVE_TRAIN:
                    driveTrain(canvas);
                    break;
                case GameRule.SPLOT_LIGHT:
                    splotLight(canvas);
                    break;
                case GameRule.EAT_ALL:
                    eatAll(canvas);
                    break;
                default:
                    break;
            }

            if (gameState != GameRule.NORMAL
                    && gameState != GameRule.DRIVE_TRAIN
                    && gameState != GameRule.SPLOT_LIGHT) {
                isFlash = !isFlash;
                sleep(200);
            } else {
                isFlash = false;
            }
            showScore(canvas);
        }
    }


    /**
     * 转圈功能
     *
     * @param canvas
     */
    private void gameRunAround(Canvas canvas) {
        canvas.save();

        if (isRunning) {
            ++currentPosition;
            if (count < 5) {
                count++;
                sleep(10);
            }
        }

        //启动时往前累加，停止时从尾部递减
        if (count == 1) {
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(currentPosition % 24).left,
                            itemInfos.get(currentPosition % 24).top,
                            itemInfos.get(currentPosition % 24).right,
                            itemInfos.get(currentPosition % 24).bottom), null);
        }
        if (count == 2) {
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(currentPosition % 24).left,
                            itemInfos.get(currentPosition % 24).top,
                            itemInfos.get(currentPosition % 24).right,
                            itemInfos.get(currentPosition % 24).bottom), null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 1) % 24).left,
                            itemInfos.get((currentPosition - 1) % 24).top,
                            itemInfos.get((currentPosition - 1) % 24).right,
                            itemInfos.get((currentPosition - 1) % 24).bottom),
                    null);
        }
        if (count == 3) {
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(currentPosition % 24).left,
                            itemInfos.get(currentPosition % 24).top,
                            itemInfos.get(currentPosition % 24).right,
                            itemInfos.get(currentPosition % 24).bottom), null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 1) % 24).left,
                            itemInfos.get((currentPosition - 1) % 24).top,
                            itemInfos.get((currentPosition - 1) % 24).right,
                            itemInfos.get((currentPosition - 1) % 24).bottom),
                    null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 2) % 24).left,
                            itemInfos.get((currentPosition - 2) % 24).top,
                            itemInfos.get((currentPosition - 2) % 24).right,
                            itemInfos.get((currentPosition - 2) % 24).bottom),
                    null);
        }
        if (count == 4) {
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(currentPosition % 24).left,
                            itemInfos.get(currentPosition % 24).top,
                            itemInfos.get(currentPosition % 24).right,
                            itemInfos.get(currentPosition % 24).bottom), null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 1) % 24).left,
                            itemInfos.get((currentPosition - 1) % 24).top,
                            itemInfos.get((currentPosition - 1) % 24).right,
                            itemInfos.get((currentPosition - 1) % 24).bottom),
                    null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 2) % 24).left,
                            itemInfos.get((currentPosition - 2) % 24).top,
                            itemInfos.get((currentPosition - 2) % 24).right,
                            itemInfos.get((currentPosition - 2) % 24).bottom),
                    null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 3) % 24).left,
                            itemInfos.get((currentPosition - 3) % 24).top,
                            itemInfos.get((currentPosition - 3) % 24).right,
                            itemInfos.get((currentPosition - 3) % 24).bottom),
                    null);
        }
        if (count == 5) {
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(currentPosition % 24).left,
                            itemInfos.get(currentPosition % 24).top,
                            itemInfos.get(currentPosition % 24).right,
                            itemInfos.get(currentPosition % 24).bottom), null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 1) % 24).left,
                            itemInfos.get((currentPosition - 1) % 24).top,
                            itemInfos.get((currentPosition - 1) % 24).right,
                            itemInfos.get((currentPosition - 1) % 24).bottom),
                    null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 2) % 24).left,
                            itemInfos.get((currentPosition - 2) % 24).top,
                            itemInfos.get((currentPosition - 2) % 24).right,
                            itemInfos.get((currentPosition - 2) % 24).bottom),
                    null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 3) % 24).left,
                            itemInfos.get((currentPosition - 3) % 24).top,
                            itemInfos.get((currentPosition - 3) % 24).right,
                            itemInfos.get((currentPosition - 3) % 24).bottom),
                    null);
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition - 4) % 24).left,
                            itemInfos.get((currentPosition - 4) % 24).top,
                            itemInfos.get((currentPosition - 4) % 24).right,
                            itemInfos.get((currentPosition - 4) % 24).bottom),
                    null);

            //转3圈，然后转到目标位置
            if (lastPosition == currentPosition % 24) {
                groupCount++;
            }
            //停止时只有一个灯亮，故应在目标位置前6个位置逐渐减少亮灯至count=1
            int tempPosition = destPosition - 6;
            if (tempPosition < 0) {
                tempPosition += 24;
            }

            if (groupCount > 4) {
                if ((currentPosition % 24) == tempPosition) {
                    isRunning = !isRunning;//false
                }
            }
        }

        //转完3圈后停在目标位置
        if (!isRunning) {
            if (count > 1) {
                --count;
                ++currentPosition;
            } else {
                if (isWorked) {
                    if (!isRunning) {
                        Log.i("Slot", "destPosition==" + destPosition);
                        if (endCount < 2) {
                            endCount++;
                            currentPosition++;
                            if (endCount == 2) {
                                GameRule.stopBgSound();
                                if (destPosition == 9 || destPosition == 21) {
                                    LoadResource.soundPool.play(LoadResource.sound_select, 1, 1, 0, 0, 1);
                                    Message msg = Message.obtain();
                                    msg.what = IS_LUCK_STATE;
                                    handler.sendMessage(msg);
                                } else {
                                    GameRule.playWinSound(destPosition);
                                    GameRule.scoreWin = GameRule.getWinScore(destPosition);
                                    Log.i("Slot", "scoreWin==" + GameRule.scoreWin);
                                    GameRule.playWinBgSound(context, destPosition, GameRule.scoreWin);
                                }
                            }
                        }

                    }
                }
            }
        }
        canvas.restore();
    }

    /**
     * 延时
     *
     * @param i
     */
    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示得分情况
     */
    private void showScore(Canvas canvas) {
        canvas.save();
        char[] strTotalScore = strToCharArr(GameRule.scoreTotal);
        char[] strWinScore = strToCharArr(GameRule.scoreWin);
        for (int i = 0; i < strTotalScore.length; i++) {
            canvas.drawBitmap(LoadResource.digtalList
                            .get(Integer.parseInt(String.valueOf(strTotalScore[strTotalScore.length - 1 - i]))),
                    null, totalScore.get(i), null);
        }
        for (int i = 0; i < strWinScore.length; i++) {
            canvas.drawBitmap(LoadResource.digtalList
                            .get(Integer.parseInt(String.valueOf(strWinScore[strWinScore.length - 1 - i]))),
                    null, winScore.get(i), null);
        }

        //绘制下注数码灯
        for (int i = 0; i < 16; i++) {
            getBetPoint();
            canvas.drawBitmap(LoadResource.digtalList.get(betPointList.get(i)), null, betScore.get(i), null);
        }
        canvas.restore();
    }

    /**
     * @param score
     * @return
     */
    private char[] strToCharArr(int score) {
        String str = String.valueOf(score);
        char[] stringArr = str.toCharArray();
        return stringArr;
    }

    /**
     * 下注点数
     */
    private void getBetPoint() {
        betPointList.clear();
        betPointList.add(GameRule.pointApple % 10);
        betPointList.add(GameRule.pointApple / 10);
        betPointList.add(GameRule.pointOrange % 10);
        betPointList.add(GameRule.pointOrange / 10);
        betPointList.add(GameRule.pointMango % 10);
        betPointList.add(GameRule.pointMango / 10);
        betPointList.add(GameRule.pointBell % 10);
        betPointList.add(GameRule.pointBell / 10);
        betPointList.add(GameRule.pointWatermelon % 10);
        betPointList.add(GameRule.pointWatermelon / 10);
        betPointList.add(GameRule.pointStar % 10);
        betPointList.add(GameRule.pointStar / 10);
        betPointList.add(GameRule.point_77 % 10);
        betPointList.add(GameRule.point_77 / 10);
        betPointList.add(GameRule.pointBar % 10);
        betPointList.add(GameRule.pointBar / 10);
    }

    /**
     * 获取幸运值
     */
    private void isLuckState() {
        if (destPosition == 9 || destPosition == 21) {
            int luck = GameRule.getLuck();
//            int luck = 28;//debug
            sleep(1000);
            switch (luck) {
                case 24:
                    GameRule.playWinSound(GameRule.SMALL_FOUR_WINDS);
                    gameState = GameRule.SMALL_FOUR_WINDS;
                    break;
                case 25:
                    GameRule.playWinSound(GameRule.SMALL_THREE_DRAGONS);
                    gameState = GameRule.SMALL_THREE_DRAGONS;
                    break;
                case 26:
                    GameRule.playWinSound(GameRule.BIG_THREE_DRAGONS);
                    gameState = GameRule.BIG_THREE_DRAGONS;
                    break;
                case 27:
                    GameRule.playWinSound(GameRule.GRAND_SLAM);
                    gameState = GameRule.GRAND_SLAM;
                    break;
                case 28:
                    GameRule.playWinSound(GameRule.LANDY_FAIRY);
                    gameState = GameRule.LANDY_FAIRY;
                    randomPosition = GameRule.getLuckyPosition();
                    break;
                case 29:
                    GameRule.playWinSound(GameRule.NINE_GATES);
                    gameState = GameRule.NINE_GATES;
                    randomPosition = GameRule.getLuckyPosition();
                    break;
                case 30:
                    GameRule.playWinSound(GameRule.DRIVE_TRAIN);
                    sleep(1000);
                    gameState = GameRule.DRIVE_TRAIN;
                    randomPosition = GameRule.getLuckyPosition();
                    break;
                case 31:
                    gameState = GameRule.SPLOT_LIGHT;
                    randomPosition = GameRule.getLuckyPosition();
                    break;
                case 32:
                    GameRule.playWinSound(GameRule.EAT_ALL);
                    gameState = GameRule.EAT_ALL;
                    break;
                default:
                    break;
            }
            Log.e("Slot", "randomPosition==" + randomPosition);
        }
    }

    /**
     * 小四喜==四个苹果 position=5、10、16、22
     *
     * @param canvas
     */
    private void smallFourWinds(Canvas canvas) {
        if (isFlash) {
            canvas.save();
            canvas.drawBitmap(LoadResource.luckyPictureList.get(5), null, showRectF,
                    null);

            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(5).left, itemInfos.get(5).top,
                            itemInfos.get(5).right,
                            itemInfos.get(5).bottom), null);
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(10).left, itemInfos.get(10).top,
                            itemInfos.get(10).right,
                            itemInfos.get(10).bottom), null);
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(16).left, itemInfos.get(16).top,
                            itemInfos.get(16).right,
                            itemInfos.get(16).bottom), null);
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(22).left, itemInfos.get(22).top,
                            itemInfos.get(22).right,
                            itemInfos.get(22).bottom), null);
            canvas.restore();

            if (!isFlag) {
                Message msg = Message.obtain();
                msg.what = SMALL_FOUR_WINDS;
                handler.sendMessage(msg);
                isFlag = !isFlag;
            }
        }
    }

    /**
     * 小三元==橘子芒果铃铛 position=0、6、13
     *
     * @param canvas
     */
    private void smallThreeDragons(Canvas canvas) {
        if (isFlash) {
            canvas.save();

            canvas.drawBitmap(LoadResource.luckyPictureList.get(4), null, showRectF,
                    null);

            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(0).left, itemInfos.get(0).top,
                            itemInfos.get(0).right,
                            itemInfos.get(0).bottom), null);
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(6).left, itemInfos.get(6).top,
                            itemInfos.get(6).right,
                            itemInfos.get(6).bottom), null);
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(13).left, itemInfos.get(13).top,
                            itemInfos.get(13).right,
                            itemInfos.get(13).bottom), null);
            canvas.restore();

            if (!isFlag) {
                Message msg = Message.obtain();
                msg.what = SMALL_THREE_DRAGONS;
                handler.sendMessage(msg);
                isFlag = !isFlag;
            }
        }
    }

    /**
     * 大三元===双星、双7、西瓜 position==7、15、19
     *
     * @param canvas
     */
    private void bigThreeDragons(Canvas canvas) {
        if (isFlash) {
            canvas.save();

            canvas.drawBitmap(LoadResource.luckyPictureList.get(2), null, showRectF,
                    null);

            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(7).left, itemInfos.get(7).top,
                            itemInfos.get(7).right,
                            itemInfos.get(7).bottom), null);
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(15).left, itemInfos.get(15).top,
                            itemInfos.get(15).right,
                            itemInfos.get(15).bottom), null);
            canvas.drawBitmap(getFgBitmap(), null,
                    new RectF(itemInfos.get(19).left, itemInfos.get(19).top,
                            itemInfos.get(19).right,
                            itemInfos.get(19).bottom), null);
            canvas.restore();

            if (!isFlag) {
                Message msg = Message.obtain();
                msg.what = BIG_THREE_DRAGONS;
                handler.sendMessage(msg);
                isFlag = !isFlag;
            }
        }
    }

    /**
     * 大满贯===通通有奖 position==all
     *
     * @param canvas
     */
    private void grandSlam(Canvas canvas) {
        if (isFlash) {
            canvas.save();

            canvas.drawBitmap(LoadResource.luckyPictureList.get(1), null, showRectF,
                    null);

            for (int i = 0; i < 24; i++) {
                canvas.drawBitmap(getFgBitmap(), null,
                        new RectF(itemInfos.get(i).left,
                                itemInfos.get(i).top,
                                itemInfos.get(i).right,
                                itemInfos.get(i).bottom), null);
            }
            canvas.restore();

            if (!isFlag) {
                Message msg = Message.obtain();
                msg.what = GRAND_SLAM;
                handler.sendMessage(msg);
                isFlag = !isFlag;
            }
        }
    }

    /**
     * 仙女闪花
     *
     * @param canvas
     * @throws InterruptedException
     */
    private int i = 0;//天女散花位置标记

    private void landyFairy(Canvas canvas) {
        if (!isFlag) {
            ++i;
            if (i % 3 == 0) {
//                selectedPointList.add(i);
//                Log.e("Slot", "Here===" + i);
                canvas.save();
                canvas.drawBitmap(getFgBitmap(), null,
                        new RectF(
                                itemInfos.get((randomPosition + i) % 24).left,
                                itemInfos.get((randomPosition + i) % 24).top,
                                itemInfos.get((randomPosition + i) % 24).right,
                                itemInfos.get((randomPosition + i) % 24).bottom),
                        null);
                canvas.restore();
                GameRule.playWinSound(GameRule.SPLOT_LIGHT);
                sleep(500);
            }

            if (i == 19) {
                isFlag = true;
                i = 0;
                Message msg = Message.obtain();
                msg.what = LANDY_FAIRY;
                handler.sendMessage(msg);
            }
        } else {
            for (Integer selected : selectedPointList) {
                if (isFlash) {
                    canvas.save();
                    canvas.drawBitmap(getFgBitmap(), null,
                            new RectF(itemInfos.get((randomPosition + selected) % 24).left,
                                    itemInfos.get((randomPosition + selected) % 24).top,
                                    itemInfos.get((randomPosition + selected) % 24).right,
                                    itemInfos.get((randomPosition + selected) % 24).bottom),
                            null);
                    canvas.restore();
                }
            }
            return;
        }
    }

    /**
     * 九莲宝灯 position==随机9个位置
     *
     * @param canvas
     */
    private void nineGates(Canvas canvas) {
        if (isFlash) {
            canvas.save();
            for (int i = 0; i < 9; i++) {
                canvas.drawBitmap(getFgBitmap(), null,
                        new RectF(
                                itemInfos.get((randomPosition + i) % 24).left,
                                itemInfos.get((randomPosition + i) % 24).top,
                                itemInfos.get((randomPosition + i) % 24).right,
                                itemInfos.get((randomPosition + i) % 24).bottom),
                        null);
            }
            canvas.restore();
            if (!isFlag) {
                Message msg = Message.obtain();
                msg.what = NINE_GATES;
                handler.sendMessage(msg);

                isFlag = !isFlag;
            }

        }
    }

    /**
     * 开火车
     *
     * @param canvas
     */
    private void driveTrain(Canvas canvas) {
        canvas.save();

        canvas.drawBitmap(LoadResource.luckyPictureList.get(3), null, showRectF, null);

        for (int i = 0; i < 5; i++) {
            canvas.drawBitmap(
                    getFgBitmap(),
                    null,
                    new RectF(itemInfos.get((currentPosition + i) % 24).left,
                            itemInfos.get((currentPosition + i) % 24).top,
                            itemInfos.get((currentPosition + i) % 24).right,
                            itemInfos.get((currentPosition + i) % 24).bottom),
                    null);
            sleep(10);
        }
        canvas.restore();

        if (randomPosition == currentPosition % 24) {
            if (isSecond) {
                if (!isFlag) {
                    Message msg = Message.obtain();
                    msg.what = DRIVE_TRAIN;
                    handler.sendMessage(msg);

                    isFlag = !isFlag;
                }
                return;
            }
            isSecond = true;
        }
        ++currentPosition;
        //火车启动后，播放一次开火车背景音效
        if (isWorked) {
            LoadResource.soundPool.play(LoadResource.sound_bg_drive_train, 1, 1, 0, 0, 1.0f);
            isWorked = !isWorked;
        }
    }

    /**
     * 射灯
     *
     * @param canvas
     */
    private void splotLight(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(
                getFgBitmap(),
                null,
                new RectF(itemInfos.get(currentPosition % 24).left, itemInfos
                        .get(currentPosition % 24).top, itemInfos
                        .get(currentPosition % 24).right, itemInfos
                        .get(currentPosition % 24).bottom), null);
        canvas.restore();

        if (randomPosition == currentPosition % 24) {
            if (!isFlag) {
                Message msg = Message.obtain();
                msg.what = SPLOT_LIGHT;
                handler.sendMessage(msg);

                isFlag = !isFlag;
            }
            return;
        }

        if (Math.abs(12 - randomPosition) >= 6) {
            ++currentPosition;
        } else {
            --currentPosition;
        }
        sleep(500);
        GameRule.playWinSound(GameRule.SPLOT_LIGHT);
    }

    /**
     * 统统吃掉
     *
     * @param canvas
     */
    private void eatAll(Canvas canvas) {
        canvas.save();
        mCanvas.drawBitmap(LoadResource.luckyPictureList.get(0), null,
                showRectF, null);
        canvas.drawBitmap(
                getFgBitmap(),
                null,
                new RectF(itemInfos.get(currentPosition % 24).left, itemInfos
                        .get(currentPosition % 24).top, itemInfos
                        .get(currentPosition % 24).right, itemInfos
                        .get(currentPosition % 24).bottom), null);
        canvas.restore();
    }
}
