package com.example.zhsh.gaussblur;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.zhy.autolayout.AutoLayoutActivity;


public  class BaseActivity extends AutoLayoutActivity {

    private SystemBarTintManager tintManager;
   // public static SVProgressHUD mSVProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mSVProgressHUD = new SVProgressHUD(this);
       // initWindow();
    }


    @TargetApi(19)
    private void initWindow(){

        //Build.VERSION.SDK_INT  判断当前版本号是否大于19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            tintManager = new SystemBarTintManager(this);
            //tintManager.setTintColor(Color.parseColor("#FF9900"));	//仅设置任务栏颜色
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.blue));
            //tintManager.setStatusBarTintColor(Color.parseColor("#3272DE"));
            tintManager.setStatusBarTintEnabled(true);
            //tintManager.setNavigationBarTintEnabled(true);
        }
    }


    /**
     * 替代findviewById
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
