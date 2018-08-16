package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

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

    private Diagrama_Pianola_t a_dp;

    private Nota_Canvas_t a_nota;

    // ---------------------------------------------------------------------------------------------

    public Action_Canvas_t(Diagrama_Pianola_t p_dp, float p_x, float p_y)
    {
        a_dp = p_dp;

        a_nota = a_dp.dp_hay_colision_con_nota(p_x, p_y);

        if (a_nota != null)
        {
            ae_action = action_type_t.ACTION_MOVE_NOTE;
            Log.e("Nota accedida: ", a_nota.nt_canvas_valores());
        }
        else
        {
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

    public void ac_action_move(float p_x, float p_y)
    {
        switch (ae_action)
        {
            case ACTION_MOVE_NOTE:

                i_move_note(p_x, p_y);
                break;

            case ACTION_RESIZE_NOTE:
                break;

            case ACTION_DELETE_NOTE:
                break;

            case ACTION_CREATE_NOTE:
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
            int[] rango_compases = a_dp.dp_calcula_rango_compases_vista();

            a_nota.nt_canvas_editar_nota_reproductor(p_compases, rango_compases[0], rango_compases[1], p_partitura);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void ac_action_up(ArrayList<Compas_Canvas_t> p_compases, Partitura_t p_partitura)
    {
        switch (ae_action)
        {
            case ACTION_MOVE_NOTE:

                i_edita_nota_action_move(p_compases, p_partitura);
                break;

            case ACTION_RESIZE_NOTE:
                break;

            case ACTION_DELETE_NOTE:
                break;

            case ACTION_CREATE_NOTE:
                break;

            default:
                break;
        }
    }
}
