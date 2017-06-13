package com.ezworking.wechatunlock.wxapi;

import android.app.Activity;
import android.app.LoaderManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dou361.statusbar.StatusBarUtil;
import com.ezworking.my_android.base.utils.LogUtil;
import com.ezworking.my_android.base.utils.PageJumps;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.my_android.base.view.LoadingDialog;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.application.Constant;
import com.ezworking.wechatunlock.application.MainApplication;
import com.ezworking.wechatunlock.ui.activity.RegisterActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sxj on 2017/6/9.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private LoadingDialog mLoadDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果没回调onResp，八成是这句没有写
        MainApplication.mWxApi.handleIntent(getIntent(), this);

    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {


    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        LogUtil.e(resp.errStr);
        LogUtil.e("错误码 : " + resp.errCode + "");
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) ToastUtil.showToast(this, "分享失败");
                else ToastUtil.showToast(this, "登录失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        LogUtil.e("code = " + code);

                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        getAccess_token(code);

                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        // ToastUtil.showToast(this,"微信分享成功");
                        // finish();
                        break;
                }
                break;
        }
    }

    /*private void showLoading(String msg) {
        mLoadDialog = new LoadingDialog(msg);
        mLoadDialog.show(getFragmentManager(), LoadingDialog.TAG);
    }

    private void dismissLoading() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
    }*/

    /*
        获取openid accessToken值用于后期操作
    *
    * */
    private void getAccess_token(String code) {
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.WX_APP_ID + "&secret=" + Constant.WX_APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
        RequestApi.jsonGet(this, URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "utf-8");
                    Log.e("getAccess_token", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String openid = jsonObject.getString("openid").toString().trim();
                        String access_token = jsonObject.getString("access_token").toString().trim();
                        getUserMesg(access_token, openid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }


    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {

        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        LogUtil.e("getUserMesg：" + path);
        RequestApi.jsonGet(this, path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "utf-8");
                    Log.e("getAccess_token", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String nickname = jsonObject.getString("nickname");
                        int sex = Integer.parseInt(jsonObject.get("sex").toString());
                        String headimgurl = jsonObject.getString("headimgurl");

                        LogUtil.e("用户基本信息:");
                        LogUtil.e("nickname:" + nickname);
                        LogUtil.e("sex:" + sex);
                        LogUtil.e("headimgurl:" + headimgurl);

                        Bundle bundle = new Bundle();
                        bundle.putString("nickname", nickname);
                        bundle.putString("headimgurl", headimgurl);
                        finish();
                        PageJumps.PageJumps(WXEntryActivity.this, RegisterActivity.class, bundle);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }


}
