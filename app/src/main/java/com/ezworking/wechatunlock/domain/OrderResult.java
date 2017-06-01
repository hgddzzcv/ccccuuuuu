package com.ezworking.wechatunlock.domain;

import java.util.List;

/**
 * Created by wangchao on 2017/5/14 0014.
 */

public class OrderResult extends ResultBean {


    public List<Order> data;

    public class Order{
        public String oID;

        public String phone;

        public String state;

        public String points;

        public String createTime;

        public Order(String oID, String phone, String state, String points, String createTime) {
            this.oID = oID;
            this.phone = phone;
            this.state = state;
            this.points = points;
            this.createTime = createTime;
        }
    }


}
