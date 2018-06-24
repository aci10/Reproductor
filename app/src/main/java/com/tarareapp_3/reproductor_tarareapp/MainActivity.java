package com.tarareapp_3.reproductor_tarareapp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    Partitura_t a_partitura;

    boolean a_en_reproduccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("MOSTRAR MENSAJE");

        a_partitura = new Partitura_t(
                        "Intro_got",
                        60,
                        6,
                        8,
                        "semicorchea");

        i_crea_juego_de_tronos();

        a_en_reproduccion = false;
    }

    public void addNota(View view){
        EditText et_indice_compas;
        EditText et_bit_inicial;
        EditText et_bit_final;
        Spinner sp_nombre;
        MyNumberPicker np_octava;

        et_indice_compas = (EditText)findViewById(R.id.compasEditText);
        int indice_compas = Integer.parseInt(et_indice_compas.getText().toString());

        et_bit_inicial = (EditText)findViewById(R.id.bitInicialText);
        int bit_inicial = Integer.parseInt(et_bit_inicial.getText().toString());

        et_bit_final = (EditText)findViewById(R.id.bitFinalText);
        int bit_final = Integer.parseInt(et_bit_final.getText().toString());

        sp_nombre = (Spinner)findViewById(R.id.notaSpinner);
        String nombre = sp_nombre.getSelectedItem().toString();

        np_octava = (MyNumberPicker)findViewById(R.id.octavaPicker);
        int octava = np_octava.getValue();

        a_partitura.partitura_append_nota_a_compas(
                indice_compas,
                bit_inicial,
                bit_final,
                nombre,
                octava);

        a_partitura.partitura_muestra_vista();
    }

    public void reproducirPartitura(View view)
    {
        if (a_partitura != null)
        {
            a_en_reproduccion = true;
            a_partitura.partitura_reproducir_notas_compases();
        }
    }

    public void reproducirPartitura_desde_bit_x(View view)
    {
        if (a_partitura != null)
        {
            EditText et_indice_compas;
            EditText et_bit_inicial;

            et_indice_compas = findViewById(R.id.compasEditText);
            int indice_compas = Integer.parseInt(et_indice_compas.getText().toString());

            et_bit_inicial = findViewById(R.id.bitInicialText);
            int bit_inicial = Integer.parseInt(et_bit_inicial.getText().toString());

            a_partitura.partitura_reproducir_notas_compases(indice_compas, bit_inicial);
        }
    }

    public void detener_reproduccion(View view)
    {
        a_partitura.partitura_detener_reproduccion(false);
    }

    public void pausar_reproduccion(View view)
    {
        a_partitura.partitura_detener_reproduccion(true);
    }

    public void exportar_mxml(View view)
    {
        Context ctx = getApplicationContext();

        a_partitura.partitura_exportar_mxml(ctx);
    }

    private void i_crea_juego_de_tronos()
    {
        a_partitura.partitura_append_nota_a_compas(0, 0, 2, "A", 4);
        a_partitura.partitura_append_nota_a_compas(0, 2, 2, "D", 4);

        a_partitura.partitura_append_nota_a_compas(0, 4, 1, "F", 4);
        a_partitura.partitura_append_nota_a_compas(0, 5, 1, "G", 4);

        a_partitura.partitura_append_nota_a_compas(0, 6, 2, "A", 4);
        a_partitura.partitura_append_nota_a_compas(0, 8, 2, "D", 4);

        a_partitura.partitura_append_nota_a_compas(0, 10, 1, "F", 4);
        a_partitura.partitura_append_nota_a_compas(0, 11, 1, "G", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(1, 0, 2, "A", 4);
        a_partitura.partitura_append_nota_a_compas(1, 2, 2, "D", 4);

        a_partitura.partitura_append_nota_a_compas(1, 4, 1, "F", 4);
        a_partitura.partitura_append_nota_a_compas(1, 5, 1, "G", 4);

        a_partitura.partitura_append_nota_a_compas(1, 6, 2, "A", 4);
        a_partitura.partitura_append_nota_a_compas(1, 8, 2, "D", 4);

        a_partitura.partitura_append_nota_a_compas(1, 10, 1, "F", 4);
        a_partitura.partitura_append_nota_a_compas(1, 11, 1, "G", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(2, 0, 2, "A", 4);
        a_partitura.partitura_append_nota_a_compas(2, 2, 2, "D", 4);

        a_partitura.partitura_append_nota_a_compas(2, 4, 1, "F#", 4);
        a_partitura.partitura_append_nota_a_compas(2, 5, 1, "G", 4);

        a_partitura.partitura_append_nota_a_compas(2, 6, 2, "A", 4);
        a_partitura.partitura_append_nota_a_compas(2, 8, 2, "D", 4);

        a_partitura.partitura_append_nota_a_compas(2, 10, 1, "F#", 4);
        a_partitura.partitura_append_nota_a_compas(2, 11, 1, "G", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(3, 0, 2, "A", 4);
        a_partitura.partitura_append_nota_a_compas(3, 2, 2, "D", 4);

        a_partitura.partitura_append_nota_a_compas(3, 4, 1, "F#", 4);
        a_partitura.partitura_append_nota_a_compas(3, 5, 1, "G", 4);

        a_partitura.partitura_append_nota_a_compas(3, 6, 2, "A", 4);
        a_partitura.partitura_append_nota_a_compas(3, 8, 2, "D", 4);

        a_partitura.partitura_append_nota_a_compas(3, 10, 1, "F#", 4);
        a_partitura.partitura_append_nota_a_compas(3, 11, 1, "G", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(4, 0, 6, "A", 4);
        a_partitura.partitura_append_nota_a_compas(4, 6, 6, "D", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(5, 0, 1, "F", 4);
        a_partitura.partitura_append_nota_a_compas(5, 1, 1, "G", 4);

        a_partitura.partitura_append_nota_a_compas(5, 2, 4, "A", 4);
        a_partitura.partitura_append_nota_a_compas(5, 6, 4, "D", 4);

        a_partitura.partitura_append_nota_a_compas(5, 10, 1, "F", 4);
        a_partitura.partitura_append_nota_a_compas(5, 11, 1, "G", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(6, 0, 24, "E", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(8, 0, 6, "G", 4);
        a_partitura.partitura_append_nota_a_compas(8, 6, 6, "C", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(9, 0, 1, "F", 4);
        a_partitura.partitura_append_nota_a_compas(9, 1, 1, "E", 4);

        a_partitura.partitura_append_nota_a_compas(9, 2, 4, "G", 4);
        a_partitura.partitura_append_nota_a_compas(9, 6, 4, "C", 4);

        a_partitura.partitura_append_nota_a_compas(9, 10, 1, "F", 4);
        a_partitura.partitura_append_nota_a_compas(9, 11, 1, "E", 4);

        //******************************************************************************************

        a_partitura.partitura_append_nota_a_compas(10, 0, 24, "D", 4);

        a_partitura.partitura_muestra_vista();
    }
}

