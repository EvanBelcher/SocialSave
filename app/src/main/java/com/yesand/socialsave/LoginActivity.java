package com.yesand.socialsave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class LoginActivity extends AppCompatActivity {

    Window window = LoginActivity.getWindow();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
