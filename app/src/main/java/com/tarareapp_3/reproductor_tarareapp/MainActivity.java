package com.tarareapp_3.reproductor_tarareapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tarareapp_3.reproductor_tarareapp.Grabadora.Pitch_Detector_t;
import com.tarareapp_3.reproductor_tarareapp.Grabadora.Recorder_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;
import com.tarareapp_3.reproductor_tarareapp.Reproductor.rep_activity;
import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Dp_activity;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private Pitch_Detector_t a_pd;
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        crea_boton_enlace(R.id.test_rep, rep_activity.class);
        crea_boton_enlace(R.id.test_dp, Dp_activity.class);
        crea_boton_enlace(R.id.rep_detc_pitch, Recorder_t.class);

        a_pd = null;
    }

    // ---------------------------------------------------------------------------------------------

    private void crea_boton_enlace(final int p_id_vista, final Class<?> cls)
    {
        Button next = (Button) findViewById(p_id_vista);
        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(MainActivity.this, cls);

                if (p_id_vista == R.id.rep_detc_pitch)
                {
                    myIntent.putExtra("nombre", "ejemplo");
                    myIntent.putExtra("bpm", 60);
                    myIntent.putExtra("pulsos_compas", 4);
                    myIntent.putExtra("valor_pulso", 4);
                    myIntent.putExtra("precision", "semicorchea");
                }

                startActivity(myIntent);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------

    public void pruebaTarsosDSP(View vista)
    {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e)
            {
                final float pitchInHz = result.getPitch();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        TextView text = findViewById(R.id.textView1);
                        text.setText("" + pitchInHz);
                    }
                });
            }
        };

        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);

        dispatcher.addAudioProcessor(p);

        new Thread(dispatcher,"Audio Dispatcher").start();
    }
}

