package com.example.zhsh.gaussblur;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.example.zhsh.gaussblur.utils.ScreenUtils;

import java.util.List;

/**
 * ===========================================
 * 作    者：zhsh
 * 版    本：1.0
 * 创建日期：2017/2/16.
 * 描    述：可以自动换行的group
 * ===========================================
 */

public class MyOptionViewGroup extends ViewGroup {

    private final static String TAG = "MyViewGroup";
    private int ParentHeight;
    private int closeBtnHeight;//closeButton的高度
    private int VIEW_MARGIN=40;//child之间的间距
    private int movespeed = 200;//移动时间
    private int despeed = 60;//延迟时间
    private int flag = 1;//表示可以动画，2表示不可以动画
    private int addCount = 0;
    private int HUITAN = 20;//回弹距离
    private List<ChildXY> childxylist;//child坐标
    int Tonghangcount = 0;//判断同行次数
    int jishuqi = 0;//计算同行次数
    private Context context;
    private boolean isfirst = true;
    private boolean addFirst = true;
    private ReMoveViewListener reMoviewListener;
    public interface ReMoveViewListener{
        void reMove();
    }


    public MyOptionViewGroup(Context context,int VIEW_MARGIN,int ParentHeight,int closeBtnHeight,ReMoveViewListener reMoveViewListener,int flag) {
        super(context);
        this.context = context;
        this.VIEW_MARGIN = VIEW_MARGIN;
        this.reMoviewListener = reMoveViewListener;
        this.ParentHeight = ParentHeight;
        this.flag = flag;
        this.closeBtnHeight = closeBtnHeight;
    }

