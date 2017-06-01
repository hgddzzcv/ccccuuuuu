package com.ezworking.my_android.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements IBaseActivity{

    public Activity aty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInst().addActivity(this);
        aty = this;
        setRootView();
        ButterKnife.bind(this);
        if (openStatus()) {
            //StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.maincolor));
        }
        initSetting();
        initData();
        initListener();
    }

    public void initSetting() {
    }

    protected boolean openStatus() {
        return true;
    }



    @Override
    protected void onDestroy() {
        BaseApplication.getInst().remove(this);
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    /**
     * 退出程序方法
     */
    protected void onExit() {
        BaseApplication.getInst().exit();
    }


    /**
     * 退出并且重启动
     * 为了不与Activity的onRestart()冲突
     */
    protected void onExitAndStart() {
        BaseApplication.getInst().restart();
    }

    @Override
    public void setRootView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
