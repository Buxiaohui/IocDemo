package com.bxh.iocdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.example.AutoCreate;
import com.example.IocBindView;

/**
 * Created by buxiaohui on 6/15/17.
 */
@AutoCreate
public class MainActivity extends Activity {
    @IocBindView(R.id.show_appwall_btn)
    Button buttonApply;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
