package com.ezworking.wechatunlock.domain;

/**
 * Created by sxj on 2017/5/17.
 */
public class RegsiterInfoBean extends ResultBean{


    public String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        return "RegsiterInfoBean{" +
                "userToken='" + userToken + '\'' +
                '}';
    }
}
