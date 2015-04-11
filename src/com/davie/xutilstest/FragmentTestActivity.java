package com.davie.xutilstest;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ResInject;

/**
 * User: davie
 * Date: 15-4-9
 */
@ContentView(R.layout.fragmentactivity)
public class FragmentTestActivity extends FragmentActivity {

    /**
     * 代表获取应用程序的名称
     */
    @ResInject(id=R.string.app_name,type= ResType.String)
    private String appName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //所有的注解想要生效,必须先调用ViewUtils.inject()
        ViewUtils.inject(this);
        //所有的注解的对象全部初始化l
        setTitle(appName);

    }
}