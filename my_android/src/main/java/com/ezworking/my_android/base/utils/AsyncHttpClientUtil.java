package com.ezworking.my_android.base.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


/**
 * Created by dujiande on 2016/3/21.
 * 网络请求结果工具类
 */
public class AsyncHttpClientUtil {

    /**
     * 对返回的http错误码进行处理
     * @param context
     * @param statusCode
     */
    public static void onFailure(Context context, int statusCode){
        if(context == null){
            return;
        }
        if(context instanceof Activity){
            Activity activity = (Activity)context;
            if(activity.isFinishing()){
                return;
            }
        }
        if(statusCode == 401){
            //ToastUtil.showToast(context,"Token 失效，请重新登录");
            ToastUtil.showToast(context,"网络异常，请检查网络设置");
        }else if(statusCode == 0){
            ToastUtil.showToast(context,"网络异常，请检查网络设置");
        }else if(statusCode == 408){
            ToastUtil.showToast(context,"请求超时");
            //ToastUtil.showToast(context,"网络异常，请检查网络设置");
        }else if(statusCode == 504){
            ToastUtil.showToast(context,"网关超时");
            //ToastUtil.showToast(context,"网络异常，请检查网络设置");
        }else{
            ToastUtil.showToast(context,"API 错误："+statusCode);
            //ToastUtil.showToast(context,"网络异常，请检查网络设置");
        }
        Log.e("api","错误码："+statusCode);
    }

}
