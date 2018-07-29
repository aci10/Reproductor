package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Diagrama_Pianola_t extends View{

    private Partitura_t a_partitura;

    private Canvas a_canvas;

    private float a_zoom;

    private float [] a_coordenadas_vista;

    private Bitmap a_capa_notas;
    private Bitmap a_capa_compases;

    private i_fila_nota [] av_filas;
    private ArrayList<Compas_Canvas_t> av_compases;

    private final static double X100_WIDTH_NOTE_LS = 0.1;
    private final static double X100_HEIGHT_NOTE_LS = 0.15;

    private final static double X100_WIDTH_MEASURE_LS = 0.3;
    private final static double X100_HEIGHT_MEASURE_LS = 0.1;

    private static Paint [] a_pinceles_filas;
    private static float a_filas_right;

    private static Paint a_pincel_negro;

    // ---------------------------------------------------------------------------------------------

    private int i_crea_fila_nota(String nombre, int octava, int indicador, float p_top_0, float p_bottom_0, float p_height_fila)
    {
        if (av_filas != null)
        {
            float top, bottom;

            nombre = nombre + octava;

            if (indicador <= 0) {
                top = p_top_0;
                bottom = p_bottom_0;
            }
            else
            {
                top = p_bottom_0 + p_height_fila * (indicador - 1);
                bottom = top + p_height_fila;
            }

            av_filas[indicador] = new i_fila_nota(indicador, top, bottom, nombre);

            indicador++;
        }

        return indicador;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_crea_filas_notas(float p_altura_lienzo, float p_ancho_lienzo)
    {
        int indicador;
        float top_0, bottom_0, alto_fila;

        av_filas = new i_fila_nota[85];
        a_filas_right = (float) (p_ancho_lienzo * X100_WIDTH_NOTE_LS);

        top_0 = (float) (p_altura_lienzo * X100_HEIGHT_MEASURE_LS);
        bottom_0 = (float) (top_0 + p_altura_lienzo * X100_HEIGHT_NOTE_LS);

        alto_fila = (float) (p_altura_lienzo * X100_HEIGHT_NOTE_LS);

        indicador = 0;

        for (int i = 1; i <= 8; i++)
        {
            indicador = i_crea_fila_nota("C", i, indicador, top_0, bottom_0, alto_fila);

            if (i < 8)
            {
                indicador = i_crea_fila_nota("C#", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("D", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("D#", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("E", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("F", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("F#", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("G", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("G#", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("A", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("A#", i, indicador, top_0, bottom_0, alto_fila);

                indicador = i_crea_fila_nota("B", i, indicador, top_0, bottom_0, alto_fila);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Diagrama_Pianola_t (Context p_context, Partitura_t p_partitura)
    {
        super(p_context);

        if (p_partitura != null)
        {
            a_partitura = p_partitura;
            a_zoom = 1;

            a_pinceles_filas = new Paint [2];

            a_pinceles_filas[0] = new Paint();
            a_pinceles_filas[0].setColor(Color.WHITE);
            a_pinceles_filas[0].setShadowLayer(4, 10, 10, Color.BLACK);

            a_pinceles_filas[1] = new Paint();
            a_pinceles_filas[1].setColor(Color.GRAY);
            a_pinceles_filas[1].setShadowLayer(4, 10, 10, Color.BLACK);

            a_pincel_negro = new Paint();
            a_pincel_negro.setColor(Color.BLACK);
            a_pincel_negro.setTextSize(30);
            a_pincel_negro.setStrokeWidth(2);
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_inicializa_datos_diagrama()
    {
        float [] x0, yf;
        float width_celda_compas;

        a_filas_right = (float) (a_canvas.getWidth() * X100_WIDTH_NOTE_LS) * a_zoom;

        width_celda_compas = (float) (a_canvas.getWidth() * X100_WIDTH_MEASURE_LS) * a_zoom;

        x0 = new float[2];
        x0[0] = a_filas_right;
        x0[1] = 0;

        yf = new float[2];
        yf[0] = a_filas_right + width_celda_compas;
        yf[1] = (float) (a_canvas.getHeight() * X100_HEIGHT_MEASURE_LS);

        /*a_coordenadas_vista = new float[2];
        a_coordenadas_vista[0] = 0;
        a_coordenadas_vista[1] = width_celda_nota * 12 * 4; // Para centrarlo en la 4 octava*/

        i_crea_filas_notas(a_canvas.getHeight(), a_canvas.getWidth());

        av_compases = a_partitura.partitura_crea_compases_canvas(this, x0, yf, width_celda_compas);
    }

    // ---------------------------------------------------------------------------------------------

    private void i_dibuja_filas_notas()
    {
        if (av_filas != null)
        {
            for (int i = 0; i < 6; i++)
            {
                av_filas[i].fila_dibuja();
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_dibuja_compases()
    {
        if (av_compases != null)
        {
            for (int i = 0; i < 3; i++)
            {
                av_compases.get(i).cmp_dibuja(a_canvas, a_pincel_negro);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas)
    {
        boolean inicializar = a_canvas == null;

        super.onDraw(canvas);

        a_canvas = canvas;

        if (a_canvas != null)
        {
            if (inicializar)
                i_inicializa_datos_diagrama();

            // a_pincel.setARGB(1, 160, 97, 167);
            // a_pincel.setARGB(1, 126, 76, 132);
            // a_pincel.setARGB(1, 215, 151, 222);

            a_canvas.drawColor(Color.RED);

            i_dibuja_compases();

            i_dibuja_filas_notas();

            /*float desplazamiento = (float) (a_canvas.getWidth() * X100_WIDTH_MEASURE_LS);
            float desplazamiento_am = (float) (a_canvas.getHeight() * X100_HEIGHT_MEASURE_LS);
            pincel = new Paint();
            pincel.setColor(Color.BLUE);
            a_canvas.drawRect(
                    desplazamiento_n,
                    0,
                    desplazamiento_n + desplazamiento,
                    (float) (a_canvas.getHeight() * X100_HEIGHT_MEASURE_LS),
                    pincel);

            pincel = new Paint();
            pincel.setColor(Color.GREEN);
            a_canvas.drawRect(
                    desplazamiento_n + desplazamiento,
                    0,
                    desplazamiento_n + desplazamiento * 2,
                    (float) (a_canvas.getHeight() * X100_HEIGHT_MEASURE_LS),
                    pincel);

            pincel = new Paint();
            pincel.setColor(Color.YELLOW);
            a_canvas.drawRect(
                    desplazamiento_n + desplazamiento * 2,
                    0,
                    desplazamiento_n + desplazamiento * 3,
                    (float) (a_canvas.getHeight() * X100_HEIGHT_MEASURE_LS),
                    pincel);

            a_canvas.drawRect(
                    0,
                    0,
                    desplazamiento_n + desplazamiento,
                    (float) (a_canvas.getHeight() * X100_HEIGHT_MEASURE_LS),
                    pincel);*/
        }
    }

    // ---------------------------------------------------------------------------------------------

    public float [] dp_get_top_bottom_nota(Nota_t p_nota)
    {
        float [] pos = new float[2];

        pos[0] = 0;
        pos[1] = 0;

        if (p_nota != null)
        {
            int i_inicial = (p_nota.nota_get_octava() * 12) - 12;

            for (int i = i_inicial; i < (i_inicial + 12); i++)
            {
                if (p_nota.nota_compara_nombre_octava(av_filas[i].a_nombre))
                {
                    pos[0] = av_filas[i].a_pos[0];
                    pos[1] = av_filas[i].a_pos[1];
                    break;
                }
            }
        }

        return pos;
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- CLASE FILA NOTAS --------------------------------------

    private class i_fila_nota
    {
        private Paint a_pincel;
        private float [] a_pos;
        private String a_nombre;

        // -----------------------------------------------------------------------------------------

        public i_fila_nota(int p_pincel, float p_top, float p_bottom, String p_nombre)
        {
            if (p_pincel % 2 == 0)
                a_pincel = a_pinceles_filas[0];
            else
            {
                a_pincel = a_pinceles_filas[1];
            }

            a_pos = new float [2];
            a_pos[0] = p_top;
            a_pos[1] = p_bottom;

            if (p_nombre != null)
                a_nombre = p_nombre;
            else
                a_nombre = "ND";
        }

        // -----------------------------------------------------------------------------------------

        public void fila_mueve(float p_distancia)
        {
            if (p_distancia != 0)
            {
                a_pos[0] += p_distancia;
                a_pos[1] += p_distancia;
            }
        }

        // -----------------------------------------------------------------------------------------

        public void fila_dibuja()
        {
            if (a_canvas != null)
            {
                // Dibujamos el recuadro en donde se especifica la nota de esta fila
                a_canvas.drawRect(0, a_pos[0], a_filas_right, a_pos[1], a_pincel);

                // Dibujamos la linea divisoria superior que marca la fila de esta nota
                a_canvas.drawLine(a_filas_right, a_pos[1], a_canvas.getWidth(), a_pos[1], a_pincel_negro);

                // Dibujamos el nombre de la nota
                a_canvas.drawText(a_nombre, a_filas_right / 4, a_pos[0] + ((a_pos[1] - a_pos[0]) / 2), a_pincel_negro);
            }
        }
    }
}
