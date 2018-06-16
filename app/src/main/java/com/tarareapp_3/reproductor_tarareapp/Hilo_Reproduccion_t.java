package com.tarareapp_3.reproductor_tarareapp;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
// import android.media.VolumeShaper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Hilo_Reproduccion_t {

    private Nota_t a_nota;

    private double a_duracion;
    private int a_frecuencia_muestreo;
    private int a_num_muestras;
    private double a_f_tono;
    private double av_muestras[];

    private Thread a_hilo;
    private AudioTrack a_audioTrack;
    private Runnable am_play;

    private byte av_sonido_generado[];

    private android.os.Handler a_handler;

    // private boolean a_detenido;

    // ---------------------------------------------------------------------------------------------

    private void i_calcula_variables()
    {
        a_num_muestras = (int) Math.floor(a_duracion * 4 * a_frecuencia_muestreo);
        av_muestras = new double[a_num_muestras];
        av_sonido_generado = new byte[2 * a_num_muestras];
    }

    // ---------------------------------------------------------------------------------------------

    public Hilo_Reproduccion_t(Nota_t p_nota, double p_duracion, double p_f_tono)
    {
        a_nota = p_nota;

        a_duracion = p_duracion;
        a_frecuencia_muestreo = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        a_f_tono = p_f_tono;

        a_audioTrack = null;

        i_calcula_variables();

        a_handler = new android.os.Handler();

        a_hilo = null;

        am_play = null;

        // a_detenido = false;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_gen_tone()
    {
        for (int i = 0; i < a_num_muestras; ++i)
        {
            av_muestras[i] = Math.sin(2 * Math.PI * i / (a_frecuencia_muestreo/a_f_tono));
        }

        int idx = 0;
        for (final double dVal : av_muestras)
        {
            final short val = (short) ((dVal * 32767));
            av_sonido_generado[idx++] = (byte) (val & 0x00ff);
            av_sonido_generado[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_play()
    {
        // System.out.println("reproduce hilo nota");

        //if (!a_detenido)
        // {
            try
            {
                a_audioTrack = new AudioTrack(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build(),
                        new AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                                .setSampleRate(a_frecuencia_muestreo)
                                .build(),
                        a_num_muestras,
                        AudioTrack.MODE_STATIC,
                        AudioManager.AUDIO_SESSION_ID_GENERATE);

            /*VolumeShaper.Configuration config =
                    new VolumeShaper.Configuration.Builder()
                            .setDuration((long) (a_duracion * 1000))
                            .setCurve(
                                    new float[] {0.f, 0.05f, 0.95f, 1.f},
                                    new float[] {0.2f, 1.f, 1.f, 0.2f})
                            .setInterpolatorType(VolumeShaper.Configuration.INTERPOLATOR_TYPE_LINEAR)
                            .build();*/

                if (a_audioTrack != null && a_audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED)
                {
                    // System.out.println("se ejecuta sonido, id: " + a_audioTrack.getAudioSessionId());

                    // System.out.println("Numero de muestras: " + a_num_muestras);

                    a_audioTrack.write(av_sonido_generado, 0, av_sonido_generado.length);

                    //VolumeShaper vol_shaper = a_audioTrack.createVolumeShaper(config);

                    //vol_shaper.apply(VolumeShaper.Operation.PLAY);

                    a_audioTrack.play();

                }
                else
                {
                    Log.e("hilo_play", "audioTrack no inicializado.");
                    a_audioTrack.release();
                    a_audioTrack = null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        // }
        // else
           // a_detenido = false;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_libera_audioTrack_hilo()
    {
        if (a_audioTrack != null){
            System.out.println("entra en liberacion audioTrack, id: " + a_audioTrack.getAudioSessionId());
            a_audioTrack.stop();
            a_audioTrack.release();
            a_audioTrack = null;
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_inicializa_reproduccion(final long p_delay, double p_duracion)
    {
        double duracion_aux = a_duracion;

        a_duracion = p_duracion;

        i_libera_audioTrack_hilo();

        final Thread hilo = new Thread(new Runnable() {
            Timer timer = new Timer();

            public void run() {
                i_gen_tone();

                am_play = new Runnable() {
                    public void run() {
                        i_play();
                    }
                };

                a_handler.postDelayed(am_play, p_delay);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run()
                    {
                        i_libera_audioTrack_hilo();
                        a_nota.nota_finaliza_reproduccion();
                        am_play = null;
                    }
                }, p_delay + (long) Math.floor(a_duracion * 1000));
            }
        });
        hilo.start();

        a_hilo = hilo;

        a_duracion = duracion_aux;
    }

    // ---------------------------------------------------------------------------------------------

    public void hilo_reproduccion_set_duracion(double p_duracion)
    {
        a_duracion = p_duracion;
        i_calcula_variables();
    }

    // ---------------------------------------------------------------------------------------------

    public void hilo_reproduccion_set_frecuencia_tono(double p_f_tono)
    {
        a_f_tono = p_f_tono;
        i_calcula_variables();
    }

    // ---------------------------------------------------------------------------------------------

    public void hilo_reproduccion_inicializar(final long p_delay)
    {
        i_inicializa_reproduccion(p_delay, a_duracion);
    }

    public void hilo_reproduccion_inicializar(final long p_delay, double p_duracion)
    {
        if (p_duracion < 0)
            p_duracion = a_duracion;

        System.out.println(p_duracion);
        System.out.println(a_duracion);

        i_inicializa_reproduccion(p_delay, p_duracion);
    }

    // ---------------------------------------------------------------------------------------------

    public void hilo_reproduccion_detener()
    {
        Log.e("Detencion reproduccion hilo", "a_audioTrack");
        System.out.println("a_audioTrack: " + a_audioTrack);
        System.out.println("a_hilo: " + a_hilo);
        System.out.println("hilo interrumpido: " + a_hilo.isInterrupted());

        if (a_hilo != null && !a_hilo.isInterrupted())
        {
            System.out.println("Entra en detencion hilo");

            if (a_audioTrack != null)
                i_libera_audioTrack_hilo();
            else
            {
                if (a_handler != null && am_play != null)
                {
                    a_handler.removeCallbacks(am_play);
                    a_handler = null;
                    am_play = null;
                    // a_detenido = true;
                }
            }

            a_hilo.interrupt();
            a_nota.nota_finaliza_reproduccion();
            a_hilo = null;
        }
    }
}

