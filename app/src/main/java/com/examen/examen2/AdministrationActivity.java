package com.examen.examen2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class AdministrationActivity extends AppCompatActivity {

    private EditText etNombre, etSimbolo, etNumero;
    private Spinner spEstado;
    private Button btnInsertar, btnModificar, btnBorrar, btnVolver;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);

        etNombre = findViewById(R.id.etNombre);
        etSimbolo = findViewById(R.id.etSimbolo);
        etNumero = findViewById(R.id.etNumero);
        spEstado = findViewById(R.id.spEstado);

        btnInsertar = findViewById(R.id.btnInsertar);
        btnModificar = findViewById(R.id.btnModificar);
        btnBorrar = findViewById(R.id.btnBorrar);

        btnVolver = findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(v -> {
            finish();
        });

        fragmentManager = getSupportFragmentManager();

        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.stream(Estado.values()).map(Estado::toString).toArray(String[]::new));
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(estadoAdapter);

        btnInsertar.setOnClickListener(v -> {

            Elemento elemento = construirElemento();
            if (elemento != null && elemento.getNombre() != null) {
                List<Elemento> elementos = getPorNombre(elemento.getNombre());

                    if (!elementos.isEmpty()) {
                        DialogoNoInsertar dialogo = new DialogoNoInsertar();
                        dialogo.show(fragmentManager, getString(R.string.no_insertar));
                    } else {
                        operarBD(db -> {
                            ContentValues cv = new ContentValues();
                            cv.put("nombre", elemento.getNombre());
                            cv.put("simbolo", elemento.getSimbolo());
                            cv.put("numero", elemento.getNumero());
                            cv.put("estado", elemento.getEstado().toString());

                            long exito = db.insert("Elementos", null, cv);
                            if (exito > 0) {
                                Toast.makeText(this, getString(R.string.elemento2) + elemento.getNombre() + getText(R.string.insertado), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, getString(R.string.elemento2) + elemento.getNombre() + getText(R.string.nopudoinsertado), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
            } else {
                Toast.makeText(this, R.string.datos_erroneos, Toast.LENGTH_SHORT).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            Elemento elemento = construirElemento();
            if (elemento != null && elemento.getNombre() != null) {
                List<Elemento> elementos = getPorNombre(elemento.getNombre());

                if (elementos.size() != 1) {
                    DialogoNoModificar dialogo = new DialogoNoModificar();
                    dialogo.show(fragmentManager, getString(R.string.no_modificar));
                } else {

                    operarBD(db -> {
                        ContentValues cv = new ContentValues();
                        cv.put("nombre", elemento.getNombre());
                        cv.put("simbolo", elemento.getSimbolo());
                        cv.put("numero", elemento.getNumero());
                        cv.put("estado", elemento.getEstado().toString());

                        int exito = db.update("Elementos", cv, "LOWER(nombre) = ?", new String[]{elemento.getNombre().toLowerCase()});
                        if (exito == 1) {
                            Toast.makeText(this, getText(R.string.elemento2) + elemento.getNombre() + getText(R.string.modificado), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getText(R.string.elemento2) + elemento.getNombre() + getText(R.string.nopudomodificado), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } else {
                Toast.makeText(this, R.string.datos_erroneos, Toast.LENGTH_SHORT).show();
            }
        });

        btnBorrar.setOnClickListener(v -> {
            String nombreElem = etNombre.getText().toString();
            if (nombreElem != null && !nombreElem.isEmpty()) {
                List<Elemento> elementos = getPorNombre(nombreElem);

                if (elementos.size() != 1) {
                    DialogoNoModificar dialogo = new DialogoNoModificar();
                    dialogo.show(fragmentManager, getString(R.string.no_borrar));
                } else {
                    operarBD(db -> {
                        int exito = db.delete("Elementos", "LOWER(nombre) = ?", new String[]{nombreElem.toLowerCase()});

                        if (exito == 1) {
                            Toast.makeText(this, getText(R.string.elemento2) + nombreElem + getString(R.string.borrado), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getText(R.string.elemento2) + nombreElem + getString(R.string.nopudoborrado), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, R.string.datos_erroneos, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Elemento construirElemento() {
        try {
            Elemento elem = new Elemento()
                    .setNombre(etNombre.getText().toString().trim())
                    .setSimbolo(etSimbolo.getText().toString().trim())
                    .setNumero(Integer.parseInt(etNumero.getText().toString().trim()))
                    .setEstado(spEstado.getSelectedItem() != null ? Estado.valueOf(spEstado.getSelectedItem().toString()) : null);

            if (elem.getNombre() != null && !elem.getNombre().trim().isEmpty()) {
                return elem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void operarBD(Consumer<SQLiteDatabase> callback) {
        ElementosSQLiteHelper helper = new ElementosSQLiteHelper(this, "DBElementos", null, 1);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            callback.accept(db);
        }
    }

    private List<Elemento> getPorNombre(String nombre) {
        List<Elemento> elementos = new LinkedList<>();
        if (nombre != null && !nombre.trim().isEmpty()) {
            operarBD(db -> {
                Cursor cur = db.rawQuery("SELECT * FROM Elementos WHERE LOWER(nombre) = ?", new String[] {nombre.toLowerCase()});
                while (cur.moveToNext()) {
                    Integer id = cur.getInt(0);
                    String nom = cur.getString(1);
                    String simbolo = cur.getString(2);
                    Integer num = cur.getInt(3);
                    Estado estado = cur.getString(4) != null ? Estado.valueOf(cur.getString(4)) : null;
                    elementos.add(new Elemento().setId(id).setNombre(nom).setSimbolo(simbolo).setNumero(num).setEstado(estado));
                }
                cur.close();
            });
        }
        return elementos;
    }
}