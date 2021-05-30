package com.veganway.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by CAMI on 17/08/2015.
 */
public class DBManager {

    private DBHelper bdHelper;
    private SQLiteDatabase sqlDB;

    //Tablas
    public static final String NOMBRE_TABLA_BARRIO= "Barrio";
    public static final String NOMBRE_TABLA_LUGAR= "Lugar";
    public static final String NOMBRE_TABLA_FAVORITO = "Favorito";
    public static final String NOMBRE_TABLA_WISHED = "Wished";

    //Campos Tabla Barrio
    public static final String ID_BARRIO = "_id";
    public static final String NOMBRE_BARRIO = "Nombre";

    //Campos Tabla Lugar
    public static final String ID_LUGAR = "_id";
    public static final String NOMBRE_LUGAR = "Nombre";
    public static final String DIR_LUGAR = "Direccion";
    public static final String HORARIO_LUGAR = "Horario";
    public static final String TEL_LUGAR = "Tel";
    public static final String FACE_LUGAR = "Facebook";
    public static final String WWW_LUGAR = "PaginaWeb";
    public static final String LAT_LUGAR = "Latitud";
    public static final String LONG_LUGAR = "Longitud";
    public static final String ID_BAR = "_id_barrio";
    public static final String IMG_LUGAR = "Imagen";

    //Campos Tabla Favorito
    public static final String ID_FAVORITO = "_id";
    public static final String ES_FAVORITO = "es_favorito";
    public static final String NOMBRE_LUGAR_FAVORITO = "nombre_lugar_favorito";

    public static final String FAVORITO = "favorito";

    //Campos Tabla Wished
    public static final String ID_WISHED = "_id";
    public static final String IS_WISHED = "is_wished";
    public static final String NAME_PLACE_WISHED = "name_place_wished";

    public static final String WISHED = "wished";

    //Creacion Tablas

    public static final String CREAR_TABLA_BARRIO = "CREATE TABLE "+NOMBRE_TABLA_BARRIO
            +" ("+ID_BARRIO+" integer PRIMARY KEY AUTOINCREMENT, "
            +NOMBRE_BARRIO+" text NOT NULL)";

    public static final String CREAR_TABLA_LUGAR = "CREATE TABLE "+NOMBRE_TABLA_LUGAR
            +" ("+ID_LUGAR+" integer PRIMARY KEY AUTOINCREMENT, "
            +NOMBRE_LUGAR+" text NOT NULL,"
            +DIR_LUGAR+" text NOT NULL,"+HORARIO_LUGAR+" text,"
            +TEL_LUGAR+" text,"+FACE_LUGAR+" text,"+WWW_LUGAR+" text,"
            +LAT_LUGAR+" double,"+LONG_LUGAR+" double,"
            +ID_BAR+" integer CONSTRAINT fk_foreign_key REFERENCES Barrio (_id),"
            +IMG_LUGAR+" text);";

    public static final String CREAR_TABLA_FAVORITO = "CREATE TABLE "+NOMBRE_TABLA_FAVORITO
            +" ("+ID_FAVORITO+" integer PRIMARY KEY AUTOINCREMENT, "
            +ES_FAVORITO+" boolean NOT NULL,"
            +NOMBRE_LUGAR_FAVORITO+" text NOT NULL);";

    public static final String CREAR_TABLA_WISHED = "CREATE TABLE "+NOMBRE_TABLA_WISHED
            +" ("+ID_WISHED+" integer PRIMARY KEY AUTOINCREMENT, "
            +IS_WISHED+" boolean NOT NULL,"
            +NAME_PLACE_WISHED+" text NOT NULL);";

    //Borrar tablas
    public static final String DROP_TABLA_BARRIO = "DROP TABLE IF EXISTS " +NOMBRE_TABLA_BARRIO ;
    public static final String DROP_TABLA_LUGAR = "DROP TABLE IF EXISTS " +NOMBRE_TABLA_LUGAR ;

    //Constructor
    public DBManager(Context context) {

        bdHelper = DBHelper.getDatabaseInstance(context);
    }

    //Abrir conexion a base de datos
    public void leerDB(){
        sqlDB = bdHelper.getReadableDatabase();

    }

    public void escribirDB() {
        sqlDB = bdHelper.getWritableDatabase();

    }

    // Cerrar conexion a la base de datos
    public void cerrarConexion()
    {
        bdHelper.close();
    }

    //Generar valores
    public ContentValues generarValoresBarrio(String nombre){
        ContentValues valores = new ContentValues();
        valores.put(NOMBRE_BARRIO, nombre);
        return valores;
    }

    public ContentValues generarValoresLugar(String nombre, String direccion, String horario, String tel, String face, String pagWeb, double latitud, double longitud, int id_barrio, String img){
        ContentValues valores = new ContentValues();
        valores.put(NOMBRE_LUGAR ,nombre);
        valores.put(DIR_LUGAR,direccion);
        valores.put(HORARIO_LUGAR, horario);
        valores.put(TEL_LUGAR, tel);
        valores.put(FACE_LUGAR, face);
        valores.put(WWW_LUGAR, pagWeb);
        valores.put(LAT_LUGAR, latitud);
        valores.put(LONG_LUGAR,longitud);
        valores.put(ID_BAR, id_barrio);
        valores.put(IMG_LUGAR, img);

        return valores;
    }

