package com.ezworking.wechatunlock.ui.activity;

import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dou361.statusbar.StatusBarUtil;
import com.ezworking.my_android.base.BaseApplication;
import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.utils.CommonActionBar;
import com.ezworking.my_android.base.utils.LogUtil;
import com.ezworking.my_android.base.utils.PageJumps;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.my_android.base.view.LoadingDialog;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.application.AppCache;
import com.ezworking.wechatunlock.domain.UserInfoBean;
import com.ezworking.wechatunlock.ui.AppBaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppBaseActivity {
    @Bind(R.id.login_account_et)
    EditText mAccountEt;

    @Bind(R.id.login_pwd_et)
    EditText mPwdEt;

    @Bind(R.id.btn_login)
    Button mLoginBtn;

    @Bind(R.id.show_code_ib)
    ImageButton mShowCodeIb;

    @Bind(R.id.no_accout)
    TextView noAccount;

    @Bind(R.id.tv_forgot)
    TextView forgot;

    private LoadingDialog mLoadDialog;
    private long exitTime = 0;
    private Boolean isShow = false;
    private String account;
    private String pWd;

    @Bind(R.id.iv_avatar)
    public SimpleDraweeView simpleDraweeView;

    @Bind(R.id.lLayout)
    LinearLayout lLayout;


    @Override
    public void setRootView() {
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initData() {
        initCustomActionBar();
        StatusBarUtil.setColorNoTranslucent(this,getResources().getColor(R.color.maincolor));
        /*WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams layoutParams = lLayout.getLayoutParams();
        layoutParams.height = height /4;
        lLayout.setLayoutParams(layoutParams);*/
        simpleDraweeView.setImageResource(R.drawable.icon_avatar);
        //将以上次登录保存的账号密码显示
        //将以上次登录保存的账号密码显示
       // String savedAccount = AppCache.getInstance().getAccount();
       // mAccountEt.setText(savedAccount);
      //  mAccountEt.setSelection(savedAccount.length());
        mLoginBtn.setEnabled(false);
        mLoginBtn.setAlpha(0.4f);
        checkEditText(mAccountEt);
        checkEditText(mPwdEt);



    }

    @Override
    public void initListener() {

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLogin();
            }
        });
        mShowCodeIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShow){
                    mPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShow = true;
                    mShowCodeIb.setImageResource(R.drawable.icon_pwd_open);
                }else{
                    mPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShow = false;
                    mShowCodeIb.setImageResource(R.drawable.icon_pwd_close);
                }
                mPwdEt.setSelection(mPwdEt.getText().length());
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            PageJumps.PageJumps(aty,RegisterActivity.class,null);

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageJumps.PageJumps(aty,ForgotPwdActivity.class,null);
            }
        });


    }


    private void showLoading(String msg) {
        mLoadDialog = new LoadingDialog(msg);
        //mLoadDialog.show(getSupportFragmentManager(), LoadingDialog.TAG);
    }

    private void dismissLoading() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
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
                account = mAccountEt.getText().toString().trim();
                pWd = mPwdEt.getText().toString().trim();

                LogUtil.e("account----------" + account);
                LogUtil.e("pWd---------" + pWd);

                checkInputContent();
            }
        };
        editText.addTextChangedListener(tw);
    }

    private void checkInputContent() {
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pWd) && pWd.trim().length() >= 6) {
            mLoginBtn.setEnabled(true);
            mLoginBtn.setAlpha(1.0f);
        } else {
            mLoginBtn.setEnabled(false);
            mLoginBtn.setAlpha(0.4f);
        }
    }

    private void gotoLogin() {

        final String account = mAccountEt.getText().toString();
        final String pwd = mPwdEt.getText().toString();



        try {

            //String md5Psw = MD5.getMessageDigest(pwd.getBytes());
            //md5Psw = pwd;
            AppCache.getInstance().setPassword(pwd);
            RequestApi.login(aty, account, pwd, new AsyncHttpResponseHandler() {

                public void onStart() {
                    showLoading("正在登录，请稍后…");
                }

                public void onFinish() {
                    dismissLoading();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "utf-8");
                        Log.d("api",response);
                        UserInfoBean userInfoBean = new Gson().fromJson(response, UserInfoBean.class);

                        Log.e("111","userInfoBean" + userInfoBean.data.getName() + "---"  + userInfoBean.data.getUserToken());
                        Log.e("111","userInfoBean.isSuccess" +userInfoBean.success);
                        if (userInfoBean.success.equals("0")) {
                            Log.e("111","111" +userInfoBean.getErrorMsg());
                            ToastUtil.showToast(aty, userInfoBean.getErrorMsg());
                            return;
                        }
                        Log.e("111","333");
                        if (userInfoBean == null) {
                            ToastUtil.showToast(aty, "请求失败，请稍后重试!");
                            return;
                        }

                        AppCache.getInstance().saveUserInfo(userInfoBean);
                        AppCache.getInstance().setUserLogin(true);

                        ToastUtil.showToast(aty,"请求成功。");


                        PageJumps.PageJumpsAlpha(aty, MainActivity.class, null);
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

                            }

                            @Override
                            public void setTitle(TextView v) {
                                v.setText("登录");
                                v.setTextColor(getResources().getColor(R.color.black_text3));
                            }
                        });
        TextView tvTitle
                = commonActionBar.getTitleView();
        commonActionBar.setImgLeftViewVisibility(View.GONE);
        commonActionBar.setimgRightViewVisibility(View.GONE);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showToast(aty, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                //SharePreferenceUtils.clearValueFromSP(context, Constant.USER_TOKEN);
                finish();
                Process.killProcess(Process.myPid());
               // System.exit(0);
                BaseApplication.getInst().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
