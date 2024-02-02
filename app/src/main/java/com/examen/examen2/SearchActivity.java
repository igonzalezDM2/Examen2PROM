package com.examen.examen2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SearchActivity extends AppCompatActivity {
    private int veces = 0;
    private EditText etBusqueda;
    private Button btnBuscar, btnVolver;

    private ListView lvElementos;

    FragmentManager fragmentManager = null;

    LinkedList<Elemento> listaElementos = new LinkedList<>();
    AdaptadorElemento adaptador = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etBusqueda = findViewById(R.id.etBusqueda);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnVolver = findViewById(R.id.btnVolver);
        lvElementos = findViewById(R.id.lvElementos);

        fragmentManager = getSupportFragmentManager();


        adaptador = new AdaptadorElemento(this, listaElementos);

        lvElementos.setAdapter(adaptador);

        btnBuscar.setOnClickListener(v -> {
            if (btnBuscar.getText().toString().equalsIgnoreCase("buscar")) {
                veces++;
                String strCampo = etBusqueda.getText().toString().toLowerCase();
                if (strCampo != null && !strCampo.trim().isEmpty()) {
                    String cadena = "%" + strCampo + "%";
                    operarBD(db -> {
                        Cursor cur = db.rawQuery("SELECT * FROM Elementos WHERE LOWER(nombre) LIKE ?", new String[] {cadena});
                        List<Elemento> elementos = new LinkedList<>();
                        while (cur.moveToNext()) {
                            Integer id = cur.getInt(0);
                            String nom = cur.getString(1);
                            String simbolo = cur.getString(2);
                            Integer num = cur.getInt(3);
                            Estado estado = cur.getString(4) != null ? Estado.valueOf(cur.getString(4)) : null;
                            elementos.add(new Elemento().setId(id).setNombre(nom).setSimbolo(simbolo).setNumero(num).setEstado(estado));
                        }
                        cur.close();

                        if (!elementos.isEmpty()) {
                            btnBuscar.setText("Limpiar");
                        } else {
                            DialogoSinResultados dialogo = new DialogoSinResultados();
                            dialogo.show(fragmentManager, "SIN RESULTADOS");
                        }

                        listaElementos.clear();
                        listaElementos.addAll(elementos);
                        adaptador.notifyDataSetChanged();
                    });
                }
            } else if (btnBuscar.getText().toString().equalsIgnoreCase("limpiar")) {
                etBusqueda.setText("");
                listaElementos.clear();
                adaptador.notifyDataSetChanged();
                btnBuscar.setText("Buscar");
            }
        });

        btnVolver.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("veces", veces);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }

    protected void operarBD(Consumer<SQLiteDatabase> callback) {
        ElementosSQLiteHelper helper = new ElementosSQLiteHelper(this, "DBElementos", null, 1);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            callback.accept(db);
        }
    }

}