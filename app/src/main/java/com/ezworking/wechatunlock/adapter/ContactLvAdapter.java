package com.ezworking.wechatunlock.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.domain.ContactResult;

import java.util.List;

/**
 * Created by wangchao on 2017/5/14 0014.
 */

public class ContactLvAdapter<T> extends BaseAdapter {

    private Context context;

    private  List<ContactResult> contacts;

    public ContactLvAdapter(Context context , List<ContactResult> contacts) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_contact,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.contactName.setText(contacts.get(position).name);
        holder.contactPhone.setText("( " +contacts.get(position).phone + " )");
        holder.contactWechat.setText(contacts.get(position).wechat);
        return convertView;
    }

    class ViewHolder{

        public ViewHolder(View convertView) {
            contactName = (TextView) convertView.findViewById(R.id.contact_name);
            contactPhone = (TextView) convertView.findViewById(R.id.contact_phone);
            contactWechat = (TextView) convertView.findViewById(R.id.contact_wechat);
        }

        TextView contactName;

        TextView contactPhone;

        TextView contactWechat;


    }
}
