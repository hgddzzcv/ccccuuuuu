package com.ezworking.wechatunlock.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.domain.OrderResult;

import java.util.List;

/**
 * Created by wangchao on 2017/5/14 0014.
 */

public class OrderLvAdapter<T> extends BaseAdapter {

    private static final String STATE_LOST_EFFORT = "-1";

    private static final String STATE_EFFORT = "0";

    private static final String STATE_ACCEPT = "1";

    private static final String STATE_REFUSE = "2";

    private Context context;

    private  List<OrderResult.Order> orders;

    public OrderLvAdapter(Context context , List<OrderResult.Order> orders) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_order,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.orderNumber.setText(" " + orders.get(position).oID);
        holder.orderTiem.setText(" " +  orders.get(position).createTime);
        holder.helpPhone.setText(" " + orders.get(position).phone +" "+ context.getResources().getString(R.string.lock));

        switch (orders.get(position).state){
            case STATE_LOST_EFFORT:
                holder.stateButton.setBackgroundResource(R.drawable.btn_default_bg);
                holder.stateButton.setText(context.getResources().getString(R.string.lose_effort));
                holder.stateButton.setClickable(false);
                break;

            case STATE_EFFORT:
                holder.stateButton.setClickable(true);
                holder.stateButton.setText(context.getResources().getString(R.string.effort));
                holder.stateButton.setBackgroundResource(R.drawable.main_bottom);
                break;

            case STATE_ACCEPT:
                holder.stateButton.setClickable(false);
                holder.stateButton.setBackgroundResource(R.drawable.btn_default_bg);
                holder.stateButton.setText(context.getResources().getString(R.string.accept));
                break;

            case STATE_REFUSE:
                holder.stateButton.setClickable(false);
                holder.stateButton.setBackgroundResource(R.drawable.btn_default_bg);
                holder.stateButton.setText(context.getResources().getString(R.string.refuse));
                break;
        }
        return convertView;
    }

    class ViewHolder{

        public ViewHolder(View convertView) {
            orderNumber = (TextView) convertView.findViewById(R.id.order_number);
            orderTiem = (TextView) convertView.findViewById(R.id.order_time);
            helpPhone = (TextView) convertView.findViewById(R.id.help_phone);
            stateButton = (Button) convertView.findViewById(R.id.order_state);
        }

        TextView orderNumber;

        TextView orderTiem;

        TextView helpPhone;

        Button stateButton;


    }
}
