package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Tools_Bar_t {

    private String a_name;
    private Bitmap a_icon;
    private float [] a_pos_x;
    private static float [] a_pos_y;
    private Paint a_pincel;

    private boolean disabled;

    public enum type_tool_t
    {
        TOOL_PLAY,
        TOOL_STOP,
        TOOL_PAUSE,
        TOOL_EDIT_MODE,
        TOOL_EXPORT
    }

    private type_tool_t ae_type_tool;

    // ---------------------------------------------------------------------------------------------

    public Tools_Bar_t (String p_name, Bitmap p_icon, float [] p_pos, type_tool_t p_type)
    {
        a_name = p_name;
        a_icon = p_icon;
        ae_type_tool = p_type;
        disabled = false;

        a_pincel = new Paint();
        switch (ae_type_tool)
        {
            case TOOL_EXPORT:

                a_pincel.setColor(Color.GREEN);
                break;
            case TOOL_PLAY:

                a_pincel.setColor(Color.BLUE);
                break;

            case TOOL_EDIT_MODE:

                a_pincel.setColor(Color.YELLOW);
                break;
        }

        a_pos_x = new float [2];
        a_pos_x[0] = p_pos[0];
        a_pos_x[1] = p_pos[2];

        if (a_pos_y == null)
        {
            a_pos_y = new float [2];
            a_pos_y[0] = p_pos[1];
            a_pos_y[1] = p_pos[3];
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void tb_set_top(float p_top)
    {
        a_pos_y[0] = p_top;
    }

    // ---------------------------------------------------------------------------------------------

    private boolean i_check_collition(float p_x, float p_y)
    {
        boolean collitioned = false;

        if (a_pos_x != null && a_pos_y != null
                && p_x >= a_pos_x[0] && p_x <= a_pos_x[1]
                && p_y >= a_pos_y[0] && p_y <= a_pos_y[1])
        {
            collitioned = true;
        }

        return collitioned;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean tb_execute_action(Diagrama_Pianola_t p_dp, float p_x, float p_y)
    {
        boolean executed = false;

        if (p_dp != null && !disabled && i_check_collition(p_x, p_y))
        {
            switch (ae_type_tool)
            {
                case TOOL_PLAY:

                    p_dp.dp_play_score();
                    ae_type_tool = type_tool_t.TOOL_STOP;
                    break;

                case TOOL_STOP:

                    p_dp.dp_stop_score(false);
                    ae_type_tool = type_tool_t.TOOL_PLAY;
                    break;

                case TOOL_PAUSE:

                    p_dp.dp_stop_score(true);
                    ae_type_tool = type_tool_t.TOOL_PLAY;
                    break;

                case TOOL_EDIT_MODE:

                    p_dp.dp_change_mode();
                    break;

                case TOOL_EXPORT:

                    p_dp.dp_export_score();
                    break;
            }
        }

        return executed;
    }

    // ---------------------------------------------------------------------------------------------

    public void tb_draw(Canvas p_canvas, Paint p_pincel_negro)
    {
        if (p_canvas != null)
        {
            p_canvas.drawRect(a_pos_x[0], a_pos_y[0], a_pos_x[1], a_pos_y[1], a_pincel);
            p_canvas.drawLine(a_pos_x[0], a_pos_y[0], a_pos_x[0], a_pos_y[1], p_pincel_negro);
        }
    }
}
