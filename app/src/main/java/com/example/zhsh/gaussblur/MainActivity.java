package com.example.zhsh.gaussblur;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements TabView.OnTabChangeListener, View.OnClickListener {

    private TabView mTabView;
    private int mCurrentTabIndex = 0;   //当前状态
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;
    private TextView mTitleTextView;


    private int CZScreenWidth = 750;// UI screenWidth
    private int childCount = 8;        // 默认创造8个childView
    private int margin;        // child之间的间距
    private int column = 3;            // 3列
    private int screenWidth;        // 屏幕宽
    private int childWidth;            // 子view的宽度

    private List<PopWindowButtonInfo> childLayoutList;
    private PopupWindow popupWindow;
    private LinearLayout parientlayout;
    private int ScrollViewHeight;    // scrollView高度
    private MyOptionViewGroup myOptionViewGroup;
    private PopWindowButtonInfo newPopButtonBean;// 返回跳转后新增按钮的信息
    private boolean isFirst = true;//判断是不是第一次点击快速入口按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        mTitleTextView = (TextView) findViewById(R.id.text_title);
        mTabView = (TabView) findViewById(R.id.tab_view);
        mTabView.setOnTabChangeListener(this);
        mTabView.setCurrentTab(mCurrentTabIndex);
    }

    private void initData() {
        screenWidth = ScreenUtils.getScreenWidth(this);                // 屏幕宽
        margin = screenWidth * 70 / CZScreenWidth;
        childWidth = (screenWidth - ((column + 1) * margin)) / column;    // popwindow 子view的宽度
    }

    @Override
    public void onTabChange(String tag) {
        if (tag != null) {
            if (tag.equals("one")) {
                mCurrentTabIndex = 0;
                mTitleTextView.setText("one");
                replaceFragment(FragmentOne.class);

            } else if (tag.equals("two")) {
                mCurrentTabIndex = 1;
                mTitleTextView.setText("two");
                replaceFragment(FragmentTwo.class);

            } else if (tag.equals("three")) {
                mCurrentTabIndex = 2;
                mTitleTextView.setText("three");
                replaceFragment(FragmentThree.class);
            } else if (tag.equals("four")) {
                mCurrentTabIndex = 3;
                mTitleTextView.setText("four");
                replaceFragment(FragmentFour.class);
            }
        }

    }

    private void replaceFragment(Class<? extends Fragment> newFragment) {
        mCurrentFragment = FragmentUtils.switchFragment(mFragmentManager,
                R.id.layout_content, mCurrentFragment,
                newFragment, null, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quick:
                if (AppUtils.isFastDoubleClick()) {
                    return;
                } else {
                    applyBlur();
                }
                break;
        }
    }

    // 截屏获取毛玻璃view
    private void applyBlur() {

        // 一个自定义的布局，作为显示的内容 popwindow的内容
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_main_quick_entry, null);

        View view = null;
        view = getWindow().getDecorView().getRootView();
        // 截屏前设置true
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);

        //获取当前窗口快照，相当于截屏/创建毛玻璃图片和popwindow
        Bitmap sbitmap = BitmapUtils.small(ScreenUtils.snapShotWithStatusBar(this), 0.2f, 0.2f);
        Bitmap bitmap = BitmapUtils.blurBitmap(sbitmap, MainActivity.this);// 0<radius<=25
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        contentView.setBackgroundDrawable(drawable);

        showPopwindow(contentView);

        // 截屏后设置false 清空缓存
        view.setDrawingCacheEnabled(false);
        view.buildDrawingCache(false);
    }

    protected void showPopwindow(View contentView) {

        childLayoutList = new ArrayList<PopWindowButtonInfo>();

        popupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);


        parientlayout = (LinearLayout) contentView.findViewById(R.id.quickEntry_child_layout);
        ImageView close = (ImageView) contentView.findViewById(R.id.quick_close_button);
        final LinearLayout closeLiLayout = (LinearLayout) contentView.findViewById(R.id.quick_close_liLayout);
        final ScrollView svResult = (ScrollView) contentView.findViewById(R.id.quickEntry_child_scrollV);
        svResult.setFillViewport(true);
        ViewTreeObserver vto = svResult.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                svResult.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                ScrollViewHeight = svResult.getHeight();
                myOptionViewGroup = createViewGroup(margin, screenWidth, ScrollViewHeight, closeLiLayout.getHeight(), 1);
                addGroupView(childLayoutList, childCount, childWidth, myOptionViewGroup);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (AppUtils.isFastDoubleClick()) {
                    return;
                } else {
                    myOptionViewGroup.reMoveChildView();
                }

            }
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }

    private MyOptionViewGroup createViewGroup(int margin, int screenWidth,
                                              int ScrollViewHeight, int closeBtnHeight, int flag) {


        myOptionViewGroup = new MyOptionViewGroup(this, margin,
                ScrollViewHeight, closeBtnHeight,
                new MyOptionViewGroup.ReMoveViewListener() {

                    @Override
                    public void reMove() {

                        new Thread() {
                            public void run() {
                                try {
                                    sleep(700);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //EventBus.getDefault().post(new EventMain(EVENT_POPWINDOW_TAG, ""));
                                handler.sendEmptyMessage(0);

                            }
                        }.start();
                    }
                }, flag);

        LinearLayout.LayoutParams viewGroupParam = new LinearLayout.LayoutParams(screenWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        myOptionViewGroup.setLayoutParams(viewGroupParam);
        return myOptionViewGroup;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    popupWindow.dismiss();
                    if (popupWindow != null) {
                        popupWindow = null;
                    }
                    break;
            }
        }
    };

    private void addGroupView(final List<PopWindowButtonInfo> childLayoutList, final int childCount, final int childWidth, final MyOptionViewGroup viewGroup) {

        if (childLayoutList != null) {
            // 创造8个child view
            for (int i = 0; i < childCount; i++) {

                int imageSrc = 0;   //图标
                String text = "";   //文字
                String action = ""; //action

                switch (i) {

                    case 0:
                        imageSrc = R.mipmap.ic_me_normal;
                        text = "one";
                        action = "one";
                        break;
                    case 1:
                        imageSrc = R.mipmap.ic_me_normal;
                        text = "two";
                        action = "two";
                        break;
                    case 2:
                        imageSrc = R.mipmap.ic_me_normal;
                        text = "three";
                        action = "three";
                        break;
                    case 3:
                        imageSrc = R.mipmap.ic_me_normal;
                        text = "four";
                        action = "four";
                        break;

                    case 4:
                        imageSrc = R.mipmap.ic_me_normal;
                        text = "five";
                        action = "five";
                        break;
                    case 5:
                        imageSrc = R.mipmap.ic_me_normal;
                        text = "six";
                        action = "six";
                        break;
                    case 6:
                        imageSrc = R.mipmap.ic_me_normal;
                        text = "seven";
                        action = "seven";
                        break;
                    case 7:
                        imageSrc = R.mipmap.ic_me_normal;
                        text = "eight";
                        action = "eight";
                        break;
                        /*case 8:
                        imageSrc = R.drawable.quick_add;
                        text = "新增";
                        action = "newview";
                        break;*/
                }


                final String tempaction = action;

                newPopButtonBean = new PopWindowButtonInfo();
                newPopButtonBean.setImagesrc(imageSrc);
                newPopButtonBean.setText(text);
                newPopButtonBean.setAction(action);
                LinearLayout childLayout = createMyLayout(childWidth, imageSrc, text);

                childLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

//                        Intent intent = new Intent();
//                        intent.setAction(tempaction);
//                        startActivity(intent);
                        Toast.makeText(MainActivity.this,tempaction,Toast.LENGTH_SHORT).show();

                    }

                });

                childLayoutList.add(newPopButtonBean);
                viewGroup.addView(childLayout);

            }

            parientlayout.addView(viewGroup);
        }
    }


    // 创建子view
    private LinearLayout createMyLayout(int childWidth, int imageSrc, String text) {

        MyOptionView viewGroupImpl = CreateMyView(imageSrc, text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(childWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(this);
        //layout.setBackgroundColor(Color.RED);
        layout.setLayoutParams(layoutParams);
        layout.setGravity(Gravity.CENTER);
        layout.addView(viewGroupImpl);
        return layout;
    }

    private MyOptionView CreateMyView(int imageSrc, String text) {

        MyOptionView viewGroupImpl = new MyOptionView(this);
        ImageView imageView = new ImageView(this);
        TextView textView = new TextView(this);
        viewGroupImpl.setItemImagesrc(imageSrc);
        // 图片的宽度和高度
        viewGroupImpl.setItemImageWidth(screenWidth * 143 / CZScreenWidth);
        viewGroupImpl.setItemImageHeight(screenWidth * 143 / CZScreenWidth);
        viewGroupImpl.setItemText(text);
        viewGroupImpl.setItemTextColor(getResources().getColor(R.color.ct_cutline_gray));
        viewGroupImpl.addInScreen(imageView, textView);
        return viewGroupImpl;
    }


}
