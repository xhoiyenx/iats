package com.hoiyen.iats.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.hoiyen.iats.R;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String header_a = getString(R.string.intro_header_a);
        final String header_b = getString(R.string.intro_header_b);
        final String header_c = getString(R.string.intro_header_c);
        final String desc_a = getString(R.string.intro_description_a);
        final String desc_b = getString(R.string.intro_description_b);
        final String desc_c = getString(R.string.intro_description_c);

        addSlide(AppIntro2Fragment.newInstance(header_a, desc_a, R.drawable.ic_student, ContextCompat.getColor(this, R.color.colorStatusBar)));
        addSlide(AppIntro2Fragment.newInstance(header_b, desc_b, R.drawable.ic_student, ContextCompat.getColor(this, R.color.colorStatusBar)));
        addSlide(AppIntro2Fragment.newInstance(header_c, desc_c, R.drawable.ic_student, ContextCompat.getColor(this, R.color.colorStatusBar)));

        showSkipButton(false);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }
}
