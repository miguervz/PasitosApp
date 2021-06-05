package es.studium.pasitosapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper
{


    private static final String NOMBRE_BASE_DE_DATOS = "pasitosApp",
             NOMBRE_TABLA_DATOS = "datos";
    private static final int VERSION_BASE_DE_DATOS = 1;

    public DB(Context context)
    {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " latitud TEXT, longitud TEXT, bateria TEXT)", NOMBRE_TABLA_DATOS));
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXIST %s", NOMBRE_TABLA_DATOS));
        onCreate(db);
    }
}
