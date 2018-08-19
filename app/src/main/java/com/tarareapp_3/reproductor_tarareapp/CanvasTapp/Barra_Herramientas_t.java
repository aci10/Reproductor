package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Barra_Herramientas_t {

    private Diagrama_Pianola_t a_dp;
    private Vista_Canvas_t a_vista;

    private boolean a_opened;
    private boolean in_animation;

    private float [] a_pos_arrow;
    private float [] a_pos_bar;
    private float a_max_top;
    private Paint a_pincel;

    private ArrayList<Tools_Bar_t> av_tools;

    // ---------------------------------------------------------------------------------------------

    private void i_init_tools()
    {
        av_tools = new ArrayList<>();
        float pos [] = a_vista.vista_init_pos_first_tool(a_pos_bar[1]);
        float ancho = pos[2] - pos[0];

        av_tools.add(new Tools_Bar_t("Exportador", null, pos, Tools_Bar_t.type_tool_t.TOOL_EXPORT));

        pos[0] -= ancho;
        pos[2] -= ancho;
        av_tools.add(new Tools_Bar_t("Reproductor", null, pos, Tools_Bar_t.type_tool_t.TOOL_PLAY));

        pos[0] -= ancho;
        pos[2] -= ancho;
        av_tools.add(new Tools_Bar_t("Editor", null, pos, Tools_Bar_t.type_tool_t.TOOL_EDIT_MODE));
    }

    // ---------------------------------------------------------------------------------------------

    private void i_init_pos_arrow()
    {
        a_pos_arrow = a_vista.vista_init_pos_arrow(a_pos_bar[1]);
    }

    // ---------------------------------------------------------------------------------------------

    public Barra_Herramientas_t (Diagrama_Pianola_t p_dp, Vista_Canvas_t p_vista)
    {
        a_dp = p_dp;
        a_vista = p_vista;

        a_opened = true;
        in_animation = false;

        a_pos_bar = a_vista.vista_init_pos_bar();
        a_max_top = a_pos_bar[1];
        i_init_pos_arrow();
        i_init_tools();

        a_pincel = new Paint();
        a_pincel.setColor(Color.RED);
        a_pincel.setShadowLayer(4, 5, 0, Color.DKGRAY);
    }

    // ---------------------------------------------------------------------------------------------

    private boolean i_check_collition_arrow(float p_x, float p_y)
    {
        boolean collitioned = false;

        if (p_x >= a_pos_arrow[0] - a_pos_arrow[2] && p_x <= a_pos_arrow[0] + a_pos_arrow[2]
                && p_y >= a_pos_arrow[1] - a_pos_arrow[2] && p_y <= a_pos_arrow[1] + a_pos_arrow[2])
        {
            collitioned = true;
        }

        return collitioned;
    }

    // ---------------------------------------------------------------------------------------------

    private boolean i_check_collition_tool_bar(float p_x, float p_y)
    {
        boolean collitioned = false;

        if (p_x >= a_pos_bar[0] && p_x <= a_pos_bar[2]
                && p_y >= a_pos_bar[1] && p_y <= a_pos_bar[3])
        {
            collitioned = true;
        }

        return collitioned;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean bh_execute_action_if_collition(float p_x, float p_y)
    {
        boolean executed = false;

        if (a_opened && !in_animation)
        {
            if (i_check_collition_tool_bar(p_x, p_y))
            {
                for (int i = 0; !executed && i < av_tools.size(); i++)
                {
                    executed = av_tools.get(i).tb_execute_action(a_dp, p_x, p_y);
                }

                executed = true;
            }
        }

        if (!executed)
        {
            if (i_check_collition_arrow(p_x, p_y))
            {
                executed = true;

                a_opened = !a_opened;
                in_animation = true;
            }
        }

        return executed;
    }


    // ---------------------------------------------------------------------------------------------

    private void i_start_animation()
    {
        if (in_animation)
        {
            if (a_opened)
            {
                float top = a_pos_bar[1] - 5;

                if (top > a_max_top)
                    a_pos_bar[1] = top;
                else
                {
                    a_pos_bar[1] = a_max_top;
                    in_animation = false;
                }
            }
            else
            {
                float top = a_pos_bar[1] + 5;
                float height_canvas = a_vista.vista_get_height_canvas();

                if (top < height_canvas)
                    a_pos_bar[1] = top;
                else
                {
                    a_pos_bar[1] = height_canvas;
                    in_animation = false;
                }
            }

            a_pos_arrow[1] = a_pos_bar[1] - a_pos_arrow[2];

            if (av_tools != null && av_tools.get(0) != null)
                av_tools.get(0).tb_set_top(a_pos_bar[1]);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void bh_draw(Canvas p_canvas, Paint p_pincel_negro)
    {
        i_start_animation();

        if (a_opened || in_animation)
        {
            p_canvas.drawRect(a_pos_bar[0], a_pos_bar[1], a_pos_bar[2], a_pos_bar[3], a_pincel);

            for (int i = 0; i < av_tools.size(); i++)
            {
                av_tools.get(i).tb_draw(p_canvas, p_pincel_negro);
            }
        }

        p_canvas.drawCircle(a_pos_arrow[0], a_pos_arrow[1], a_pos_arrow[2], a_pincel);
    }
}
