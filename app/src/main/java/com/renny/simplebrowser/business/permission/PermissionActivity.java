package com.renny.simplebrowser.business.permission;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.renny.simplebrowser.business.log.Logs;


/**
 * Created by LuckyCrystal on 2017/8/7.
 */

public abstract class PermissionActivity extends AppCompatActivity {

    private APermission axdPermission;

    public void setAxdPermission(APermission axdPermission) {
        this.axdPermission = axdPermission;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Permissions.PERMISSION_SETTING_CODE) {
            onSettingResult(requestCode);
        }
    }


    protected void onSettingResult(int requestCode) {
        Logs.base.d("permission--" + "setting" + requestCode);
        if (axdPermission != null) {
            axdPermission.onSettingResult(axdPermission);
        }
    }


    /**
     * 权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (axdPermission != null && axdPermission.getPermissionListener() != null) {
            axdPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onDestroy() {
        if (axdPermission != null) {
            axdPermission.destroy();
            axdPermission = null;
        }
        super.onDestroy();
    }
}
