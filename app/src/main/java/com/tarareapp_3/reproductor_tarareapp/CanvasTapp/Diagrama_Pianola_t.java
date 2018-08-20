package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
    private Vista_Canvas_t a_vista;
    private Barra_Herramientas_t a_tool_bar;

    private Fila_Canvas_t [] av_filas;
    private ArrayList<Compas_Canvas_t> av_compases;

    private boolean a_en_edicion = true;

    private Action_Canvas_t a_action_picked;

    private boolean a_modificar;

    private Context a_context;

    // ---------------------------------------------------------------------------------------------

    private int i_crea_fila_nota(String nombre, int octava, int indicador, float p_top_0, float p_bottom_0, float p_height_fila)
    {
        if (av_filas != null)
        {
            float top, bottom;

            if (indicador <= 0)
            {
                top = p_top_0;
                bottom = p_bottom_0;
            }
            else
            {
                top = p_bottom_0 + p_height_fila * (indicador - 1);
                bottom = top + p_height_fila;
            }

            av_filas[indicador] = new Fila_Canvas_t(indicador, top, bottom, nombre, octava);

            indicador++;
        }

        return indicador;
    }

    // ---------------------------------------------------------------------------------------------

    public void dp_crea_filas_notas(float p_altura_lienzo, float p_ancho_lienzo)
    {
        int indicador;
        float top_0, bottom_0, alto_fila;

        av_filas = new Fila_Canvas_t[85];

        top_0 = a_vista.vista_get_height_compas();

        alto_fila = a_vista.vista_get_height_fila();
        bottom_0 = top_0 + alto_fila;

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

        a_context = p_context;

        a_partitura = p_partitura;

        a_motor = new Motor_t(this);

        a_holder = getHolder();

        a_vista = new Vista_Canvas_t(this);

        a_tool_bar = null;

        a_holder.addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                a_modificar = true;
                a_motor.setRunning(true);
                a_motor.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

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
        float [] coordenadas, x0, yf;

        if (a_modificar)
        {
            coordenadas = a_vista.vista_inicializar_datos(p_canvas);

            av_compases = a_partitura.partitura_crea_compases_canvas(this, coordenadas, a_vista.vista_get_width_compas());

            if (a_tool_bar == null)
                a_tool_bar = new Barra_Herramientas_t(this, a_vista);

            a_partitura.partitura_muestra_vista();

            a_modificar = false;
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void dp_change_mode()
    {
        a_en_edicion = !a_en_edicion;
    }

    // ---------------------------------------------------------------------------------------------

    public void dp_play_score()
    {
        if (a_partitura != null)
        {
            a_partitura.partitura_reproducir_notas_compases();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void dp_stop_score(boolean pause)
    {
        if (a_partitura != null)
            a_partitura.partitura_detener_reproduccion(pause);
    }

    // ---------------------------------------------------------------------------------------------

    public void dp_export_score()
    {
        if (a_partitura != null && a_context != null)
        {
            a_partitura.partitura_exportar_mxml(a_context);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public float [] dp_get_top_bottom_nota(Nota_t p_nota)
    {
        float [] pos = null;

        if (p_nota != null)
        {
            int i_inicial = (p_nota.nota_get_octava() * 12) - 12;

            if (i_inicial < 0)
                i_inicial = 0;

            for (int i = i_inicial; i < (i_inicial + 12); i++)
            {
                pos = av_filas[i].fila_get_pos(p_nota);

                if (pos != null)
                    break;
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
                a_vista.vista_dibuja_fila(av_filas[i]);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_redimensiona_vector_compases(int p_compas_max)
    {
        if (p_compas_max >= av_compases.size())
        {
            float [] coordenadas;
            coordenadas = new float[4];
            coordenadas[0] = a_vista.vista_get_width_fila();
            coordenadas[1] = 0;

            coordenadas[2] = a_vista.vista_get_width_fila() + a_vista.vista_get_width_compas();
            coordenadas[3] = a_vista.vista_get_height_compas();

            for (int j = av_compases.size(); j < p_compas_max; j++)
            {
                av_compases.add(a_partitura.partitura_add_compas_vacio(this, j, coordenadas, a_vista.vista_get_width_compas()));
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_dibuja_compases()
    {
        if (av_compases != null)
        {
            int [] rango_compases = dp_calcula_rango_compases_vista(true);
            int j = rango_compases[0];

            i_redimensiona_vector_compases(rango_compases[1]);

            for (; rango_compases[0] < rango_compases[1]; rango_compases[0]++)
            {
                a_vista.vista_dibuja_compas(av_compases.get(rango_compases[0]));
            }

            boolean es_primer_compas = true;

            for (; j < rango_compases[1]; j++)
            {
                if (j > rango_compases[0])
                    es_primer_compas = false;

                a_vista.vista_dibuja_nota(av_compases.get(j), es_primer_compas);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_Canvas_t dp_hay_colision_con_nota(float p_x, float p_y)
    {
        Nota_Canvas_t nota = null;

        if (av_compases != null)
        {
            int i = a_vista.vista_calcula_primer_compas_visible();
            float tamanyo_rejilla = a_partitura.partitura_get_tamanyo_rejilla(a_vista.vista_get_width_compas());
            int compas_max;

            if (av_compases.size() < i + 4)
                compas_max = av_compases.size();
            else
                compas_max = i + 4;

            for (; nota == null && i < compas_max; i++)
            {
                nota = av_compases.get(i).cmp_get_nota_pos(p_x, p_y, tamanyo_rejilla);
            }
        }

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    public Fila_Canvas_t dp_get_ultima_fila() {
        return av_filas[av_filas.length - 1];
    }


    // ---------------------------------------------------------------------------------------------

    public int [] dp_calcula_rango_compases_vista(boolean p_redimensionar)
    {
        int [] rango_compases = null;

        if (av_compases != null)
        {
            rango_compases = new int [2];

            int i = a_vista.vista_calcula_primer_compas_visible();
            int compas_max;

            if (av_compases.size() < i + 4 && !p_redimensionar)
                compas_max = av_compases.size();
            else
                compas_max = i + 4;

            rango_compases[0] = i;
            rango_compases[1] = compas_max;
        }

        return rango_compases;
    }

    // ---------------------------------------------------------------------------------------------

    private int [] i_calcula_rango_filas_vista()
    {
        int [] rango_filas = null;

        if (av_compases != null)
        {
            rango_filas = new int [2];

            int i = a_vista.vista_calcula_y_view();
            int nota_max;

            if (i + 7 > av_filas.length)
                nota_max = av_filas.length;
            else
                nota_max = i + 7;

            rango_filas[0] = i;
            rango_filas[1] = nota_max;
        }

        return rango_filas;
    }

    // ---------------------------------------------------------------------------------------------

    public Fila_Canvas_t dp_get_fila_marcada(float p_y)
    {
        Fila_Canvas_t fila = null;
        int [] rango_filas = i_calcula_rango_filas_vista();

        for (; rango_filas[0] < rango_filas[1]; rango_filas[0]++)
        {
            float [] pos_en_vista = av_filas[rango_filas[0]].fila_get_pos_en_vista();

            if (pos_en_vista != null && pos_en_vista[0] <= p_y && pos_en_vista[1] >= p_y)
            {
                fila = av_filas[rango_filas[0]];
                break;
            }
        }

        return fila;
    }

    // ---------------------------------------------------------------------------------------------

    public Compas_Canvas_t dp_get_compas_marcado(float p_x)
    {
        Compas_Canvas_t compas = null;
        int [] rango_compases = dp_calcula_rango_compases_vista(false);

        for (; rango_compases[0] < rango_compases[1]; rango_compases[0]++)
        {
            float [] pos_en_vista = av_compases.get(rango_compases[0]).cmp_get_pos();

            if (pos_en_vista != null && pos_en_vista[0] <= p_x && pos_en_vista[1] >= p_x)
            {
                compas = av_compases.get(rango_compases[0]);
            }
        }

        return compas;
    }

    // ---------------------------------------------------------------------------------------------

    public void dp_dibuja_nota_desplazada(Nota_Canvas_t p_nota, float p_x, float p_top, String p_nombre_fila, int p_octava_fila)
    {
        boolean nota_dibujada = false;
        int [] rango_compases = dp_calcula_rango_compases_vista(false);
        float x = a_vista.vista_calcula_posicion_x(p_x);

        for (; p_top > 0 && !nota_dibujada && rango_compases[0] < rango_compases[1]; rango_compases[0]++)
        {
            nota_dibujada = av_compases.get(rango_compases[0]).cmp_dibuja_nota_desplazada(p_nota, x, p_top, p_nombre_fila, p_octava_fila);
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        int notas_desplazadas;

        if (a_action_picked != null)
            a_action_picked.ac_aplicar_scroll_vista();

        notas_desplazadas = a_vista.vista_calcula_y_view();

        i_dibuja_compases();

        i_dibuja_filas_notas(notas_desplazadas);

        a_vista.vista_dibuja_tool_bar(a_tool_bar);
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

                if (a_tool_bar != null && !a_tool_bar.bh_execute_action_if_collition(x, y))
                {
                    if (a_en_edicion)
                    {
                        if (a_action_picked == null)
                            a_action_picked = new Action_Canvas_t(this, a_vista, x, y);
                    }
                    else
                    {
                        a_vista.vista_inicializa_coordenadas_touch(x, y);
                        a_motor.motor_parar_desaceleracion();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                synchronized (getHolder())
                {
                    if (a_en_edicion && a_action_picked != null)
                        a_action_picked.ac_action_move(x, y, a_partitura);
                    else if (!a_en_edicion)
                        a_vista.vista_mover(av_filas, x, y);
                }
                break;

            case MotionEvent.ACTION_UP:

                synchronized (getHolder())
                {
                    if (a_en_edicion && a_action_picked != null)
                    {
                        a_action_picked.ac_action_up(av_compases, a_partitura);

                        if (!a_action_picked.ac_es_delete())
                        {
                            a_action_picked = null;
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
}