    public MyOptionViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void addLayoutView(View viewchild, int flag, boolean addfirst, List<ChildXY> childxylist, int addCount){
        this.flag =flag;
        this.addFirst = addfirst;
        this.childxylist = childxylist;
        this.addCount = addCount;
        addView(viewchild);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        int lengthX = 0;
        int lengthY = 0;
        int row = 0;
        int left=0,top=0;
        for (int i = 0; i < count; i++) {

            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

        }
        // 设置自定义的控件MyViewGroup的大小
        int measureWidth = measureWidth(widthMeasureSpec);
        for(int i=0;i<count;i++){
            View child = getChildAt(i);
            left = child.getLeft();
            top = child.getTop();
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            lengthX+=width+VIEW_MARGIN;
            lengthY=row*(height+VIEW_MARGIN)+height;
            if(lengthX>measureWidth){
                lengthX=width+VIEW_MARGIN+left;
                row++;
                lengthY=row*(height+VIEW_MARGIN)+height;

            }

        }
        if(lengthY<ParentHeight){
            //setMeasuredDimension(measureWidth, ParentHeight+closeBtnHeight);
            setMeasuredDimension(measureWidth, ParentHeight);
        }else{
            setMeasuredDimension(measureWidth, lengthY+closeBtnHeight);
        }
        Log.i("onMeasure","lengthY"+lengthY+ "ParentHeight = "+ParentHeight+" closeBtnHeight = "+closeBtnHeight);

    }
    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width=50dip，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private void setAnim(final View view,float fromAlpha, float toAlpha,
                         final float fromYDelta, final float toYDelta,long offset) {

        Animation animation = new TranslateAnimation(0, 0, fromYDelta, toYDelta-HUITAN);
        animation.setDuration(movespeed);
        animation.setStartOffset(offset);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                int move = (int) (toYDelta-HUITAN - fromYDelta);
                int left = view.getLeft();
                int top = view.getTop() + move;
                int width = view.getWidth();
                int height = view.getHeight();
                view.clearAnimation();
                view.setX(left);
                view.setY(top);
//				Log.i("", "addview leftx"+left+"addview lefty"+top);
                Animation animation2 = new TranslateAnimation(0, 0, 0, HUITAN);
                animation2.setDuration(movespeed/10);
                animation2.setStartOffset(0);
                animation2.setFillAfter(true);
                animation2.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int move = (int) (toYDelta+HUITAN - fromYDelta);
                        int left = view.getLeft();
                        int top = view.getTop() + move;
                        int width = view.getWidth();
                        int height = view.getHeight();
                        view.clearAnimation();
                        view.setX(left);
                        view.setY(top);

                    }
                });
                view.setAnimation(animation2);

            }
        });

        view.startAnimation(animation);

    }
    /**
     * add按钮和新增的view之间的动画
     * @param view 传入childView
     * @param fromXAlpha
     * @param toXAlpha
     * @param fromYDelta
     * @param toYDelta
     * @param offset 延迟时间
     * @param isadd 是否addChild按钮
     * @param isUseY 是否在移动的时候使用Y位移
     * @param distenx 如果该参数不为0则使用该参数确定X坐标
     * @param disteny 如果该参数不为0则使用该参数确定Y坐标
     */
    private void setchangeAnim(final View view,final float fromXAlpha, final float toXAlpha,
                               final float fromYDelta, final float toYDelta,long offset,
                               final boolean isadd,boolean isUseY,final int distenx,final int disteny) {
        Animation animation = null;
//		if(isadd&&!isUseY){
//			animation = new TranslateAnimation(fromXAlpha, toXAlpha, fromYDelta, 0);
//		}else{
        animation = new TranslateAnimation(fromXAlpha, toXAlpha, fromYDelta, toYDelta);
//		}

        animation.setDuration(2*movespeed);
        animation.setStartOffset(offset);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                int Xmove =(int)(toXAlpha-fromXAlpha);
                Log.i("", "addview Xmove"+Xmove);
                if(distenx !=0){
                    Xmove = distenx;
                }
                int Ymove = (int) (toYDelta - fromYDelta);
                Log.i("", "addview view.getLeft()"+view.getLeft());
                if(disteny !=0){
                    Ymove = disteny;
                }
                int left = 0;

                if(Xmove < 0){
                    left = view.getLeft()+Xmove;
                    if(left<0){
                        left = VIEW_MARGIN;
                    }
                }else{
                    left = Xmove+VIEW_MARGIN;
                }


                //int left = Xmove;
                int top = 0;
//				if(addCount == 0){
                if(isadd){//是增加按钮的移动
                    top =( view.getTop() - ScreenUtils.getScreenHeight(context))+ Ymove;
                }else{
                    top = view.getTop() + Ymove;
                }
//				}else{
//					if(isadd){//是增加按钮
//						top =( view.getTop() -ScreenUtils.getScreenHeight(context))+ Ymove;
//					}else{
//						top = view.getTop() + Ymove;
//					}
                //top = view.getTop() + Ymove;
//				}

                view.clearAnimation();
                view.setX(left);
                view.setY(top);
                Log.i("", "addview leftx"+left+"addview lefty"+top);

            }
        });
        view.startAnimation(animation);

    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        Log.i(TAG, "changed = "+arg0+" left = "+arg1+" top = "+arg2+" right = "+arg3+" botom = "+arg4);
        final int count = getChildCount();

        int row=0;// which row lay you view relative to parent
        int lengthX=arg1;    // right position of child relative to parent
        int lengthY=arg2;    // bottom position of child relative to parent
        for(int i=0;i<count;i++){

            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            lengthX+=width+VIEW_MARGIN;
            lengthY=row*(height+VIEW_MARGIN)+height;
            //lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+arg2;
            //if it can't drawing on a same line , skip to next line
            if(lengthX>arg3){
                lengthX=width+VIEW_MARGIN+arg1;
                row++;
                //lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+arg2;
                // lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height;
                lengthY=row*(height+VIEW_MARGIN)+height;
            }
            //child.layout(lengthX-width, lengthY-height, lengthX, lengthY);
            if(flag ==1){
                child.layout(lengthX-width, lengthY-height+ScreenUtils.getScreenHeight(context), lengthX, lengthY+ScreenUtils.getScreenHeight(context));
                Log.i(TAG, "changed count = "+i+" left = "+(lengthX-width)+" top = "+(lengthY-height)+" right = "+lengthX+" botom = "+(lengthY+ScreenUtils.getScreenHeight(context))+"VIEW_MARGIN="+VIEW_MARGIN);
            }else if(flag == 2){
                int childsize = childxylist.size();
                Log.i("childxylist", "childxylist.size() = "+childxylist.size());

                if(i <=childsize-1){

                    ChildXY childxy = childxylist.get(i);
                    child.layout(childxy.getLeft(), childxy.getTop(), childxy.getRight(), childxy.getBottom());

                }else{
                    child.layout(lengthX-width, lengthY-height, lengthX, lengthY+HUITAN);
                }
                Log.i(TAG, "changed count = "+i+" left = "+(lengthX-width)+" top = "+(lengthY-height)+" right = "+lengthX+" botom = "+(lengthY));
            }

        }

        if(flag == 2&&addFirst){

            Log.i("count add--", "count ="+count);
            View newChild = getChildAt(count-1);//最后一个view(新增 的view)

            int newLeft = newChild.getLeft();
            int newTop = newChild.getTop();
            int newRight = newChild.getRight();
            int newBottom = newChild.getBottom();
            Log.i("newChild--", "newLeft ="+newLeft+"newTop ="+newTop+"newRight ="+newRight+"newBottom ="+newBottom);
            View befaddChild = getChildAt(count-2);//add类型后面一个view
            int befaddLeft = befaddChild.getLeft();
            int befaddTop = befaddChild.getTop();
            int befaddRight = befaddChild.getRight();
            int befaddBottom = befaddChild.getBottom();
            Log.i("befaddChild--", "befaddLeft ="+befaddLeft+"befaddTop ="+befaddTop+"befaddRight ="+befaddRight+"befaddBottom ="+befaddBottom);
            View addChild;
            if(addCount>=1){
                addChild = getChildAt(count-2-addCount);//add类型的view

            }else{
                addChild = befaddChild;
            }
            int addLeft = addChild.getLeft();
            int addTop = addChild.getTop();
            int addRight = addChild.getRight();
            int addBottom = addChild.getBottom();
            Log.i("addChild--", "addLeft ="+addLeft+"addTop ="+addTop+"addRight ="+addRight+"addBottom ="+addBottom);
            if(addCount==0){
                if(newLeft>addLeft){//在同一行
                    jishuqi = 0;
                    int xMove = newRight-addRight;

                    setchangeAnim(addChild, 0, xMove, 0, 0, 2*despeed,true,true,newLeft-VIEW_MARGIN,HUITAN);
                    setchangeAnim(newChild, 0, -xMove, 0, 0,  2*despeed,false,true,0,0);
                }else{//new child另起一行
                    if(jishuqi==0){
                        Tonghangcount++;
                    }
                    int yMove = newBottom - (addBottom-ScreenUtils.getScreenHeight(context));
                    int xMove = addRight - newRight;

                    setchangeAnim(addChild, 0, -xMove, 0,yMove,  2*despeed,true,true,0,0);
                    setchangeAnim(newChild, 0, xMove, 0,-yMove,  2*despeed,false,true,0,0);
                    jishuqi++;
                }
            }else{
                if(newLeft>befaddLeft){//在同一行
                    jishuqi = 0;
                    int xMove = newRight-befaddRight;
                    int yMove = Tonghangcount*(newBottom-newTop+VIEW_MARGIN);
                    Log.i("xMove", "xMove"+xMove);
                    Log.i("yMove", "yMove"+yMove);
                    setchangeAnim(addChild, 0, xMove, 0, 0,  2*despeed,true,false,befaddRight,yMove);
                    setchangeAnim(newChild, 0, -xMove, 0, 0,  2*despeed,false,true,0,0);
                    Log.i("Tonghangcount", "Tonghangcount"+Tonghangcount);
                }else{//new child另起一行
                    if(jishuqi==0){
                        Tonghangcount++;
                    }


                    int yMove = newBottom - (addBottom-ScreenUtils.getScreenHeight(context));
                    int newMove = newBottom-befaddBottom;
                    int xMove = addRight - newRight;
                    int newxMove = befaddRight-newRight;
                    int destenY = 0;
                    if(yMove>(addChild.getHeight()+VIEW_MARGIN)){
                        destenY = addChild.getHeight()+VIEW_MARGIN;
                    }
                    setchangeAnim(addChild, 0, -newxMove, 0,newMove,  2*despeed,true,true,-xMove,yMove);
                    setchangeAnim(newChild, 0, newxMove, 0,-newMove,  2*despeed,false,true,0,0);
                    jishuqi++;
                }
            }

            addFirst = false;
        }
        if(isfirst&&flag == 1){
            for(int i=0;i<count;i++){
                final View child = this.getChildAt(i);
                setAnim(child, 0, 0, 0, -ScreenUtils.getScreenHeight(context),i*despeed);
            }
            isfirst = false;
        }


    }
    //倒序移动
    public void reMoveChildView(){
        final int count = getChildCount();
        int timesize = 0;
        if(count>0){
            for(int i=count-1;i>=0;i--){
                final View child = this.getChildAt(i);
                setAnim(child, 0, 0, 0, ScreenUtils.getScreenHeight(context),timesize*despeed);
                Log.i("remove child", "child i="+i);
                timesize++;
            }
            reMoviewListener.reMove();
        }


    }


}
