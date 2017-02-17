package com.example.zhsh.gaussblur;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ===========================================
 * 作    者：zhsh
 * 版    本：1.0
 * 创建日期：2017/2/16.
 * 描    述：
 * ===========================================
 */

public class MyOptionView extends ViewGroup {

    private int  desireWidth,desireHeight;// 计算所有child view 要占用的空间
    private  String itemText;

    private  int itemTextColor = R.color.ct_cutline_gray;
    private  int itemTextSize =15;
    private  int itemImagesrc;
    private  int itemImageWidth;
    private  int itemImageHeight;
    private  int margin = 0;
    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public int getItemTextColor() {
        return itemTextColor;
    }

    public void setItemTextColor(int itemTextColor) {
        this.itemTextColor = itemTextColor;
    }

    public int getItemTextSize() {
        return itemTextSize;
    }

    public void setItemTextSize(int itemTextSize) {
        this.itemTextSize = itemTextSize;
    }

    public int getItemImagesrc() {
        return itemImagesrc;
    }

    public void setItemImagesrc(int itemImagesrc) {
        this.itemImagesrc = itemImagesrc;
    }

    public int getItemImageWidth() {
        return itemImageWidth;
    }

    public void setItemImageWidth(int itemImageWidth) {
        this.itemImageWidth = itemImageWidth;
    }

    public int getItemImageHeight() {
        return itemImageHeight;
    }

    public void setItemImageHeight(int itemImageHeight) {
        this.itemImageHeight = itemImageHeight;
    }



    public MyOptionView(Context context) {
        this(context, null);

    }

    public MyOptionView(Context context, AttributeSet attrs) {
        super(context, attrs,0);


    }
    public MyOptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    public void addInScreen(ImageView childImageV, TextView childT) {
        //childImageV.setBackgroundResource(itemImagesrc);
        childImageV.setImageResource(itemImagesrc);

        childT.setText(itemText);
        childT.setTextColor(itemTextColor);
        childT.setTextSize(itemTextSize);
        MyMarginLayoutParams marginLayoutParamstext =
                new MyMarginLayoutParams(itemImageWidth,MyMarginLayoutParams.WRAP_CONTENT);
        childT.setLayoutParams(marginLayoutParamstext);
        childT.setGravity(Gravity.CENTER);
        MyMarginLayoutParams marginLayoutParams =
                new MyMarginLayoutParams(itemImageWidth,itemImageHeight);
        marginLayoutParams.bottomMargin = margin;
        childImageV.setLayoutParams(marginLayoutParams);
        addView(childImageV);
        addView(childT);
    }

    @Override
    protected void onMeasure(
            int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算所有child view 要占用的空间
        desireWidth = 0;
        desireHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                MyMarginLayoutParams lp =  (MyMarginLayoutParams) v.getLayoutParams();
                //将measureChild改为measureChildWithMargin
                // measureChild(v, widthMeasureSpec, heightMeasureSpec);
                measureChildWithMargins(v, widthMeasureSpec, 0,
                        heightMeasureSpec, 0);
                desireWidth = Math.max(desireWidth, v.getMeasuredWidth()
                        + lp.leftMargin + lp.rightMargin);
                desireHeight += v.getMeasuredHeight() + lp.topMargin
                        + lp.bottomMargin;
            }

        }
        desireWidth += getPaddingLeft() + getPaddingRight();
        desireHeight += getPaddingTop() + getPaddingBottom();
        desireWidth = Math.max(desireWidth, getSuggestedMinimumWidth());
        desireHeight = Math.max(desireHeight, getSuggestedMinimumHeight());
        setMeasuredDimension(resolveSize(desireWidth, widthMeasureSpec),
                resolveSize(desireHeight, heightMeasureSpec));

    }


    @Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new MyMarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(
            AttributeSet attrs) {
        return new MyMarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(
            android.view.ViewGroup.LayoutParams p) {
        return new MyMarginLayoutParams(p);
    }
    public static class MyMarginLayoutParams extends MarginLayoutParams {
        public int gravity = -1;

        public MyMarginLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

        }

        public MyMarginLayoutParams(int width, int height) {
            this(width, height, -1);
        }

        public MyMarginLayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public MyMarginLayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public MyMarginLayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        if (BuildConfig.DEBUG)
            Log.d("onlayout", "parentleft: " + parentLeft + "   parenttop: "
                    + parentTop + "   parentright: " + parentRight
                    + "   parentbottom: " + parentBottom);

        int left = parentLeft;
        int top = parentTop;

        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                MyMarginLayoutParams lp =  (MyMarginLayoutParams) v.getLayoutParams();
                final int childWidth = v.getMeasuredWidth();
                final int childHeight = v.getMeasuredHeight();

                left = parentLeft;
                top += lp.topMargin;
                v.layout(left, top, left + childWidth, top + childHeight);
                top += childHeight + lp.bottomMargin;
            }
        }
    }

    // draw递归 不需要我们接管,

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }



}
