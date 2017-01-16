package com.hoiyen.iats.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hoiyen.iats.R;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.UserModel;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @BindView(R.id.username_edit)
    EditText username;
    @BindView(R.id.password_edit)
    EditText password;
    @BindView(R.id.loader)
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        password.setTypeface(Typeface.SERIF);
    }

    @OnClick(R.id.login_button)
    public void doLogin(final View view) {

        boolean error = false;

        if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.error_empty_form), Toast.LENGTH_SHORT).show();
            error = true;
        }

        // Request API
        if (!error) {

            view.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);

            ApiRequest.LoginRequest(getString(R.string.api_login), username.getText().toString(), password.getText().toString(), new ApiRequest.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    final UserModel user = UserModel.parseJSON(response);
                    Prefs.putString("token", user.token);
                    Prefs.putString("username", user.username);
                    Prefs.putString("usermail", user.usermail);
                    Prefs.putString("usercell", user.usercell);
                    startActivity(new Intent(LoginActivity.this, BlogActivity.class));
                    finish();
                }

                @Override
                public void onErrorResponse(String response) {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                    view.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                }
            });

        }

    }

    @OnClick(R.id.register_button)
    public void doRegister() {
        startActivity(new Intent(this, IntroActivity.class));
    }
}
