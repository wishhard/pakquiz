package com.wishhard.waqas.pakquiz;

import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestionsAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static QuestionsAccess questionsAccess;

    private QuestionsAccess(Context context) {
        this.openHelper = new Questions(context);
    }

    public static QuestionsAccess getInstance(Context context) {
        if(questionsAccess == null) {
            questionsAccess = new QuestionsAccess(context);
        }
        return questionsAccess;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if(database != null) {
            this.database.close();
        }

    }


    public char[] getKeys(int rowIndex) {
        String s = "";
        char[] ch = new char[4];

        int count =0;
        Cursor c = database.rawQuery("SELECT ch1,ch2,ch3,ch4 FROM pak WHERE id =" + rowIndex,null);
        c.moveToFirst();

        while (!c.isAfterLast() && count < 4) {
            s  += c.getString(count);
            ch[count] = s.charAt(count);
            count++;
        }

        c.close();;
        return ch;
    }

    public String[] getQuestions(int rIndex) {
        String[] sArr = new String[5];

        int count =0;
        Cursor c = database.rawQuery("SELECT qu,opt1,opt2,opt3,opt4 FROM pak WHERE id ="+rIndex,null);
        c.moveToFirst();

        while (!c.isAfterLast() && count < 5) {
            sArr[count] = c.getString(count);
            count++;
        }

        c.close();;
        return sArr;
    }
}

