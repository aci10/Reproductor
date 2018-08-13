package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Nota_Canvas_t {

    private int a_id;
    private Nota_t a_nota;
    private Compas_Canvas_t a_compas;
    private Compas_Canvas_t a_compas_0;

    private static int a_ultima_id = 0;

    private float [] a_x0;
    private float [] a_yf;

    private float [] a_pos0_desplazamiento;

    private float [] a_pos_en_vista;

    private int a_rejilla_inicial;
    private int a_num_bits;
    private String a_nombre;
    private int a_octava;

    // ---------------------------------------------------------------------------------------------

    public Nota_Canvas_t (Nota_t p_nota, Compas_Canvas_t p_compas, float p_left, float p_top, float p_right, float p_bottom)
    {
        if (p_nota != null)
        {
            a_id = a_ultima_id;
            a_ultima_id++;

            a_nota = p_nota;
            a_compas = p_compas;
            a_compas_0 = p_compas;

            a_rejilla_inicial = a_nota.nota_get_bit_inicial();
            a_num_bits = a_nota.nota_get_num_bits();
            a_nombre = a_nota.nota_get_nombre();
            a_octava = a_nota.nota_get_octava();

            a_x0 = new float [2];
            a_x0[0] = p_left;
            a_x0[1] = p_top;

            a_yf = new float [2];
            a_yf[0] = p_right;
            a_yf[1] = p_bottom;

            a_pos_en_vista = null;
        }
    }

    // ---------------------------------------------------------------------------------------------

    private int i_get_id()
    {
        return a_id;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean nt_compara_notas(Nota_Canvas_t p_nota)
    {
        return p_nota != null && p_nota.i_get_id() == a_id;
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_Canvas_t nt_canvas_hay_colision(float p_x, float p_y)
    {
        Nota_Canvas_t nota = null;

        if (p_x >= a_pos_en_vista[0] && p_x <= a_pos_en_vista[2]
            && p_y >= a_pos_en_vista[1] && p_y <= a_pos_en_vista[3])
        {
            nota = this;
            a_pos0_desplazamiento = new float [4];
            a_pos0_desplazamiento[0] = a_x0[0];
            a_pos0_desplazamiento[1] = a_x0[1];
            a_pos0_desplazamiento[2] = a_yf[0];
            a_pos0_desplazamiento[3] = a_yf[1];
        }

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    public String nt_canvas_valores()
    {
        return a_nota.nota_get_nombre() + " | " + a_nota.nota_get_duracion();
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_canvas_desplaza(Compas_Canvas_t p_compas, float p_x0_rejilla, float p_top_fila, int p_bit_inicial, String p_nombre_fila, int p_octava_fila)
    {
        float right = a_yf[0] - a_x0[0], bottom = a_yf[1] - a_x0[1];

        a_x0[0] = p_x0_rejilla;
        a_x0[1] = p_top_fila;
        a_yf[0] = a_x0[0] + right;
        a_yf[1] = a_x0[1] + bottom;

        a_rejilla_inicial = p_bit_inicial;
        a_nombre = p_nombre_fila;
        a_octava = p_octava_fila;

        if (!p_compas.cmp_compara_compases(a_compas))
        {
            a_compas.cmp_canvas_remove_nota(this);

            p_compas.cmp_canvas_set_nota(this);

            a_compas = p_compas;
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_canvas_editar_nota_reproductor(Partitura_t p_partitura)
    {
        a_compas_0.cmp_canvas_remove_nota(a_nota.nota_get_bit_inicial(), a_nota.nota_get_frecuencia());

        p_partitura.partitura_append_nota_a_compas(a_compas.cmp_canvas_get_id(), a_rejilla_inicial, a_num_bits, a_nombre, a_octava);
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_canvas_dibuja(
                        Canvas p_canvas,
                        float p_cmp_left, float p_cmp_left_0,
                        float [] p_x_vista,
                        float [] p_y_vista, float [] p_vista_0,
                        Paint p_pincel)
    {
        if (p_canvas != null && p_pincel != null)
        {
            float left, right, top, bottom;

            if (a_x0[0] < p_x_vista[0])
            {
                left = p_cmp_left;
                right = left + (a_yf[0] - p_x_vista[0]);
            }
            else
            {
                if (p_cmp_left_0 > p_x_vista[0])
                    left = p_cmp_left + (a_x0[0] - p_cmp_left_0);
                else
                    left = p_cmp_left + (a_x0[0] - p_x_vista[0]);

                if (a_yf[0] > p_x_vista[1])
                    right = p_canvas.getWidth();
                else
                    right = left + (a_yf[0] - a_x0[0]);
            }

            if (a_x0[1] < p_y_vista[0])
            {
                top = p_vista_0[1];
                bottom = top + (a_yf[1] - p_y_vista[0]);
            }
            else
            {
                top = p_vista_0[1] + (a_x0[1] - p_y_vista[0]);

                if (a_yf[1] > p_y_vista[1])
                    bottom = p_canvas.getHeight();
                else
                    bottom = top + (a_yf[1] - a_x0[1]);
            }

            a_pos_en_vista = new float [4];
            a_pos_en_vista[0] = left;
            a_pos_en_vista[1] = top;
            a_pos_en_vista[2] = right;
            a_pos_en_vista[3] = bottom;

            p_canvas.drawRect(left, top, right, bottom, p_pincel);
        }
    }
}
