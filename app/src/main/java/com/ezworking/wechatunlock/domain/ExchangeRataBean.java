package com.ezworking.wechatunlock.domain;

/**
 * Created by sxj on 2017/5/20.
 */
public class ExchangeRataBean extends ResultBean {

    public int qqRate;
    public int cashRate;
    public int qqNum;
    public int cashNum;

    public int getQqRate() {
        return qqRate;
    }

    public void setQqRate(int qqRate) {
        this.qqRate = qqRate;
    }

    public int getCashRate() {
        return cashRate;
    }

    public void setCashRate(int cashRate) {
        this.cashRate = cashRate;
    }

    public int getQqNum() {
        return qqNum;
    }

    public void setQqNum(int qqNum) {
        this.qqNum = qqNum;
    }

    public int getCashNum() {
        return cashNum;
    }

    public void setCashNum(int cashNum) {
        this.cashNum = cashNum;
    }

    @Override
    public String toString() {
        return "ExchangeRataBean{" +
                "qqRate=" + qqRate +
                ", cashRate=" + cashRate +
                ", qqNum=" + qqNum +
                ", cashNum=" + cashNum +
                '}';
    }
}
