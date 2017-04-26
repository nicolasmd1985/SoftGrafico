package mahecha.nicolas.softgrafico.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by nicolas on 27/07/2016.
 */
public class DBController extends SQLiteOpenHelper {


    private static final String NOMBRE_BASE_DATOS = "sgrafico.db";
    private static final int VERSION_ACTUAL = 1;
    private Context contexto;




    public DBController(Context contexto) {
        super(contexto,NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;
        ///////////////BASE DE USUARIOS//////////////////
        query = "CREATE TABLE dispositivo ( id_dispositivo TEXT PRIMARY KEY, nombre TEXT, posx INTEGER, posy INTEGER, plano TEXT )";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS dispositivo";
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);

    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase)
    {
        super.onOpen(sqLiteDatabase);
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
    }


///////////////////*****************DISPOSITIVOS DE CONFIGURACION***************/////////////ok

    /**
     * Inserts User into SQLite DB   * @param queryValues
     */
//    public void inserdispo(HashMap<String, String> queryValues) {
    public void inserdispo(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_dispositivo", queryValues.get("id_dispositivo"));
        values.put("nombre", queryValues.get("nombre"));


        database.insert("dispositivo", null, values);
        database.close();
    }


////////////////////*************OBTENER LISTA DE USUARIOS***********///////////ok

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT id_dispositivo, nombre FROM dispositivo";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_dispositivo", cursor.getString(0));
                map.put("nombre", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }


//////////////////////************************ACTUALIZAR DISPOSITIVOS*********/////////////////
    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void updips(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("codigoscan", queryValues.get("codigo"));
        values.put("nombre", queryValues.get("nombre"));
        values.put("descripcion", queryValues.get("descripcion"));

        String id =  "'"+queryValues.get("codigo")+"'";

        database.update("dispositivos", values ,"codigoscan"+"="+id, null);
        database.close();
    }

////////////////*********************ELIMINA DISPOSITIVO**************///////////////

    public void dipsup (String iddisp) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("dispositivo", "id_dispositivo='"+iddisp+"'", null);
    }
}