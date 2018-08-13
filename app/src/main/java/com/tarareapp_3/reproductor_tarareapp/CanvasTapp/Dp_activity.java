package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Dp_activity extends Activity{

    Diagrama_Pianola_t a_diagrama;
    Partitura_t a_partitura;

    // ---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        a_partitura = new Partitura_t(
                "IntroGOT",
                60,
                6,
                8,
                "semicorchea");

        i_crea_juego_de_tronos(a_partitura);

        a_diagrama = new Diagrama_Pianola_t(this, a_partitura);

        setContentView(a_diagrama);
    }

    @Override public void onResume() {

        super.onResume();
    }
    @Override public void onPause() {

        super.onPause();
    }

    // ---------------------------------------------------------------------------------------------

    private void crea_boton_enlace(int p_id_vista, final Class<?> cls)
    {
        Button next = (Button) findViewById(p_id_vista);
        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(Dp_activity.this, cls);
                startActivity(myIntent);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------

    private void i_crea_juego_de_tronos(Partitura_t a_partitura)
    {
        a_partitura.partitura_append_nota_a_compas(0, 0, 2, "C", 4);

    }
}
