package com.tarareapp_3.reproductor_tarareapp;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Crono_t
{
    private int a_bit_inicial;
    private int a_bit_actual;
    private double a_duracion_bit;

    private Thread a_hilo;

    private boolean a_en_reproduccion;

    // ---------------------------------------------------------------------------------------------

    public Crono_t(double p_duracion_bit)
    {
        a_bit_inicial = 0;
        a_bit_actual = 0;

        if (p_duracion_bit >= 0.)
            a_duracion_bit = p_duracion_bit;
        else
            a_duracion_bit = 0.;

        a_en_reproduccion = false;

        a_hilo = null;
    }

    // ---------------------------------------------------------------------------------------------

    public int crono_get_bit_inicial()
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
            if (a_hilo != null && a_hilo.isAlive() && !a_hilo.isInterrupted())
                a_hilo.interrupt();

            a_en_reproduccion = true;

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
                        }
                    }, 500, (long) Math.floor(a_duracion_bit * 1000));
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
        if (a_en_reproduccion)
        {
            a_en_reproduccion = false;

            if (a_hilo != null && a_hilo.isAlive() && !a_hilo.isInterrupted())
                a_hilo.interrupt();

            a_hilo = null;
        }
        else
        {
            Log.e("crono_parar", "crono ya parado.");
        }

        return a_bit_actual;
    }

    // ---------------------------------------------------------------------------------------------

    public void crono_reiniciar()
    {
        a_bit_actual = a_bit_inicial;

        if (a_en_reproduccion)
        {
            a_en_reproduccion = false;

            if (a_hilo != null && a_hilo.isAlive() && !a_hilo.isInterrupted())
                a_hilo.interrupt();

            a_hilo = null;
        }
    }
}

