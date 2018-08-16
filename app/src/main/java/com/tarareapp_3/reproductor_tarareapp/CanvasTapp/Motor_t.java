package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Motor_t extends Thread{
    private Diagrama_Pianola_t a_dp;

    private boolean running = false;

    private boolean a_desacelerando_x;
    private boolean a_desacelerando_y;

    private double a_speed_x0;
    private double a_speed_y0;
    private long a_t0;

    private final static double a_FR = -5;

    // ---------------------------------------------------------------------------------------------

    public Motor_t(Diagrama_Pianola_t p_dp) {
        this.a_dp = p_dp;

        a_desacelerando_x = false;
        a_desacelerando_x = false;
    }

    // ---------------------------------------------------------------------------------------------

    public void setRunning(boolean run) {
        running = run;
    }

    // ---------------------------------------------------------------------------------------------

    public void motor_parar_desaceleracion()
    {
        a_desacelerando_x = false;
        a_desacelerando_y = false;
    }

    // ---------------------------------------------------------------------------------------------

    public void motor_desacelerar(double p_speed_x0, double p_speed_y0, long p_t0)
    {
        a_t0 = p_t0;

        if (!a_desacelerando_x)
        {
            a_desacelerando_x = true;
            a_speed_x0 = p_speed_x0;
        }

        if (!a_desacelerando_y)
        {
            a_desacelerando_y = true;
            a_speed_y0 = p_speed_y0;
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_calcula_vista_dp()
    {
        if (a_desacelerando_x || a_desacelerando_y)
        {
            float desplazamiento_x = 0, desplazamiento_y = 0;
            long t = (System.currentTimeMillis() - a_t0) / 1000;

            if (a_desacelerando_x)
            {
                desplazamiento_x = (float) (a_speed_x0 * t + (a_FR / 2) * t);

                if (desplazamiento_x <= 0)
                    a_desacelerando_x = false;
            }

            if (a_desacelerando_y)
            {
                desplazamiento_y = (float) (a_speed_y0 * t + (a_FR / 2) * t);

                if (desplazamiento_y <= 0)
                    a_desacelerando_y = false;
            }

            // a_dp.dp_move_view(desplazamiento_x, desplazamiento_y);
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public void run()
    {
        while (running)
        {
            Canvas canvas = null;

            try
            {
                canvas = a_dp.getHolder().lockCanvas();

                synchronized (a_dp.getHolder())
                {
                    a_dp.dp_inicializa_datos_diagrama(canvas);

                    i_calcula_vista_dp();

                    a_dp.draw(canvas);
                }
            }
            finally
            {
                if (canvas != null)
                    a_dp.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}
