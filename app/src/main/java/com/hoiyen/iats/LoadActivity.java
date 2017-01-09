package com.hoiyen.iats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hoiyen.iats.activities.BlogActivity;
import com.hoiyen.iats.activities.ChatActivity;
import com.hoiyen.iats.activities.GalleryActivity;
import com.hoiyen.iats.activities.LoginActivity;
import com.hoiyen.iats.activities.RegisterActivity;

public class LoadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startActivity( new Intent(this, RegisterActivity.class) );
        //startActivity( new Intent(this, GalleryActivity.class) );
        //startActivity( new Intent(this, BlogActivity.class) );
        //startActivity( new Intent(this, ProductActivity.class) );
        startActivity( new Intent(this, ChatActivity.class) );
        finish();
    }
    
}
