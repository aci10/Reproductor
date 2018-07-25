package com.tarareapp_3.reproductor_tarareapp;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tarareapp_3.reproductor_tarareapp.Grabadora.Audio_Record_t;
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

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        crea_boton_enlace(R.id.test_rep, rep_activity.class);
        crea_boton_enlace(R.id.test_dp, Dp_activity.class);
        crea_boton_enlace(R.id.rep_detc_pitch, Audio_Record_t.class);
    }

    // ---------------------------------------------------------------------------------------------

    private void crea_boton_enlace(int p_id_vista, final Class<?> cls)
    {
        Button next = (Button) findViewById(p_id_vista);
        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(MainActivity.this, cls);
                startActivity(myIntent);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------

    private int i_getValidSampleRates() {

        int sample_rate = 0;

        for (int rate : new int[] {22050, 44100, 16000, 11025, 8000}) {  // add the rates you wish to check against
            int bufferSize = AudioRecord.getMinBufferSize(rate, android.media.AudioFormat.CHANNEL_IN_MONO, android.media.AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize > 0) {
                // buffer size is valid, Sample rate supported
                Log.e("check sample rate", "rate: " + rate);
                Log.e("check sample rate", "BufferSize: " + bufferSize);

                sample_rate = rate;
                break;
            }
        }

        return sample_rate;
    }

    // ---------------------------------------------------------------------------------------------

    /*public void pruebaTarsosDSP(View vista)
    {
        int sample_rate;

        sample_rate = i_getValidSampleRates();

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sample_rate,1024,0);
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
    }*/
}

