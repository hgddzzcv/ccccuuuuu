package com.ezworking.wechatunlock.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ezworking.my_android.base.BaseFragment;
import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.view.LoadingDialog;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.api.ConstantNetUrl;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.domain.ContactResult;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;


/**
 * Created by wangchao on 2017/5/14 0014.
 */

public class ContactsFragment extends BaseFragment {

    /**
     * 群组id
     */
    private static final Long ID_WECHAT_GROUP = 123456l;

    @Bind(R.id.lv_contact)
    ListView lvContact;

    @Bind(R.id.download_contact)
    Button download;


    private LoadingDialog mLoadDialog;

    private List<ContactResult> contacts = new ArrayList<>();

    @Override
    public int setRootView() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initData() {

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContacs();

            }
        });
    }



    public  void getContacs(){
        final  List<ContactResult> contacts = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        RequestApi.jsonPost(getActivity(), ConstantNetUrl.DOWNLOADCONTACTS, jsonObject, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                showLoading("正在下载");
            }

            @Override
            public void onFinish() {
                dismissLoading();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
//                    String json = "{\"errorCode\":\"0\",\"data\":[{\n" +
//                            "\"name\":\"张三\",\n" +
//                            "\"phone\":\"13xxxxxxx\",\n" +
//                            "\"wechat\":\"sdffs\"\n" +
//                            "}]}";
                    String response = new String(responseBody, "utf-8");
                    //ContactResult contactResult = new Gson().fromJson(json, ContactResult.class);
                    JSONObject json1 = new JSONObject(response);
                    Log.e("111","json1" + json1.toString());
                    String data = json1.optString("data");
                    JSONArray array1 = new JSONArray(data);
                    Log.e("111","array1" + array1.toString());

                    //创建微信解锁群组
                    createGroup();

                    for(int i = 0 ; i < array1.length();i++){
                        JSONObject jsonObject1 = array1.getJSONObject(i);

                            String name = jsonObject1.optString("name");
                            String phone = jsonObject1.optString("phone");
                        String  wechat = jsonObject1.optString("wechat");
                            Log.e("111",name.toString() + phone.toString() + wechat.toString());
                        ContactResult result = new ContactResult();
                        result.name = name;
                        result.phone = phone;
                        result.wechat = wechat;
                        contacts.add(result);
                        //addContactToGroup(ID_WECHAT_GROUP,？);

                    }








//                    String points = json1.optString("points");
//                    String data = json1.optString("data");
//                    JSONArray array = new JSONArray(data);
//                    Log.e("111","array" + array);
//                    Log.e("111","points" + points);
//                    if (contactResult.success.equals("0")) {
//                        Log.e("111","111" +contactResult.getErrorMsg());
//                        ToastUtil.showToast(getActivity(), contactResult.getErrorMsg());
//                        return;
//                    }
//
//                    Log.e("111","contactResult" + contactResult.toString());
//
//                    if (contactResult!=null){
//                        Log.e("111","contactResult" + contactResult.data);
//                        //contacts.addAll(contactResult.data);
//                        ToastUtil.showToast(getActivity(),"下载成功");
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                AsyncHttpClientUtil.onFailure(getActivity(), statusCode);
            }
        });


    }


    /**
     * 查询联系人id
     */

    public long getContactId(){
        return 0;
    }


    /**
     * 创建微信解锁群组
     */
    private void createGroup() {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Groups.TITLE, "微信解锁");
        values.put(ContactsContract.Groups._ID, ID_WECHAT_GROUP);
        getActivity().getContentResolver().insert(ContactsContract.Groups.CONTENT_URI, values);
    }

    /**
     * 将联系人存到群组
     * @param contactId
     * @param groupId
     */
    private void addContactToGroup(int contactId,int groupId) {
        //judge whether the contact has been in the group
        boolean b1 = ifExistContactInGroup(contactId, groupId);
        if (b1) {
            //the contact has been in the group
            return;
        } else {
            ContentValues values = new ContentValues();
            values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,contactId);
            values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,groupId);
            values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
            getActivity().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        }
    }


    /**
     * 群组中是否存在该联系人
     * @param contactId
     * @param groupId
     * @return
     */
    private boolean ifExistContactInGroup(int contactId, int groupId) {
        String where = ContactsContract.Contacts.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE
                + "' AND " + ContactsContract.Contacts.Data.DATA1 + " = '" + groupId
                + "' AND " + ContactsContract.Contacts.Data.RAW_CONTACT_ID + " = '" + contactId + "'";
        Cursor markCursor = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.DISPLAY_NAME}, where, null, null);
        if (markCursor.moveToFirst()) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void initListener() {

    }

//    public  List<Contact> getContacs() {
//        List<Contact> contacts = new ArrayList<>();
//        contacts.add(new Contact("testContact0","13537766590","wechat0"));
//        contacts.add(new Contact("testContact1","13537766590","wechat1"));
//        contacts.add(new Contact("testContact2","13537766590","wechat2"));
//        contacts.add(new Contact("testContact3","13537766590","wechat3"));
//        contacts.add(new Contact("testContact4","13537766590","wechat4"));
//        contacts.add(new Contact("testContact5","13537766590","wechat5"));
//        contacts.add(new Contact("testContact6","13537766590","wechat6"));
//        contacts.add(new Contact("testContact7","13537766590","wechat7"));
//        contacts.add(new Contact("testContact8","13537766590","wechat8"));
//        return contacts;
//    }

    private void showLoading(String msg) {
        mLoadDialog = new LoadingDialog(msg);
        //mLoadDialog.show(getActivity().getFragmentManager(), LoadingDialog.TAG);
    }

    private void dismissLoading() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
    }
}
