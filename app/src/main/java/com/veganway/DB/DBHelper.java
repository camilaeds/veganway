package com.veganway.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CAMI on 17/08/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String BD_NOMBRE = "veganWay";
    static int BD_ESQUEMA_VERSION = 1;
    private static DBHelper databaseInstance;

    public static DBHelper getDatabaseInstance(Context context)
    {
        if(databaseInstance == null)
        {
            databaseInstance = new DBHelper(context);
        }
        return databaseInstance;
    }

    public DBHelper(Context context) {
        super(context, BD_NOMBRE, null, BD_ESQUEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBManager.CREAR_TABLA_BARRIO);
        db.execSQL(DBManager.CREAR_TABLA_LUGAR);
        db.execSQL(DBManager.CREAR_TABLA_FAVORITO);
        db.execSQL(DBManager.CREAR_TABLA_WISHED);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBManager.DROP_TABLA_BARRIO);
        db.execSQL(DBManager.DROP_TABLA_LUGAR);
        db.execSQL(DBManager.CREAR_TABLA_BARRIO);
        db.execSQL(DBManager.CREAR_TABLA_LUGAR);
    }
}

