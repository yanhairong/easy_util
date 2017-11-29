package com.broaddeep.safe.base.permission;

import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Description:
 *
 * @author Zhang Fangtao
 * @version 2.0
 * @since 16/9/26
 */
public interface PermCallback extends ActivityCompat.OnRequestPermissionsResultCallback {
    //权限被授予回调
    void onPermissionGranted(int requestCode, List<String> permissions);

    //权限被拒绝回调
    void onPermissionDenied(int requestCode, List<String> permissions);

    //所有权限被授予回调
    void onPermissionAllGranted();
}
