package com.ezworking.wechatunlock.application;


import com.ezworking.my_android.base.BaseApplication;
import com.ezworking.my_android.base.utils.CrashHandler;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by dujiande on 2016/9/13.
 */
public class MainApplication extends BaseApplication {

    private final String TAG = MainApplication.class.getSimpleName();

    public boolean isDownload;
 //   public ButtonBean buttonBean = null;
    public static IWXAPI mWxApi;

    public static MainApplication mContext = null;
    public synchronized static MainApplication getInstance() {
        return mContext;
    }

    /**
     * 初始化分享平台
     */
    {
        //微信 appid appsecret
    //    PlatformConfig.setWeixin(Constant.WX_APP_ID,Constant.WX_APP_SECRET);
        // QQ和Qzone appid appkey
    //    PlatformConfig.setQQZone(Constant.QQ_APP_ID,Constant.QQ_APP_SECRET);

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        mContext = this;

        registToWX();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        AppCache.getInstance().init(application);

    }

    public static String getMainExternalCacheDir() {
        return getMainExternalCacheDir("");
    }

    public static String getMainExternalCacheDir(String dirName) {
        return mContext.getExternalFilesDir(dirName).getAbsolutePath();
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constant.WX_APP_ID);
    }


}
