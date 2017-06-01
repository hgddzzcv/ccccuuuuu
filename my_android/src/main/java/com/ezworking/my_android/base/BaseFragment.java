package com.ezworking.my_android.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by sxj on 2017/5/13.
 */
public abstract class BaseFragment extends Fragment {

    public  String TAG = getClass().getSimpleName();

    public Context context = null;
    View convertView = null;
    private Boolean hasInitData = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(convertView == null){
            convertView = inflater.inflate(setRootView(), container,false);
        }
        return convertView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        if(!hasInitData ){
            initData();
            initListener();
            hasInitData = true;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        ((ViewGroup) convertView.getParent()).removeView(convertView);
    }

    public abstract int setRootView();

    public abstract void initData();

    public abstract  void initListener();

}
