package com.example.ht.downloadmodel;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created By HanTe
 * 权限检测工具
 */

public class PermissionCheck {
    private Context mContext;

    public PermissionCheck(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 判断权限 集合是否获得允许
     *
     * @param permissions 权限集合
     * @return true 获得允许 false 未获得允许
     */
    public boolean leaksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测某个权限是否获得允许
     *
     * @param permission 权限
     * @return true 获得权限 false 未获得权限
     */
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}