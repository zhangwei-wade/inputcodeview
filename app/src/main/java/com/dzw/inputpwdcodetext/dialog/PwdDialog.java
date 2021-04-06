package com.dzw.inputpwdcodetext.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.dzw.inputcode.InputCodeEditText;
import com.dzw.inputpwdcodetext.R;

import java.util.Random;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * @author zhangwei on 2021/4/6.
 */
public class PwdDialog extends Dialog {

    private String[] pwdKeyNum = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "back"};
    /**
     * 键盘是否随机
     */
    private boolean isNumRand = false;

    public PwdDialog(@NonNull Context context) {
        this(context, 0);

    }

    public PwdDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.InputDialog);
    }


    private InputCodeEditText mCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_dialog);
        mCodeText = findViewById(R.id.code_pwd_view);
        mCodeText.setEnabled(false);
        findViewById(R.id.cancel_iv).setOnClickListener(v -> dismiss());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        randNum();

        onBindKeyView(findViewById(R.id.key_layout), 3);
        setBottom();
    }

    /**设置键盘数字随机*/
    private void randNum() {
        if (!isNumRand) {
            return;
        }
        for (int i = 0; i < pwdKeyNum.length; i++) {
            int rand = new Random().nextInt(11);
            if (i == 9 || i == 11 || rand == 9 || rand == 11) {
                continue;
            }
            String temp = pwdKeyNum[i];
            pwdKeyNum[i] = pwdKeyNum[rand];
            pwdKeyNum[rand] = temp;
        }
    }

    public PwdDialog setNumRand(boolean isNumRand) {
        this.isNumRand = isNumRand;
        return this;
    }

    /*现在在底层*/
    private void setBottom() {
        //1、使用Dialog、设置style
        //2、设置布局
        //设置弹出位置
        getWindow().setGravity(BOTTOM);
        //设置对话框大小
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        //设置弹出动画
        getWindow().setWindowAnimations(R.style.view_slide_anim);
    }


    /**
     * 城市键盘绑定数据
     */
    private void onBindKeyView(LinearLayout contentView, int num) {
        for (int index = 0; index < pwdKeyNum.length; index++) {
            String value = pwdKeyNum[index];
            if (index % num == 0) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                int height = dip2px(50f);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(CENTER_HORIZONTAL);
                linearLayout.setBackgroundColor(Color.parseColor("#DDDDDD"));
                linearLayout.setLayoutParams(params);
                contentView.addView(linearLayout);
            }
            if (contentView.getChildAt(contentView.getChildCount() - 1) instanceof LinearLayout) {
                TextView customTv = new TextView(getContext());
                customTv.setTextSize(24f);
                customTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                customTv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                if (value == "back") {
                    customTv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pwd_view_delete_bg));
                    customTv.setTag(value);
                } else if (value.isEmpty()) {
                    customTv.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    customTv.setText(value);
                }
                customTv.setGravity(CENTER);
                customTv.setTextColor(Color.parseColor("#333333"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                params.topMargin = 1;
                if (((index + 1) % num) != 0) {
                    params.rightMargin = 1;
                }
                customTv.setLayoutParams(params);
                customTv.setOnClickListener(tv -> {
                            if (!customTv.getText().toString().isEmpty()) {
                                mCodeText.addText(customTv.getText().toString());
                            }
                            if (customTv.getTag() == "back") {
                                mCodeText.removeText();
                            }
                        }
                );
                ((LinearLayout) contentView.getChildAt(contentView.getChildCount() - 1)).addView(customTv);
            }
        }

    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @return
     */

    private int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
