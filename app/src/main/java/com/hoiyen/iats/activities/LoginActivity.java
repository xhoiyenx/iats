package com.hoiyen.iats.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.hoiyen.iats.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @BindView(R.id.username_edit)
    EditText username;
    @BindView(R.id.password_edit)
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        password.setTypeface(Typeface.SERIF);
    }

    @OnClick(R.id.login_button)
    public void doLogin() {

        boolean error = false;

        if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.error_empty_form), Toast.LENGTH_SHORT).show();
            error = true;
        }

        // Request API
        if (!error) {

        }

    }

    @OnClick(R.id.register_button)
    public void doRegister() {
        startActivity(new Intent(this, IntroActivity.class));
    }
}
