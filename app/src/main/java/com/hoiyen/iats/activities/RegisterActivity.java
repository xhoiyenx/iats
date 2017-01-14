package com.hoiyen.iats.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hoiyen.iats.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends Activity {

    @BindView(R.id.username_edit)
    EditText username;
    @BindView(R.id.password_edit)
    EditText password;
    @BindView(R.id.email_edit)
    EditText email;
    @BindView(R.id.mobile_edit)
    EditText mobile;
    @BindView(R.id.loader)
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        password.setTypeface(Typeface.SERIF);
    }

    @OnClick(R.id.register_button)
    public void doRegister(View view) {

        boolean error = false;

        // Validate
        if (username.getText().length() == 0 || password.getText().length() == 0 || email.getText().length() == 0 || mobile.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.error_empty_form), Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (!error) {

        }

    }
}
