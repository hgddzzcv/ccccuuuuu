package com.ezworking.wechatunlock.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezworking.wechatunlock.R;


/**
 * Created by dujiande on 2016/9/21.
 * 自定义对话框
 */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public CustomDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private String title="";
		private String message;
		private Uri imagePath;
		private int gravity = -1;
		private int visibility = -1;
		private String positiveButtonText;
		private String negativeButtonText;
		private int positiveButtonTextColor = Color.parseColor("#010101");
        private int negativeButtonTextColor = Color.parseColor("#1fb8ff");
		private OnClickListener positiveButtonClickListener,
				negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public void setPositiveButtonTextColor(int positiveButtonTextColor){
			this.positiveButtonTextColor = positiveButtonTextColor;
		}

		public void setNegativeButtonTextColor(int negativeButtonTextColor){
			this.negativeButtonTextColor = negativeButtonTextColor;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setMessageIcon(Uri imagePath) {
			this.imagePath = imagePath;
			return this;
		}

		public Builder setGravity(int gravity) {
			this.gravity = gravity;
			return this;
		}

		public Builder setIconVisibility(int visibility) {
			this.visibility = visibility;
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomDialog dialog = new CustomDialog(context,
					R.style.customDialog);
			View layout = inflater.inflate(R.layout.custom_dialog, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			TextView titleTv = (TextView) layout.findViewById(R.id.dialogTitle);
			if("".equals(title)){
				titleTv.setVisibility(View.GONE);
			}else{
				titleTv.setVisibility(View.VISIBLE);
				titleTv.setText(title);
			}

			TextView rightBtn = (TextView) layout.findViewById(R.id.dialogRightBtn);
			TextView leftBtn = (TextView) layout.findViewById(R.id.dialogLeftBtn);
			View view = layout.findViewById(R.id.view);
			leftBtn.setTextColor(negativeButtonTextColor);
			rightBtn.setTextColor(positiveButtonTextColor);

			positiveButtonTextColor = Color.parseColor("#010101");
			negativeButtonTextColor = Color.parseColor("#1fb8ff");

			if (positiveButtonText != null) {
				rightBtn.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					rightBtn.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				rightBtn.setVisibility(View.GONE);
				view.setVisibility(View.GONE);
			}
			if (negativeButtonText != null) {
				leftBtn.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					leftBtn.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				leftBtn.setVisibility(View.GONE);
				view.setVisibility(View.GONE);
			}
			if (imagePath == null&&message != null) {
				((ImageView) layout.findViewById(R.id.dialogIcon))
						.setVisibility(View.GONE);
				((TextView) layout.findViewById(R.id.dialogContent))
						.setVisibility(View.VISIBLE);
				((TextView) layout.findViewById(R.id.dialogContent))
						.setText(message);
			} else if (imagePath != null&&message != null) {
				((ImageView) layout.findViewById(R.id.dialogIcon))
						.setVisibility(View.VISIBLE);
				((TextView) layout.findViewById(R.id.dialogContent))
						.setVisibility(View.VISIBLE);
				((ImageView) layout.findViewById(R.id.dialogIcon))
						.setImageURI(imagePath);
				LayoutParams layoutParams = ((ImageView) layout
						.findViewById(R.id.dialogIcon)).getLayoutParams();
				layoutParams.height = LayoutParams.WRAP_CONTENT;
				((ImageView) layout
						.findViewById(R.id.dialogIcon)).setLayoutParams(layoutParams);

				((LinearLayout) layout.findViewById(R.id.dialogText))
						.setGravity(Gravity.CENTER);
			}
			if (gravity != -1) {
				((LinearLayout) layout.findViewById(R.id.dialogText))
						.setGravity(gravity);
			}
			if (visibility != -1) {
				((ImageView) layout.findViewById(R.id.dialogIcon))
						.setVisibility(View.VISIBLE);
				LayoutParams layoutParams = ((ImageView) layout
						.findViewById(R.id.dialogIcon)).getLayoutParams();
				layoutParams.width = LayoutParams.WRAP_CONTENT;
				layoutParams.height = LayoutParams.WRAP_CONTENT;
				((ImageView) layout
						.findViewById(R.id.dialogIcon)).setLayoutParams(layoutParams);
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}
}