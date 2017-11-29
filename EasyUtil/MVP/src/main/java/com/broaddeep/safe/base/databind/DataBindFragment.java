package com.broaddeep.safe.base.databind;

import android.os.Bundle;

import com.broaddeep.safe.base.presenter.PresenterFragment;
import com.broaddeep.safe.base.view.ViewDelegate;

/**
 * Description: 数据绑定基类
 *
 * @author Zhang Fangtao
 * @version 1.0
 * @since 16/4/20
 */
public abstract class DataBindFragment<T extends ViewDelegate, D extends DataBinder> extends PresenterFragment<T> {
    protected D mBinder = getDataBinder();

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mBinder == null) {
            mBinder = getDataBinder();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder = null;
    }

    /**
     * 获取数据绑定处理类
     * @return 数据绑定处理类
     */
    public abstract D getDataBinder();
}
