package com.dzw.inputcode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * 自定义文本输入框，增加清空按钮
 */
public class InputCodeView extends EditText implements TextWatcher {

    private Paint paint;//绘制方框
    private Paint textPaint;//绘制字体
    private float bgCenterY;
    private OnCodeCompleteListener onCodeCompleteListener;
    /**
     * 输入框的宽高
     */
    private int tvWidthSize = dip2px(40);
    /**
     * 文本的长度
     */
    private int mTextLen = 6;
    /**
     * 是否为密码输入框
     */
    private boolean isPassWord = true;//是否为密码输入框
    /**
     * 验证码间隔
     */
    private int intervalSize = dip2px(0);
    /**
     * 文字大小
     */
    private int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
    /**
     * 圆角大小
     */
    private int radius = dip2px(0);
    /**
     * 文字颜色
     */
    private int mTextColor = Color.BLACK;
    /**
     * 边框颜色
     */
    private int mBorderColor = Color.GRAY;

    /**
     * 边框颜色
     */
    private int mFocusBorderColor = -1;

    /**
     * 边框样式
     * -1表示自定义图片
     */
    private int mStyle = 0;

    /**
     * 自定义密码图片选中
     */
    private int mSelect = R.mipmap.input_selelct;

    /**
     * 自定义密码图片未选中选中
     */
    private int mUnSelect = R.mipmap.input_unselect;
    /**
     * 设置paint宽度
     */
    private int mStrokeWidth;


