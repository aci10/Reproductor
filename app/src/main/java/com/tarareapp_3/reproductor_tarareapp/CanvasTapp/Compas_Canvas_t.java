package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Compas_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Compas_Canvas_t {

    private Compas_t a_compas;

    private float [] a_x0;
    private static float [] a_yf = null;

    private int a_tipo_pincel;
    private static Paint [] a_pincel = null;
    private static Paint [][] a_pincel_rejilla = null;
    private static Paint [] a_pincel_notas = null;

    private float [][] av_rejillas;
    private ArrayList<Nota_Canvas_t> av_notas;

    // ---------------------------------------------------------------------------------------------

    private void i_crea_rejillas_compas(int p_num_rejillas)
    {
        float ancho;

        if (a_pincel_rejilla == null)
        {
            a_pincel_rejilla = new Paint[2][2];

            a_pincel_rejilla[0][0] = new Paint();
            a_pincel_rejilla[0][0].setColor(Color.GREEN);

            a_pincel_rejilla[0][1] = new Paint();
            a_pincel_rejilla[0][1].setColor(Color.WHITE);

            a_pincel_rejilla[1][0] = new Paint();
            a_pincel_rejilla[1][0].setColor(Color.YELLOW);

            a_pincel_rejilla[1][1] = new Paint();
            a_pincel_rejilla[1][1].setColor(Color.BLUE);
        }

        if (p_num_rejillas > 0)
            av_rejillas = new float[p_num_rejillas][2];
        else
            av_rejillas = new float[12][2];

        ancho = (a_x0[1] - a_x0[0]) / p_num_rejillas;

        for (int i = 0; i < av_rejillas.length; i++)
        {
            av_rejillas[i][0] = a_x0[0] + ancho * i;
            av_rejillas[i][1] = (a_x0[0] + ancho * i) + ancho;
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Compas_Canvas_t(Compas_t p_compas, float [] p_x0, float [] p_yf, int p_num_rejillas, int p_tipo_pincel)
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
                    a_yf[0] = p_yf[0];
                else
                    a_yf[0] = 0;

                if (p_yf[1] >= 0)
                    a_yf[1] = p_yf[1];
                else
                    a_yf[1] = 0;
            }

            if (a_pincel == null)
            {
                a_pincel = new Paint[2];
                a_pincel[0] = new Paint();
                a_pincel[0].setColor(Color.GREEN);
                a_pincel[0].setShadowLayer(4, 5, 5, Color.DKGRAY);

                a_pincel[1] = new Paint();
                a_pincel[1].setColor(Color.BLUE);
                a_pincel[1].setShadowLayer(4, 5, 5, Color.DKGRAY);
            }

            if (p_tipo_pincel % 2 == 0)
                a_tipo_pincel = 0;
            else
                a_tipo_pincel = 1;

            i_crea_rejillas_compas(p_num_rejillas);

            av_notas = new ArrayList<>();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void cmp_canvas_set_nota(Nota_t p_nota, int p_num_rejilla, float [] p_pos, float p_tamanyo_rejilla)
    {
        if (av_notas != null && p_nota != null && p_num_rejilla >= 0 && p_pos != null)
        {
            Nota_Canvas_t nota = p_nota.nota_crear_canvas_nota(av_rejillas[p_num_rejilla][0], p_pos, p_tamanyo_rejilla);

            if (nota != null)
            {
                av_notas.add(nota);

                if (a_pincel_notas == null)
                {
                    a_pincel_notas = new Paint [2];

                    a_pincel_notas[0] = new Paint();
                    a_pincel_notas[0].setColor(Color.GRAY);
                    a_pincel_notas[0].setShadowLayer(4, 3, 3, Color.DKGRAY);

                    a_pincel_notas[1] = new Paint();
                    a_pincel_notas[1].setColor(Color.RED);
                    a_pincel_notas[1].setShadowLayer(4, 3, 3, Color.DKGRAY);
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_dibuja_rejillas(Canvas p_canvas)
    {
        for (int i = 0; p_canvas != null && i < av_rejillas.length; i++)
        {
            Paint pincel;

            if (i % 2 == 0)
                pincel = a_pincel_rejilla[a_tipo_pincel][0];
            else
                pincel = a_pincel_rejilla[a_tipo_pincel][1];

            p_canvas.drawRect(av_rejillas[i][0], a_yf[1], av_rejillas[i][1], p_canvas.getHeight(), pincel);
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_dibuja_notas(Canvas p_canvas)
    {
        if (av_notas != null)
        {
            for (int i = 0; i < av_notas.size(); i++)
            {
                Paint pincel = a_pincel_notas[a_tipo_pincel];

                av_notas.get(i).nt_canvas_dibuja(p_canvas, pincel);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void cmp_dibuja(Canvas p_canvas, Paint p_pincel_negro)
    {
        if (p_canvas != null)
        {
            i_dibuja_rejillas(p_canvas);

            i_dibuja_notas(p_canvas);

            p_canvas.drawRect(a_x0[0], a_yf[0], a_x0[1], a_yf[1], a_pincel[a_tipo_pincel]);

            // Dibujamos la linea divisoria superior que marca la fila de esta nota
            p_canvas.drawLine(a_x0[1], a_yf[1], a_x0[1], p_canvas.getHeight(), p_pincel_negro);

            // Dibujamos el nombre de la nota
            p_canvas.drawText("C" + (a_compas.compas_get_id() + 1), a_x0[0] + ((a_x0[1] - a_x0[0]) / 2), a_yf[1] / 2, p_pincel_negro);
        }
    }
}
