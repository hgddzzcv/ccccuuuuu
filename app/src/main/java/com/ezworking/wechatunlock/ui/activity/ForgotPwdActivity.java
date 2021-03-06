package com.ezworking.wechatunlock.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.utils.CommonActionBar;
import com.ezworking.my_android.base.utils.LogUtil;
import com.ezworking.my_android.base.utils.MD5;
import com.ezworking.my_android.base.utils.PageJumps;
import com.ezworking.my_android.base.utils.SzSign;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.my_android.base.view.LoadingDialog;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.api.ConstantNetUrl;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.domain.ResultBean;
import com.ezworking.wechatunlock.domain.UserInfoBean;
import com.ezworking.wechatunlock.ui.AppBaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * Created by sxj on 2017/6/4.
 */
public class ForgotPwdActivity extends AppBaseActivity {
    @Bind(R.id.confirm)
    Button mConfirm;

    @Bind(R.id.iv_avatar)
    public SimpleDraweeView simpleDraweeView;

    @Bind(R.id.phone_num)
    EditText mPhoneNum;

    @Bind(R.id.code)
    EditText mCode;

    @Bind(R.id.get_code)
    Button mGetCode;


    @Bind(R.id.pwd)
    EditText mPwd;


    private LoadingDialog mLoadDialog;
    private Timer timer;
    private int time = 30;
    private TimerTask timerTask;
    private String phoneNum;
    private String Code;
    private String pwd;


    private ResultBean resultbean;
    private UserInfoBean userInfoBean;
    private ResultBean result;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_forgot);
    }

    @Override
    public void initData() {
        initCustomActionBar();
        mConfirm.setAlpha(0.4f);
        mConfirm.setEnabled(false);
        checkEditText(mPhoneNum);
        checkEditText(mCode);
        checkEditText(mPwd);
       /* WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams layoutParams = lLayout.getLayoutParams();
        layoutParams.height = height /4;
        lLayout.setLayoutParams(layoutParams);*/
        simpleDraweeView.setImageResource(R.drawable.icon_avatar);

        mGetCode.setEnabled(false);
        mGetCode.setAlpha(0.4f);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mGetCode.setEnabled(false);
                    mGetCode.setBackgroundResource(R.drawable.shape_gray);
                    mGetCode.setText(time + "");
                    time--;
                    if (time < 0) {
                        mGetCode.setEnabled(true);
                        mGetCode.setBackgroundResource(R.drawable.shape_orage);
                        mGetCode.setText(R.string.get_code);
                        timer.cancel();
                        timer = null;
                        timerTask = null;
                        time = 30;
                    }
                    break;
            }

        }
    };
    @Override
    public void initListener() {
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });


        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode();
            }
        });


    }


    public void checkEditText(EditText editText) {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNum = mPhoneNum.getText().toString().trim();
                Code = mCode.getText().toString().trim();
                pwd = mPwd.getText().toString().trim();

                if (time==30&&phoneNum.length()==11){
                    mGetCode.setEnabled(true);
                    mGetCode.setAlpha(1.0f);
                    mGetCode.setBackgroundResource(R.drawable.shape_orage);
                }else if(time==30&&phoneNum.length()!=11){
                    mGetCode.setEnabled(false);
                    mGetCode.setAlpha(0.4f);
                }

                checkInputContent();
            }
        };
        editText.addTextChangedListener(tw);
    }


    private void checkInputContent() {
        if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(Code) && !TextUtils.isEmpty(pwd)&& pwd.length()>=6) {
            mConfirm.setEnabled(true);
            mConfirm.setAlpha(1.0f);
        } else {
            mConfirm.setEnabled(false);
            mConfirm.setAlpha(0.4f);
        }
    }
    private void showLoading(String msg) {
        mLoadDialog = new LoadingDialog(msg);
        mLoadDialog.show(getSupportFragmentManager(), LoadingDialog.TAG);
    }

    private void dismissLoading() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
    }

    private void getVerifyCode() {
        try {
            JSONObject jsonObject = new JSONObject();
            String signStr = SzSign.createSign(jsonObject);
            String token = MD5.getMessageDigest((signStr+"JXQ").getBytes());
            jsonObject.put("key", signStr);
            jsonObject.put("token", token);
            jsonObject.put("phone", phoneNum);
            jsonObject.put("type","2");
            RequestApi.jsonPost(aty, ConstantNetUrl.SENDCODE, jsonObject, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    showLoading("");
                }

                @Override
                public void onFinish() {
                    dismissLoading();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "utf-8");
                        Log.e("aaaaa",response);
                        resultbean = new Gson().fromJson(response, ResultBean.class);
                        if (resultbean.success.equals("0")) {
                            ToastUtil.showToast(aty, resultbean.getErrorMsg());
                            LogUtil.e("getVerifyCodeByPhoneNumber---------"+resultbean.toString());
                            return;
                        }
                        LogUtil.e("getVerifyCodeByPhoneNumber---------"+resultbean.success);
                        if (timer == null) {
                            timer = new Timer();
                            timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    Message msg = Message.obtain();
                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                }
                            };
                            timer.schedule(timerTask, 0, 1000);
                        }


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
    private void getData() {



        try {
            //  String md5Pwd = MD5.getMessageDigest(pwd.getBytes());
            //  LogUtil.e("md5Pwd--------" + md5Pwd);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pwd", pwd);
            jsonObject.put("phone", phoneNum);
            jsonObject.put("verifyCode", Code);
            RequestApi.jsonPost(aty, ConstantNetUrl.UPDATEPWD, jsonObject, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    showLoading("");
                }

                @Override
                public void onFinish() {
                    dismissLoading();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = null;
                    try {
                        response = new String(responseBody, "utf-8");
                        try{
                            userInfoBean = new Gson().fromJson(response, UserInfoBean.class);
                        }catch (Exception e){
                            result =  new Gson().fromJson(response,ResultBean.class);
                            //deal wrong
                            ToastUtil.showToast(aty, result.getErrorMsg());
                            return;
                        }
                        LogUtil.e(userInfoBean.success+"2");
                        if (userInfoBean.success.equals("0")) {
                            ToastUtil.showToast(aty, userInfoBean.getErrorMsg());
                            return;
                        }


                        //修改成功
                        ToastUtil.showToast(aty, "密码已重置!");
                        // UserInfoBean userInfoBean = new UserInfoBean();
                      //   userInfoBean.data.setUserToken(regsiterInfoBean.getUserToken());
                        // AppCache.getInstance().saveUserInfo(userInfoBean);
                        //  AppCache.getInstance().setUserLogin(true);
                        //  AppCache.getInstance().setPassword(pwd);
                        PageJumps.PageJumps(aty,LoginActivity.class,null);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AsyncHttpClientUtil.onFailure(aty, statusCode);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initCustomActionBar() {
        CommonActionBar commonActionBar =
                new CommonActionBar(aty,
                        new CommonActionBar.IActionBarListener() {
                            @Override
                            public void onBtnRight(View v) {
                            }

                            @Override
                            public void onBtnLeft(View v) {
                                if(timer!=null){
                                    timer.cancel();
                                    timer = null;
                                }
                                finish();
                            }

                            @Override
                            public void setTitle(TextView v) {
                                v.setText("修改密码");
                                v.setTextColor(getResources().getColor(R.color.black_text3));
                            }
                        });
        TextView tvTitle
                = commonActionBar.getTitleView();
        commonActionBar.setImgLeftViewVisibility(View.VISIBLE);
        commonActionBar.setimgRightViewVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
        }
    }
}
