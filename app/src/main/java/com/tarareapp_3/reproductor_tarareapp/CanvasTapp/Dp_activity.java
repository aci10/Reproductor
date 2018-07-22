package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tarareapp_3.reproductor_tarareapp.MainActivity;
import com.tarareapp_3.reproductor_tarareapp.R;

public class Dp_activity extends Activity{

    // ---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagrama_pianola);

        crea_boton_enlace(R.id.prev, MainActivity.class);;
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
}
