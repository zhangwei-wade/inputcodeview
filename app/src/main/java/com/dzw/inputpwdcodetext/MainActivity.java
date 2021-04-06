package com.dzw.inputpwdcodetext;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dzw.inputpwdcodetext.dialog.PwdDialog;

/**
 * @author zhangwei on 2020/12/19.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }


    public void onClickShowDialog(View view) {
        new PwdDialog(this).setNumRand(true).show();
    }
}
