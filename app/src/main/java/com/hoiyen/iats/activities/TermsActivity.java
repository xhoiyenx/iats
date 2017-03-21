package com.hoiyen.iats.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.hoiyen.iats.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsActivity extends Activity {

    @BindView(R.id.tablayout) TabLayout tabLayout;
    @BindView(R.id.tnc_text) TextView tnc_text;
    @BindView(R.id.register_button) Button register;
    @BindView(R.id.tnc_check) CheckBox tnc_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);

        // On load, set contents of tnc
        tnc_text.setText(R.string.tnc_adart);

        // Tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    tnc_text.setText(R.string.tnc_iats);
                }
                else {
                    tnc_text.setText(getString(R.string.dummy_tnc));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Register listener
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Proceed to registration if user check the tnc
                if (tnc_check.isChecked()) {
                    startActivity(new Intent(TermsActivity.this, RegisterActivity.class));
                }
                // Warn user
                else {
                    Toast.makeText(TermsActivity.this, getString(R.string.error_checktnc), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
