package es.studium.pasitosapp.Controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import es.studium.pasitosapp.DB;
import es.studium.pasitosapp.Modelos.InformacionMovil;

public class Controlador {
    private DB ayudanteBaseDeDatos;
    private String NOMBRE_TABLA = "datosPasitos";

    public Controlador(Context context){
        ayudanteBaseDeDatos = new DB(context);
    }

    //Introducir datos en la BD
    public long nuevoDatos(InformacionMovil datos){
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues datosParaInsertar = new ContentValues();
        datosParaInsertar.put("latitud", datos.getLatitud());
        datosParaInsertar.put("longitud", datos.getLongitud());
        datosParaInsertar.put("bateria", datos.getBateriaMovil());
        return baseDeDatos.insert(NOMBRE_TABLA, null, datosParaInsertar);
    }


}
