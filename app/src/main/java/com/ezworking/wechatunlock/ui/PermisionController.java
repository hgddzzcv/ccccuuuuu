package com.ezworking.wechatunlock.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by dujiande on 2017/1/5.
 */
public class PermisionController {
    private static PermisionController ourInstance = new PermisionController();

    public static PermisionController getInstance() {
        return ourInstance;
    }

    private PermisionController() {

    }

    /**
     * 检查是否具有权限 都有返回 true ，否则返回false
     * @param activity
     * @param mPermissionList
     * @return
     */
    public boolean checkPermission(Activity activity, String[] mPermissionList){
        for(int i=0;i<mPermissionList.length;i++){
            int permissionCheck = ContextCompat.checkSelfPermission(activity,mPermissionList[i]);
            if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     * @param activity
     */
    public void requestPermission( Activity activity){
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS
            };

            if(!checkPermission(activity,mPermissionList)){
                ActivityCompat.requestPermissions(activity,mPermissionList,123);
            }
        }
    }

    public void requestPermission( Activity activity,String[] mPermissionList){
        if(Build.VERSION.SDK_INT>=23) {
            if (!checkPermission(activity, mPermissionList)) {
                ActivityCompat.requestPermissions(activity, mPermissionList, 123);
            }
        }
    }
}
