package com.ezworking.wechatunlock.ui;

import com.ezworking.my_android.base.BaseActivity;
import com.ezworking.wechatunlock.api.RequestApi;


/**
 * Created by dujiande on 2016/12/28.
 */
public class AppBaseActivity extends BaseActivity {

    public void onDestroy(){
        super.onDestroy();
        RequestApi.cancelRequest(aty,true);
    }

}
