package com.ezworking.wechatunlock.ui.fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ezworking.my_android.base.BaseFragment;
import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.view.LoadingDialog;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.adapter.ContactLvAdapter;
import com.ezworking.wechatunlock.api.ConstantNetUrl;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.application.AppCache;
import com.ezworking.wechatunlock.domain.ContactResult;
import com.ezworking.wechatunlock.greendao.DBManager;
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
    private static final Long ID_WECHAT_GROUP = 12345l;

    @Bind(R.id.lv_contact)
    ListView lvContact;

    @Bind(R.id.download_contact)
    Button download;


    private LoadingDialog mLoadDialog;

    private List<ContactResult> contacts = new ArrayList<>();
    private ContactLvAdapter adapter;

    @Override
    public int setRootView() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initData() {
        Log.e("token",AppCache.getInstance().getToken());
        adapter = new ContactLvAdapter(getActivity(),contacts);
        lvContact.setAdapter(adapter);
        List<ContactResult> contactResults = DBManager.getInstance(getActivity(),AppCache.getInstance().getToken()).queryUserList();
        Log.e("db",contactResults.size() + contactResults.toString());
        if(contactResults != null && contactResults.size() != 0){
            contacts.addAll(contactResults);
            adapter.notifyDataSetChanged();

        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContacs();

            }
        });
    }

    public  void getContacs(){
        final  List<ContactResult> contacts1 = new ArrayList<>();
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
                    String response = new String(responseBody, "utf-8");
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
                            String  identifier = jsonObject1.optString("identifier");
                            Log.e("111",name.toString() + phone.toString() + wechat.toString());
                            ContactResult result = new ContactResult();
                            result.name = name;
                            result.phone = phone;
                            result.wechat = wechat;
                            result.identifier = identifier;
                            contacts1.add(result);


                        long id = testWriteContact(name, phone, wechat);

                        ContentValues values1 = new ContentValues();
                        values1.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,id);
                        values1.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,ID_WECHAT_GROUP);
                        values1.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
                        getActivity().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values1);
                        //保存在本地群组
                            addContactToGroup(id,ID_WECHAT_GROUP);

                            //保存数据到数据库
                            DBManager.getInstance(getActivity(), AppCache.getInstance().getToken()).insertContact(result);

                    }
                    //contacts.clear();
                    contacts.addAll(contacts1);
                    adapter.notifyDataSetChanged();

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



    private Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
    private Uri dataUri = Uri.parse("content://com.android.contacts/data");

    public long testWriteContact(String name ,String phone ,String wechat) {
        ContentResolver resolver = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        // 先向raw_contacts表中写一个id, 自动生成
        Uri resultUri = resolver.insert(rawContactsUri, values);
        long id = ContentUris.parseId(resultUri);

        // 然后用这个id向data表中写3条数据
        values.put("raw_contact_id", id);
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data1", name);
        resolver.insert(dataUri, values);

        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data1", phone);
        resolver.insert(dataUri, values);



        values.put("mimetype", "vnd.android.cursor.item/im");
        values.put("data1", wechat);
        resolver.insert(dataUri, values);
        return id;
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
    private void addContactToGroup(long contactId,long groupId) {
        //judge whether the contact has been in the group

        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,contactId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,groupId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
        getActivity().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
//        boolean b1 = ifExistContactInGroup(contactId, groupId);
//        if (b1) {
//            //the contact has been in the group
//            return;
//        } else {
//
//        }
    }


    /**
     * 群组中是否存在该联系人
     * @param contactId
     * @param groupId
     * @returns
     */
    private boolean ifExistContactInGroup(long contactId, long groupId) {
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


    private void showLoading(String msg) {
        mLoadDialog = new LoadingDialog(msg);
    }

    private void dismissLoading() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        DBManager instance = DBManager.getInstance(getActivity(), AppCache.getInstance().getToken());
        instance = null;
    }
}
