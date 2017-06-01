package com.ezworking.wechatunlock.api;


/**
 * Created by sxj on 2017/5/17.
 * 存放，管理与后台交互的地址url
 */
public class ConstantNetUrl {



    public static final String BaseUrl = "http://www.ezworking.net/api/";
    /**
     * 登录
     */
    public static final String LOGIN = BaseUrl + "user/login";
    /**
     * 注册
     */
    public static final String REGISTER = BaseUrl + "user/register";
    /**
     * 发送验证码
     */

    public static final String SENDCODE = BaseUrl + "user/sendCode";

  /**
     * 上传联系人
     */

    public static final String UPLOADCONTACTS = BaseUrl + "user/uploadContacts";
    /**
     * 下载联系人
     */


    public static final String DOWNLOADCONTACTS = BaseUrl + "task/downloadNums";


    /* 我的资料
     */

    public static final String MYINFOS = BaseUrl + "user/getMyInfo";


    /**
     * 获得兑换汇率
     */

    public static final String GETEXCHANGERATE = BaseUrl + "config/getExchangeRate";
     /**
     * 提交兑换
     */

    public static final String SUBMITEXCHANGE = BaseUrl + "config/submitExchange";
    /**
     * 我的订单
     */

    public static final String GETMYORDERLIST = BaseUrl + "task/getMyOrderList";
    /**
     * 系统消息
     */

    public static final String GETSYSINFO = BaseUrl + "config/getSysInfo";

    /**
     * 解锁号码
     */

    public static final String UNLOCKPHONE = BaseUrl + "task/unlockPhone";

}