    public InputCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public InputCodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.InputCodeView);
        intervalSize = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_tvMargin, intervalSize);
        radius = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_tvRadius, radius);
        textSize = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_tvTextSize, textSize);
        tvWidthSize = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_tvWidth, tvWidthSize);
        mTextLen = typedArray.getInt(R.styleable.InputCodeView_tvLen, mTextLen);
        isPassWord = typedArray.getBoolean(R.styleable.InputCodeView_tvIsPwd, isPassWord);
        mTextColor = typedArray.getColor(R.styleable.InputCodeView_tvTextColor, mTextColor);
        mBorderColor = typedArray.getColor(R.styleable.InputCodeView_tvBorderColor, mBorderColor);
        mFocusBorderColor = typedArray.getColor(R.styleable.InputCodeView_tvFocusBorderColor, mFocusBorderColor);
        mStyle = typedArray.getInt(R.styleable.InputCodeView_tvStyle, mStyle);
        mSelect = typedArray.getResourceId(R.styleable.InputCodeView_tvCustomSelectIcon, mSelect);
        mUnSelect = typedArray.getResourceId(R.styleable.InputCodeView_tvUnCustomSelectIcon, mUnSelect);
        mStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.InputCodeView_tvStrokeWidth, dip2px(1));
        typedArray.recycle();
        setBackgroundColor(Color.WHITE);
        // 增加文本监听器.
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setColor(mBorderColor);

        // 增加文本监听器.
        textPaint = new Paint();
        textPaint.setColor(mTextColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        setCursorVisible(false);
        setTextSize(0);
        disableCopyAndPaste(this);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTextLen)});
        addTextChangedListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        float bgWidth;
        bgWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        float bgHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            bgHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        } else {
            bgHeight = tvWidthSize + dip2px(2);
        }
        bgCenterY = bgHeight / 2;
        setMeasuredDimension((int) bgWidth, (int) bgHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStyle == 0) {
            drawDefaultRect(mTextLen, canvas);
        } else if (mStyle == 1) {
            drawStatEndRadiusRect(mTextLen, canvas);
        } else if (mStyle == 2) {
            drawWeChatRect(mTextLen, canvas);
        } else if (mStyle == 3) {
            drawLineRect(mTextLen, canvas);
        } else if (mStyle == -1) {
            drawBitmap(mTextLen, canvas);
        }
        drawText(mTextLen, canvas);
    }


    /**
     * 绘制前后圆角输入框不支持焦点输入框
     */
    private void drawStatEndRadiusRect(int count, Canvas canvas) {
        if (radius == 0) {
            radius = dip2px(5);
        }
        intervalSize = 0;
        int left = (getWidth() - count * (tvWidthSize + intervalSize)) / 2;
        for (int i = 1; i < count - 1; i++) {
            RectF rectF = new RectF(left + (tvWidthSize + intervalSize) * i,
                    bgCenterY - tvWidthSize / 2f, left + (tvWidthSize + intervalSize) * i + tvWidthSize, bgCenterY + tvWidthSize / 2f);
            canvas.drawRoundRect(rectF, 0, 0, paint);
        }
        RectF rectF = new RectF(left, bgCenterY - tvWidthSize / 2f, left + (tvWidthSize + intervalSize) * (count - 1) + tvWidthSize, bgCenterY + tvWidthSize / 2f);
        canvas.drawRoundRect(rectF, radius, radius, paint);
    }

    /**
     * 绘制输入框
     */
    private void drawDefaultRect(int count, Canvas canvas) {
        int left = (getWidth() - count * (tvWidthSize + intervalSize)) / 2;
        for (int i = 0; i < count; i++) {
            setFocusColor(i);
            RectF rectF = new RectF(left + (tvWidthSize + intervalSize) * i,
                    bgCenterY - tvWidthSize / 2f, left + (tvWidthSize + intervalSize) * i + tvWidthSize, bgCenterY + tvWidthSize / 2f);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
    }


    /**
     * 仿安卓最新支付输入框
     */
    private void drawWeChatRect(int count, Canvas canvas) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int left = (getWidth() - count * (tvWidthSize + intervalSize)) / 2;
        for (int i = 0; i < count; i++) {
            setFocusColor(i);
            RectF rectF = new RectF(left + (tvWidthSize + intervalSize) * i,
                    bgCenterY - tvWidthSize / 2f, left + (tvWidthSize + intervalSize) * i + tvWidthSize, bgCenterY + tvWidthSize / 2f);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
    }


    /**
     * 绘制输入框
     */
    private void drawLineRect(int count, Canvas canvas) {
        if (intervalSize == 0) {
            intervalSize = dip2px(5);
        }
        paint.setStrokeWidth(dip2px(2));
        int left = (getWidth() - count * (tvWidthSize + intervalSize)) / 2;
        for (int i = 0; i < count; i++) {
            setFocusColor(i);
            canvas.drawLine(left + (tvWidthSize + intervalSize) * i, bgCenterY + tvWidthSize / 2f, left + (tvWidthSize + intervalSize) * i + tvWidthSize, bgCenterY + tvWidthSize / 2f, paint);
        }
    }

    /**
     * 绘制图片
     */
    private void drawBitmap(int count, Canvas canvas) {
        if (intervalSize == 0) {
            intervalSize = dip2px(5);
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mUnSelect);
        Bitmap focusBitmap = BitmapFactory.decodeResource(getResources(), mSelect);
        tvWidthSize = bitmap.getWidth();
        int left = (getWidth() - count * (tvWidthSize + intervalSize)) / 2;
        for (int i = 0; i < count; i++) {
            if (getText().length() > i) {
                canvas.drawBitmap(focusBitmap, left + (tvWidthSize + intervalSize) * i + tvWidthSize / 2f - bitmap.getWidth() / 2f, bgCenterY - bitmap.getHeight() / 2f, new Paint(Paint.ANTI_ALIAS_FLAG));
            } else {
                canvas.drawBitmap(bitmap, left + (tvWidthSize + intervalSize) * i + tvWidthSize / 2f - bitmap.getWidth() / 2f, bgCenterY - bitmap.getHeight() / 2f, new Paint(Paint.ANTI_ALIAS_FLAG));
            }
        }
        if (bitmap.isRecycled()) {
            bitmap.recycle();
        }
        if (focusBitmap.isRecycled()) {
            focusBitmap.recycle();
        }
    }


    /**
     * 绘制文字
     */
    private void drawText(int count, Canvas canvas) {
        if (mStyle == -1) {
            return;
        }
        int left = (getWidth() - count * (tvWidthSize + intervalSize)) / 2;
        for (int i = 0; i < length(); i++) {
            String text = getText().toString().substring(i, i + 1);
            if (isPassWord) {
                text = "●";
            }
            float textWidth = textPaint.measureText(text);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float y = bgCenterY + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            canvas.drawText(text, left + (tvWidthSize + intervalSize) * i + tvWidthSize / 2f - textWidth / 2, y, textPaint);
        }
    }


    /**
     * 设置焦点颜色
     */
    private void setFocusColor(int i) {
        if (getText().length() == i && mFocusBorderColor != -1) {
            paint.setColor(mFocusBorderColor);
        } else {
            paint.setColor(mBorderColor);
        }
    }


    /**
     * 添加密碼
     */
    public void addText(String text) {
        if (getText().length() < mTextLen) {
            append(text);
//            setText(getText().toString() + text);
        }
    }

    /**
     * 每次删除一个
     */
    public void removeText() {
        if (getText().length() > 0) {
            setText(getText().toString().substring(0, getText().length() - 1));
        }
    }


    /**
     * 把密度转换为像素
     */
    private int dip2px(float px) {
        final float scale = getScreenDensity();
        return (int) (px * scale + 0.5);
    }

    /**
     * 得到设备的密度
     */
    private float getScreenDensity() {
        return getResources().getDisplayMetrics().density;
    }

    /**
     * 禁止输入框复制粘贴菜单
     */
    public void disableCopyAndPaste(final EditText editText) {
        try {
            if (editText == null) {
                return;
            }

            editText.setOnLongClickListener(v -> true);
            editText.setLongClickable(false);

            editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        invalidate();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (onCodeCompleteListener != null) {
            if (getText().length() == mTextLen) {
                onCodeCompleteListener.inputCodeComplete(getText().toString());
            } else {
                onCodeCompleteListener.inputCodeInput(getText().toString());
            }
        }
    }


    /**
     * 输入完成回调接口
     */
    public interface OnCodeCompleteListener {
        //完成输入
        void inputCodeComplete(String verificationCode);

        //未完成输入
        void inputCodeInput(String verificationCode);
    }


    public void setOnCodeCompleteListener(OnCodeCompleteListener onCodeCompleteListener) {
        this.onCodeCompleteListener = onCodeCompleteListener;
    }
}