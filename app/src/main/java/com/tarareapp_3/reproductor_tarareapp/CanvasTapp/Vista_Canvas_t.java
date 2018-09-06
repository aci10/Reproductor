package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.tarareapp_3.reproductor_tarareapp.Color_Picker_t;

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

    private final static double X100_HEIGHT_TOOL_BAR_LS = 0.2;

    private final static double X100_WIDTH_NOTE_LS = 0.1;
    private final static double X100_HEIGHT_NOTE_LS = 0.15;

    private final static double X100_WIDTH_MEASURE_LS = 0.3;
    private final static double X100_HEIGHT_MEASURE_LS = 0.1;

    private final static double X100_TOOL_WIDTH = 0.125;

    private float a_width_compas;
    private float a_height_compas;

    private float a_width_fila;
    private float a_height_fila;

    private static Paint a_pincel_negro;

    private long a_last_scroll;

    private boolean a_tamanyos_inicializar;

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

        a_last_scroll = 0;

        a_tamanyos_inicializar = true;

        if (a_pincel_negro == null)
        {
            a_pincel_negro = new Paint();
            a_pincel_negro.setColor(Color.BLACK);
            a_pincel_negro.setTextSize(30);
            a_pincel_negro.setStrokeWidth(2);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public float vista_get_height_canvas()
    {
        return a_height_canvas;
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

    public float [] vista_get_coordenadas_touch_0()
    {
        return a_coordenadas_onToucheMove_0;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_inicializa_tamanyos()
    {
        if (a_tamanyos_inicializar)
        {
            a_height_canvas = a_canvas.getHeight();
            a_width_canvas = a_canvas.getWidth();

            a_width_fila = (float) (a_width_canvas * X100_WIDTH_NOTE_LS) * a_zoom;
            a_height_fila = (float) (a_height_canvas * X100_HEIGHT_NOTE_LS) * a_zoom;

            a_width_compas = (float) (a_width_canvas * X100_WIDTH_MEASURE_LS) * a_zoom;
            a_height_compas = (float) (a_height_canvas * X100_HEIGHT_MEASURE_LS) * a_zoom;

        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_inicializa_vista(float [] p_coordenadas)
    {
        if (a_tamanyos_inicializar)
        {
            a_coordenadas_vista_0 = new float[2];
            a_coordenadas_vista_0[0] = a_width_fila;
            a_coordenadas_vista_0[1] = p_coordenadas[3];

            a_coordenadas_vista_f = new float[2];
            a_coordenadas_vista_f[0] = a_width_fila;;
            a_coordenadas_vista_f[1] = p_coordenadas[3] + (float) (a_height_canvas * X100_HEIGHT_NOTE_LS * 12 * 3);

            a_dp.dp_crea_filas_notas(a_height_canvas, a_width_canvas);

            a_tamanyos_inicializar = false;
        }
    }

    // ---------------------------------------------------------------------------------------------

    public float [] vista_inicializar_datos(Canvas p_canvas)
    {
        float [] coordenadas;

        a_canvas = p_canvas;

        a_canvas.drawColor(Color.WHITE);

        i_inicializa_tamanyos();

        coordenadas = new float[4];
        coordenadas[0] = a_width_fila;
        coordenadas[1] = 0;

        coordenadas[2] = a_width_fila + a_width_compas;
        coordenadas[3] = (float) (a_height_canvas * X100_HEIGHT_MEASURE_LS);

        i_inicializa_vista(coordenadas);

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

    public float vista_calcula_posicion_x(float p_x)
    {
        float desplazamiento_vista_en_x = (a_coordenadas_vista_f[0] - a_coordenadas_vista_0[0]);

        return desplazamiento_vista_en_x + p_x;
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

    public void vista_aplicar_scroll(float [] p_pos_nota_en_vista, Nota_Canvas_t.type_collition_t p_collition_type)
    {
        float desplazamiento;
        long current_time = System.currentTimeMillis();

        if (p_pos_nota_en_vista != null && current_time >= a_last_scroll + 250)
        {
            a_last_scroll = current_time;

            if (p_collition_type != Nota_Canvas_t.type_collition_t.COLLITION_RIGHT
                    && p_pos_nota_en_vista[0] <= a_coordenadas_vista_0[0] + (a_width_compas / 10))
            {
                desplazamiento = (a_width_compas / 3);

                if (a_coordenadas_vista_f[0] - desplazamiento >= a_coordenadas_vista_0[0])
                    a_coordenadas_vista_f[0] -= desplazamiento;
                else
                    a_coordenadas_vista_f[0] = a_coordenadas_vista_0[0];
            }
            else if (p_pos_nota_en_vista[1] <= a_coordenadas_vista_0[1] + (a_height_fila / 3))
            {
                desplazamiento = a_height_fila;

                if (a_coordenadas_vista_f[1] - desplazamiento >= a_coordenadas_vista_0[1])
                    a_coordenadas_vista_f[1] -= desplazamiento;
                else
                    a_coordenadas_vista_f[1] = a_coordenadas_vista_0[1];
            }
            else if (p_collition_type != Nota_Canvas_t.type_collition_t.COLLITION_LEFT
                        && p_pos_nota_en_vista[2] >= a_width_canvas - (a_width_compas / 10))
            {
                desplazamiento = (a_width_compas / 3);
                a_coordenadas_vista_f[0] += desplazamiento;
            }
            else if (p_pos_nota_en_vista[3] >= a_height_canvas - (a_height_fila / 3))
            {
                desplazamiento = a_height_fila;
                Fila_Canvas_t ultima_fila = a_dp.dp_get_ultima_fila();

                if (ultima_fila != null && a_coordenadas_vista_f[1] + desplazamiento <= ultima_fila.fila_get_pos()[1] - (a_height_canvas - a_height_compas))
                    a_coordenadas_vista_f[1] += desplazamiento;
            }
        }
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

    public float [] vista_init_pos_bar()
    {
        float [] pos_bar = new float [4];

        pos_bar[0] = 0;
        pos_bar[1] = (float)(a_height_canvas - (a_height_canvas * X100_HEIGHT_TOOL_BAR_LS));
        pos_bar[2] = a_width_canvas;
        pos_bar[3] = a_height_canvas;

        return pos_bar;
    }

    // ---------------------------------------------------------------------------------------------

    public float [] vista_init_pos_arrow(float bottom)
    {
        float [] pos_arrow = new float [3];

        float circle_radius = a_width_fila / 2;

        pos_arrow[0] = a_width_canvas - 20 - circle_radius;
        pos_arrow[1] = bottom - circle_radius;
        pos_arrow[2] = circle_radius;

        return pos_arrow;
    }

    // ---------------------------------------------------------------------------------------------

    public float [] vista_init_pos_first_tool(float top)
    {
        float [] pos_bar = new float [4];

        pos_bar[0] = (float)(a_width_canvas - a_width_canvas * X100_TOOL_WIDTH);
        pos_bar[1] = top;
        pos_bar[2] = a_width_canvas;
        pos_bar[3] = a_height_canvas;

        return pos_bar;
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
            p_compas.cmp_dibuja(a_canvas, a_x_vista, a_coordenadas_vista_0, a_pincel_negro);
    }

    // ---------------------------------------------------------------------------------------------

    public void vista_dibuja_nota(Compas_Canvas_t p_compas, boolean p_es_primer_compas)
    {
        if (p_compas != null)
            p_compas.cmp_dibuja_notas(a_canvas, a_x_vista, a_y_vista, a_coordenadas_vista_0, p_es_primer_compas);
    }

    // ---------------------------------------------------------------------------------------------

    public void vista_dibuja_tool_bar(Barra_Herramientas_t p_tool_bar)
    {
        if (p_tool_bar != null)
            p_tool_bar.bh_draw(a_canvas, a_pincel_negro);
    }
}
