package com.hoiyen.iats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hoiyen.iats.activities.BlogActivity;
import com.hoiyen.iats.activities.LoginActivity;
import com.pixplicity.easyprefs.library.Prefs;

public class LoadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if token available
        final String token = Prefs.getString("token", "");
        if ("".equals(token)) {
            // empty token, go to login activity
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            // token available, go to blog
            startActivity(new Intent(this, BlogActivity.class));
        }
        finish();

    }

}
