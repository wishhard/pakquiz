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

public class ScendScreenDialog extends DialogFragment {

    public static ScendScreenDialog newInslance() {
        ScendScreenDialog ssd = new ScendScreenDialog();
        return ssd;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scend_screen_dialog,container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Button main_btn,resume_btn;

        main_btn = (Button) view.findViewById(R.id.mainBtn);
        resume_btn = (Button) view.findViewById(R.id.resumeBtn);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ScendScreenActivity)getActivity()).mainPressed();
                dismiss();
            }
        });

        resume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ScendScreenActivity)getActivity()).resumeGamePressed();
                 dismiss();
            }
        });
    }
}
