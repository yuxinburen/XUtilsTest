package com.davie.xutilstest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Date;

public class MainActivity extends Activity {

    //定义一个成员变量textview
    //所有以@开头的标记叫注解
    @ViewInject(R.id.txt_date)
    private TextView textView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //inject(Activity activity)
        ViewUtils.inject(this);
        textView.setText(new Date().toString());

    }

    @NotImplement
    @Override
    protected void onResume() {
        super.onResume();
    }
}
