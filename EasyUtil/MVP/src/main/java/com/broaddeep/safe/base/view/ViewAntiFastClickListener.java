package com.broaddeep.safe.base.view;

import android.view.View;

/**
 * Description:
 *
 * @author Zhang Fangtao
 * @version 1.0
 * @since 16/7/1
 */
public class ViewAntiFastClickListener implements View.OnClickListener {
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime = 0;

    /**
     * 判断是否是快速点击
     */
    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        }
        return true;
    }

    private View.OnClickListener listener;

    public ViewAntiFastClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (isFastClick()) return;
        listener.onClick(v);
    }
}
