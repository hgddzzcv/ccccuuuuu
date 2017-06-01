package com.ezworking.wechatunlock.ui.activity;


import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.dou361.statusbar.StatusBarUtil;
import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.utils.NetWorkUtil;
import com.ezworking.my_android.base.utils.PageJumps;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.application.AppCache;
import com.ezworking.wechatunlock.domain.UserInfoBean;
import com.ezworking.wechatunlock.ui.AppBaseActivity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class LoadingActivity extends AppBaseActivity {

    private static final String TAG = LoadingActivity.class.getSimpleName();

    public static final int ENTER_APP_CHECK = 1;

    @Bind(R.id.loading_rootview)
    RelativeLayout loading_rootview;

    private Boolean isAnimationEnd = false;


   /* private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case ENTER_APP_CHECK:
                    if(isAnimationEnd){
                        handler.removeMessages(ENTER_APP_CHECK);
                        enterApp();
                    }
                    break;
            }
        }
    };*/

    public void setRootView(){
        setTheme(R.style.AppTheme);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_loading);
    }

    public void initData(){
       // startLoadingAnimation();
        StatusBarUtil.setColorNoTranslucent(this,getResources().getColor(R.color.white));
        initAnimation();
        /*isAnimationEnd = true;
        handler.sendEmptyMessage(ENTER_APP_CHECK);*/
     //   enterApp();

    }

    private void initAnimation() {

       /* //旋转动画
        RotateAnimation rotate = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2000);
        rotate.setRepeatMode(Animation.REVERSE);
        rotate.setFillAfter(true); //设置动画在结束的状态*/

        //缩放动画
        //  ScaleAnimation
//        ScaleAnimation scale = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
//        scale.setDuration(300);
//        scale.setRepeatMode(Animation.REVERSE);
//        scale.setFillAfter(true);

        //透明度动画效果
        // AlphaAnimation
        AlphaAnimation alpha = new AlphaAnimation(0.6f, 1.0f);
        alpha.setDuration(300);
        alpha.setRepeatMode(Animation.REVERSE);
        alpha.setFillAfter(true);

        //动画的集合,把要实现的动画添加进来,同时开启
        AnimationSet set = new AnimationSet(true);
     //   set.addAnimation(rotate);
        //set.addAnimation(scale);
        set.addAnimation(alpha);

        loading_rootview.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                enterApp();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

   /* private void startLoadingAnimation() {
        ViewHelper.setAlpha(loading_rootview,0.0f);
        ViewPropertyAnimator.animate(loading_rootview).alpha(1.0f).setDuration(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        isAnimationEnd = true;
                        handler.sendEmptyMessage(ENTER_APP_CHECK);
                    }
                });
    }*/

    private void enterApp() {
        if(AppCache.getInstance().isUserLogin()){
            slienceLogin();
            Log.e("登录状态","true");
        }else{
           gotoLogin();
            Log.e("登录状态","false");
        }

    }

    private void gotoLogin(){
         PageJumps.PageJumps(aty, LoginActivity.class, null);
         finish();
    }

    private void slienceLogin() {
        if(!NetWorkUtil.isNetworkConnected(aty)){
            ToastUtil.showToast(aty,"网络不可用!");
            PageJumps.PageJumpsAlpha(aty,MainActivity.class,null);
            finish();
            return;
        }
        try{
            String account = AppCache.getInstance().getAccount();
            String md5Psw = AppCache.getInstance().getPassword();
            RequestApi.login(aty, account, md5Psw, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try{
                        String response = new String(responseBody,"utf-8");
                        Log.d("api",response);
                        UserInfoBean userInfoBean = new Gson().fromJson(response, UserInfoBean.class);
                       if (userInfoBean.success.equals("0")){
                            ToastUtil.showToast(aty, userInfoBean.getErrorMsg());
                           Log.e("1",userInfoBean.getErrorMsg());
                            gotoLogin();
                            return;
                        }
                        if (userInfoBean == null) {
                            gotoLogin();
                            return;
                        }
                        AppCache.getInstance().saveUserInfo(userInfoBean);
                        AppCache.getInstance().setUserLogin(true);
                        PageJumps.PageJumpsAlpha(aty,MainActivity.class,null);
                        Log.e("登录之后再次进入",AppCache.getInstance().getToken());
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                        gotoLogin();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AsyncHttpClientUtil.onFailure(aty,statusCode);
                    gotoLogin();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            gotoLogin();
        }

    }
}
