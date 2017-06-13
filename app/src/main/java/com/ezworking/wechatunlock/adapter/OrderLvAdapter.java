package com.ezworking.wechatunlock.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.api.ConstantNetUrl;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.domain.OrderResult;
import com.ezworking.wechatunlock.domain.ResultBean;
import com.ezworking.wechatunlock.ui.view.CustomDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by wangchao on 2017/5/14 0014.
 */

public class OrderLvAdapter<T> extends BaseAdapter implements View.OnClickListener {

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

        holder.position = position;
        holder.stateButton.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        View convertView = (View) v.getParent();
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final int position = holder.position;
        final String oID = orders.get(position).oID;
        final String phone = orders.get(position).phone;
        final String points = orders.get(position).points;
        switch (v.getId()){
            case R.id.order_state:
               /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("解锁订单");
                builder.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestState(oID,"0",position);
                    }
                });
                builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestState(oID,"1",position);

                    }
                });
                builder.show();
                holder.stateButton.setClickable(false);*/



                Dialog dialog = null;
                CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
                customBuilder.setTitle("订单接受");
                customBuilder.setMessage("您即将接受订单号为: " +oID+", 帮手机号为"+phone+"的微信号解锁,成功确定之后将获得"+points+"积分");
                customBuilder.setGravity(Gravity.CENTER);
                customBuilder.setPositiveButtonTextColor(context.getResources().getColor(R.color.main_bottom_btn_press1));
                customBuilder.setNegativeButtonTextColor(context.getResources().getColor(R.color.dialog_gray1));
                customBuilder.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestState(oID,"0",position);
                        dialogInterface.dismiss();
                    }
                });
                customBuilder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestState(oID,"1",position);
                        dialog.dismiss();
                    }
                });
                dialog = customBuilder.create();
                dialog.show();
                holder.stateButton.setClickable(false);
                notifyDataSetChanged();
                break;
        }
    }


    /**
     *
     * @param oID
     * @param type  0 - 解锁 1-拒绝
     */
    public void requestState(String oID, final String type, final int position){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oID",oID);
            jsonObject.put("type",type);
            RequestApi.jsonPost(context, ConstantNetUrl.UNLOCKPHONE, jsonObject, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody, "utf-8");
                        ResultBean resultBean = new Gson().fromJson(response, ResultBean.class);
                        if (resultBean.success.equals("0")) {
                            Log.e("111","111" +resultBean.getErrorMsg());
                            ToastUtil.showToast(context, resultBean.getErrorMsg());
                            return;
                        }

                        if (resultBean!=null){
                            if("0".equals(type)){
                                orders.get(position).state = "1";
                            }else if("1".equals(type)){
                                orders.get(position).state = "2";
                            }
                        }
                        notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AsyncHttpClientUtil.onFailure(context, statusCode);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    };

    class ViewHolder{



        public ViewHolder(View convertView) {

            orderNumber = (TextView) convertView.findViewById(R.id.order_number);
            orderTiem = (TextView) convertView.findViewById(R.id.order_time);
            helpPhone = (TextView) convertView.findViewById(R.id.help_phone);
            stateButton = (Button) convertView.findViewById(R.id.order_state);
        }

        int position;
        TextView orderNumber;

        TextView orderTiem;

        TextView helpPhone;

        Button stateButton;


    }
}
