package com.ezworking.wechatunlock.utils;


import android.support.v4.app.Fragment;

import com.ezworking.wechatunlock.ui.fragment.ContactsFragment;
import com.ezworking.wechatunlock.ui.fragment.HomeFragment;
import com.ezworking.wechatunlock.ui.fragment.OrdersFragment;

/**
 * Created by wangchao on 2017/5/14 0014.
 */

public class FragmentFactory {
    private static FragmentFactory instance;

    private FragmentFactory(){}

    public static FragmentFactory getInstance(){
        synchronized (FragmentFactory.class){
            if(instance == null){
                instance = new FragmentFactory();
            }
        }
        return instance;
    }

    /**
     * 管理类
     * 0,1 为顶部tab
     * 3,4,5为底部tab
     * @param position
     */
    public Fragment createFragment(int position){
        Fragment fragment = null;
        switch (position){
           case 0:
               fragment = new ContactsFragment();
               break;

           case 1:
               fragment = new OrdersFragment();
               break;

           case 2:
               fragment = new HomeFragment();
               break;

           case 3:
               break;

           case 4:
               break;

           case 5:
               break;
       }
        return fragment;
    }
}





