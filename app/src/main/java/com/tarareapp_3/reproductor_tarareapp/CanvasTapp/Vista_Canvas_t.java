package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Vista_Canvas_t {

    private Diagrama_Pianola_t a_dp;

    private Canvas a_canvas;
    private static float a_zoom = 1;

    private static float [] a_coordenadas_vista_0 = null;
    private float [] a_coordenadas_vista_f;

    private float [] a_coordenadas_onToucheMove_0;
    private float [] a_coordenadas_vista_f0_onToucheMove;

    private float [] a_y_vista;
    private float [] a_x_vista;

    private float a_height_canvas;
    private float a_width_canvas;

    private final static double X100_WIDTH_NOTE_LS = 0.1;
    private final static double X100_HEIGHT_NOTE_LS = 0.15;

    private final static double X100_WIDTH_MEASURE_LS = 0.3;
    private final static double X100_HEIGHT_MEASURE_LS = 0.1;

    private float a_width_compas;
    private float a_height_compas;

    private float a_width_fila;
    private float a_height_fila;

    private static Paint a_pincel_negro;

    // ---------------------------------------------------------------------------------------------

    public Vista_Canvas_t (Diagrama_Pianola_t p_dp)
    {
        a_dp = p_dp;

        a_y_vista = new float [2];
        a_x_vista = new  float [2];

        a_coordenadas_onToucheMove_0 = new float[2];
        a_coordenadas_onToucheMove_0[0] = 0;
        a_coordenadas_onToucheMove_0[1] = 0;

        a_coordenadas_vista_f0_onToucheMove = new float[2];

        if (a_pincel_negro == null)
        {
            a_pincel_negro = new Paint();
            a_pincel_negro.setColor(Color.BLACK);
            a_pincel_negro.setTextSize(30);
            a_pincel_negro.setStrokeWidth(2);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public float vista_get_height_compas()
    {
        return a_height_compas;
    }

    // ---------------------------------------------------------------------------------------------

    public float vista_get_width_compas()
    {
        return a_width_compas;
    }

    // ---------------------------------------------------------------------------------------------

    public float vista_get_height_fila()
    {
        return a_height_fila;
    }

    // ---------------------------------------------------------------------------------------------

    public float vista_get_width_fila()
    {
        return a_width_fila;
    }

    // ---------------------------------------------------------------------------------------------

    public float [] vista_get_y_view()
    {
        return a_y_vista;
    }

    // ---------------------------------------------------------------------------------------------

    public float [] vista_inicializar_datos(Canvas p_canvas)
    {
        float [] coordenadas;

        a_canvas = p_canvas;

        a_canvas.drawColor(Color.WHITE);

        a_height_canvas = p_canvas.getHeight();
        a_width_canvas = p_canvas.getWidth();

        a_width_fila = (float) (a_width_canvas * X100_WIDTH_NOTE_LS) * a_zoom;
        a_height_fila = (float) (a_height_canvas * X100_HEIGHT_NOTE_LS) * a_zoom;

        a_width_compas = (float) (a_width_canvas * X100_WIDTH_MEASURE_LS) * a_zoom;
        a_height_compas = (float) (a_height_canvas * X100_HEIGHT_MEASURE_LS) * a_zoom;

        coordenadas = new float[4];
        coordenadas[0] = a_width_fila;
        coordenadas[1] = 0;

        coordenadas[2] = a_width_fila + a_width_compas;
        coordenadas[3] = (float) (a_height_canvas * X100_HEIGHT_MEASURE_LS);

        a_coordenadas_vista_0 = new float[2];
        a_coordenadas_vista_0[0] = a_width_fila;
        a_coordenadas_vista_0[1] = coordenadas[3];

        a_coordenadas_vista_f = new float[2];
        a_coordenadas_vista_f[0] = a_width_fila;;
        a_coordenadas_vista_f[1] = coordenadas[3] + (float) (a_height_canvas * X100_HEIGHT_NOTE_LS * 12 * 3);

        a_dp.dp_crea_filas_notas(a_height_canvas, a_width_canvas);

        return coordenadas;
    }

    // ---------------------------------------------------------------------------------------------

    public int vista_calcula_y_view()
    {
        float desplazamiento_vista_en_y = (a_coordenadas_vista_f[1] - a_coordenadas_vista_0[1]);

        int notas_desplazadas = (int) (desplazamiento_vista_en_y / a_height_fila);
        float resto_pixeles = desplazamiento_vista_en_y % a_height_fila;

        a_y_vista[0] = a_coordenadas_vista_0[1] + (notas_desplazadas * a_height_fila) + resto_pixeles;
        a_y_vista[1] = a_height_canvas + (notas_desplazadas * a_height_fila) + resto_pixeles;

        return notas_desplazadas;
    }

    // ---------------------------------------------------------------------------------------------

    public int vista_calcula_primer_compas_visible()
    {
        float desplazamiento_vista_en_x = (a_coordenadas_vista_f[0] - a_coordenadas_vista_0[0]);

        int compases_desplazados = (int) (desplazamiento_vista_en_x / a_width_compas);
        float resto_pixeles = desplazamiento_vista_en_x % a_width_compas;

        a_x_vista[0] = a_coordenadas_vista_0[0] + (compases_desplazados * a_width_compas) + resto_pixeles;
        a_x_vista[1] = a_width_canvas + (compases_desplazados * a_width_compas) + resto_pixeles;

        return compases_desplazados;
    }

    // ---------------------------------------------------------------------------------------------

    public void vista_mover(Fila_Canvas_t [] p_filas, float p_x, float p_y)
    {
        float desplazamiento_x, desplazamiento_y;

        desplazamiento_x = a_coordenadas_vista_f0_onToucheMove[0] + (p_x - a_coordenadas_onToucheMove_0[0]) *-1;

        if (desplazamiento_x >= a_coordenadas_vista_0[0])
            a_coordenadas_vista_f[0] = desplazamiento_x;
        else
            Log.e("move view: ", "fuera de canvas x: " + desplazamiento_x);

        desplazamiento_y = a_coordenadas_vista_f0_onToucheMove[1] + (p_y - a_coordenadas_onToucheMove_0[1]) *-1;

        if (desplazamiento_y >= a_coordenadas_vista_0[1])
        {
            if (p_filas[p_filas.length - 1].fila_get_pos()[1] - (a_height_canvas - (a_height_canvas * X100_HEIGHT_MEASURE_LS))
                    > desplazamiento_y)
            {
                a_coordenadas_vista_f[1] = desplazamiento_y;
            }
            else {
                Log.e("move view: ", "fuera de canvas y_1: " + desplazamiento_y);
            }
        }
        else
            Log.e("move view: ", "fuera de canvas y: " + desplazamiento_y);
    }

    // ---------------------------------------------------------------------------------------------

    public void vista_inicializa_coordenadas_touch(float p_x, float p_y)
    {
        a_coordenadas_onToucheMove_0[0] = p_x;
        a_coordenadas_onToucheMove_0[1] = p_y;

        a_coordenadas_vista_f0_onToucheMove[0] = a_coordenadas_vista_f[0];
        a_coordenadas_vista_f0_onToucheMove[1] = a_coordenadas_vista_f[1];
    }

    // ---------------------------------------------------------------------------------------------

    public void vista_dibuja_fila(Fila_Canvas_t p_fila)
    {
        if (p_fila != null)
        {
            p_fila.fila_dibuja(
                        a_canvas,
                        a_y_vista,
                        a_coordenadas_vista_0,
                        a_height_canvas,
                        a_width_fila,
                        a_width_canvas,
                        a_pincel_negro);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void vista_dibuja_compas(Compas_Canvas_t p_compas)
    {
        if (p_compas != null)
            p_compas.cmp_dibuja(a_canvas, a_x_vista, a_y_vista, a_coordenadas_vista_0, a_pincel_negro);
    }

    // ---------------------------------------------------------------------------------------------

    public void vista_dibuja_nota(Compas_Canvas_t p_compas)
    {
        if (p_compas != null)
            p_compas.cmp_dibuja_notas(a_canvas, a_x_vista, a_y_vista, a_coordenadas_vista_0);
    }
}
