package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Diagrama_Pianola_t extends View{

    private Partitura_t a_partitura;

    private Canvas a_canvas;
    private Paint a_pincel;

    private float a_zoom;

    private double a_altura_vista;
    private double a_ancho_vista;

    private Bitmap a_capa_notas;
    private Bitmap a_capa_compases;

    private i_fila_nota [] av_filas;
    private ArrayList<Compas_Canvas_t> av_compases;

    // ---------------------------------------------------------------------------------------------

    private int i_crea_fila_nota(
                        String nombre,
                        int octava,
                        int indicador,
                        float ancho,
                        float alto)
    {
        if (av_filas != null)
        {
            nombre += octava;

            av_filas[indicador] = new i_fila_nota(alto + alto * indicador, nombre, ancho, alto);

            indicador++;
        }

        return indicador;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_crea_filas_notas(float p_altura_lienzo, float p_ancho_lienzo)
    {
        int indicador;
        double ancho, alto;

        av_filas = new i_fila_nota[85];

        ancho = p_ancho_lienzo * 0.1;
        alto = p_altura_lienzo * 0.1;

        indicador = 0;

        for (int i = 1; i <= 8; i++)
        {
            indicador = i_crea_fila_nota("C", i, indicador, (float)ancho, (float)alto);

            if (i < 8)
            {
                indicador = i_crea_fila_nota("C#", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("D", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("D#", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("E", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("F", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("F#", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("G", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("G#", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("A", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("A#", i, indicador, (float)ancho, (float)alto);

                indicador = i_crea_fila_nota("B", i, indicador, (float)ancho, (float)alto);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Diagrama_Pianola_t (Context p_context, Partitura_t p_partitura)
    {
        super(p_context);

        if (p_partitura != null)
        {
            float [] x0, yf;
            float width_celda_nota, width_celda_compas;

            a_partitura = p_partitura;

            a_canvas = new Canvas();

            a_pincel = new Paint();

            a_zoom = 1;

            width_celda_nota = (float) (0.1 * a_zoom);

            width_celda_compas = (float) (0.2 * a_zoom);

            x0 = new float[2];
            x0[0] = a_canvas.getWidth() * width_celda_nota;
            x0[1] = (float) (a_canvas.getHeight() * 0.1);

            yf = new float[2];
            yf[0] = a_canvas.getWidth() * (width_celda_nota + width_celda_compas);
            yf[1] = (float) (a_canvas.getHeight() * (0.1 * 2));

            i_crea_filas_notas(a_canvas.getHeight(), a_canvas.getWidth());

            av_compases = a_partitura.partitura_crea_compases_canvas(x0, yf, width_celda_compas);
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (canvas != null)
        {
            super.onDraw(canvas);


        }
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- CLASE FILA NOTAS --------------------------------------

    private class i_fila_nota
    {
        private float a_ancho;
        private float a_alto;
        private float a_x0;
        private String a_nombre;

        // -----------------------------------------------------------------------------------------

        public i_fila_nota(float p_linea_sup, String p_nombre, float p_ancho, float p_alto)
        {
            a_ancho = p_ancho;
            a_alto = p_alto;
            a_x0 = p_linea_sup;
            a_nombre = p_nombre;
        }

        // -----------------------------------------------------------------------------------------

        public float get_ancho()
        {
            return a_ancho;
        }

        // -----------------------------------------------------------------------------------------

        public float get_alto()
        {
            return a_alto;
        }

        // -----------------------------------------------------------------------------------------

        public float get_x0()
        {
            return a_x0;
        }

        // ---------------------------------------------------------------------------------------------

        public String get_nombre()
        {
            return a_nombre;
        }
    }
}
