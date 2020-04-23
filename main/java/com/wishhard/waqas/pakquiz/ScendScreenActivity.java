package com.wishhard.waqas.pakquiz;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ScendScreenActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String DURATION ="com.greeenflag.waqas.pakquiz_duration";
    private static final String QUESTIONCOUNT ="com.greeenflag.waqas.pakquiz_qc";
    private static final String SCEND_SCREEN_DIALOG_DATA = "com.greeenflag.waqas.pakquiz.ssd";

    private static final String ONE_M = "01:00";
    private static final String THREE_M = "03:00";
    private static final String FIVE_M = "05:00";

    private  char[] chKey = new char[4];
    private String[] strQuestions = new String[5];

    private FragmentManager fm;
    private ScendScreenDialog dialog;

     QuestionsAccess questionsAccess;


    private TextView questionTV,questionCoundTV,constTimeTV,timerTV;
    private TextView option1,option2,option3,option4;
    private CountDownTimer Timer;
    private int minutes,secz,qNumb,qCounter = 1,inCorrect = 0,attempedQuestions =0;
    private  int start,end;
    private CountDownTimer timer;
    private long duration = 0,durationAfterPause;
    private String totalTimeStr,totalQuestionsStr;
    private MyPref pref;


    private boolean create,isBackPassed = false,isStoped = false,isPaused = false,isTimeLimitOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scend_screen);

        questionTV = (TextView) findViewById(R.id.questionTV);

        questionCoundTV = (TextView) findViewById(R.id.questionCound);
        constTimeTV = (TextView) findViewById(R.id.const_time);
        timerTV = (TextView) findViewById(R.id.timerTV);

        option1 = (TextView) findViewById(R.id.opt1TV);
        option2 = (TextView) findViewById(R.id.opt2TV);
        option3 = (TextView) findViewById(R.id.opt3TV);
        option4 = (TextView) findViewById(R.id.opt4TV);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        create = true;
        duration = getIntent().getLongExtra(DURATION, 0);
        qNumb = getIntent().getIntExtra(QUESTIONCOUNT, 0);

        totalTimeStr = setTotalTimeStrAssigning(qNumb);

        fm = getSupportFragmentManager();
        dialog = ScendScreenDialog.newInslance();

        pref = new MyPref(this);
        actualNumberOfQuestions();
        pref.setIndex(qNumb);

        end = pref.getIndex();
        start = 1+(end-qNumb);
        totalQuestionsStr = "Questions: "+qNumb+"/";

        pref.subtractFromTotal(qNumb);

        questionsAccess = QuestionsAccess.getInstance(this);
        changeQuestion();
    }

    private void changeQuestion() {
        questionAndAnswersStr();
        answerKeys();
        setQuestionsAndAnswer(strQuestions);
        questionCoundTV.setText(totalQuestionsStr + qCounter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        create = false;
        isPaused = true;
        timer.cancel();

    }

    @Override
    protected void onStop() {
        super.onStop();
        isStoped = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

         if(isPaused && !isStoped && !isBackPassed) {
             isPaused = false;
         }

        if(!dialog.isVisible() && !isBackPassed && isPaused && isStoped) {
             showDialog();
             onPause();
         } else if(!isBackPassed && !isPaused && !isStoped) {
             long t =(create)? duration:durationAfterPause;

             timer = new CountDownTimer(t,1000) {
                 @Override
                 public void onTick(long millisUntilFinished) {
                     minutes = (int) (millisUntilFinished/60000);
                     secz = (int)((millisUntilFinished/1000)%60);
                     timerTV.setText(totalTimeStr+"/"+timeToString(minutes, secz));


                     if(minutes == 0 && secz == 10) {
                         timerTV.setBackgroundColor(Color.RED);
                         constTimeTV.setBackgroundColor(Color.RED);
                     }

                     durationAfterPause = millisUntilFinished;

                 }

                 @Override
                 public void onFinish() {
                        timerTV.setText("00:00");
                        secz =0;
                        allViewsClickable(false);
                        isTimeLimitOver = true;
                        addDelay();
                 }
             }.start();
         }


    }

    @Override
    public void onClick(View v) {
        start++;
        qCounter++;

        int ra = riteAnswer();
        int userClicked =0;

        if(v == option1) {
            userClicked = R.id.opt1TV;
        } else if(v == option2) {
            userClicked = R.id.opt2TV;
        } else if(v == option3) {
            userClicked = R.id.opt3TV;
        } else if(v == option4) {
            userClicked = R.id.opt4TV;
        }

        if(qCounter > qNumb) {
            timer.cancel();
            qCounter--;
            addDelay();
        }


        if (ra == userClicked) {
            riteAnim(ra);
        } else {
            inCorrect++;
            riteAnim(ra);
            wrongAnim(userClicked);
        }

    }

    private void addDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                movingToScoreActivity();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        isBackPassed = true;
        showDialog();
        onPause();
    }

    public void mainPressed() {
        Intent i = new Intent(this,FirstScreenActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        finish();
    }

    public void resumeGamePressed() {
            isBackPassed = false;
            isPaused = false;
        isStoped = false;
            onResume();
    }



    public static Intent goToScendActivity(Context context,long time,int qc) {
        Intent i = new Intent(context,ScendScreenActivity.class);
        i.putExtra(DURATION, time);
        i.putExtra(QUESTIONCOUNT, qc);
        return i;
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

    private String setTotalTimeStrAssigning(int qn) {
        switch (qn) {
            case 1:
             return ONE_M;
            case 3:
                return THREE_M;
            case 5:
                return FIVE_M;
        }
        return null;
    }

    private void showDialog() {
        dialog.setCancelable(false);
        dialog.show(fm, SCEND_SCREEN_DIALOG_DATA);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    private String[] questionAndAnswersStr() {
        questionsAccess.open();
        strQuestions = questionsAccess.getQuestions(start);
        questionsAccess.close();
        return strQuestions;
    }

    private char[] answerKeys() {
        questionsAccess.open();
        chKey = questionsAccess.getKeys(start);
        questionsAccess.close();
        return chKey;
    }

    private void setQuestionsAndAnswer(String[] s) {
        questionTV.setText(""+s[0]);
        option1.setText(""+s[1]);
        option2.setText("" + s[2]);
        option3.setText(""+s[3]);
        option4.setText("" + s[4]);
    }


    private void actualNumberOfQuestions() {
        switch (qNumb) {
            case 1:
                qNumb = 10;
                break;
            case 3:
                qNumb = 20;
                break;
            case 5:
                qNumb = 40;
        }
    }


    private int riteAnswer() {
        if(chKey[0] == '+') {
            return R.id.opt1TV;
        } else if(chKey[1] == '+') {
            return R.id.opt2TV;
        } else if(chKey[2] == '+') {
            return R.id.opt3TV;
        }

        return R.id.opt4TV;
    }

    private void riteAnim(int id) {
        attempedQuestions++;
        final Animation scale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);
        final TextView rTV = (TextView) findViewById(id);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                allViewsClickable(false);
                rTV.setBackgroundResource(R.drawable.rite);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rTV.setBackgroundResource(R.drawable.custom_button);
                if(qCounter <= qNumb && !isTimeLimitOver) {
                    changeQuestion();
                    allViewsClickable(true);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rTV.startAnimation(scale);
    }

    private void wrongAnim(int us) {
        final Animation scale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);
         final TextView usTV = (TextView) findViewById(us);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                usTV.setBackgroundResource(R.drawable.wrong);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                usTV.setBackgroundResource(R.drawable.custom_button);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        usTV.startAnimation(scale);
    }


    private void allViewsClickable(boolean c) {
        if(c) {
            option1.setClickable(c);
            option2.setClickable(c);
            option3.setClickable(c);
            option4.setClickable(c);
        } else {
            option1.setClickable(c);
            option2.setClickable(c);
            option3.setClickable(c);
            option4.setClickable(c);
        }
    }


    private void movingToScoreActivity() {
        Intent i = ScoreActivity.goToScoreActivity(ScendScreenActivity.this,totalTimeStr,minutes,secz,qNumb,attempedQuestions,inCorrect);
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        finish();
    }




}
