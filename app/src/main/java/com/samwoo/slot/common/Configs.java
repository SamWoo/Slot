package com.samwoo.slot.common;

/**
 * Created by Administrator on 2018/1/5.
 */

public class Configs {
    private static final float DEFAULT_WIDTH = 480.0f;
    private static final float DEFAULT_HEIGHT = 800.0f;
    private static int screenWidth;//屏幕的宽度
    private static int screenHeight;//屏幕的高度

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return screenWidth;
    }

    /**
     * 设置屏幕的宽度
     *
     * @param screenWidth
     */
    public static void setScreenWidth(int screenWidth) {
        Configs.screenWidth = screenWidth;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return screenHeight;
    }

    /**
     * 设置屏幕的高度
     *
     * @param screenHeight
     */
    public static void setScreenHeight(int screenHeight) {
        Configs.screenHeight = screenHeight;
    }

    /**
     * 当前屏幕与默认宽度的比例
     *
     * @return
     */
    public static float getRateOfWidth() {
        return getScreenWidth() / DEFAULT_WIDTH;
    }

    /**
     * 当前屏幕高度与默认高度的比例
     *
     * @return
     */
    public static float getRateOfHeight() {
        return getScreenHeight() / DEFAULT_HEIGHT;
    }

    /**
     * 获取经过比例缩放的宽度
     *
     * @param width
     * @return
     */
    public static float getRateWidth(float width) {
        return width * getRateOfWidth();
    }

    /**
     * 获取经过比例缩放的高度
     *
     * @param height
     * @return
     */
    public static float getRateHeight(float height) {
        return height * getRateOfHeight();
    }
}
