package com.ezworking.my_android.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sxj on 2017/5/13.
 */
public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();

    protected List<Activity> activityStack = new LinkedList<Activity>();

    protected static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        Fresco.initialize(this);
    }

    public static BaseApplication getInst(){
        return application;
    }

    public int activityStackCount(){
        return activityStack.size();
    }

    public void addActivity(Activity activity){
        activityStack.add(activity);
    }

    public void remove(Activity activity){
        if(activityStack.contains(activity))
            activityStack.remove(activity);
    }

    public void restart(){
        for(Activity act:activityStack){
            act.finish();
        }
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void clear() {
        for(int i =activityStack.size()-1; i > -1; i -- ){
            Activity act = activityStack.get(i);
            act.finish();
        }
    }

    public void clearLeaveTop() {
        try {
            for(int i =activityStack.size()-1; i > -1; i -- ){
                Activity act = activityStack.get(i);
                //if(!(act instanceof MainActivity))
                    act.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        clear();
        System.exit(0);
    }
}
