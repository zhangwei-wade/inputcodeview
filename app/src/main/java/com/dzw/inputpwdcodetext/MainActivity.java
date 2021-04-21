package com.dzw.inputpwdcodetext;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dzw.inputcode.InputCodeView;
import com.dzw.inputpwdcodetext.databinding.MainActivityBinding;
import com.dzw.inputpwdcodetext.dialog.PwdDialog;

/**
 * @author zhangwei on 2020/12/19.
 */
public class MainActivity extends AppCompatActivity implements InputCodeView.OnCodeCompleteListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding mainActivityBinding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(mainActivityBinding.getRoot());
        mainActivityBinding.inputCode1.setOnCodeCompleteListener(this);
        mainActivityBinding.inputCode2.setOnCodeCompleteListener(this);
        mainActivityBinding.inputCode3.setOnCodeCompleteListener(this);
        mainActivityBinding.inputCode4.setOnCodeCompleteListener(this);
        mainActivityBinding.inputCode5.setOnCodeCompleteListener(this);
        mainActivityBinding.inputCode6.setOnCodeCompleteListener(this);
        mainActivityBinding.inputCode7.setOnCodeCompleteListener(this);
    }


    public void onClickShowDialog(View view) {
        new PwdDialog(this).setNumRand(true).show();
    }

    @Override
    public void inputCodeComplete(String verificationCode) {
        Toast.makeText(this, verificationCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inputCodeInput(String verificationCode) {

    }
}
