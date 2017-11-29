package com.broaddeep.safe.base.permission;

/**
 * Description:
 *
 * @author Zhang Fangtao
 * @version 2.0
 * @since 16/9/27
 */
public interface PermCheckCallback {
    void onGetPermission();
    void onPermissionDenied();
}
