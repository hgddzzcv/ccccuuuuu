package com.ezworking.wechatunlock.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangchao on 2017/5/14 0014.
 */
@Entity
public class ContactResult extends ResultBean {
    @Id
    public String identifier;
    public String name;
    public String phone;
    public String wechat;
    public String getWechat() {
        return this.wechat;
    }
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIdentifier() {
        return this.identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    @Generated(hash = 959752039)
    public ContactResult(String identifier, String name, String phone, String wechat) {
        this.identifier = identifier;
        this.name = name;
        this.phone = phone;
        this.wechat = wechat;
    }
    @Generated(hash = 1457122003)
    public ContactResult() {
    }

    @Override
    public String toString() {
        return "ContactResult{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", wechat='" + wechat + '\'' +
                '}';
    }

    //    public String points;
//    public List<DataDataBean> data;
//
//    public static class DataDataBean {
//
//    }
}
