package com.ezworking.my_android.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by liangjiangli on 2016/5/4.
 */
public class ImageViewForClick extends ImageView {

    public ImageViewForClick(Context context) {
        super(context);
    }

    public ImageViewForClick(Context context, AttributeSet attrsr) {
        super(context, attrsr);
    }

    public ImageViewForClick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.setAlpha(0.4f);
        }else if (event.getAction() == MotionEvent.ACTION_UP) {
            this.setAlpha(1f);
        }
        return super.onTouchEvent(event);
    }

}
