package com.ezworking.wechatunlock.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import android.util.Log;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezworking.my_android.base.utils.CommonActionBar;
import com.ezworking.my_android.base.utils.LogUtil;
import com.ezworking.my_android.base.utils.SharePreferenceUtils;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.my_android.base.view.LoadingDialog;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.api.ConstantNetUrl;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.domain.ResultBean;
import com.ezworking.wechatunlock.ui.AppBaseActivity;
import com.ezworking.wechatunlock.ui.view.CustomDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class ExchangeLikeActivity extends AppBaseActivity {
    @Bind(R.id.rate)
    EditText edittext;
    @Bind(R.id.tv_like1)
    TextView Like;
    @Bind(R.id.tv_like2)
    TextView tvLike;
    @Bind(R.id.point)
    TextView points;
    @Bind(R.id.exchange)
    Button exchange;
    private int qqRate;
    private int qqNum;
    private String et;
    private String point;
    private LoadingDialog mLoadDialog;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_exchange_like);
    }

    @Override
    public void initData() {
        initCustomActionBar();
        exchange.setEnabled(false);
        exchange.setAlpha(0.4f);
        qqNum = SharePreferenceUtils.getValueFromSP(aty, "qqNum", 0);
        qqRate = SharePreferenceUtils.getValueFromSP(aty, "qqRate", 0);
        point = SharePreferenceUtils.getValueFromSP(aty, "point", "");
        points.setText("/" + point);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et = edittext.getText().toString().trim();
                if (et != null && et.length() > 0&&Integer.parseInt(et)>Integer.parseInt(point)){
                    ToastUtil.showToast(aty,"超出可兑换积分数!");
                    edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(point.length()+1)});
                }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (et != null && et.length() > 0&&Integer.parseInt(et)<=Integer.parseInt(point)) {
                                exchange.setEnabled(true);
                                exchange.setAlpha(1.0f);
                                final int num = Integer.parseInt(et);

                            Like.setText("" + num / qqRate);
                            tvLike.setVisibility(View.VISIBLE);
                            }else{
                                Like.setText("");
                                tvLike.setVisibility(View.GONE);
                                exchange.setEnabled(false);
                                exchange.setAlpha(0.4f);
                            }
                        }
                    });
            }
        });


    }

    @Override
    public void initListener() {

        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", 1);
                    jsonObject.put("points", et);

                    RequestApi.jsonPost(aty, ConstantNetUrl.SUBMITEXCHANGE, jsonObject, new AsyncHttpResponseHandler() {


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
                                ResultBean resultbean = new Gson().fromJson(response, ResultBean.class);

                                    Log.e("111", "111" + resultbean.success);
                                if (resultbean.success.equals("0")) {
                                    ToastUtil.showToast(aty, resultbean.getErrorMsg());
                                    return;
                                }
                                    MyinfoActivity.onResumeRefresh =true;
                                    showMessageDialog("恭喜你,本次兑换"+et+"积分成功,系统审核后将第一时间与您确认!");
                                int p = Integer.parseInt(point)- Integer.parseInt(et);
                                point = String.valueOf(p);
                                points.setText("/"+point);


                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public void showMessageDialog(String message) {
        Dialog dialog = null;
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(aty);
        customBuilder.setTitle("兑换成功");
        customBuilder.setMessage(message);
        customBuilder.setGravity(Gravity.CENTER);
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

    private void initCustomActionBar() {
        CommonActionBar commonActionBar =
                new CommonActionBar(aty,
                        new CommonActionBar.IActionBarListener() {
                            @Override
                            public void onBtnRight(View v) {
                            }

                            @Override
                            public void onBtnLeft(View v) {
                                finish();
                            }

                            @Override
                            public void setTitle(TextView v) {
                                v.setText("兑换手机赞");
                                v.setTextColor(getResources().getColor(R.color.black_text3));
                            }
                        });
        TextView tvTitle
                = commonActionBar.getTitleView();
        commonActionBar.setImgLeftViewVisibility(View.VISIBLE);
        commonActionBar.setimgRightViewVisibility(View.GONE);
    }
}
