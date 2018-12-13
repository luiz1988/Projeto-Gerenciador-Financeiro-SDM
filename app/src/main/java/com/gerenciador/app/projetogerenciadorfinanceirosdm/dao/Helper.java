package com.gerenciador.app.projetogerenciadorfinanceirosdm.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper {

    private static Helper sInstance;

    private static final String DATABASE_NAME = "controle_financeiro.db";
    private static final int DATABASE_VERSION = 10;

    public static synchronized Helper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new Helper(context.getApplicationContext());
        }
        return sInstance;
    }

    private Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.TB_CONTA);
        db.execSQL(DB.TB_CENTRO_CUSTO);
        db.execSQL(DB.TB_TRANSACAO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS conta");
        db.execSQL("DROP TABLE IF EXISTS centro_custo");
        db.execSQL("DROP TABLE IF EXISTS transacao");
        onCreate(db);

    }
}