package com.wishhard.waqas.pakquiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class FirstScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String FIRST_SCREEN_DIALOG_DATA = "com.greeenflag.waqas.pakquiz.fsd";

    private static final int ONE = 1;
    private static final int THREE = 3;
    private static final int FIVE = 5;

    private static final long ONE_IN_MILI = 60000;
    private static final long THREE_IN_MILI = 180000;
    private static final long FIVE_IN_MILI = 300000;


     Button oneM_btn,threeM_btn,fiveM_btn;

    private FragmentManager fm;
    private FirstScreenDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        oneM_btn = (Button) findViewById(R.id.oneMinuteButton);
        threeM_btn = (Button) findViewById(R.id.threeMinuteButton);
        fiveM_btn = (Button) findViewById(R.id.fiveMinuteButton);

        oneM_btn.setOnClickListener(this);
        threeM_btn.setOnClickListener(this);
        fiveM_btn.setOnClickListener(this);

        fm = getSupportFragmentManager();
        dialog = FirstScreenDialog.newInslance();
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    private void showDialog() {
        dialog.setCancelable(false);
        dialog.show(fm,FIRST_SCREEN_DIALOG_DATA);
    }

    @Override
    public void onClick(View v) {
        long t = 0;
        int qc = 0;
       if(v == oneM_btn) {
           qc = ONE;
          t = ONE_IN_MILI;
       } else if(v == threeM_btn) {
           qc = THREE;
           t = THREE_IN_MILI;
       } else if(v == fiveM_btn) {
           qc = FIVE;
           t = FIVE_IN_MILI;
       }

        Intent i = ScendScreenActivity.goToScendActivity(FirstScreenActivity.this,t,qc);
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        finish();
    }

    public void yesBtnPress() {

        dialog.dismiss();
        finish();
    }

}
