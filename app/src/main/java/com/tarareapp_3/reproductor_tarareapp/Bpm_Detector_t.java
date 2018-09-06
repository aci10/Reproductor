package com.tarareapp_3.reproductor_tarareapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Bpm_Detector_t extends Activity
{
    private int a_muestras;
    private double a_sumatorio;
    private int a_bpm;
    private long a_last_click;
    private boolean a_started;

    private String a_name;
    private int a_tempo;
    private int a_pulsos;
    private int a_valor_pulso;
    private String a_precision;
    private String a_clef;
    private String a_type_signature;
    private int a_q_signature;

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bpm_detector);

        a_muestras = 0;
        a_sumatorio = 0;
        a_bpm = 0;
        a_last_click = 0;
        a_started = false;

        a_name = getIntent().getExtras().getString("nombre");
        a_tempo = getIntent().getExtras().getInt("bpm");
        a_pulsos = getIntent().getExtras().getInt("pulsos_compas");
        a_valor_pulso = getIntent().getExtras().getInt("valor_pulso");
        a_precision = getIntent().getExtras().getString("precision");
        a_clef = getIntent().getExtras().getString("clef");
        a_type_signature = getIntent().getExtras().getString("type_signature");
        a_q_signature = getIntent().getExtras().getInt("q_signature");

        crea_boton_enlace(R.id.btnAccept, MainActivity.class);
        crea_boton_enlace(R.id.btnCancel, MainActivity.class);
        i_init_button_start();
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (a_started && event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (a_last_click > 0)
            {
                long current_time = System.currentTimeMillis();
                long diferencia = current_time - a_last_click;
                double media;

                a_sumatorio += diferencia;
                a_muestras++;
                a_last_click = current_time;

                media = a_sumatorio / a_muestras;

                a_bpm = (int)(60000 / media);

                TextView text = findViewById(R.id.textBPM);
                text.setText("" + a_bpm);
            }
            else
                a_last_click = System.currentTimeMillis();
        }

        return true;
    }

    // ---------------------------------------------------------------------------------------------

    private void crea_boton_enlace(final int p_id_vista, final Class<?> cls)
    {
        Button button = findViewById(p_id_vista);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(Bpm_Detector_t.this, cls);

                myIntent.putExtra("nombre", a_name);

                if (p_id_vista != R.id.btnCancel && a_bpm > 0)
                    myIntent.putExtra("bpm", a_bpm);
                else
                    myIntent.putExtra("bpm", a_tempo);

                myIntent.putExtra("pulsos_compas", a_pulsos);
                myIntent.putExtra("valor_pulso", a_valor_pulso);
                myIntent.putExtra("precision", a_precision);
                myIntent.putExtra("clef", a_clef);
                myIntent.putExtra("type_signature", a_type_signature);
                myIntent.putExtra("q_signatures", a_q_signature);

                startActivity(myIntent);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------

    private void i_init_button_start()
    {
        findViewById(R.id.btnStartStop).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) { a_started = !a_started; }
        });
    }
}
