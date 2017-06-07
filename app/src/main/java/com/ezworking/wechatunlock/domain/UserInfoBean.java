package com.ezworking.wechatunlock.domain;

/**
 * Created by sxj 2017/05/16
 * 个人信息结构
 */
public class UserInfoBean extends ResultBean{



    public DataBean data;

    public static class DataBean {
        public String name;
        public String phone;
        public String wechat;
        public String points;
        public String qq;
        public String userToken;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", wechat='" + wechat + '\'' +
                    ", points='" + points + '\'' +
                    ", qq='" + qq + '\'' +
                    ", userToken='" + userToken + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "data=" + data +
                '}';
    }
}
