package com.tarareapp_3.reproductor_tarareapp.Grabadora;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Dp_activity;
import com.tarareapp_3.reproductor_tarareapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.filters.LowPassFS;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.writer.WriterProcessor;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Recorder_t extends Activity{

    private final int REQUEST_PERMISSION_PHONE_STATE=1;

    AudioDispatcher dispatcher;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FILE_RESULT = "Tarareapp_example_";
    private static final String AUDIO_RECORDER_FOLDER = "Tarareapp_Repository";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static int example_file_number;
    private static final int RECORDER_BPP = 16;
    private static final int RECORDER_SAMPLERATE = 22050;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = 1024;
    private Thread recordingThread = null;

    private String current_file;

    private Pitch_Detector_t a_pd;

    private String a_partitura_nombre;
    private int a_partitura_bpm;
    private int a_partitura_pulsos_compas;
    private int a_partitura_valor_pulso;
    private double a_partitura_duracion_bit;
    private int a_partitura_num_bits_compas;

    private Thread a_thread;

    // ---------------------------------------------------------------------------------------------

    private double i_calcula_precision(String p_precision)
    {
        double a_precision = 0;
        if(p_precision != null)
        {
            switch (p_precision)
            {
                case "redonda":
                    a_precision = 4;
                    break;

                case "blanca":
                    a_precision = 2;
                    break;

                case "negra":
                    a_precision = 1;
                    break;

                case "corchea":
                    a_precision = 0.5;
                    break;

                case "semicorchea":
                    a_precision = 0.25;
                    break;

                case "fusa":
                    a_precision = 0.125;
                    break;

                case "semifusa":
                    a_precision = 0.0625;
                    break;

                default:
                    a_precision = 0.25;
                    break;
            }
        }else{a_precision = 0.25;}

        return a_precision;
    }
    // ---------------------------------------------------------------------------------------------

    private void i_init_values_score(String p_precision)
    {
        double valor_unidad_negra = a_partitura_bpm/60;
        double valor_pulso;

        if (a_partitura_valor_pulso > 0)
            valor_pulso = 4 / a_partitura_valor_pulso;
        else
            valor_pulso = 1;

        double tamano_compas = valor_pulso * a_partitura_pulsos_compas;

        double precision = i_calcula_precision(p_precision);

        a_partitura_duracion_bit = valor_unidad_negra * precision;
        a_partitura_num_bits_compas = (int) Math.floor(tamano_compas / a_partitura_duracion_bit);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grabadora);

        setButtonHandlers();
        enableButtons(false);

        // Para poder utilizar el microfono del movil se ha de pedir permiso al usuario primero.
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
            showPhoneStatePermission();

        /*bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING)*4;*/

        example_file_number = 1;
        current_file = null;
        a_pd = null;
        a_thread = null;

        a_partitura_nombre = getIntent().getExtras().getString("nombre");
        a_partitura_bpm = getIntent().getExtras().getInt("bpm");;
        a_partitura_pulsos_compas = getIntent().getExtras().getInt("pulsos_compas");
        a_partitura_valor_pulso = getIntent().getExtras().getInt("valor_pulso");

        String precision = getIntent().getExtras().getString("precision");
        i_init_values_score(precision);
    }

    // ---------------------------------------------------------------------------------------------

    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);
        String filename;

        if (!file.exists()){
            file.mkdirs();
        }

        filename = file.getAbsolutePath() + "/" + AUDIO_RECORDER_FILE_RESULT + example_file_number + AUDIO_RECORDER_FILE_EXT_WAV;

        current_file = filename;
        example_file_number++;

        Log.e("archivo: ", filename);

        return filename;
    }

    // ---------------------------------------------------------------------------------------------

    private String getTempFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if (!file.exists()){
            file.mkdirs();
        }

        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);

        if (tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    // ---------------------------------------------------------------------------------------------

    private void deleteTempFile() {
        File file = new File(getTempFilename());
        file.delete();
    }

    // ---------------------------------------------------------------------------------------------

    private void copyWaveFile(String inFilename, String outFilename)
    {
        FileInputStream in;
        FileOutputStream out;
        long totalAudioLen, totalDataLen, longSampleRate = RECORDER_SAMPLERATE;

        int channels = 1;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

        byte[] data = new byte[bufferSize];

        try
        {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }
            in.close();
            out.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException
    {
        byte [] header = new byte[44];
        header[0] = 'R';header[1] = 'I'; header[2] = 'F';header[3] = 'F';// RIFF/WAVE header
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';header[9] = 'A';header[10] = 'V';header[11] = 'E';header[12] = 'f';header[13] = 'm';header[14] = 't';header[15] = ' ';// 'fmt ' chunk
        header[16] = 16;header[17] = 0;header[18] = 0;header[19] = 0;// 4 bytes: size of 'fmt ' chunk
        header[20] = 1;header[21] = 0;header[22] = (byte) channels;header[23] = 0;// format = 1
        header[24] = (byte) (longSampleRate & 0xff);header[25] = (byte) ((longSampleRate >> 8) & 0xff);header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);header[28] = (byte) (byteRate & 0xff);header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff); header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8);header[33] = 0;// block align
        header[34] = RECORDER_BPP;header[35] = 0;header[36] = 'd';header[37] = 'a';header[38] = 't';header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);header[41] = (byte) ((totalAudioLen >> 8) & 0xff);header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);// bits per sample

        out.write(header, 0, 44);
    }

    // ---------------------------------------------------------------------------------------------

    private void i_add_dispatcher_filters()
    {
        if (dispatcher != null)
        {
            AudioProcessor lowPass = new LowPassFS(4000, RECORDER_SAMPLERATE);
            dispatcher.addAudioProcessor(lowPass);

            AudioProcessor highPass = new LowPassFS(60, RECORDER_SAMPLERATE);
            dispatcher.addAudioProcessor(highPass);
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_add_dispatcher_writer()
    {
        if (dispatcher != null)
        {
            try
            {
                RandomAccessFile outputFile = new RandomAccessFile(getTempFilename(), "rw");

                TarsosDSPAudioFormat outputFormat = new TarsosDSPAudioFormat(
                                                                RECORDER_SAMPLERATE,
                                                                16,
                                                                1,
                                                                true,
                                                                false);

                WriterProcessor writer = new WriterProcessor(outputFormat, outputFile);

                dispatcher.addAudioProcessor(writer);
            }
            catch (FileNotFoundException e) { Log.e("StartRecording: ", "archivo no encontrado"); }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_add_dispatcher_pitch_detector()
    {
        if (dispatcher != null)
        {
            a_pd = new Pitch_Detector_t(a_partitura_duracion_bit);

            PitchDetectionHandler pdh = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult result, AudioEvent e)
                {
                    final float pitchInHz = result.getPitch();

                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            if (!a_pd.pd_get_timer_init())
                                a_pd.pd_init_time_thread();

                            a_pd.pd_add_data_frecuency(pitchInHz, 0);
                        }
                    });
                    thread.start();

                    a_thread = thread;
                }
            };

            AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, RECORDER_SAMPLERATE, bufferSize, pdh);

            dispatcher.addAudioProcessor(p);
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void startRecording() {

        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(RECORDER_SAMPLERATE, bufferSize, 0);

        i_add_dispatcher_filters();

        // i_add_dispatcher_writer();

        i_add_dispatcher_pitch_detector();

        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    // ---------------------------------------------------------------------------------------------

    private void stopRecording(){

        if (dispatcher != null) {
            dispatcher.stop();

            if (a_thread != null && !a_thread.isInterrupted())
                a_thread.interrupt();

            a_thread = null;

            if (a_pd != null)
                a_pd.pd_stop_time_thread();
        }

        // copyWaveFile(getTempFilename(), getFilename());
        // deleteTempFile();
    }

    // ---------------------------------------------------------------------------------------------

    private void playRercordedFile()
    {
        if (current_file != null)
        {
            /*new AndroidFFMPEGLocator(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File wav = new File(current_file);
                    AudioDispatcher adp;
                    adp = AudioDispatcherFactory.fromPipe(wav.getAbsolutePath(),44100,5000,2500);
                    adp.addAudioProcessor(new AndroidAudioPlayer(adp.getFormat(),5000, AudioManager.STREAM_MUSIC));
                    adp.run();
                }
            }).start();*/
        }
    }


    // ---------------------------------------------------------------------------------------------

    private void i_recorder_create_score()
    {
        if (a_pd != null)
        {
            final ArrayList<Data_Note_t> notes_atributes = a_pd.pd_get_notes_attributes(a_partitura_num_bits_compas);

            if (notes_atributes != null) {
                Intent myIntent = new Intent(Recorder_t.this, Dp_activity.class);

                myIntent.putExtra("nombre", a_partitura_nombre);
                myIntent.putExtra("bpm", a_partitura_bpm);
                myIntent.putExtra("pulsos_compas", a_partitura_pulsos_compas);
                myIntent.putExtra("valor_pulso", a_partitura_valor_pulso);
                myIntent.putExtra("num_bits", a_partitura_num_bits_compas);

                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", notes_atributes);
                myIntent.putExtra("notas", args);

                startActivity(myIntent);
            }
        }
    }


    // ---------------------------------------------------------------------------------------------

    private void enableButton(int id, boolean isEnable) {
        (findViewById(id)).setEnabled(isEnable);
    }


    // ---------------------------------------------------------------------------------------------

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
        // enableButton(R.id.btnPlay, current_file != null);
        enableButton(R.id.test_dp, a_pd != null);
    }

    // ---------------------------------------------------------------------------------------------

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btnStart: {
                    enableButtons(true);
                    startRecording();
                    break;
                }

                case R.id.btnStop: {
                    stopRecording();
                    enableButtons(false);
                    break;
                }

                /*case R.id.btnPlay: {
                    enableButtons(false);
                    playRercordedFile();
                    break;
                }*/

                case R.id.test_dp: {
                    enableButtons(false);
                    i_recorder_create_score();
                    break;
                }
            }
        }
    };

    // ---------------------------------------------------------------------------------------------

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
        // ((Button) findViewById(R.id.btnPlay)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.test_dp)).setOnClickListener(btnClick);
    }

    // ---------------------------------------------------------------------------------------------

    private void showPhoneStatePermission()
    {
        int [] permissionsToCheck = new int []
                {
                        ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO),
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                };

        for (int i = 0; i < permissionsToCheck.length; i++)
        {
            if (permissionsToCheck[i] != PackageManager.PERMISSION_GRANTED)
            {
                String permiso_manifest = null;

                switch (i)
                {
                    case 0:
                        permiso_manifest = Manifest.permission.RECORD_AUDIO;
                        break;
                    case 1:
                        permiso_manifest = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        break;
                    case 2:
                        permiso_manifest = Manifest.permission.READ_EXTERNAL_STORAGE;
                        break;
                }

                if (permiso_manifest != null)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso_manifest))
                        showExplanation("Permission Needed", "Rationale", permiso_manifest, REQUEST_PERMISSION_PHONE_STATE);
                    else
                        requestPermission(permiso_manifest, REQUEST_PERMISSION_PHONE_STATE);
                }
            } else
                Toast.makeText(Recorder_t.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(Recorder_t.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Recorder_t.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });

        builder.create().show();
    }

    // ---------------------------------------------------------------------------------------------

    private void requestPermission(String permissionName, int permissionRequestCode)
    {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
    }
}
