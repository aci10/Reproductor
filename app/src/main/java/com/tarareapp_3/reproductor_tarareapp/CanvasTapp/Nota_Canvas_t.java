package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

import java.util.ArrayList;

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

    public enum type_collition_t
    {
        COLLITION_UNDEFINED,
        COLLITION_CENTER,
        COLLITION_LEFT,
        COLLITION_RIGHT
    }

    private type_collition_t ae_collition_detected;

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
            a_num_bits =  a_nota.nota_get_total_bits();
            a_nombre = a_nota.nota_get_nombre();
            a_octava = a_nota.nota_get_octava();

            a_x0 = new float [2];
            a_x0[0] = p_left;
            a_x0[1] = p_top;

            a_yf = new float [2];
            a_yf[0] = p_right;
            a_yf[1] = p_bottom;

            a_pos_en_vista = null;

            ae_collition_detected = type_collition_t.COLLITION_UNDEFINED;
        }
    }

    // ---------------------------------------------------------------------------------------------

    private int i_get_id()
    {
        return a_id;
    }

    // ---------------------------------------------------------------------------------------------

    public Compas_Canvas_t nt_canvas_get_compas() {
        return a_compas;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean nt_compara_notas(Nota_Canvas_t p_nota)
    {
        return p_nota != null && p_nota.i_get_id() == a_id;
    }

    // ---------------------------------------------------------------------------------------------

    public type_collition_t nt_canvas_get_collition_detected()
    {
        return ae_collition_detected;
    }

    // ---------------------------------------------------------------------------------------------

    public float nt_canvas_get_left()
    {
        if (a_pos0_desplazamiento != null)
            return a_pos0_desplazamiento[0];
        else
            return a_x0[0];
    }

    // ---------------------------------------------------------------------------------------------

    public float nt_canvas_get_right()
    {
        if (a_pos0_desplazamiento != null)
            return a_pos0_desplazamiento[2];
        else
            return a_yf[0];
    }

    // ---------------------------------------------------------------------------------------------

    public float [] nt_canvas_get_pos_en_vista()
    {
        return a_pos_en_vista;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_detect_collition(float p_x, float p_y, float tamanyo_rejilla)
    {
        ae_collition_detected = type_collition_t.COLLITION_UNDEFINED;

        if (a_pos_en_vista != null)
        {
            float tamanyo_nota = a_pos_en_vista[2] - a_pos_en_vista[0];

            if (p_y >= a_pos_en_vista[1] && p_y <= a_pos_en_vista[3])
            {
                if (tamanyo_nota <= tamanyo_rejilla * 2)
                {
                    if (p_x <= a_pos_en_vista[2] + tamanyo_rejilla && p_x > a_pos_en_vista[2] - (tamanyo_rejilla / 2))
                    {
                        ae_collition_detected = type_collition_t.COLLITION_RIGHT;
                    }
                    else if (p_x >= a_pos_en_vista[0] - tamanyo_rejilla && p_x <= a_pos_en_vista[0] + (tamanyo_rejilla / 2))
                    {
                        ae_collition_detected = type_collition_t.COLLITION_LEFT;
                    }
                    else if (p_x >= a_pos_en_vista[0] && p_x <= a_pos_en_vista[2])
                        ae_collition_detected = type_collition_t.COLLITION_CENTER;
                }
                else
                {
                    if (p_x <= a_pos_en_vista[2] + tamanyo_rejilla && p_x > a_pos_en_vista[2] - tamanyo_nota * 0.2 )
                    {
                        ae_collition_detected = type_collition_t.COLLITION_RIGHT;
                    }
                    else if (p_x >= a_pos_en_vista[0] - tamanyo_rejilla && p_x <= a_pos_en_vista[0] + tamanyo_nota * 0.2)
                    {
                        ae_collition_detected = type_collition_t.COLLITION_LEFT;
                    }
                    else if (p_x >= a_pos_en_vista[0] && p_x <= a_pos_en_vista[2])
                        ae_collition_detected = type_collition_t.COLLITION_CENTER;
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_canvas_set_type_collition(type_collition_t p_tipo)
    {
        ae_collition_detected = p_tipo;
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_Canvas_t nt_canvas_hay_colision(float p_x, float p_y, float p_tamanyo_rejilla)
    {
        Nota_Canvas_t nota = null;

        i_detect_collition(p_x, p_y, p_tamanyo_rejilla);

        if (ae_collition_detected != type_collition_t.COLLITION_UNDEFINED)
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

    public boolean nt_canvas_hay_colision_notas(Nota_Canvas_t p_nota)
    {
        boolean hay_colision = false;

        if (p_nota != null && p_nota.a_id != a_id)
        {
            if (p_nota.a_pos_en_vista != null && a_pos_en_vista != null && p_nota.a_pos_en_vista[1] == a_pos_en_vista[1])
            {
                if ((p_nota.a_pos_en_vista[0] >= a_pos_en_vista[0] && p_nota.a_pos_en_vista[0] < a_pos_en_vista[2])
                        || (p_nota.a_pos_en_vista[2] > a_pos_en_vista[0] && p_nota.a_pos_en_vista[2] <= a_pos_en_vista[2]))
                {
                    hay_colision = true;
                }
            }
        }
        return hay_colision;
    }

    // ---------------------------------------------------------------------------------------------

    public String nt_canvas_valores()
    {
        return a_nota.nota_get_nombre() + " | " + a_nota.nota_get_duracion();
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_canvas_desplaza(
                        Compas_Canvas_t p_compas,
                        float p_x0_rejilla,
                        float p_top_fila,
                        int p_bit_inicial,
                        String p_nombre_fila, int p_octava_fila)
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

    public void nt_canvas_eliminar()
    {
        a_compas_0.cmp_canvas_remove_nota(a_nota.nota_get_bit_inicial(), a_nota.nota_get_frecuencia());
        a_compas.cmp_canvas_remove_nota(a_nota.nota_get_bit_inicial(), a_nota.nota_get_frecuencia());

        a_compas_0.cmp_canvas_remove_nota(this);
        a_compas.cmp_canvas_remove_nota(this);
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_canvas_editar_nota_reproductor(ArrayList<Compas_Canvas_t> p_compases, int p_indice, int p_num_compases, Partitura_t p_partitura)
    {
        boolean hay_colision = true;

        for (; p_compases != null && p_indice < p_num_compases; p_indice++)
        {
            hay_colision = p_compases.get(p_indice).cmp_busca_colision_notas(this);

            if (hay_colision)
                break;
        }

        if (!hay_colision)
        {
            a_compas_0.cmp_canvas_remove_nota(a_nota.nota_get_bit_inicial(), a_nota.nota_get_frecuencia());

            Nota_t nota = p_partitura.partitura_append_nota_a_compas(a_compas.cmp_canvas_get_id(), a_rejilla_inicial, a_num_bits, a_nombre, a_octava);

            a_compas_0 = a_compas;
            a_nota = nota;
        }
        else
        {
            if (a_pos0_desplazamiento != null)
            {
                a_x0[0] = a_pos0_desplazamiento[0];
                a_x0[1] = a_pos0_desplazamiento[1];
                a_yf[0] = a_pos0_desplazamiento[2];
                a_yf[1] = a_pos0_desplazamiento[3];

                a_compas = a_compas_0;

                a_rejilla_inicial = a_nota.nota_get_bit_inicial();
                a_num_bits =  a_nota.nota_get_total_bits();
                a_nombre = a_nota.nota_get_nombre();
                a_octava = a_nota.nota_get_octava();
            }
            else
                nt_canvas_eliminar();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_rezice(Compas_Canvas_t p_compas, Compas_Canvas_t p_compas_right, float p_x, float p_x0, float tamanyo_rejilla)
    {
        float [] pos, pos0;

        if (p_compas != null)
        {
            pos = p_compas.cmp_get_pos_rejilla_marcada(p_x);

            if (p_compas_right != null)
                pos0 = p_compas_right.cmp_get_pos_rejilla_marcada(p_x0);
            else
                pos0 = a_compas_0.cmp_get_pos_rejilla_marcada(p_x0);

            if (pos != null && pos0 != null)
            {
                if (p_x >= p_x0)
                {
                    a_rejilla_inicial = a_compas_0.cmp_get_num_rejilla_marcada(p_x0);
                    a_x0[0] = pos0[0];
                    a_yf[0] = pos[1];
                }
                else
                {
                    if (!p_compas.cmp_compara_compases(a_compas))
                    {
                        a_compas.cmp_canvas_remove_nota(this);

                        p_compas.cmp_canvas_set_nota(this);

                        a_compas = p_compas;
                    }

                    a_rejilla_inicial = a_compas.cmp_get_num_rejilla_marcada(p_x);
                    a_x0[0] = pos[0];
                    a_yf[0] = pos0[1];
                }

                a_num_bits = (int) ((a_yf[0] - a_x0[0]) / tamanyo_rejilla);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_canvas_dibuja(
                        Canvas p_canvas,
                        float p_cmp_left, float p_cmp_left_0,
                        float [] p_x_vista,
                        float [] p_y_vista, float [] p_vista_0,
                        Paint p_pincel)
    {
        if (p_canvas != null && p_pincel != null && p_x_vista != null & p_y_vista != null && a_x0 != null && a_yf != null)
        {
            float left, right, top, bottom;

            if (a_yf[1] > p_y_vista[0])
            {
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
}
