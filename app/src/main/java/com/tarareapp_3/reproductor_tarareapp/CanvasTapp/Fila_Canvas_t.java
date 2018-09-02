package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tarareapp_3.reproductor_tarareapp.Color_Picker_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Fila_Canvas_t {

    private float [] a_pos;
    private float [] a_pos_en_vista;
    private String a_nombre;
    private int a_octava;

    private Paint a_pincel;
    private Paint a_pincel_linea;
    private static Paint a_pincel_texto;

    private static Paint [] a_pinceles_filas = new Paint [2];

    // -----------------------------------------------------------------------------------------

    public Fila_Canvas_t(float p_top, float p_bottom, String p_nombre, int p_octava, boolean p_pincel_gris, boolean p_inicio_octava)
    {
        Color_Picker_t picker = new Color_Picker_t();

        if (a_pinceles_filas[0] == null || a_pinceles_filas[1] == null)
        {
            a_pinceles_filas[0] = picker.getPincel(Color_Picker_t.type_color_t.GRIS);
            a_pinceles_filas[0].setShadowLayer(4, 5, 0, Color.BLACK);

            a_pinceles_filas[1] = picker.getPincel(Color_Picker_t.type_color_t.SALMON);
            a_pinceles_filas[1].setShadowLayer(4, 5, 0, Color.BLACK);
        }

        if (p_pincel_gris)
            a_pincel = a_pinceles_filas[0];
        else
        {
            a_pincel = a_pinceles_filas[1];
        }

        if (p_inicio_octava)
            a_pincel_linea = picker.getPincel(Color_Picker_t.type_color_t.VIOLETA_LIGHT);
        else
            a_pincel_linea = picker.getPincel(Color_Picker_t.type_color_t.BLANCO_VIOLETA);

        a_pincel_texto = picker.getPincel(Color_Picker_t.type_color_t.BLANCO);
        a_pincel_texto.setTextSize(20);
        a_pincel_texto.setStrokeWidth(1);

        a_pos = new float [2];
        a_pos[0] = p_top;
        a_pos[1] = p_bottom;

        a_pos_en_vista = null;

        if (p_nombre != null) {
            a_nombre = p_nombre;
            a_octava = p_octava;
        }
        else {
            a_nombre = "ND";
            a_octava = 0;
        }
    }

    // -----------------------------------------------------------------------------------------

    public String fila_get_nombre()
    {
        return a_nombre;
    }

    // -----------------------------------------------------------------------------------------

    public int fila_get_octava()
    {
        return a_octava;
    }

    // -----------------------------------------------------------------------------------------

    public float [] fila_get_pos()
    {
        return a_pos;
    }

    // -----------------------------------------------------------------------------------------

    public float [] fila_get_pos(Nota_t p_nota)
    {
        float [] pos = null;

        if (p_nota.nota_compara_nombre_octava(a_nombre + a_octava))
        {
            pos = new float [2];
            pos[0] = a_pos[0];
            pos[1] = a_pos[1];
        }

        return pos;
    }

    // -----------------------------------------------------------------------------------------

    public float fila_get_top(float p_y)
    {
        float top = -1;

        if (a_pos_en_vista != null && a_pos_en_vista[0] <= p_y && a_pos_en_vista[1] >= p_y)
            top = a_pos[0];

        return top;
    }

    // -----------------------------------------------------------------------------------------

    public float fila_get_top()
    {
        return a_pos[0];
    }

    // -----------------------------------------------------------------------------------------

    public float [] fila_get_pos_en_vista()
    {
        return a_pos_en_vista;
    }

    // -----------------------------------------------------------------------------------------

    public void fila_dibuja(
                        Canvas p_canvas,
                        float [] p_y_vista,
                        float [] p_coordenadas_vista_0,
                        float p_height_canvas,
                        float p_filas_right,
                        float p_width_canvas,
                        Paint p_pincel)
    {
        if (p_canvas != null && p_y_vista != null)
        {
            Color_Picker_t picker = new Color_Picker_t();
            float top, bottom;

            if (a_pos[0] < p_y_vista[0])
            {
                top = p_coordenadas_vista_0[1];
                bottom = top + (a_pos[1] - p_y_vista[0]);
            }
            else
            {
                top = p_coordenadas_vista_0[1] + (a_pos[0] - p_y_vista[0]);

                if (a_pos[1] > p_y_vista[1])
                    bottom = p_height_canvas;
                else
                    bottom = top + (a_pos[1] - a_pos[0]);
            }

            // Dibujamos el recuadro en donde se especifica la nota de esta fila
            p_canvas.drawRect(0, top, p_filas_right, bottom, a_pincel);

            if (a_pos_en_vista == null)
                a_pos_en_vista = new float [2];

            a_pos_en_vista[0] = top;
            a_pos_en_vista[1] = bottom;

            // Dibujamos la linea divisoria superior que marca la fila de esta nota
            if (a_pos[1] <= p_y_vista[1])
                p_canvas.drawLine(p_filas_right, bottom, p_width_canvas, bottom, a_pincel_linea);

            // Dibujamos el nombre de la nota
            if ((bottom - top) >= ((a_pos[1] - a_pos[0]) / 2))
            {
                p_canvas.drawText(
                        a_nombre + a_octava,
                        p_filas_right / 4,
                        bottom + (((top - bottom) / 12) * 5),
                        a_pincel_texto);
            }
        }
    }
}
