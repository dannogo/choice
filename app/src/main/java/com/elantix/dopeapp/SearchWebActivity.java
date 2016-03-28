package com.elantix.dopeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by oleh on 3/28/16.
 */
public class SearchWebActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton mLeftToolbarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_web_result_list);
        recyclerView.setFocusable(false);

        AdapterSearchWebGallery adapter = new AdapterSearchWebGallery(this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);


        ImageButton rightToolbarButton = (ImageButton) findViewById(R.id.right_button);
        rightToolbarButton.setVisibility(View.GONE);
        mLeftToolbarButton = (ImageButton) findViewById(R.id.left_button);
        mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
        mLeftToolbarButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mLeftToolbarButton.getId()){
            finish();
        }
    }
}
