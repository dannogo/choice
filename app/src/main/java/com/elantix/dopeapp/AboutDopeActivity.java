package com.elantix.dopeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by oleh on 4/9/16.
 */
public class AboutDopeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton mLeftToolbarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dope);

        mToolbar = (Toolbar) findViewById(R.id.about_dope_toolbar);
        mLeftToolbarBtn = (ImageButton) mToolbar.findViewById(R.id.left_button);
        ImageButton rightToolbarBtn = (ImageButton) mToolbar.findViewById(R.id.right_button);
        rightToolbarBtn.setVisibility(View.GONE);
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.about_dope_toolbar_title);

        mLeftToolbarBtn.setImageResource(R.drawable.toolbar_left_arrow);
        mLeftToolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
