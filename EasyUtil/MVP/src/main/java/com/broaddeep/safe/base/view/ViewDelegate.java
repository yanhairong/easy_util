package com.broaddeep.safe.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description: View代理接口
 *
 * @author Zhang Fangtao
 * @version 1.0
 * @since 16/4/20
 */
public interface ViewDelegate {
    /**
     * 创建View
     */
    void onCreate(LayoutInflater inflater, ViewGroup root);

    /**
     * 释放资源
     */
    void onDestroy();

    /**
     * 获取视图
     * @return 视图
     */
    View getContentView();

    /**
     * 获取依附上下文
     * @return 上下文
     */
    Context getAttachedContext();

    /**
     * 设置应用上下文
     */
    void setAttachedContext(Context context);
}
