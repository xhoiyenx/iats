package com.hoiyen.iats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hoiyen.iats.activities.BlogActivity;
import com.hoiyen.iats.activities.ChatActivity;
import com.hoiyen.iats.activities.LoginActivity;
import com.hoiyen.iats.activities.SubscribeActivity;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.UserModel;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONObject;

public class LoadActivity extends Activity {

    String notificationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            notificationSource = bundle.getString("source");
        }
        overridePendingTransition(0, 0);

        // Check if token available
        final String token = Prefs.getString("token", "");
        if ("".equals(token)) {
            // empty token, go to login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {

            // token available, load profile
            ApiRequest.ProfileRequest(new ApiRequest.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // save member profile to preferences
                    final UserModel user = UserModel.parseJSON(response);
                    Prefs.putString("username", user.username);
                    Prefs.putString("usermail", user.usermail);
                    Prefs.putString("usercell", user.usercell);
                    Prefs.putString("avatar", user.avatar_url);

                    if (user.subscribed == 0) {
                        startActivity(new Intent(LoadActivity.this, SubscribeActivity.class));
                        finish();
                        return;
                    }

                    if (notificationSource != null && notificationSource.equals("chat")) {
                        startActivity(new Intent(LoadActivity.this, ChatActivity.class));
                    } else {
                        startActivity(new Intent(LoadActivity.this, BlogActivity.class));
                    }
                    finish();
                }

                @Override
                public void onErrorResponse(String response) {

                }
            });

        }


    }

}
