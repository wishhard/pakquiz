package com.wishhard.waqas.pakquiz;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by waqas on 2/25/2016.
 */
public class MyPref {
    private static final String ROW_ID_KEEPER = "row_id_keeper";
    private static final String ROW_INDEX = "row_index";
    private static final int TOTAL_NUMBER_OF_QUESTIONS = 400;
    private int index;

    private Context context;


    public MyPref(Context context) {
        this.context = context;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
         SharedPreferences sp = context.getSharedPreferences(ROW_ID_KEEPER,Context.MODE_PRIVATE);
          SharedPreferences.Editor ed = sp.edit();
         int temp = sp.getInt(ROW_INDEX,0);

        if(temp+index <= TOTAL_NUMBER_OF_QUESTIONS) {
            ed.putInt(ROW_INDEX,temp+index);
            ed.commit();
        } else {
            ed.remove(ROW_INDEX);
            ed.putInt(ROW_INDEX,index);
            ed.commit();
        }

         index = sp.getInt(ROW_INDEX,0);

        return index;
    }

    public void subtractFromTotal(int qn) {
        SharedPreferences sp = context.getSharedPreferences(ROW_ID_KEEPER,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        int temp = sp.getInt(ROW_INDEX,0);

        ed.putInt(ROW_INDEX,temp - qn);
        ed.commit();
    }
}
