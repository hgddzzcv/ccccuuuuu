package com.ezworking.wechatunlock.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ezworking.my_android.base.BaseFragment;
import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.my_android.base.view.LoadingDialog;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.adapter.NoScrollViewPagerAdapter;
import com.ezworking.wechatunlock.api.ConstantNetUrl;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.domain.NativeContact;
import com.ezworking.wechatunlock.domain.ResultBean;
import com.ezworking.wechatunlock.ui.view.NoScrollViewPager;
import com.ezworking.wechatunlock.utils.ContactInfoUtils;
import com.ezworking.wechatunlock.utils.FragmentFactory;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * Created by sxj on 2017/5/13.
 */
public class HomeFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {



    @Bind(R.id.rb_left)
    RadioButton rbLeft;

    @Bind(R.id.rb_right)
    RadioButton rbRight;

    @Bind(R.id.rg_exam)
    RadioGroup rgExam;

    @Bind(R.id.vp_examStatus)
    NoScrollViewPager viewPager;

    private LoadingDialog mLoadDialog;

    private List<Fragment> fragments = new ArrayList<>();
    private List<NativeContact> nativeContacts;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    postContacts();
                    break;
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setRootView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initData() {
        //上传联系人数据

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("333","33333333333");
                nativeContacts = ContactInfoUtils.printContacts(getActivity());
                handler.sendEmptyMessage(1);
            }
        }).start();



        fragments.add(FragmentFactory.getInstance().createFragment(0));
        fragments.add(FragmentFactory.getInstance().createFragment(1));
        viewPager.setAdapter(new NoScrollViewPagerAdapter(getActivity().getSupportFragmentManager(),fragments));
        rbLeft.setChecked(true);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(this);
    }

    private void postContacts() {
        Log.e("111","走到了");
        //List<NativeContact> allContactInfos = ContactInfoUtils.getAllContactInfos(getActivity());




        Log.e("111","nativeContacts" + nativeContacts.size());

        try {
//            String array = "[\n" +
//                    "{\n" +
//                    "\"identifier\":\"65CA64F1-E036-467D-AB7F-1AB7EBC5D2C7\",\n" +
//                    "\"name\":\"张三\",\n" +
//                    "\" company \":\"xxxxxx\",\n" +
//                    "\"phones\":[\"13xxxxxxx\", \"13xxxxxxx\",\"13xxxxxxx\"],\n" +
//                    "\"emails\":[\"xxx@cccc\", \"xxx@cccc\",\"xxx@cccc\"]\n" +
//                    "},\n" +
//                    "{\n" +
//                    "\"identifier\":\"65CA64F1-E036-467D-AB7F-1AB7EBC5D2C8\",\n" +
//                    "\"name\":\"张三\",\n" +
//                    "\" company \":\"xxxxxx\",\n" +
//                    "\"phones\":[\"13xxxxxxx\", \"13xxxxxxx\",\"13xxxxxxx\"],\n" +
//                    "\"emails\":[\"xxx@cccc\", \"xxx@cccc\",\"xxx@cccc\"]\n" +
//                    "},\n" +
//                    "{\"identifier\":\"65CA64F1-E036-467D-AB7F-1AB7EBC5D2C9\",\n" +
//                    "\"name\":\"张三\",\n" +
//                    "\" company \":\"xxxxxx\",\n" +
//                    "\"phones\":[\"13xxxxxxx\", \"13xxxxxxx\",\"13xxxxxxx\"],\n" +
//                    "\"emails\":[\"xxx@cccc\", \"xxx@cccc\",\"xxx@cccc\"]\n" +
//                    "}\n" +
//                    "]";
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();


            for(NativeContact contact : nativeContacts){

                if(contact.getName() !=null || contact.getPhones() != null){
                    Log.e("111",contact.getName() + "---" + contact.getEmails() + "---"
                            + contact.getCompany()  + "---" + contact.getPhones());
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("identifier",contact.getIdentifier());
                    jsonObject1.put("name",contact.getName());
                    jsonObject1.put("company",contact.getCompany());
                    List<String> phones = contact.getPhones();
                    JSONArray jsonArray1 = new JSONArray();
                    for(String phone : phones){
                        if(!TextUtils.isEmpty(phone)){
                            jsonArray1.put(phone);
                        }
                    }
                    jsonObject1.put("phones",jsonArray1);


                    List<String> emails = contact.getEmails();
                    JSONArray jsonArray2 = new JSONArray();
                    for(String email : emails){
                        if(!TextUtils.isEmpty(email)){
                            jsonArray2.put(email);
                        }
                    }
                    jsonObject1.put("emails",jsonArray2);

                    if(jsonObject1 != null){
                        jsonArray.put(jsonObject1);
                    }

                }
            }
            jsonObject.put("contacts",jsonArray);

            RequestApi.jsonPost(getActivity(), ConstantNetUrl.UPLOADCONTACTS, jsonObject, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    //showLoading("");
                }

                @Override
                public void onFinish() {
                    //dismissLoading();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "utf-8");
                        ResultBean resultBean = new Gson().fromJson(response,ResultBean.class);
                        if (resultBean.success.equals("0")) {
                            Log.e("111","111" +resultBean.getErrorMsg());
                            ToastUtil.showToast(getActivity(), resultBean.getErrorMsg());
                            return;
                        }
                        if (resultBean !=null){
                            //showLoading("上传成功");
                            Log.e("111","resultBean" +resultBean.success );
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AsyncHttpClientUtil.onFailure(getActivity(), statusCode);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void showLoading(String msg) {
        mLoadDialog = new LoadingDialog(msg);
        mLoadDialog.show(getActivity().getSupportFragmentManager(), LoadingDialog.TAG);
    }

    private void dismissLoading() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
    }

    @Override
    public void initListener() {
        rgExam.setOnCheckedChangeListener(this);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){

            case R.id.rb_left:
                viewPager.setCurrentItem(0);
                break;

            case R.id.rb_right:
                viewPager.setCurrentItem(1);
                break;


        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                rbLeft.setChecked(true);
                break;
            case 1:
                rbRight.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
