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

import java.util.HashMap;
import java.util.Map;

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
    public void doRegister(final View view) {

        boolean error = false;

        // Validate
        if (username.getText().length() == 0 || password.getText().length() == 0 || email.getText().length() == 0 || mobile.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.error_empty_form), Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (!error) {

            loader.setVisibility(View.VISIBLE);
            view.setEnabled(false);

            final Map<String, String> params = new HashMap<>();
            params.put("username", username.getText().toString());
            params.put("password", password.getText().toString());
            params.put("usermail", email.getText().toString());
            params.put("usercell", mobile.getText().toString());

            ApiRequest.RegisterRequest(getString(R.string.api_register), params, new ApiRequest.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    final UserModel user = UserModel.parseJSON(response);
                    Prefs.putString("token", user.apitoken);
                    Prefs.putString("username", user.username);
                    Prefs.putString("usermail", user.usermail);
                    Prefs.putString("usercell", user.usercell);
                    startActivity(new Intent(RegisterActivity.this, BlogActivity.class));
                    finish();
                }

                @Override
                public void onErrorResponse(String response) {
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                    view.setEnabled(true);
                }
            });

        }

    }
}
