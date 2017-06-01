package com.ezworking.wechatunlock.application;


import com.ezworking.my_android.base.BaseApplication;
import com.ezworking.my_android.base.utils.CrashHandler;


/**
 * Created by dujiande on 2016/9/13.
 */
public class MainApplication extends BaseApplication {

    private final String TAG = MainApplication.class.getSimpleName();

    public boolean isDownload;
 //   public ButtonBean buttonBean = null;

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
   //     UMShareAPI.get(this);

        mContext = this;

//        crashHandler.savePath = Environment.getExternalStorageDirectory()+ File.separator+getPackageName()+"/LOG/";
//        File file = new File(crashHandler.savePath);
//        if(!file.exists()){
//            file.mkdirs();
//        }
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



}
