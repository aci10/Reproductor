package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;

public class Nota_Canvas_t {

    private Nota_t a_nota;

    private float [] a_x0;
    private float [] a_yf;

    // ---------------------------------------------------------------------------------------------

    public Nota_Canvas_t (Nota_t p_nota, float p_left, float p_top, float p_right, float p_bottom)
    {
        if (p_nota != null)
        {
            a_nota = p_nota;

            a_x0 = new float [2];
            a_x0[0] = p_left;
            a_x0[1] = p_top;

            a_yf = new float [2];
            a_yf[0] = p_right;
            a_yf[1] = p_bottom;
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void nt_canvas_dibuja(Canvas p_canvas, Paint p_pincel)
    {
        if (p_canvas != null && p_pincel != null)
            p_canvas.drawRect(a_x0[0], a_x0[1], a_yf[0], a_yf[1], p_pincel);
    }
}
