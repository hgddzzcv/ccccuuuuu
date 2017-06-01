package com.ezworking.wechatunlock.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.ezworking.wechatunlock.domain.NativeContact;

import java.util.ArrayList;
import java.util.List;

public class ContactInfoUtils {
    /**
     * 读取联系人工具类
     *
     * @param context
     * @return
     */
    public static List<NativeContact> getAllContactInfos(Context context) {
        List<NativeContact> infos = new ArrayList<NativeContact>();
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"},
                null, null, null);
        while (cursor.moveToNext()) {
            //String id = cursor.getString(0);
           // System.out.println("Id:" + id);
           // if (id != null) {
                NativeContact info = new NativeContact();
                Cursor datacursor = resolver.query(datauri, new String[]{
                                "data1", "mimetype"}, "raw_contact_id=?",
                        null, null);
                List<String> phones = new ArrayList<>();
                List<String> emails = new ArrayList<>();
                while (datacursor.moveToNext()) {
                    String data1 = datacursor.getString(0);
                    String mimetype = datacursor.getString(1);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        info.setName(data1);
                    } else if ("vnd.android.cursor.item/im".equals(mimetype)) {

                    } else if ("vnd.android.cursor.item/email_v2"
                            .equals(mimetype)) {
                        emails.add(data1);
                    } else if ("vnd.android.cursor.item/phone_v2"
                            .equals(mimetype)) {
                        phones.add(data1);
                    }else if("vnd.android.cursor.item/organization".equals(mimetype)){
                        info.setCompany(data1);
                    }
                }
                info.setPhones(phones);
                info.setEmails(emails);
                datacursor.close();
                infos.add(info);
           // }
        }

        cursor.close();
        return infos;
    }


    public static List<NativeContact> printContacts(Context context) {

        List<NativeContact> infos = new ArrayList<NativeContact>();
        //生成ContentResolver对象
        ContentResolver contentResolver = context.getContentResolver();

        // 获得所有的联系人
        /*Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
         */
        //这段代码和上面代码是等价的，使用两种方式获得联系人的Uri
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/contacts"),null,null,null,null);

        // 循环遍历
        if (cursor.moveToFirst()) {



            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int displayNameColumn = cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);



            do {

                NativeContact info = new NativeContact();
                List<String> phones = new ArrayList<>();
                List<String> emils = new ArrayList<>();
                // 获得联系人的ID
                String contactId = cursor.getString(idColumn);
                // 获得联系人姓名
                String displayName = cursor.getString(displayNameColumn);

                //String company = cursor.getString(displayCompantColumn);

                String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] orgWhereParams = new String[]{contactId,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                Cursor orgCur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                        null, orgWhere, orgWhereParams, null);
                List<String> list = new ArrayList<>();
                if (orgCur.moveToFirst()) {
                    //组织名 (公司名字)
                    String company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                    list.add(company);

                }

                orgCur.close();

                int id = cursor.getColumnIndex(ContactsContract.Contacts._ID);

                // 查看联系人有多少个号码，如果没有号码，返回0
                int phoneCount = cursor
                        .getInt(cursor
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (phoneCount > 0) {
                    // 获得联系人的电话号码列表
                    Cursor phoneCursor = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=" + contactId, null, null);
                    if(phoneCursor.moveToFirst())
                    {
                        do
                        {
                            //遍历所有的联系人下面所有的电话号码
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phones.add(phoneNumber );

                        }while(phoneCursor.moveToNext());
                    }
                }


                Cursor emailCursors = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                        null, null);
                while (emailCursors.moveToNext())
                {
                    String emailAddress = emailCursors.getString(emailCursors.getColumnIndex(
                            ContactsContract.CommonDataKinds.Email.DATA));
                    //添加Email的信息

                    emils.add(emailAddress);


                }
                emailCursors.close();



                info.setName(displayName);
                if(list.size()==0||list.get(0)==null){
                    info.setCompany("");
                }else{
                info.setCompany(list.get(0));
                }
                info.setPhones( phones );
                info.setEmails(emils);
                info.setIdentifier(String.valueOf(id));
                infos.add(info);


            } while (cursor.moveToNext());
        }

        return infos;

    }

}
