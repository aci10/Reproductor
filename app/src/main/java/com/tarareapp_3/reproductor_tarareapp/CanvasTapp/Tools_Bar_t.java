package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tarareapp_3.reproductor_tarareapp.Color_Picker_t;
import com.tarareapp_3.reproductor_tarareapp.Image_Picker_t;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Tools_Bar_t {

    private String a_name;
    private Image_Picker_t a_icon;
    private float [] a_pos_x;
    private static float [] a_pos_y;

    private Paint a_pincel;
    private Paint a_pincel_linea;

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

    public Tools_Bar_t (String p_name, float [] p_pos, type_tool_t p_type, Context p_context)
    {
        Color_Picker_t picker = new Color_Picker_t();
        a_name = p_name;
        ae_type_tool = p_type;
        disabled = false;

        a_icon = new Image_Picker_t(p_context);

        a_pos_x = new float [2];
        a_pos_x[0] = p_pos[0];
        a_pos_x[1] = p_pos[2];

        if (a_pos_y == null)
        {
            a_pos_y = new float [2];
            a_pos_y[0] = p_pos[1];
            a_pos_y[1] = p_pos[3];
        }

        switch (ae_type_tool)
        {
            case TOOL_EXPORT:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.SAVE, Color_Picker_t.type_color_t.BLANCO);
                break;

            case TOOL_PLAY:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.PLAY, Color_Picker_t.type_color_t.BLANCO);
                break;

            case TOOL_EDIT_MODE:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA);
                a_icon.initialize(Image_Picker_t.icon_t.EDIT, Color_Picker_t.type_color_t.BLANCO);
                break;

            default:
                a_icon = null;
                a_pincel = null;
        }

        a_pincel_linea = picker.getPincel(Color_Picker_t.type_color_t.BLANCO_VERDE);
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
            p_canvas.drawLine(a_pos_x[0], a_pos_y[0], a_pos_x[0], a_pos_y[1], a_pincel_linea);

            float [] icon_pos = new float [2];
            icon_pos[0] = a_pos_x[0] + ((a_pos_x[1] - a_pos_x[0]) / 16) * 6;
            icon_pos[1] = a_pos_y[0] + (a_pos_y[1] - a_pos_y[0]) / 4;


            a_icon.setPos(icon_pos);
            a_icon.draw(p_canvas);
        }
    }
}
