package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tarareapp_3.reproductor_tarareapp.Color_Picker_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Compas_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Compas_Canvas_t {

    private Compas_t a_compas;

    private float [] a_x0;
    private static float [] a_yf = null;

    private float [] a_pos_en_canvas;

    private int a_tipo_pincel;
    private static Paint [][] a_pincel = null;
    private static Paint [][] a_pincel_rejilla = null;
    private static Paint [] a_pincel_notas = null;

    private float [][] av_rejillas;
    private ArrayList<Nota_Canvas_t> av_notas;
    private ArrayList<Nota_Canvas_t> av_notas_ligadas;

    // ---------------------------------------------------------------------------------------------

    private void i_crea_rejillas_compas(int p_num_rejillas)
    {
        float ancho;

        a_pos_en_canvas = null;

        if (a_pincel_rejilla == null)
        {
            Color_Picker_t picker = new Color_Picker_t();

            a_pincel_rejilla = new Paint[2][2];

            a_pincel_rejilla[0][0] = picker.getPincel(Color_Picker_t.type_color_t.MORADO);
            a_pincel_rejilla[0][1] = picker.getPincel(Color_Picker_t.type_color_t.VIOLETA_LIGHT);

            a_pincel_rejilla[1][0] = picker.getPincel(Color_Picker_t.type_color_t.MORADO_LIGHT);
            a_pincel_rejilla[1][1] = picker.getPincel(Color_Picker_t.type_color_t.BLANCO_VIOLETA);
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
                Color_Picker_t picker = new Color_Picker_t();

                a_pincel = new Paint[2][2];
                a_pincel[0][0] = picker.getPincel(Color_Picker_t.type_color_t.MORADO);
                a_pincel[0][0].setShadowLayer(4, 0, 5, Color.BLACK);
                a_pincel[0][1] = picker.getPincel(Color_Picker_t.type_color_t.BLANCO);
                a_pincel[0][1].setTextSize(30);
                a_pincel[0][1].setStrokeWidth(1);

                a_pincel[1][0] = picker.getPincel(Color_Picker_t.type_color_t.MORADO_LIGHT);
                a_pincel[1][0].setShadowLayer(4, 0, 5, Color.BLACK);
                a_pincel[1][1] = picker.getPincel(Color_Picker_t.type_color_t.BLANCO);
                a_pincel[1][1].setTextSize(30);
                a_pincel[1][1].setStrokeWidth(1);
            }

            if (p_tipo_pincel % 2 == 0)
                a_tipo_pincel = 0;
            else
                a_tipo_pincel = 1;

            i_crea_rejillas_compas(p_num_rejillas);

            av_notas = new ArrayList<>();
            av_notas_ligadas = new ArrayList<>();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public int cmp_canvas_get_id()
    {
        return a_compas.compas_get_id();
    }

    // ---------------------------------------------------------------------------------------------

    public int cmp_get_tipo_pincel()
    {
        return a_tipo_pincel;
    }

    // ---------------------------------------------------------------------------------------------

    public float [] cmp_get_pos()
    {
        return a_x0;
    }

    // ---------------------------------------------------------------------------------------------

    public int cmp_get_num_rejilla_marcada(float p_x)
    {
        int rejilla = -1;

        if (p_x >= a_x0[0] && p_x <= a_x0[1])
        {
            for (int i = 0; i < av_rejillas.length; i++)
            {
                if (p_x >= av_rejillas[i][0] && p_x <= av_rejillas[i][1])
                {
                    rejilla = i;
                    break;
                }
            }
        }

        return rejilla;
    }

    // ---------------------------------------------------------------------------------------------

    public float [] cmp_get_pos_rejilla_marcada(float p_x)
    {
        float [] pos = null;

        if (p_x >= a_x0[0] && p_x <= a_x0[1])
        {
            for (int i = 0; i < av_rejillas.length; i++)
            {
                if (p_x >= av_rejillas[i][0] && p_x <= av_rejillas[i][1])
                {
                    pos = new float [2];
                    pos[0] = av_rejillas[i][0];
                    pos[1] = av_rejillas[i][1];
                    break;
                }
            }
        }

        return pos;
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_Canvas_t cmp_canvas_set_nota(Nota_t p_nota, int p_num_rejilla, float [] p_pos, float p_tamanyo_rejilla)
    {
        Nota_Canvas_t nota = null;

        if (av_notas != null && p_nota != null && p_num_rejilla >= 0 && p_pos != null)
        {
            nota = p_nota.nota_crear_canvas_nota(this, av_rejillas[p_num_rejilla][0], p_pos, p_tamanyo_rejilla);

            if (nota != null)
            {
                av_notas.add(nota);

                if (a_pincel_notas == null)
                {
                    Color_Picker_t picker = new Color_Picker_t();

                    a_pincel_notas = new Paint [2];

                    a_pincel_notas[0] = picker.getPincel(Color_Picker_t.type_color_t.SALMON);
                    a_pincel_notas[0].setShadowLayer(4, 3, 3, Color.BLACK);

                    a_pincel_notas[1] = picker.getPincel(Color_Picker_t.type_color_t.VIOLETA);
                    a_pincel_notas[1].setShadowLayer(4, 3, 3, Color.BLACK);
                }
            }
            else if (p_nota.nota_es_hija())
            {
                Nota_Canvas_t nota_ligada = p_nota.nota_get_nt_canvas_padre();

                if (nota_ligada != null)
                    av_notas_ligadas.add(nota_ligada);
            }
        }

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    public void cmp_canvas_set_nota(Nota_Canvas_t p_nota)
    {
        if (p_nota != null)
            av_notas.add(p_nota);
    }

    // ---------------------------------------------------------------------------------------------

    public void cmp_canvas_remove_nota(Nota_Canvas_t p_nota)
    {
        if (p_nota != null)
        {
            for (int i = 0; i < av_notas.size(); i++)
            {
                if (av_notas.get(i).nt_compara_notas(p_nota))
                    av_notas.remove(i);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void cmp_canvas_remove_nota(int p_bit, double p_frecuencia)
    {
        a_compas.compas_borra_nota(p_bit, p_frecuencia, false);
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_Canvas_t cmp_get_nota_pos(float p_x, float p_y, float p_tamanyo_rejilla)
    {
        Nota_Canvas_t nota = null;

        for (int i = 0; nota == null && i < av_notas.size(); i++)
        {
            nota = av_notas.get(i).nt_canvas_hay_colision(p_x, p_y, p_tamanyo_rejilla);
        }

        if (nota == null)
        {
            for (int i = 0; nota == null && i < av_notas_ligadas.size(); i++)
            {
                nota = av_notas_ligadas.get(i).nt_canvas_hay_colision(p_x, p_y, p_tamanyo_rejilla);
            }
        }

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    private Compas_t cmp_get_compas()
    {
        return a_compas;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean cmp_compara_compases(Compas_Canvas_t p_compas)
    {
        boolean son_iguales = false;

        if (p_compas != null)
            son_iguales = a_compas.compas_comparar(p_compas.cmp_get_compas());

        return son_iguales;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean cmp_busca_colision_notas(Nota_Canvas_t p_nota)
    {
        boolean hay_colision = false;

        for (int i = 0; !hay_colision && i < av_notas.size(); i++)
        {
            hay_colision = av_notas.get(i).nt_canvas_hay_colision_notas(p_nota);
        }

        return hay_colision;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_dibuja_rejillas(Canvas p_canvas, float p_cmp_left, float [] p_x_vista)
    {
        for (int i = 0; p_canvas != null && i < av_rejillas.length; i++)
        {
            float left, right;
            Paint pincel;

            if (av_rejillas[i][0] < p_x_vista[0])
            {
                left = p_cmp_left;
                right = left + (av_rejillas[i][1] - p_x_vista[0]);
            }
            else
            {
                if (a_x0[0] > p_x_vista[0])
                    left = p_cmp_left + (av_rejillas[i][0] - a_x0[0]);
                else
                    left = p_cmp_left + (av_rejillas[i][0] - p_x_vista[0]);

                if (av_rejillas[i][1] > p_x_vista[1])
                    right = p_canvas.getWidth();
                else
                    right = left + (av_rejillas[i][1] - av_rejillas[i][0]);
            }

            pincel = a_pincel_rejilla[a_tipo_pincel][0];

            p_canvas.drawRect(left, a_yf[1], right, p_canvas.getHeight(), pincel);
            p_canvas.drawLine(left, a_yf[1], left, p_canvas.getHeight(), a_pincel_rejilla[a_tipo_pincel][1]);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public boolean cmp_dibuja_nota_desplazada(Nota_Canvas_t p_nota, float p_x, float p_top, String p_nombre_fila, int p_octava_fila)
    {
        boolean nota_dibujada = false;

        if (p_nota != null && p_x >= a_x0[0] && p_x <= a_x0[1])
        {
            for (int i = 0; i < av_rejillas.length; i++)
            {
                if (p_x >= av_rejillas[i][0] && p_x <= av_rejillas[i][1])
                {
                    nota_dibujada = true;
                    p_nota.nt_canvas_desplaza(this, av_rejillas[i][0], p_top, i, p_nombre_fila, p_octava_fila);
                }
            }
        }

        return nota_dibujada;
    }

    // ---------------------------------------------------------------------------------------------

    public void cmp_dibuja(Canvas p_canvas, float [] p_x_vista, float [] p_vista_0, Paint p_pincel_negro)
    {
        if (p_canvas != null)
        {
            float left, right;

            if (a_x0[0] < p_x_vista[0])
            {
                left = p_vista_0[0];
                right = left + (a_x0[1] - p_x_vista[0]);
            }
            else
            {
                left = p_vista_0[0] + (a_x0[0] - p_x_vista[0]);

                if (a_x0[1] > p_x_vista[1])
                    right = p_canvas.getWidth();
                else
                    right = left + (a_x0[1] - a_x0[0]);
            }

            if (a_pos_en_canvas == null)
                a_pos_en_canvas = new float [2];

            a_pos_en_canvas[0] = left;
            a_pos_en_canvas[1] = right;

            i_dibuja_rejillas(p_canvas, left, p_x_vista);

            p_canvas.drawLine(right, a_yf[1], right, p_canvas.getHeight(), p_pincel_negro);

            p_canvas.drawRect(left, a_yf[0], right, a_yf[1], a_pincel[a_tipo_pincel][0]);

            float tamanyo = right - left;

            if ((right - left) >= ((a_x0[1] - a_x0[0]) / 2))
                p_canvas.drawText("C" + (a_compas.compas_get_id() + 1), left + ((tamanyo / 16) * 7), (a_yf[1] / 3) * 2, a_pincel[a_tipo_pincel][1]);
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_calcula_posicion_en_canvas(Canvas p_canvas, float [] p_x_vista, float [] p_y_vista, float [] p_vista_0)
    {
        float left, right;

        if (a_x0[0] < p_x_vista[0])
        {
            left = p_vista_0[0];
            right = left + (a_x0[1] - p_x_vista[0]);
        }
        else
        {
            left = p_vista_0[0] + (a_x0[0] - p_x_vista[0]);

            if (a_x0[1] > p_x_vista[1])
                right = p_canvas.getWidth();
            else
                right = left + (a_x0[1] - a_x0[0]);
        }

        if (a_pos_en_canvas == null)
            a_pos_en_canvas = new float [2];

        a_pos_en_canvas[0] = left;
        a_pos_en_canvas[1] = right;
    }

    // ---------------------------------------------------------------------------------------------

    public void cmp_dibuja_notas(Canvas p_canvas, float [] p_x_vista, float [] p_y_vista, float [] p_vista_0, boolean p_es_primer_compas)
    {
        if (p_canvas != null && a_pos_en_canvas != null && av_notas != null && a_pincel_notas != null)
        {
            Paint pincel = a_pincel_notas[a_tipo_pincel];

            for (int i = 0; i < av_notas.size(); i++)
            {
                av_notas.get(i).nt_canvas_dibuja(p_canvas, a_pos_en_canvas[0], a_x0[0], p_x_vista, p_y_vista, p_vista_0, pincel);
            }

            if (p_es_primer_compas && av_notas_ligadas != null)
            {
                Compas_Canvas_t compas_ligadura;

                for (int i = 0; i < av_notas_ligadas.size(); i++)
                {
                    if (av_notas_ligadas.get(i) != null)
                    {
                        compas_ligadura = av_notas_ligadas.get(i).nt_canvas_get_compas();
                        pincel = a_pincel_notas[compas_ligadura.cmp_get_tipo_pincel()];

                        if (compas_ligadura.a_pos_en_canvas == null)
                            compas_ligadura.i_calcula_posicion_en_canvas(p_canvas, p_x_vista, p_y_vista, p_vista_0);

                        if (compas_ligadura != null && pincel != null && compas_ligadura.a_pos_en_canvas != null)
                        {
                            av_notas_ligadas.get(i).nt_canvas_dibuja(
                                        p_canvas,
                                        compas_ligadura.a_pos_en_canvas[0],
                                        compas_ligadura.a_x0[0],
                                        p_x_vista, p_y_vista,
                                        p_vista_0,
                                        pincel);
                        }
                    }
                }
            }
        }
    }
}
