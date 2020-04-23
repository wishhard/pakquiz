package com.wishhard.waqas.pakquiz;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {
      private static final String TOTAL_TIME = "com.wishhard.waqas_total_time";
      private static final String TIME_REMAINED_MIN ="com.wishhard.waqas_minz";
      private static final String TIME_REMAIN_SCE = "com.wishhard.waqas_scez";
      private static final String TOTAL_QUESTIONS_COUNT = "com.wishhard.waqas_questionCount";
      private static final String ATTEMPTED_COUNT = "com.wishhard.waqas.attempt_count";
      private static final String INCORRECT_COUNT = "com.wishhard.waqas_incorrect_count";

      private static final String SCORE_DIALOG = "score dialog";

      private MyPref pref;

    private String totalTime;
    private String remainedTime;
    private String attempted;
    private String unAttempted;
    private String correctStr;
    private String inCorrectStr;
    private String precentageStr;

    private int remainedMinz,remainedScez,totalQuestionsCount,attemptedQuestionsCount,unAttemptedQuestionsCount,corrent,inCorrect
            ,precentage;
    private TextView totalTimeTV,remainedTimeTV,totalQuestionsCountTV,attemptedQuestionTV,unAttemptedQuestionsCountTV,
    correctTV,inCorrectTV,precenntageTV;
    private ProgressBar progressBar;

    private FragmentManager fm;
    private ScoreScreenDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

         pref = new MyPref(this);
        totalTimeTV = (TextView) findViewById(R.id.scoreTotalTimeTV);
        remainedTimeTV = (TextView) findViewById(R.id.scoreRemainedTimeTV);
        totalQuestionsCountTV = (TextView) findViewById(R.id.scoreTotalQuestionCountTV);
        attemptedQuestionTV = (TextView) findViewById(R.id.scoreTotalAttemptedQuestionCountTV);
        unAttemptedQuestionsCountTV = (TextView) findViewById(R.id.scoreTotalUnAttemptedQuestionCountTV);
        correctTV = (TextView) findViewById(R.id.scoreTotalCorrectAnswersTV);
        inCorrectTV = (TextView) findViewById(R.id.scoreTotalInCorrectAnswersTV);
        progressBar = (ProgressBar) findViewById(R.id.userProgress);
        precenntageTV = (TextView) findViewById(R.id.precentageTV);

        fm = getSupportFragmentManager();
        dialog = ScoreScreenDialog.newInslance();

        totalTime = getIntent().getStringExtra(TOTAL_TIME);
        remainedMinz = getIntent().getIntExtra(TIME_REMAINED_MIN, 0);
        remainedScez = getIntent().getIntExtra(TIME_REMAIN_SCE, 0);
        totalQuestionsCount = getIntent().getIntExtra(TOTAL_QUESTIONS_COUNT, 0);
        attemptedQuestionsCount = getIntent().getIntExtra(ATTEMPTED_COUNT,0);
        unAttemptedQuestionsCount = totalQuestionsCount - attemptedQuestionsCount;
        remainedTime = timeToString(remainedMinz, remainedScez);
        attempted = doubleZero(attemptedQuestionsCount);
        unAttempted = doubleZero(unAttemptedQuestionsCount);
        inCorrect = getIntent().getIntExtra(INCORRECT_COUNT, 0);
        inCorrect +=  unAttemptedQuestionsCount;
        inCorrectStr = doubleZero(inCorrect);
        corrent = totalQuestionsCount-inCorrect;
        correctStr = doubleZero(corrent);
        precentage = precentageCalcu(corrent, totalQuestionsCount);
        precentageStr = doubleZero(precentage);


        if(precentage >= 50) {
            pref.setIndex(totalQuestionsCount);
            pref.getIndex();
            Toast.makeText(this, R.string.pass_str, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.low_precentage_str, Toast.LENGTH_SHORT).show();

        }

        totalTimeTV.setText(totalTime);
        remainedTimeTV.setText(remainedTime);
        totalQuestionsCountTV.setText(""+totalQuestionsCount);
        attemptedQuestionTV.setText(attempted);
        unAttemptedQuestionsCountTV.setText(unAttempted);
        inCorrectTV.setText(inCorrectStr);
        correctTV.setText(correctStr);
        progressBar.setProgress(precentage);
        precenntageTV.setText(precentageStr+"%");

    }

    public static Intent goToScoreActivity(Context context,String tt,int minz,int scez,int qn,int qc,int incorrect) {
        Intent i = new Intent(context,ScoreActivity.class);
        i.putExtra(TOTAL_TIME,tt);
        i.putExtra(TIME_REMAINED_MIN,minz);
        i.putExtra(TIME_REMAIN_SCE,scez);
        i.putExtra(TOTAL_QUESTIONS_COUNT,qn);
        i.putExtra(ATTEMPTED_COUNT,qc);
        i.putExtra(INCORRECT_COUNT,incorrect);
        return i;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private String timeToString(int m,int s) {
        String str = "0"+m+":";

        if(s >= 0 && s <= 9) {
            str = str+"0"+s;
        } else {
            str = str+s;
        }

        return str;
    }

    private String doubleZero(int value) {
        String str = (value >= 0 && value <= 9)? "0"+value:""+value;
        return str;
    }

    private int precentageCalcu(int part,int wole){
        float w = wole;
        float precent = (part/w)*100.0f;
        return (int)precent;
    }

    private void showDialog() {
        dialog.setCancelable(false);
        dialog.show(fm,SCORE_DIALOG);
    }

    public void mainBtnPress() {
        Intent i = new Intent(this,FirstScreenActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        finish();
    }

    public void quitPress() {
        dialog.dismiss();
        ScoreActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }
}
