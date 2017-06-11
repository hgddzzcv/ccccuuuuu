package com.ezworking.wechatunlock.ui.activity;

import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.ezworking.my_android.base.BaseApplication;
import com.ezworking.my_android.base.utils.CommonActionBar;
import com.ezworking.my_android.base.utils.PageJumps;
import com.ezworking.my_android.base.utils.ToastUtil;
import com.ezworking.wechatunlock.R;
import com.ezworking.wechatunlock.application.AppCache;
import com.ezworking.wechatunlock.domain.UserInfoBean;
import com.ezworking.wechatunlock.ui.AppBaseActivity;
import com.ezworking.wechatunlock.ui.fragment.HomeFragment;

import java.util.List;

public class MainActivity extends AppBaseActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView tv_tile = null;
    RelativeLayout rl_title = null;

    private List<Fragment> fragmentList = null;
    private long exitTime = 0;



    @Override
    public void setRootView() {
        setContentView(R.layout.activity_main);

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"EiFpiWImzUijXEXKmWGiFCcI");
    }



    @Override
    public void initData() {
        //PermisionController.getInstance().requestPermission(this);
        initCustomActionBar();
  //      checkVersion();

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_container,new HomeFragment());
        fragmentTransaction.commit();


    }

    @Override
    public void initListener() {

    }

    private void initCustomActionBar() {
        CommonActionBar commonActionBar =
                new CommonActionBar(aty,
                        new CommonActionBar.IActionBarListener() {
                            @Override
                            public void onBtnRight(View v) {

                               PageJumps.PageJumps(aty, MyinfoActivity.class, null);
    }

                            @Override
                            public void onBtnLeft(View v) {
                             //   PageJumps.PageJumps(aty, MyDetailActivity.class, null);
                            }

                            @Override
                            public void setTitle(TextView v) {
                                // TODO Auto-generated method stub


                                v.setText("首页");
                                v.setTextColor(getResources().getColor(R.color.black_text3));

                            }
                        });
        tv_tile = commonActionBar.getTitleView();
        commonActionBar.setImgLeftViewVisibility(View.VISIBLE);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        UserInfoBean mUserInfo = AppCache.getInstance().getUserInfo();
        //2017.5.10  avatarSdv.setImageURI(Uri.parse(mUserInfo.getuAvatar()));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showToast(aty, "再按一次退出洗涤学院");

                exitTime = System.currentTimeMillis();
            } else {

                Log.e("exit","aaaaaaaaaaaaaaa");
               // finish();
                Process.killProcess(Process.myPid());
              //  System.exit(0);
                BaseApplication.getInst().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
