package com.broaddeep.safe.base.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.broaddeep.safe.base.permission.PermCallback;
import com.broaddeep.safe.base.permission.PermCheckCallback;
import com.broaddeep.safe.base.permission.PermHelper;
import com.broaddeep.safe.base.view.ViewDelegate;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: Presenter基类
 * @author Zhang Fangtao
 * @version 1.0
 * @since 16/4/20
 */
public abstract class PresenterActivity<T extends ViewDelegate> extends AppCompatActivity implements PermCallback {
    protected T mViewDelegate;

    public PresenterActivity() {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mViewDelegate == null) return;
        mViewDelegate.setAttachedContext(this);
        mViewDelegate.onCreate(getLayoutInflater(), null);
        setContentView(mViewDelegate.getContentView());
        bindEventListener();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception ignored) {
        }
        try {
            if (mViewDelegate == null) {
                Class<T> c = getViewDelegateClass();
                if (c == null) {
                    return;
                }
                Constructor<?> constructor = c.getDeclaredConstructor();
                constructor.setAccessible(true);
                mViewDelegate = c.newInstance();
                mViewDelegate.setAttachedContext(this);
                mViewDelegate.onCreate(getLayoutInflater(), null);
            }
        } catch (Exception e) {
            throw new RuntimeException("onCreate view delegate error!", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewDelegate != null) {
            mViewDelegate.onDestroy();
        }
        mViewDelegate = null;
        mPermCheckCallback = null;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermHelper.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    protected static final int REQUEST_CODE_PERM = 123;

    private PermCheckCallback mPermCheckCallback;

    public boolean checkPermission(String... permissions) {
        return permissions == null || PermHelper.hasPermissions(this, permissions);
    }

    public void requestPermission(PermCheckCallback callback, int resString, String... permissions) {
        if (permissions == null) return;
        if (PermHelper.hasPermissions(this, permissions)) {
            if (callback != null) {
                callback.onGetPermission();
            }
        } else {
            mPermCheckCallback = callback;
            ArrayList<String> list = new ArrayList<>(permissions.length);
            for (String perm : permissions) {
                if (PermHelper.hasPermissions(this, perm)) continue;
                list.add(perm);
            }
            String[] perms = new String[list.size()];
            list.toArray(perms);
            PermHelper.requestPermissions(this, getString(resString), REQUEST_CODE_PERM, perms);
        }
    }

    @Override
    public void onPermissionAllGranted() {
        if (mPermCheckCallback != null) {
            mPermCheckCallback.onGetPermission();
        }
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> permissions) {
        if (mPermCheckCallback != null) {
            mPermCheckCallback.onPermissionDenied();
        }
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> permissions) {
        if (!PermHelper.checkDeniedPermissionsNeverAskAgain(this,
                "通信卫士缺少必要权限。\n请点击\"设置\"-\"权限\"-打开所需权限。",
                android.R.string.ok, android.R.string.cancel, null, permissions)) {
            if (mPermCheckCallback != null) {
                mPermCheckCallback.onPermissionDenied();
            }
        }
    }
}
