package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Action_Canvas_t {

    public enum action_type_t
    {
        ACTION_MOVE_NOTE,
        ACTION_RESIZE_NOTE,
        ACTION_DELETE_NOTE,
        ACTION_CREATE_NOTE
    }

    private action_type_t ae_action;
    private action_type_t ae_last_action;

    private Diagrama_Pianola_t a_dp;

    private Nota_Canvas_t a_nota;

    private Vista_Canvas_t a_vista;

    private long a_action_up;
    private boolean a_moved;

    // ---------------------------------------------------------------------------------------------

    public Action_Canvas_t(Diagrama_Pianola_t p_dp, Vista_Canvas_t p_vista, float p_x, float p_y)
    {
        a_dp = p_dp;

        a_vista = p_vista;

        a_nota = a_dp.dp_hay_colision_con_nota(p_x, p_y);

        a_action_up = -1;
        a_moved = false;

        if (a_nota != null)
        {
            switch(a_nota.nt_canvas_get_collition_detected())
            {
                case COLLITION_CENTER:

                    ae_action = action_type_t.ACTION_MOVE_NOTE;

                    Log.e("Nota accedida: ", a_nota.nt_canvas_valores());

                    break;

                case COLLITION_LEFT:
                case COLLITION_RIGHT:

                    ae_action = action_type_t.ACTION_RESIZE_NOTE;

                    Log.e("Nota accedida: ", a_nota.nt_canvas_valores());

                    break;

                case COLLITION_UNDEFINED:
                default:
                    break;
            }
        }
        else
        {
            a_vista.vista_inicializa_coordenadas_touch(p_x, p_y);

            ae_action = action_type_t.ACTION_CREATE_NOTE;

            Log.e("Nota accedida: ", "false");
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_move_note(float p_x, float p_y)
    {
        if (a_nota != null)
        {
            Fila_Canvas_t fila = a_dp.dp_get_fila_marcada(p_y);

            if (fila != null)
            {
                a_dp.dp_dibuja_nota_desplazada(a_nota, p_x, fila.fila_get_top(), fila.fila_get_nombre(), fila.fila_get_octava());
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_rezice_note(float p_x, Partitura_t p_partitura)
    {
        if (a_nota != null)
        {
            float x, x0;

            x = a_vista.vista_calcula_posicion_x(p_x);

            Compas_Canvas_t compas = a_dp.dp_get_compas_marcado(x);
            float tamanyo_rejilla = p_partitura.partitura_get_tamanyo_rejilla(a_vista.vista_get_width_compas());

            switch (a_nota.nt_canvas_get_collition_detected())
            {
                case COLLITION_LEFT:

                    x0 = a_nota.nt_canvas_get_right() - 1;
                    Compas_Canvas_t compas_right = a_dp.dp_get_compas_marcado(x0);
                    a_nota.nt_rezice(compas, compas_right, x, x0, tamanyo_rejilla);
                    break;

                case COLLITION_RIGHT:

                    x0 = a_nota.nt_canvas_get_left() + 1;
                    a_nota.nt_rezice(compas, null, x, x0, tamanyo_rejilla);
                    break;

                default:
                    break;
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_create_note(float p_x, float p_y, Partitura_t p_partitura)
    {
        float [] coordenadas_touch_0 = a_vista.vista_get_coordenadas_touch_0();
        float x = a_vista.vista_calcula_posicion_x(p_x);

        Compas_Canvas_t compas = a_dp.dp_get_compas_marcado(x);

        float tamanyo_rejilla = p_partitura.partitura_get_tamanyo_rejilla(a_vista.vista_get_width_compas());
        int bit_inicial;

        if (a_nota == null)
        {
            Fila_Canvas_t fila = a_dp.dp_get_fila_marcada(p_y);

            if (compas != null && fila != null && coordenadas_touch_0 != null)
            {
                bit_inicial = compas.cmp_get_num_rejilla_marcada(a_vista.vista_calcula_posicion_x(coordenadas_touch_0[0]));

                if (bit_inicial >= 0)
                {
                    Nota_t nota = p_partitura.partitura_append_nota_a_compas(
                                            compas.cmp_canvas_get_id(),
                                            bit_inicial,
                                            1,
                                            fila.fila_get_nombre(),
                                            fila.fila_get_octava());

                    if (nota != null)
                    {
                        float [] pos_nota = a_dp.dp_get_top_bottom_nota(nota);

                        a_nota = compas.cmp_canvas_set_nota(
                                            nota,
                                            bit_inicial,
                                            pos_nota,
                                            tamanyo_rejilla);

                        a_nota.nt_canvas_set_type_collition(Nota_Canvas_t.type_collition_t.COLLITION_RIGHT);
                    }
                }
            }
        }

        if (a_nota != null)
        {
            float x0 = a_vista.vista_calcula_posicion_x(coordenadas_touch_0[0]);

            a_nota.nt_rezice(compas, null, x, x0, tamanyo_rejilla);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void ac_aplicar_scroll_vista()
    {
        if (a_vista != null && a_nota != null)
            a_vista.vista_aplicar_scroll(a_nota.nt_canvas_get_pos_en_vista(), a_nota.nt_canvas_get_collition_detected());
    }

    // ---------------------------------------------------------------------------------------------

    public boolean ac_es_delete()
    {
        boolean es_delete = false;

        if (a_nota != null && ae_action == action_type_t.ACTION_DELETE_NOTE)
            es_delete = true;

        return es_delete;
    }

    // ---------------------------------------------------------------------------------------------

    public void ac_action_move(float p_x, float p_y, Partitura_t p_partitura)
    {
        a_moved = true;

        switch (ae_action)
        {
            case ACTION_MOVE_NOTE:

                i_move_note(p_x, p_y);
                break;

            case ACTION_RESIZE_NOTE:

                i_rezice_note(p_x, p_partitura);
                break;

            case ACTION_DELETE_NOTE:

                if (a_nota != null)
                {
                    ae_action = ae_last_action;
                    ac_action_move(p_x, p_y, p_partitura);
                }
                break;

            case ACTION_CREATE_NOTE:

                i_create_note(p_x, p_y, p_partitura);
                break;

            default:
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_edita_nota_action_move(ArrayList<Compas_Canvas_t> p_compases, Partitura_t p_partitura)
    {
        if (a_nota != null)
        {
            int[] rango_compases = a_dp.dp_calcula_rango_compases_vista(false);

            a_nota.nt_canvas_editar_nota_reproductor(p_compases, rango_compases[0], rango_compases[1], p_partitura);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void ac_action_up(ArrayList<Compas_Canvas_t> p_compases, Partitura_t p_partitura)
    {

        switch (ae_action)
        {
            case ACTION_MOVE_NOTE:
            case ACTION_RESIZE_NOTE:

                if (a_moved)
                    i_edita_nota_action_move(p_compases, p_partitura);
                else
                {
                    a_action_up = System.currentTimeMillis();

                    ae_last_action = ae_action;
                    ae_action = action_type_t.ACTION_DELETE_NOTE;
                }
                break;

            case ACTION_CREATE_NOTE:

                i_edita_nota_action_move(p_compases, p_partitura);
                break;

            case ACTION_DELETE_NOTE:

                if (!a_moved && a_nota != null)
                {
                    if (System.currentTimeMillis() - a_action_up < 1000)
                    {
                        a_nota.nt_canvas_eliminar();
                        a_nota = null;
                    }
                }
                break;

            default:
                break;
        }
    }
}
