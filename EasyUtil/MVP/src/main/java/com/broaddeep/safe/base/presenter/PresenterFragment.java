package com.broaddeep.safe.base.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.broaddeep.safe.base.view.ViewDelegate;

import java.lang.reflect.Constructor;

/**
 * Description: Presenter基类
 *
 * @author Zhang Fangtao
 * @version 1.0
 * @since 16/4/20
 */
public abstract class PresenterFragment<T extends ViewDelegate> extends Fragment {
    protected T mViewDelegate;

    public PresenterFragment() {
        try {
            Class<T> c = getViewDelegateClass();
            if (c == null) return;
            Constructor<T> constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            mViewDelegate = c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("onCreate view delegate error!", e);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mViewDelegate != null) {
            mViewDelegate.setAttachedContext(context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mViewDelegate != null) {
            mViewDelegate.setAttachedContext(null);
        }
        mViewDelegate = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mViewDelegate != null) {
            View view = mViewDelegate.getContentView();
            if (view != null) {
                return view;
            }
            mViewDelegate.onCreate(inflater, container);
            return mViewDelegate.getContentView();
        }
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindEventListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewDelegate != null) {
            mViewDelegate.onDestroy();
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            if (mViewDelegate == null) {
                Class<T> c = getViewDelegateClass();
                if (c == null) return;
                Constructor<?> constructor = c.getDeclaredConstructor();
                constructor.setAccessible(true);
                mViewDelegate = c.newInstance();
                mViewDelegate.setAttachedContext(getActivity());
                mViewDelegate.onCreate(getLayoutInflater(savedInstanceState), null);
            }
        } catch (Exception e) {
            throw new RuntimeException("onCreate view delegate error!", e);
        }
    }

    /**
     * 绑定事件
     */
    protected void bindEventListener() {
    }

    /**
     * 获取View代理类
     * @return View代理实现类
     */
    protected abstract Class<T> getViewDelegateClass();
}
