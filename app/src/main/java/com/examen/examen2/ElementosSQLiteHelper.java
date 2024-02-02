package com.examen.examen2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ElementosSQLiteHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ELEMENTOS =
            "CREATE TABLE Elementos (id INTEGER PRIMARY KEY NOT NULL," +
                    " nombre TEXT NOT NULL, " +
                    " simbolo TEXT NOT NULL, " +
                    " numero INTEGER  NOT NULL, " +
                    "estado TEXT  NOT NULL)";

    private static final String SQL_INSERT_ELEMENTOS =
            "INSERT INTO Elementos (nombre, simbolo, numero, estado) " +
                    "VALUES ('HELIO', 'He', 2, 'GAS'), " +
                    "('HIERRO', 'Fe', 26, 'SOLIDO'), " +
                    "('MERCURIO', 'Hg', 80, 'LIQUIDO')";

    public ElementosSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ELEMENTOS);
        db.execSQL(SQL_INSERT_ELEMENTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] campos = new String[] {"dni", "nombre", "imagen", "numero"};
        List<Elemento> elementos = new LinkedList<>();

        Cursor cur = db.query("Elementos", campos, null, null, null, null, null);
        while (cur.moveToNext()) {
            Integer id = cur.getInt(0);
            String nom = cur.getString(1);
            String simbolo = cur.getString(2);
            Integer num = cur.getInt(3);
            Estado estado = cur.getString(4) != null ? Estado.valueOf(cur.getString(3)) : null;

            elementos.add(new Elemento().setId(id).setNombre(nom).setSimbolo(simbolo).setNumero(num).setEstado(estado));
        }
        cur.close();
        db.execSQL("DROP TABLE IF EXISTS `Elementos`");
        db.execSQL(SQL_CREATE_ELEMENTOS);

        elementos.forEach(c -> {
            ContentValues cv = new ContentValues();
            cv.put("id", c.getId());
            cv.put("nombre", c.getNombre());
            cv.put("simbolo", c.getSimbolo());
            cv.put("numero", c.getNumero());
            cv.put("estado", c.getEstado().toString());

            db.insert("Elementos", null, cv);
        });

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
