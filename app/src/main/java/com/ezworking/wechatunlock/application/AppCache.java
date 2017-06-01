package com.ezworking.wechatunlock.application;

import android.content.Context;

import com.google.gson.Gson;
import com.ezworking.my_android.base.utils.SharePreferenceUtils;
import com.ezworking.wechatunlock.domain.UserInfoBean;

/**
 * Created by sxj on 2017/5/16.
 */
public class AppCache {
    private static AppCache ourInstance = new AppCache();
    private Context context;

    public static AppCache getInstance() {
        return ourInstance;
    }

    private AppCache() {
    }

    public  void init(Context context){
        this.context = context;
    }

    public void saveUserInfo(UserInfoBean userInfoBean){
        String jsonStr = new Gson().toJson(userInfoBean);
        if(context != null){
            SharePreferenceUtils.putValueToSP(context, Constant.USER_INFO,jsonStr);
        }

    }

    public UserInfoBean getUserInfo(){
        if(context != null){
            String jsonStr = SharePreferenceUtils.getValueFromSP(context,Constant.USER_INFO,"");
            UserInfoBean userInfoBean = new Gson().fromJson(jsonStr,UserInfoBean.class);
            return userInfoBean;
        }
        return null;
    }

    public String getToken(){
        UserInfoBean userInfoBean = getUserInfo();
        if(userInfoBean != null){
            return userInfoBean.data.getUserToken();
        }
        return "";
    }

    public String getAccount(){
        UserInfoBean userInfoBean = getUserInfo();
        if(userInfoBean != null){
            return userInfoBean.data.getName();
        }
        return "";
    }

    public void setUserLogin(boolean isLogin){
        if(context != null){
            SharePreferenceUtils.putValueToSP(context,Constant.USER_IS_LOGIN,isLogin);
        }
    }

    public Boolean isUserLogin(){
        return SharePreferenceUtils.getValueFromSP(context,Constant.USER_IS_LOGIN,false);
    }

    public void setPassword(String psw){
        SharePreferenceUtils.putValueToSP(context,Constant.USER_PASSWORD,psw);
    }

    public String getPassword(){
        return SharePreferenceUtils.getValueFromSP(context, Constant.USER_PASSWORD,"");
    }






}
