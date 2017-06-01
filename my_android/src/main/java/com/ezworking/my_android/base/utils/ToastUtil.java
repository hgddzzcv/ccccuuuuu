package com.ezworking.my_android.base.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezworking.my_android.R;


/**
 * 显示Toast
 * @author jiangyuehua
 *
 */
public class ToastUtil {
	/**
	 * 显示Toast
	 * @param context
	 */
	public static void showToast(Context context, String ToastMessage){
		if(context == null){
			return;
		}
		Toast toast=new Toast(context);
		View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
		TextView title = (TextView) view.findViewById(R.id.tvToast);
		title.setText(ToastMessage);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();
	}
	public static void showToast(Context context, int ToastMessage){
		if(context == null){
			return;
		}
		Toast toast=new Toast(context);
		View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
		TextView title = (TextView) view.findViewById(R.id.tvToast);
		title.setText(ToastMessage);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();
	}
	public static void showToast(Context context, String ToastMessage, int duration){
		if(context == null){
			return;
		}
		Toast toast=new Toast(context);
		View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
		TextView title = (TextView) view.findViewById(R.id.tvToast);
		title.setText(ToastMessage);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
		toast.setDuration(duration);
		toast.setView(view);
		toast.show();
	}



}