    public ContentValues generarValoresFavorito(boolean es_favorito, String nombre_lugar_favorito){
        ContentValues valores = new ContentValues();

        if(es_favorito == true){
            valores.put(ES_FAVORITO , 1);
        }
        else{
            valores.put(ES_FAVORITO, 0);
        }

        valores.put(NOMBRE_LUGAR_FAVORITO, nombre_lugar_favorito);

        return valores;
    }

    public ContentValues generarValoresWished(boolean is_wished, String name_place_wished){
        ContentValues valores = new ContentValues();

        if(is_wished == true){
            valores.put(IS_WISHED , 1);
        }
        else{
            valores.put(IS_WISHED, 0);
        }

        valores.put(NAME_PLACE_WISHED, name_place_wished);

        return valores;
    }

    //Inserciones
    public void insertarBarrio(String nombre){
        sqlDB.insert(NOMBRE_TABLA_BARRIO, null, generarValoresBarrio(nombre));
    }

    public void insertarLugar(String nombre, String direccion, String horario, String tel, String face, String pagWeb, double latitud, double longitud, int id_barrio, String img){
        sqlDB.insert(NOMBRE_TABLA_LUGAR, null, generarValoresLugar(nombre, direccion, horario, tel, face, pagWeb, latitud, longitud, id_barrio, img)); //Si esta funcion devuelve -1 es que hubo un error
    }

    public void insertarFavorito(boolean es_favorito, String nombre_lugar_fav){
        escribirDB();
        sqlDB.insert(NOMBRE_TABLA_FAVORITO, null, generarValoresFavorito(es_favorito, nombre_lugar_fav));
    }

    public void insertarWished(boolean is_wished, String name_place_wished){
        escribirDB();
        sqlDB.insert(NOMBRE_TABLA_WISHED, null, generarValoresWished(is_wished, name_place_wished));
    }

    //Consultas
    public Cursor cargarBarrios(){
        String columnas[] = new String[]{ID_BARRIO, NOMBRE_BARRIO};
        return sqlDB.query(NOMBRE_TABLA_BARRIO,columnas,null, null, null,null, null);
    }

    public Cursor cargarLugares(){
        String columnas[] = new String[]{ID_LUGAR, NOMBRE_LUGAR, DIR_LUGAR, HORARIO_LUGAR, TEL_LUGAR, FACE_LUGAR, WWW_LUGAR, LAT_LUGAR, LONG_LUGAR, ID_BAR};
        return sqlDB.query(NOMBRE_TABLA_LUGAR, columnas, null, null, null, null, null);
    }

    public Cursor consultarLugar(String nombre_lugar){
        String sql = "SELECT * FROM "+NOMBRE_TABLA_LUGAR+" WHERE "+NOMBRE_LUGAR+" = '"+nombre_lugar+"'";
        return sqlDB.rawQuery(sql, null);
    }

    public Cursor consultarLugarporId(int id_lugar) {
        String sql = "SELECT * FROM " + NOMBRE_TABLA_LUGAR + " WHERE " + ID_LUGAR + " = '" + id_lugar + "'";
        return sqlDB.rawQuery(sql, null);
    }

    public Cursor consultarFavoritos() {
        leerDB();
        String sql = "SELECT * FROM "+NOMBRE_TABLA_FAVORITO+" ORDER BY " + NOMBRE_LUGAR_FAVORITO;
        return sqlDB.rawQuery(sql,null);
    }

    public Cursor consultarWishes() {
        leerDB();
        String sql = "SELECT * FROM "+NOMBRE_TABLA_WISHED+" ORDER BY " +NAME_PLACE_WISHED;
        return sqlDB.rawQuery(sql,null);
    }

    public void eliminarFavorito(String nombre_lugar_fav){
        escribirDB();
        sqlDB.delete(NOMBRE_TABLA_FAVORITO, NOMBRE_LUGAR_FAVORITO + "= '" + nombre_lugar_fav+"'", null);
    }

    public void eliminarWish(String nombre_place_wished){
        escribirDB();
        sqlDB.delete(NOMBRE_TABLA_WISHED, NAME_PLACE_WISHED + "= '" + nombre_place_wished+"'", null);
    }

    public Cursor esFavorito(String nombre_lugar_fav){
        leerDB();
        String sql = "SELECT * FROM "+NOMBRE_TABLA_FAVORITO+" WHERE "+NOMBRE_LUGAR_FAVORITO+" = '"+nombre_lugar_fav+"'";
        return sqlDB.rawQuery(sql, null);
    }

    public Cursor isWished(String place_wished){
        leerDB();
        String sql = "SELECT * FROM "+NOMBRE_TABLA_WISHED+" WHERE "+NAME_PLACE_WISHED+" = '"+place_wished+"'";
        return sqlDB.rawQuery(sql, null);
    }

    public void borrarTablas() {
        sqlDB.delete(NOMBRE_TABLA_BARRIO, null , null);
        sqlDB.delete(NOMBRE_TABLA_LUGAR, null , null);
    }

    public Cursor lugaresporBarrio(int id_barrio) {
        String query = "SELECT * FROM "+NOMBRE_TABLA_LUGAR+" WHERE "+NOMBRE_TABLA_LUGAR+"."+ID_BAR+" = "+id_barrio;
        return sqlDB.rawQuery(query, null);
    }

}
