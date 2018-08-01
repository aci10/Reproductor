package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Motor_t extends Thread{
    private Diagrama_Pianola_t a_dp;

    private boolean running = false;

    // ---------------------------------------------------------------------------------------------

    public Motor_t(Diagrama_Pianola_t p_dp) {
        this.a_dp = p_dp;
    }

    // ---------------------------------------------------------------------------------------------

    public void setRunning(boolean run) {
        running = run;
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
