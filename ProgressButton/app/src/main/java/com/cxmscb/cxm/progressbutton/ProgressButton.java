package com.cxmscb.cxm.progressbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by cxm on 2016/9/7.
 */
public class ProgressButton extends Button {


    private int mProgress; //当前进度
    private int mMaxProgress = 100; //最大进度：默认为100
    private int mMinProgress = 0;//最小进度：默认为0
    private GradientDrawable mProgressDrawable;// 加载进度时的进度颜色
    private GradientDrawable mProgressDrawableBg;// 加载进度时的背景色
    private StateListDrawable mNormalDrawable; // 按钮在不同状态的颜色效果
    private boolean isShowProgress;  //是否展示进度
    private boolean isFinish; //状态是否结束
    private OnFinishListener onFinishListener; //结束时的监听
    private float cornerRadius;



    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attributeSet) {

        mNormalDrawable = new StateListDrawable();
        mProgressDrawable = (GradientDrawable)getResources().getDrawable(
                R.drawable.rect_progress).mutate();
        mProgressDrawableBg = (GradientDrawable)getResources().getDrawable(
                R.drawable.rect_progressbg).mutate();

        TypedArray attr =  context.obtainStyledAttributes(attributeSet, R.styleable.progressbutton);

        try {


            float defValue = getResources().getDimension(R.dimen.corner_radius);
            cornerRadius = attr.getDimension(R.styleable.progressbutton_buttonCornerRadius, defValue);


            isShowProgress = attr.getBoolean(R.styleable.progressbutton_showProgressNum,true);


            mNormalDrawable.addState(new int[]{android.R.attr.state_pressed},
                    getPressedDrawable(attr));
            mNormalDrawable.addState(new int[] { }, getNormalDrawable(attr));




            int defaultProgressColor = getResources().getColor(R.color.purple_progress);
            int progressColor = attr.getColor(R.styleable.progressbutton_progressColor,defaultProgressColor);
            mProgressDrawable.setColor(progressColor);


            int defaultProgressBgColor = getResources().getColor(R.color.progress_bg);
            int progressBgColor = attr.getColor(R.styleable.progressbutton_progressBgColor,defaultProgressBgColor);
            mProgressDrawableBg.setColor(progressBgColor);



        } finally {
            attr.recycle();
        }

        isFinish = false;


        mProgressDrawable.setCornerRadius(cornerRadius);
        mProgressDrawableBg.setCornerRadius(cornerRadius);
        setBackgroundCompat(mNormalDrawable);
    }



    @Override
    protected void onDraw(Canvas canvas) {

        if (mProgress > mMinProgress && mProgress <= mMaxProgress && !isFinish) {

            float scale = (float) getProgress() / (float) mMaxProgress;
            float indicatorWidth = (float) getMeasuredWidth() * scale;

            mProgressDrawable.setBounds(0, 0, (int) indicatorWidth, getMeasuredHeight());

            mProgressDrawable.draw(canvas);

            if(mProgress==mMaxProgress) {
                setBackgroundCompat(mProgressDrawable);
                isFinish = true;
                if(onFinishListener!=null) {
                    onFinishListener.onFinish();
                }

            }

        }

        super.onDraw(canvas);
    }

    public void setProgress(int progress) {

        if(!isFinish){
            mProgress = progress;
            if(isShowProgress) setText(mProgress + " %");
            // 设置背景
            setBackgroundCompat(mProgressDrawableBg);
            invalidate();
        }

    }



    public int getProgress() {
        return mProgress;
    }

    private void setBackgroundCompat(Drawable drawable) {
        int pL = getPaddingLeft();
        int pT = getPaddingTop();
        int pR = getPaddingRight();
        int pB = getPaddingBottom();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
        setPadding(pL, pT, pR, pB);
    }



    public void initState(){
        setBackgroundCompat(mNormalDrawable);
        isFinish = false;
        mProgress = 0;
    }



    private Drawable getNormalDrawable( TypedArray attr) {

        GradientDrawable drawableNormal =
                (GradientDrawable) getResources().getDrawable(R.drawable.rect_normal).mutate();// 修改时就不会影响其它drawable对象的状态
        drawableNormal.setCornerRadius(cornerRadius); // 设置圆角半径

        int defaultNormal =  getResources().getColor(R.color.blue_normal);
        int colorNormal =  attr.getColor(R.styleable.progressbutton_buttonNormalColor,defaultNormal);
        drawableNormal.setColor(colorNormal);//设置颜色

        return drawableNormal;
    }

    private Drawable getPressedDrawable( TypedArray attr) {
        GradientDrawable drawablePressed =
                (GradientDrawable) getResources().getDrawable(R.drawable.rect_pressed).mutate();// 修改时就不会影响其它drawable对象的状态
        drawablePressed.setCornerRadius(cornerRadius);// 设置圆角半径

        int defaultPressed = getResources().getColor(R.color.blue_pressed);
        int colorPressed = attr.getColor(R.styleable.progressbutton_buttonPressedColor,defaultPressed);
        drawablePressed.setColor(colorPressed);//设置颜色

        return drawablePressed;
    }

    interface OnFinishListener{

        abstract void onFinish();

    }

    public void setOnFinishListener(OnFinishListener onFinishListener){
        this.onFinishListener = onFinishListener;
    }

    public void isShowProgressNum(boolean b){
        this.isShowProgress = b;
    }



}
