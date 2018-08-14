package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Nota_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Diagrama_Pianola_t extends SurfaceView{

    private Partitura_t a_partitura;

    private SurfaceHolder a_holder;
    private Motor_t a_motor;

    private Canvas a_canvas;

    private float a_zoom;
    private long a_lastClick;

    private static float [] a_coordenadas_vista_0 = null;
    private float [] a_coordenadas_vista_f;
    private float [] a_coordenadas_onToucheMove_0;
    private float [] a_coordenadas_vista_f0_onToucheMove;

    private float [] a_y_vista;
    private float [] a_x_vista;

    private i_fila_nota [] av_filas;
    private ArrayList<Compas_Canvas_t> av_compases;

    private final static double X100_WIDTH_NOTE_LS = 0.1;
    private final static double X100_HEIGHT_NOTE_LS = 0.15;

    private final static double X100_WIDTH_MEASURE_LS = 0.3;
    private final static double X100_HEIGHT_MEASURE_LS = 0.1;

    private static Paint [] a_pinceles_filas;
    private static float a_filas_right;

    private static Paint a_pincel_negro;

    private boolean a_modificar = false;
    private boolean a_en_edicion = true;

    private float a_height_canvas;
    private float a_width_canvas;

    private Nota_Canvas_t a_nota_seleccionada;

    // ---------------------------------------------------------------------------------------------

    private int i_crea_fila_nota(String nombre, int octava, int indicador, float p_top_0, float p_bottom_0, float p_height_fila)
    {
        if (av_filas != null)
        {
            float top, bottom;

            if (indicador <= 0) {
                top = p_top_0;
                bottom = p_bottom_0;
            }
            else
            {
                top = p_bottom_0 + p_height_fila * (indicador - 1);
                bottom = top + p_height_fila;
            }

            av_filas[indicador] = new i_fila_nota(indicador, top, bottom, nombre, octava);

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

    private void i_inicializa_pinceles_y_partitura(Partitura_t p_partitura)
    {
        if (p_partitura != null)
        {
            a_partitura = p_partitura;
            a_zoom = 1;

            a_pinceles_filas = new Paint [2];

            a_pinceles_filas[0] = new Paint();
            a_pinceles_filas[0].setColor(Color.WHITE);
            a_pinceles_filas[0].setShadowLayer(4, 5, 0, Color.DKGRAY);

            a_pinceles_filas[1] = new Paint();
            a_pinceles_filas[1].setColor(Color.GRAY);
            a_pinceles_filas[1].setShadowLayer(4, 5, 0, Color.DKGRAY);

            a_pincel_negro = new Paint();
            a_pincel_negro.setColor(Color.BLACK);
            a_pincel_negro.setTextSize(30);
            a_pincel_negro.setStrokeWidth(2);

            a_y_vista = new float [2];
            a_x_vista = new  float [2];

            a_coordenadas_onToucheMove_0 = new float[2];
            a_coordenadas_onToucheMove_0[0] = 0;
            a_coordenadas_onToucheMove_0[1] = 0;

            a_coordenadas_vista_f0_onToucheMove = new float[2];
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Diagrama_Pianola_t (Context p_context, Partitura_t p_partitura)
    {
        super(p_context);

        a_motor = new Motor_t(this);

        a_holder = getHolder();

        i_inicializa_pinceles_y_partitura(p_partitura);

        a_holder.addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                a_modificar = true;
                a_motor.setRunning(true);
                a_motor.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                boolean retry = true;

                a_motor.setRunning(false);

                while (retry)
                {
                    try
                    {
                        a_motor.join();
                        retry = false;
                    }
                    catch (InterruptedException e) { }
                }
            }
        });
    }


    // ---------------------------------------------------------------------------------------------

    public void dp_inicializa_datos_diagrama(Canvas p_canvas)
    {
        float [] x0, yf;
        float width_celda_compas;

        if (a_modificar)
        {
            a_canvas = p_canvas;

            a_height_canvas = p_canvas.getHeight();
            a_width_canvas = p_canvas.getWidth();

            a_filas_right = (float) (a_width_canvas * X100_WIDTH_NOTE_LS) * a_zoom;

            width_celda_compas = (float) (a_width_canvas * X100_WIDTH_MEASURE_LS) * a_zoom;

            x0 = new float[2];
            x0[0] = a_filas_right;
            x0[1] = 0;

            yf = new float[2];
            yf[0] = a_filas_right + width_celda_compas;
            yf[1] = (float) (a_height_canvas * X100_HEIGHT_MEASURE_LS);

            a_coordenadas_vista_0 = new float[2];
            a_coordenadas_vista_0[0] = a_filas_right;
            a_coordenadas_vista_0[1] = yf[1];

            a_coordenadas_vista_f = new float[2];
            a_coordenadas_vista_f[0] = a_filas_right;;
            a_coordenadas_vista_f[1] = yf[1] + (float) (a_height_canvas * X100_HEIGHT_NOTE_LS * 12 * 3);

            i_crea_filas_notas(a_height_canvas, a_width_canvas);

            av_compases = a_partitura.partitura_crea_compases_canvas(this, x0, yf, width_celda_compas);

            a_partitura.partitura_muestra_vista();

            a_modificar = false;
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
                if (p_nota.nota_compara_nombre_octava(av_filas[i].a_nombre + av_filas[i].a_octava))
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

    private void i_dibuja_filas_notas(int p_notas_desplazadas)
    {
        if (av_filas != null)
        {
            int i = p_notas_desplazadas;
            int nota_max;


            if (i + 7 > av_filas.length)
                nota_max = av_filas.length;
            else
                nota_max = i + 7;

            for (; i < nota_max; i++)
            {
                av_filas[i].fila_dibuja(a_y_vista);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private int i_calcula_primer_compas_visible()
    {
        float width_compas = (float) (a_width_canvas * X100_WIDTH_MEASURE_LS);
        float desplazamiento_vista_en_x = (a_coordenadas_vista_f[0] - a_coordenadas_vista_0[0]);

        int compases_desplazados = (int) (desplazamiento_vista_en_x / width_compas);
        float resto_pixeles = desplazamiento_vista_en_x % width_compas;

        a_x_vista[0] = a_coordenadas_vista_0[0] + (compases_desplazados * width_compas) + resto_pixeles;
        a_x_vista[1] = a_width_canvas + (compases_desplazados * width_compas) + resto_pixeles;

        return compases_desplazados;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_redimensiona_vector_compases(int p_compas_max)
    {
        if (p_compas_max >= av_compases.size())
        {
            float width_celda_compas = (float) (a_width_canvas * X100_WIDTH_MEASURE_LS) * a_zoom;

            float [] x0, yf;
            x0 = new float[2];
            x0[0] = a_filas_right;
            x0[1] = 0;

            yf = new float[2];
            yf[0] = a_filas_right + width_celda_compas;
            yf[1] = (float) (a_height_canvas * X100_HEIGHT_MEASURE_LS);

            for (int j = av_compases.size(); j < p_compas_max; j++)
            {
                av_compases.add(a_partitura.partitura_add_compas_vacio(this, j, x0, yf, width_celda_compas));
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_dibuja_compases()
    {
        if (av_compases != null)
        {
            int i = i_calcula_primer_compas_visible();
            int compas_max = i + 4, j = i;

            i_redimensiona_vector_compases(compas_max);

            for (; i < compas_max; i++)
            {
                av_compases.get(i).cmp_dibuja(a_canvas, a_x_vista, a_y_vista, a_coordenadas_vista_0, a_pincel_negro);
            }

            for (; j < compas_max; j++)
            {
                av_compases.get(j).cmp_dibuja_notas(a_canvas, a_x_vista, a_y_vista, a_coordenadas_vista_0);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private int i_calcula_y_vista()
    {
        float height_fila = (float) (a_height_canvas * X100_HEIGHT_NOTE_LS);
        float desplazamiento_vista_en_y = (a_coordenadas_vista_f[1] - a_coordenadas_vista_0[1]);

        int notas_desplazadas = (int) (desplazamiento_vista_en_y / height_fila);
        float resto_pixeles = desplazamiento_vista_en_y % height_fila;

        a_y_vista[0] = a_coordenadas_vista_0[1] + (notas_desplazadas * height_fila) + resto_pixeles;
        a_y_vista[1] = a_height_canvas + (notas_desplazadas * height_fila) + resto_pixeles;

        return notas_desplazadas;
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        if (a_canvas != null)
        {
            int notas_desplazadas;

            a_canvas.drawColor(Color.RED);

            notas_desplazadas = i_calcula_y_vista();

            i_dibuja_compases();

            i_dibuja_filas_notas(notas_desplazadas);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void dp_move_view(float p_x, float p_y)
    {
        float desplazamiento_x, desplazamiento_y;

        desplazamiento_x = a_coordenadas_vista_f0_onToucheMove[0] + p_x * -1;

        if (desplazamiento_x >= a_coordenadas_vista_0[0])
            a_coordenadas_vista_f[0] = desplazamiento_x;

        desplazamiento_y = a_coordenadas_vista_f0_onToucheMove[1] + p_y * -1;

        if (desplazamiento_y >= a_coordenadas_vista_0[1])
        {
            if (av_filas[av_filas.length - 1].a_pos[1]
                    > a_coordenadas_vista_f[1] + desplazamiento_y + a_height_canvas - a_coordenadas_vista_0[1])
            {
                a_coordenadas_vista_f[1] = desplazamiento_y;
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_move_view(float p_x, float p_y)
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
            if (av_filas[av_filas.length - 1].a_pos[1] - (a_height_canvas - (a_height_canvas * X100_HEIGHT_MEASURE_LS))
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

    private Nota_Canvas_t i_hay_colision_con_nota(float p_x, float p_y)
    {
        Nota_Canvas_t nota = null;

        if (av_compases != null)
        {
            int i = i_calcula_primer_compas_visible();
            int compas_max;

            if (av_compases.size() < i + 4)
                compas_max = av_compases.size();
            else
                compas_max = i + 4;

            for (; nota == null && i < compas_max; i++)
            {
                nota = av_compases.get(i).cmp_get_nota_pos(p_x, p_y);
            }
        }

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    private float i_obten_top_fila_marcada(float p_y)
    {
        int i = i_calcula_y_vista();
        int nota_max;
        float top = -1;

        if (i + 7 > av_filas.length)
            nota_max = av_filas.length;
        else
            nota_max = i + 7;

        for (; top < 0 && i < nota_max; i++)
        {
            if (av_filas[i].a_pos_en_vista != null && av_filas[i].a_pos_en_vista[0] <= p_y && av_filas[i].a_pos_en_vista[1] >= p_y)
                top = av_filas[i].a_pos[0];
        }

        return top;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_edita_nota(Nota_Canvas_t p_nota, float p_x, float p_y)
    {
        if (av_compases != null)
        {
            int i = i_calcula_primer_compas_visible();
            int j = i_calcula_y_vista();
            int compas_max, nota_max;
            boolean nota_dibujada = false;
            float top = -1;

            if (av_compases.size() < i + 4)
                compas_max = av_compases.size();
            else
                compas_max = i + 4;

            if (j + 7 > av_filas.length)
                nota_max = av_filas.length;
            else
                nota_max = j + 7;

            String nombre_fila = null;
            int octava_fila = 0;

            for (; top < 0 && j < nota_max; j++)
            {
                if (av_filas[j].a_pos_en_vista != null && av_filas[j].a_pos_en_vista[0] <= p_y && av_filas[j].a_pos_en_vista[1] >= p_y)
                {
                    top = av_filas[j].a_pos[0];
                    nombre_fila = av_filas[j].a_nombre;
                    octava_fila = av_filas[j].a_octava;
                }
            }

            top = i_obten_top_fila_marcada(p_y);

            for (; top > 0 && !nota_dibujada && i < compas_max; i++)
            {
                nota_dibujada = av_compases.get(i).cmp_dibuja_nota_desplazada(p_nota, p_x, top, nombre_fila, octava_fila);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x, y;

        x = event.getX();
        y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                if (a_en_edicion)
                {
                    a_nota_seleccionada = i_hay_colision_con_nota(x, y);

                    if (a_nota_seleccionada != null) {
                        Log.e("Nota accedida: ", a_nota_seleccionada.nt_canvas_valores());
                    }
                    else
                        Log.e("Nota accedida: ", "false");
                }
                else
                {
                    a_lastClick = System.currentTimeMillis();
                    a_coordenadas_onToucheMove_0[0] = x;
                    a_coordenadas_onToucheMove_0[1] = y;

                    a_coordenadas_vista_f0_onToucheMove[0] = a_coordenadas_vista_f[0];
                    a_coordenadas_vista_f0_onToucheMove[1] = a_coordenadas_vista_f[1];

                    a_motor.motor_parar_desaceleracion();
                }
                break;

            case MotionEvent.ACTION_MOVE:

                synchronized (getHolder())
                {
                    if (a_en_edicion && a_nota_seleccionada != null)
                        i_edita_nota(a_nota_seleccionada, x, y);
                    else if (!a_en_edicion)
                        i_move_view(x, y);
                }
                break;

            case MotionEvent.ACTION_UP:

                synchronized (getHolder())
                {
                    if (a_en_edicion)
                    {
                        if (a_nota_seleccionada != null)
                        {
                            int i = i_calcula_primer_compas_visible();
                            int compas_max;

                            if (av_compases.size() < i + 4)
                                compas_max = av_compases.size();
                            else
                                compas_max = i + 4;

                            a_nota_seleccionada.nt_canvas_editar_nota_reproductor(av_compases, i, compas_max, a_partitura);
                            a_nota_seleccionada = null;
                            a_modificar = true;
                        }
                    }
                    else
                    {
                        /*long t0, action_time;
                        float moved_x, moved_y, speed_x, speed_y;

                        t0 = System.currentTimeMillis();
                        action_time = t0 - a_lastClick;

                        moved_x = a_coordenadas_onToucheMove_0[0] - x;

                        moved_y = a_coordenadas_onToucheMove_0[1] - y;

                        speed_x = moved_x / action_time;
                        speed_y = moved_y / action_time;

                        a_coordenadas_vista_f0_onToucheMove[0] = a_coordenadas_vista_f[0];
                        a_coordenadas_vista_f0_onToucheMove[1] = a_coordenadas_vista_f[1];

                        a_motor.motor_desacelerar(speed_x, speed_y, t0);*/
                    }
                }
                break;
        }

        return true;
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- CLASE FILA NOTAS --------------------------------------

    private class i_fila_nota
    {
        private Paint a_pincel;
        private float [] a_pos;
        private float [] a_pos_en_vista;
        private String a_nombre;
        private int a_octava;

        // -----------------------------------------------------------------------------------------

        private i_fila_nota(int p_pincel, float p_top, float p_bottom, String p_nombre, int p_octava)
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

        private void fila_dibuja(float [] p_y_vista)
        {
            if (a_canvas != null && p_y_vista != null)
            {
                float top, bottom;

                if (a_pos[0] < p_y_vista[0])
                {
                    top = a_coordenadas_vista_0[1];
                    bottom = top + (a_pos[1] - p_y_vista[0]);
                }
                else
                {
                    top = a_coordenadas_vista_0[1] + (a_pos[0] - p_y_vista[0]);

                    if (a_pos[1] > p_y_vista[1])
                        bottom = a_height_canvas;
                    else
                        bottom = top + (a_pos[1] - a_pos[0]);
                }

                // Dibujamos el recuadro en donde se especifica la nota de esta fila
                a_canvas.drawRect(0, top, a_filas_right, bottom, a_pincel);

                if (a_pos_en_vista == null)
                    a_pos_en_vista = new float [2];

                a_pos_en_vista[0] = top;
                a_pos_en_vista[1] = bottom;

                // Dibujamos la linea divisoria superior que marca la fila de esta nota
                if (a_pos[1] <= p_y_vista[1])
                    a_canvas.drawLine(a_filas_right, bottom, a_width_canvas, bottom, a_pincel_negro);

                // Dibujamos el nombre de la nota
                if ((bottom - top) >= ((a_pos[1] - a_pos[0]) / 2))
                    a_canvas.drawText(a_nombre + a_octava, a_filas_right / 4, bottom + ((top - bottom) / 2), a_pincel_negro);
            }
        }
    }
}
