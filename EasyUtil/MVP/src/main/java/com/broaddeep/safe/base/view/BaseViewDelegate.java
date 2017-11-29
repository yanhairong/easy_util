package com.broaddeep.safe.base.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Description: View代理基类
 *
 * @author Zhang Fangtao
 * @version 1.0
 * @since 16/4/20
 */
public abstract class BaseViewDelegate implements ViewDelegate {
    private final SparseArray<View> mViews = new SparseArray<>();

    private View mContentView;

    protected Context mAttachedContext;

    /**
     * 获取布局ID
     * @return 布局ID
     */
    protected int getLayoutId() {
        return -1;
    }

    /**
     * 获取布局控件
     * @return 布局控件
     */
    protected View getLayout() {
        return null;
    }

    @Override
    public void onCreate(LayoutInflater inflater, ViewGroup root) {
        int id = getLayoutId();
        if (id != -1) {
            mContentView = inflater.inflate(id, root, false);
        } else {
            mContentView = getLayout();
        }
    }

    @Override
    public void onDestroy() {
        mViews.clear();
    }

    @Override
    public final View getContentView() {
        return mContentView;
    }

//    public final SkinProxy getProxy() {
//        return ThemeFacade.getProxy(getAttachedContext());
//    }
//
//    public final StyleProvider getProvider() {
//        return ThemeFacade.getProvider(getAttachedContext());
//    }

    @SuppressWarnings("unchecked")
    private <T extends View> T bindView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mContentView.findViewById(id);
            mViews.put(id, view);
        }
        if (view == null) return null;
        return (T) view;
    }

    /**
     * 获取子控件
     * @param id 子控件id
     * @param <T> View派生类型
     * @return 子控件
     */
    public final <T extends View> T get(int id) {
        return bindView(id);
    }

    /**
     * 设置控件点击监听事件
     * @param listener 点击监听
     * @param ids 需要监听的控件id
     */
    public void setOnClickListener(View.OnClickListener listener, int... ids) {
        if (ids == null) return;
        for (int id : ids) {
            View view = get(id);
            if (view != null) {
                view.setOnClickListener(new ViewAntiFastClickListener(listener));
            }
        }
    }

    /**
     * toast显示信息
     * @param msg 显示信息
     */
    public void toast(CharSequence msg) {
        Toast.makeText(getAttachedContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAttachedContext(Context context) {
        mAttachedContext = context;
    }

    @Override
    public Context getAttachedContext() {
        return mAttachedContext;
    }
}
