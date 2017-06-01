package com.ezworking.wechatunlock.domain;

/**
 * Created by sxj on 2017/5/20.
 */
public class MyinfoBean extends ResultBean {


    public DataBean data;

    public static class DataBean {
        public String name;
        public String phone;
        public String wechat;
        public String points;
        public String qq;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
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
                    '}';
        }
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyinfoBean{" +
                "data=" + data +
                '}';
    }
}
