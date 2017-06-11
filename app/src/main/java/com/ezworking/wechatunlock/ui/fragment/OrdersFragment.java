package com.ezworking.wechatunlock.ui.fragment;

import android.util.Log;
import android.widget.ListView;

import com.ezworking.my_android.base.BaseFragment;
import com.ezworking.my_android.base.utils.AsyncHttpClientUtil;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.my_android.base.view.LoadingDialog;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.adapter.OrderLvAdapter;
import com.ezworking.wechatunlock.api.ConstantNetUrl;
import com.ezworking.wechatunlock.api.RequestApi;
import com.ezworking.wechatunlock.domain.OrderResult;
import com.ezworking.wechatunlock.ui.view.PullToRefreshView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * Created by wangchao on 2017/5/14 0014.
 */
public class OrdersFragment extends BaseFragment {


    @Bind(R.id.pull_to_refresh)
    PullToRefreshView refreshView;

    private LoadingDialog mLoadDialog;

    private List<OrderResult.Order> orders = new ArrayList<>();

    @Bind(R.id.orderListView)
    ListView orderListView;
    private OrderLvAdapter<Object> adapter;

    @Override
    public int setRootView() {
        return R.layout.fragment_order;
    }

    @Override
    public void initData() {

        refreshView.setEnablePullTorefresh(true);
        refreshView.setEnablePullLoadMoreDataStatus(true);
        getOrders();
        adapter = new OrderLvAdapter<>(getActivity(),orders);
        if(orders != null && orders.size() != 0){
            orderListView.setAdapter(adapter);

        }

        refreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                getOrders();
            }
        });


    }


    public void getOrders(){
        JSONObject jsonObject = new JSONObject();
        RequestApi.jsonPost(getActivity(), ConstantNetUrl.GETMYORDERLIST, jsonObject, new AsyncHttpResponseHandler() {

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
                    OrderResult orderResult = new Gson().fromJson(response, OrderResult.class);
                    if (orderResult.success.equals("0")) {
                        Log.e("111","111" +orderResult.getErrorMsg());
                        ToastUtil.showToast(getActivity(), orderResult.getErrorMsg());
                        return;
                    }

                    if (orderResult!=null){
                        Log.e("111","111" +response);
                        orders.addAll(orderResult.data);
                        adapter.notifyDataSetChanged();
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

    };

    @Override
    public void initListener() {

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
}
