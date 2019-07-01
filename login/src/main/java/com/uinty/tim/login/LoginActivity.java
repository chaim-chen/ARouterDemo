package com.uinty.tim.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.uinty.tim.annotation.BindPath;
import com.uinty.tim.arouter.ARouter;

@BindPath("login/login")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void jumpActivity(View view){
        ARouter.getInstance().jumpActivity("member/member",null);
    }
}
