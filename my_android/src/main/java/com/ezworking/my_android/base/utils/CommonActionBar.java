package com.ezworking.my_android.base.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezworking.my_android.R;


public class CommonActionBar implements OnClickListener {

    private Context context;
    private TextView tvTitle;
    private ImageView imgLeft;
    private TextView tvLeft;
    private ImageView imgRight;
    private TextView tvRight;
    private IActionBarListener listener;
    private TextView tvName;
    private View view;

    public CommonActionBar(Context context) {
        this.context = context;
        init();
    }

    public CommonActionBar(Context context, IActionBarListener listener) {
        this.context = context;
        this.listener = listener;
        init();
    }

    /**
     * fragment
     */
    public CommonActionBar(View view, IActionBarListener listener) {
        this.view = view;
        this.listener = listener;
        init(view);
    }


    public void init(View view) {
        if (view == null) {
            return;
        }
        tvTitle = (TextView) view.findViewById(R.id.tv_tbb_title);
        listener.setTitle(tvTitle);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        imgLeft = (ImageView) view.findViewById(R.id.img_back);
        tvLeft = (TextView) view.findViewById(R.id.tv_back);
        imgRight = (ImageView) view.findViewById(R.id.img_right);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        imgLeft.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    /**
     * activity 调用
     */
    public void init() {
        if (context == null) {
            return;
        }
        tvName = (TextView) ((Activity) context).findViewById(R.id.tv_name);
        tvTitle = (TextView) ((Activity) context).findViewById(R.id.tv_tbb_title);
        listener.setTitle(tvTitle);
        imgLeft = (ImageView) ((Activity) context).findViewById(R.id.img_back);
        tvLeft = (TextView) ((Activity) context).findViewById(R.id.tv_back);
        imgRight = (ImageView) ((Activity) context).findViewById(R.id.img_right);
        tvRight = (TextView) ((Activity) context).findViewById(R.id.tv_right);
        imgLeft.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    public TextView getTitleView() {
        return tvTitle;
    }

    public ImageView getImgLeftView() {
        return imgLeft;
    }

    public TextView getTvLeftView() {
        return tvLeft;
    }

    public ImageView getImgRightView() {
        return imgRight;
    }

    public TextView getTvRightView() {
        return tvRight;
    }

    public void setImgLeftViewVisibility(int visibilitys) {
        if (imgLeft != null) {
            imgLeft.setVisibility(visibilitys);
        }
    }

    public void setimgRightViewVisibility(int visibilitys) {
        if (imgRight != null) {
            imgRight.setVisibility(visibilitys);
        }
    }

    public void settvRightViewVisibility(int visibilitys) {
        if (tvRight != null) {
            tvRight.setVisibility(visibilitys);
        }

    }

    public void setTvName(String mess) {
        if (tvName != null) {
            tvName.setText(mess);
        }
    }

    public void settvTitleText(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }


    @Override
    public void onClick(View v) {
        if (listener != null) {
            if (v == imgLeft || v == tvLeft) {
                listener.onBtnLeft(v);
            } else if (v == imgRight || v == tvRight) {
                listener.onBtnRight(v);
            }
        }

    }

    public interface IActionBarListener {

        void setTitle(TextView v);

        void onBtnLeft(View v);

        void onBtnRight(View v);
    }
}
