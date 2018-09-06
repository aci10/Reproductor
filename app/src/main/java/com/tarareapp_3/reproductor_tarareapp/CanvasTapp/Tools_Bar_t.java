package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tarareapp_3.reproductor_tarareapp.Color_Picker_t;
import com.tarareapp_3.reproductor_tarareapp.Image_Picker_t;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Tools_Bar_t {

    private Diagrama_Pianola_t a_dp;

    private Image_Picker_t a_icon;
    private float [] a_pos_x;
    private static float [] a_pos_y;

    private Paint a_pincel;
    private Paint a_pincel_linea;

    private boolean disabled;

    private boolean firstAnimation;
    private boolean SecondAnimationStarted;
    private boolean onAnimation;
    private String [] av_animation_colors;
    private ValueAnimator a_animator;

    public enum type_tool_t
    {
        TOOL_PLAY,
        TOOL_STOP,
        TOOL_PAUSE,
        TOOL_EDIT_MODE,
        TOOL_EXPORT,
        TOOL_AJUSTES,
        TOOL_UNDO,
        TOOL_REDO,
        TOOL_NEXT
    }

    private Color_Picker_t picker;
    private type_tool_t ae_type_tool;

    // ---------------------------------------------------------------------------------------------

    public Tools_Bar_t (Diagrama_Pianola_t p_dp, float [] p_pos, type_tool_t p_type, Context p_context)
    {
        a_dp = p_dp;
        picker = new Color_Picker_t();

        ae_type_tool = p_type;
        disabled = false;

        a_icon = new Image_Picker_t(p_context);

        a_pos_x = new float [2];
        a_pos_x[0] = p_pos[0];
        a_pos_x[1] = p_pos[2];

        onAnimation = false;
        firstAnimation = false;
        av_animation_colors = null;
        SecondAnimationStarted = false;

        if (a_pos_y == null)
        {
            a_pos_y = new float [2];
            a_pos_y[0] = p_pos[1];
            a_pos_y[1] = p_pos[3];
        }

        switch (ae_type_tool)
        {
            case TOOL_EDIT_MODE:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA);
                a_icon.initialize(Image_Picker_t.icon_t.EDIT, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA);
                break;

            case TOOL_REDO:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.REDO, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA_LIGTH);
                break;

            case TOOL_UNDO:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.UNDO, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA_LIGTH);
                break;

            case TOOL_PLAY:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.PLAY, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA_LIGTH);
                break;

            case TOOL_PAUSE:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.PAUSE, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA_LIGTH);
                break;

            case TOOL_STOP:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.STOP, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA_LIGTH);
                break;

            case TOOL_EXPORT:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.SAVE, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA_LIGTH);
                break;

            case TOOL_AJUSTES:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.AJUSTES, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA_LIGTH);
                break;

            case TOOL_NEXT:

                a_pincel = picker.getPincel(Color_Picker_t.type_color_t.ROSA_LIGTH);
                a_icon.initialize(Image_Picker_t.icon_t.NEXT, Color_Picker_t.type_color_t.BLANCO);
                av_animation_colors = picker.getAnimation(Color_Picker_t.type_color_t.ROSA_LIGTH);
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
                    ae_type_tool = type_tool_t.TOOL_PAUSE;
                    a_icon.initialize(Image_Picker_t.icon_t.PAUSE, Color_Picker_t.type_color_t.BLANCO);
                    firstAnimation = true;
                    break;

                case TOOL_STOP:

                    p_dp.dp_stop_score(false);
                    firstAnimation = true;
                    break;

                case TOOL_PAUSE:

                    p_dp.dp_stop_score(true);
                    ae_type_tool = type_tool_t.TOOL_PLAY;
                    a_icon.initialize(Image_Picker_t.icon_t.PLAY, Color_Picker_t.type_color_t.BLANCO);
                    firstAnimation = true;
                    break;

                case TOOL_EDIT_MODE:

                    p_dp.dp_change_mode();
                    firstAnimation = true;
                    break;

                case TOOL_EXPORT:

                    p_dp.dp_export_score();
                    firstAnimation = true;
                    break;
            }
        }

        return executed;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_animate_button(String color1, String color2)
    {
        if (color1 != null && color2 != null)
        {
            final float[] from = new float[3],
                            to = new float[3];

            Color.colorToHSV(Color.parseColor(color1), from);
            Color.colorToHSV(Color.parseColor(color2), to);

            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.setDuration(250);

            final float[] hsv  = new float[3];
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                @Override public void onAnimationUpdate(ValueAnimator animation) {

                    synchronized (a_dp.getHolder())
                    {
                        // Transition along each axis of HSV (hue, saturation, value)
                        hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
                        hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
                        hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();

                        a_pincel.setColor(Color.HSVToColor(hsv));
                    }
                }
            });

            a_animator = anim;

            anim.start();
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_start_animation()
    {
        if ((firstAnimation || onAnimation) && picker != null && av_animation_colors != null)
        {
            if (firstAnimation && !onAnimation)
            {
                onAnimation = true;
                i_animate_button(av_animation_colors[0], av_animation_colors[1]);
            }
            else if (firstAnimation && onAnimation)
            {
                if (a_animator != null && !a_animator.isStarted())
                    firstAnimation = false;
            }
            else if (onAnimation)
            {
                if (!SecondAnimationStarted)
                {
                    SecondAnimationStarted = true;
                    i_animate_button(av_animation_colors[1], av_animation_colors[0]);
                }
                else if (!a_animator.isStarted())
                {
                    onAnimation = false;
                    SecondAnimationStarted = false;
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public boolean tb_change_to_play()
    {
        boolean done = false;

        if (ae_type_tool == type_tool_t.TOOL_PAUSE || ae_type_tool == type_tool_t.TOOL_PLAY)
        {
            done = true;
            if (ae_type_tool == type_tool_t.TOOL_PAUSE)
            {
                ae_type_tool = type_tool_t.TOOL_PLAY;
                a_icon.initialize(Image_Picker_t.icon_t.PLAY, Color_Picker_t.type_color_t.BLANCO);
            }
        }

        return done;
    }

    // ---------------------------------------------------------------------------------------------

    public void tb_draw(Canvas p_canvas)
    {
        if (p_canvas != null)
        {
            // i_start_animation();

            p_canvas.drawRect(a_pos_x[0], a_pos_y[0], a_pos_x[1], a_pos_y[1], a_pincel);
            p_canvas.drawLine(a_pos_x[0], a_pos_y[0], a_pos_x[0], a_pos_y[1], a_pincel_linea);

            float [] icon_pos = new float [2];
            icon_pos[0] = a_pos_x[0] + ((a_pos_x[1] - a_pos_x[0]) / 16) * 5;
            icon_pos[1] = a_pos_y[0] + (a_pos_y[1] - a_pos_y[0]) / 4;

            a_icon.setPos(icon_pos);
            a_icon.draw(p_canvas);
        }
    }
}
