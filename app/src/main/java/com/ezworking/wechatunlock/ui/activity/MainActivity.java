package com.ezworking.wechatunlock.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.ezworking.my_android.base.BaseApplication;
import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.utils.CommonActionBar;
import com.ezworking.my_android.base.utils.LogUtil;
import com.ezworking.my_android.base.utils.MD5;
import com.ezworking.my_android.base.utils.PageJumps;
import com.ezworking.my_android.base.utils.SzSign;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.api.ConstantNetUrl;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.domain.ResultBean;
import com.ezworking.wechatunlock.domain.SysInfoBean;
import com.ezworking.wechatunlock.ui.AppBaseActivity;
import com.ezworking.wechatunlock.ui.fragment.HomeFragment;
import com.ezworking.wechatunlock.ui.view.CustomDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppBaseActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView tv_tile = null;
    RelativeLayout rl_title = null;

    private long exitTime = 0;
    SysInfoBean sysInfoBean;



    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"EiFpiWImzUijXEXKmWGiFCcI");
    }



    @Override
    public void initData() {
        //PermisionController.getInstance().requestPermission(this);
        initCustomActionBar();
  //      checkVersion();

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_container,new HomeFragment());
        fragmentTransaction.commit();

        getData();

    }

    private void getData() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pId", "");
            RequestApi.jsonPost(aty, ConstantNetUrl.GETSYSINFO, jsonObject, new AsyncHttpResponseHandler() {


                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "utf-8");
                        Log.e("sysInfo",response);
                        sysInfoBean = new Gson().fromJson(response, SysInfoBean.class);
                        if (sysInfoBean.getSuccess().equals("0")) {
                            ToastUtil.showToast(aty, sysInfoBean.getErrorMsg());
                            return;
                        }
                        Uri imagePath = null;
                        SysInfoBean.PostMsgBean postMsgBean = sysInfoBean.getPostMsg();
                        List<String> picLink = postMsgBean.getPicLink();
                        for(int i = 0;i<picLink.size();i++){
                             imagePath = Uri.parse(picLink.get(0));
                        }

                        String body = postMsgBean.getBody();
                        showMessageDialog(body,imagePath);



                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AsyncHttpClientUtil.onFailure(aty, statusCode);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showMessageDialog(String message,Uri imagePath) {
        Dialog dialog = null;
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(aty);
        customBuilder.setTitle("公告栏");
        customBuilder.setMessage(message);
        customBuilder.setGravity(Gravity.CENTER);
        customBuilder.setMessageIcon(imagePath);
        customBuilder.setIconVisibility(View.VISIBLE);
        customBuilder.setPositiveButtonTextColor(aty.getResources().getColor(R.color.main_bottom_btn_press1));
        customBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


            }
        });

        dialog = customBuilder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void initListener() {

    }

    private void initCustomActionBar() {
        CommonActionBar commonActionBar =
                new CommonActionBar(aty,
                        new CommonActionBar.IActionBarListener() {
                            @Override
                            public void onBtnRight(View v) {

                               PageJumps.PageJumps(aty, MyinfoActivity.class, null);
    }

                            @Override
                            public void onBtnLeft(View v) {
                             //   PageJumps.PageJumps(aty, MyDetailActivity.class, null);
                            }

                            @Override
                            public void setTitle(TextView v) {
                                // TODO Auto-generated method stub


                                v.setText("首页");
                                v.setTextColor(getResources().getColor(R.color.black_text3));

                            }
                        });
        tv_tile = commonActionBar.getTitleView();
        commonActionBar.setImgLeftViewVisibility(View.VISIBLE);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showToast(aty, "再按一次退出程序");

                exitTime = System.currentTimeMillis();
            } else {

                Log.e("exit","aaaaaaaaaaaaaaa");
               // finish();
                Process.killProcess(Process.myPid());
                BaseApplication.getInst().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
