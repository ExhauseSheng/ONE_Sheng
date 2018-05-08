package com.sheng.one_sheng.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.sheng.one_sheng.R;

import static com.sheng.one_sheng.R.id.toolbar;

public class MovieActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setToolbar();
        changeStatusBar();
    }
}
