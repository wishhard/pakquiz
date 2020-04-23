package com.wishhard.waqas.pakquiz;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Created by waqas on 3/8/2016.
 */
public class ScoreScreenDialog extends DialogFragment {

    public static ScoreScreenDialog newInslance() {
        ScoreScreenDialog ssd = new ScoreScreenDialog();
        return ssd;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_screen_dialog,container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Button scoreMain,scoreQuit;

        scoreMain = (Button) view.findViewById(R.id.scoreMainBtn);
        scoreQuit = (Button) view.findViewById(R.id.quitBtn);

        scoreMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ScoreActivity)getActivity()).mainBtnPress();
                dismiss();
            }
        });

        scoreQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ScoreActivity)getActivity()).quitPress();

            }
        });


    }
}
