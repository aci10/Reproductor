package com.tarareapp_3.reproductor_tarareapp;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Crono_t
{
    private int a_bit_inicial;
    private int a_bit_rep_inicial;
    private int a_bit_actual;
    private double a_duracion_bit;

    private Thread a_hilo;
    private Timer a_timer;

    private boolean a_en_reproduccion;

    // ---------------------------------------------------------------------------------------------

    public Crono_t(double p_duracion_bit)
    {
        a_bit_inicial = 0;
        a_bit_rep_inicial = 0;
        a_bit_actual = 0;

        if (p_duracion_bit >= 0.)
            a_duracion_bit = p_duracion_bit;
        else
            a_duracion_bit = 0.;

        a_en_reproduccion = false;

        a_hilo = null;

        a_timer = null;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_detener_crono(boolean reiniciar)
    {
        if (a_en_reproduccion)
        {
            a_en_reproduccion = false;

            if (a_timer != null)
                a_timer.cancel();

            if (a_hilo != null && !a_hilo.isInterrupted())
                a_hilo.interrupt();

            a_hilo = null;

            if (reiniciar)
                a_bit_actual = a_bit_inicial;
            else
                a_bit_rep_inicial = a_bit_actual;
        }
    }

    // ---------------------------------------------------------------------------------------------

    public int crono_get_bit_rep_inicial()
    {
        return a_bit_inicial;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean crono_get_en_reproduccion()
    {
        return a_en_reproduccion;
    }

    // ---------------------------------------------------------------------------------------------

    public int crono_get_bit_actual()
    {
        return a_bit_actual;
    }

    // ---------------------------------------------------------------------------------------------

    public void crono_set_bit_inicial(int p_bit_inicial)
    {
        if (p_bit_inicial >= 0)
            a_bit_inicial = p_bit_inicial;
        else
            a_bit_inicial = 0;
    }

    // ---------------------------------------------------------------------------------------------

    public void crono_iniciar()
    {
        if (!a_en_reproduccion)
        {
            if (a_hilo != null && !a_hilo.isInterrupted())
                a_hilo.interrupt();

            a_en_reproduccion = true;
            // a_bit_actual = a_bit_rep_inicial;

            final Thread hilo = new Thread(new Runnable()
            {
                Timer timer = new Timer();

                public void run()
                {
                    timer.scheduleAtFixedRate(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            a_bit_actual++;
                            Log.e("bit_actual", ""+a_bit_actual);
                        }
                    }, 0, (long) Math.floor(a_duracion_bit * 1000));

                    a_timer = timer;
                }
            });
            hilo.start();

            a_hilo = hilo;
        }
        else
        {
            Log.e("crono_iniciar", "crono ya iniciado.");
        }
    }

    // ---------------------------------------------------------------------------------------------

    public int crono_parar()
    {
        i_detener_crono(false);

        return a_bit_actual;
    }

    // ---------------------------------------------------------------------------------------------

    public void crono_reiniciar()
    {
        a_bit_rep_inicial = a_bit_inicial;
        i_detener_crono(true);
    }
}

