package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Compas_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Compas_Canvas_t {

    private Compas_t a_compas;

    private float [] a_x0;
    private float [] a_yf;

    private float [] av_rejillas;

    private ArrayList<Nota_Canvas_t> av_notas;

    // ---------------------------------------------------------------------------------------------

    private void i_crea_rejillas_compas(int p_num_rejillas)
    {
        float ancho;

        if (p_num_rejillas > 0)
            av_rejillas = new float[p_num_rejillas];
        else
            av_rejillas = new float[12];

        ancho = (a_yf[0] - a_x0[0]) / p_num_rejillas;

        for (int i = 0; i < av_rejillas.length; i++)
        {
            av_rejillas[i] = i * ancho;
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Compas_Canvas_t(Compas_t p_compas, float [] p_x0, float [] p_yf, int p_num_rejillas)
    {
        if (p_compas != null)
        {
            a_compas = p_compas;

            if (p_x0 != null)
            {
                a_x0 = new float [2];

                if (p_x0[0] >= 0)
                    a_x0[0] = p_x0[0];
                else
                    a_x0[0] = 0;

                if (p_x0[1] >= 0)
                    a_x0[1] = p_x0[1];
                else
                    a_x0[1] = 0;
            }

            if (p_yf != null)
            {
                a_yf = new float [2];

                if (p_yf[0] >= 0)
                    a_yf[0] = p_x0[0];
                else
                    a_yf[0] = 0;

                if (p_yf[1] >= 0)
                    a_yf[1] = p_x0[1];
                else
                    a_yf[1] = 0;
            }

            i_crea_rejillas_compas(p_num_rejillas);

            av_notas = new ArrayList<>();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void cmp_canvas_set_nota(Nota_t p_nota, int p_num_rejilla, float p_tamanyo_rejilla)
    {
        if (av_notas != null && p_nota != null && p_num_rejilla >= 0)
        {
            Nota_Canvas_t nota = p_nota.nota_crear_canvas_nota(av_rejillas[p_num_rejilla], a_x0[1], a_yf[1], p_tamanyo_rejilla);

            if (nota != null)
                av_notas.add(nota);
        }
    }
}
