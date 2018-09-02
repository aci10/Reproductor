package com.tarareapp_3.reproductor_tarareapp.Reproductor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.tarareapp_3.reproductor_tarareapp.MainActivity;
import com.tarareapp_3.reproductor_tarareapp.MyNumberPicker;
import com.tarareapp_3.reproductor_tarareapp.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class rep_activity extends Activity {

    Partitura_t a_partitura;

    boolean a_en_reproduccion;

    // ---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_bpm);

        a_partitura = new Partitura_t(
                "IntroGOT",
                60,
                6,
                8,
                "semicorchea");

        i_crea_juego_de_tronos();

        a_en_reproduccion = false;

        crea_boton_enlace(R.id.prev, MainActivity.class);
    }

    // ---------------------------------------------------------------------------------------------

    private void crea_boton_enlace(int p_id_vista, final Class<?> cls)
    {
        Button next = (Button) findViewById(p_id_vista);
        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(rep_activity.this, cls);
                startActivity(myIntent);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------

    public void detener_reproduccion(View view)
    {
        a_partitura.partitura_detener_reproduccion(false);
    }

    // ---------------------------------------------------------------------------------------------

    public void pausar_reproduccion(View view)
    {
        a_partitura.partitura_detener_reproduccion(true);
    }

    // ---------------------------------------------------------------------------------------------

    public void exportar_mxml(View view)
    {
        Context ctx = getApplicationContext();

        a_partitura.partitura_exportar_mxml(ctx);
    }

    /*public void abrir_mxml(View view)
    {
        Context ctx = getApplicationContext();

        a_partitura.partitura_abrir_mxml(ctx);
    }*/

    // ---------------------------------------------------------------------------------------------

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
