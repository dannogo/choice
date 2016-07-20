package com.elantix.dopeapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.fabric.sdk.android.services.common.SafeToast;

/**
 * Created by oleh on 4/2/16.
 */
public class ReportPostActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mCloseToolbarButton;
    private TextView mOption1;
    private TextView mOption2;
    private TextView mOption3;
    public ProgressDialog mProgressDialog;
    DopeInfo curItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_post);

        int dopeNum = getIntent().getIntExtra("dopeNum", 0);

        if (Utilities.sDopeListType == Utilities.DopeListType.Ten){
            curItem = Utilities.sDopes10[dopeNum];
        }else if (Utilities.sDopeListType == Utilities.DopeListType.Hundred){
            curItem = Utilities.sDopes100[dopeNum];
        }else{
            curItem = Utilities.sDopesFriendsFeed[dopeNum];
        }

        mCloseToolbarButton = (ImageView) findViewById(R.id.report_post_left_toolbar_button);
        mOption1 = (TextView) findViewById(R.id.report_post_option_1);
        mOption2 = (TextView) findViewById(R.id.report_post_option_2);
        mOption3 = (TextView) findViewById(R.id.report_post_option_3);

        mCloseToolbarButton.setOnClickListener(this);
        mOption1.setOnClickListener(this);
        mOption2.setOnClickListener(this);
        mOption3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        Intent returnIntent = new Intent();
        int result = 0;

        if (id == mOption1.getId()){
            result = R.string.report_post_option_cause_1;
        }else if (id == mOption2.getId()){
            result = R.string.report_post_option_cause_2;
        }else if (id == mOption3.getId()){
            result = R.string.report_post_option_cause_3;
        }else{

            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
            return;
        }

        if (Utilities.sToken == null){
            Toast.makeText(ReportPostActivity.this, "You are not logged in", Toast.LENGTH_SHORT).show();
        }else {
            HttpKit http = new HttpKit(ReportPostActivity.this);
            http.reportPost(Utilities.sToken, curItem.id, getResources().getString(result));

            returnIntent.putExtra("result", result);
            setResult(Activity.RESULT_OK, returnIntent);
        }
//        finish();
    }
}
