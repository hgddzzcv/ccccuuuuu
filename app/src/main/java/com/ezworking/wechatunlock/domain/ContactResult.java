package com.ezworking.wechatunlock.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangchao on 2017/5/14 0014.
 */
@Entity
public class ContactResult extends ResultBean {
    @Id(autoincrement = true)
    public Long id;
    public String name;
    public String phone;
    public String wechat;
    public String identifier;
    public String getIdentifier() {
        return this.identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
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
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 658022499)
    public ContactResult(Long id, String name, String phone, String wechat,
            String identifier) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.wechat = wechat;
        this.identifier = identifier;
    }
    @Generated(hash = 1457122003)
    public ContactResult() {
    }
   
}
