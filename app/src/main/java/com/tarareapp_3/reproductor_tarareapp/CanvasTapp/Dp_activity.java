package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.tarareapp_3.reproductor_tarareapp.Grabadora.Data_Note_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Dp_activity extends Activity{

    Diagrama_Pianola_t a_diagrama;
    Partitura_t a_partitura;

    // ---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String nombre = intent.getExtras().getString("nombre");
        int bpm = intent.getExtras().getInt("bpm");
        int pulsos_compas = intent.getExtras().getInt("pulsos_compas");
        int valor_pulso = intent.getExtras().getInt("valor_pulso");
        int num_bits = intent.getExtras().getInt("num_bits");

        a_partitura = new Partitura_t(
                nombre,
                bpm,
                pulsos_compas,
                valor_pulso,
                num_bits);

        Bundle args = intent.getBundleExtra("notas");
        ArrayList<Data_Note_t> notes = (ArrayList<Data_Note_t>) args.getSerializable("ARRAYLIST");

        i_add_notes_to_score(notes);

        a_diagrama = new Diagrama_Pianola_t(this, a_partitura);

        setContentView(a_diagrama);
    }

    @Override public void onResume() {

        super.onResume();
    }
    @Override public void onPause() {

        super.onPause();
    }

    private void i_add_notes_to_score(ArrayList<Data_Note_t> p_notes)
    {
        if (p_notes != null)
        {
            for (int i = 0; i < p_notes.size(); i++)
            {
                Data_Note_t nota = p_notes.get(i);

                if (nota != null)
                {
                    a_partitura.partitura_append_nota_a_compas(
                                    nota.dn_get_id_compas(),
                                    nota.dn_get_bit_inicial(),
                                    nota.dn_get_num_bits(),
                                    nota.dn_get_nombre(),
                                    nota.dn_get_octava());
                }
            }
        }
    }
}
