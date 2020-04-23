package com.wishhard.waqas.pakquiz;

/**
 * Created by waqas on 2/27/2016.
 */
import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Questions extends SQLiteAssetHelper {

    private static final String  DB_NAME = "questions.db";

    public Questions(Context context) {
        super(context, DB_NAME, null, 1);
    }
}

