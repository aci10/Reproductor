package com.tarareapp_3.reproductor_tarareapp.Grabadora;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tarareapp_3.reproductor_tarareapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.writer.WriterProcessor;

public class Recorder_t extends Activity{

    private final int REQUEST_PERMISSION_PHONE_STATE=1;

    AudioDispatcher dispatcher;
    float freqChange;
    float tollerance;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "Crowd_Speech";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_BPP = 16;
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = 1024;
    private Thread recordingThread = null;

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

        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING)*4;
    }

    // ---------------------------------------------------------------------------------------------

    private void enableButton(int id, boolean isEnable) {
        (findViewById(id)).setEnabled(isEnable);
    }


    // ---------------------------------------------------------------------------------------------

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
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
                    enableButtons(false);
                    stopRecording();
                    break;
                }
            }
        }
    };

    // ---------------------------------------------------------------------------------------------

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
    }

    // ---------------------------------------------------------------------------------------------

    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);
        String filename;

        if(!file.exists()){
            file.mkdirs();
        }

        filename = file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV;

        Log.e("archivo: ", filename);

        return filename;
    }

    // ---------------------------------------------------------------------------------------------

    private String getTempFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);

        if(tempFile.exists())
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
        byte[] header = new byte[44];
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

        for (int i = 0; i < header.length; i++)
        {
            System.out.print(header[i]);
        }
        System.out.println("");
        Log.e("", "----------------------------------------------------------------------");

        out.write(header, 0, 44);
    }

    // ---------------------------------------------------------------------------------------------

    private void startRecording() {

        try
        {
            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(RECORDER_SAMPLERATE, bufferSize, 0);
            // AudioProcessor p = new BandPass(freqChange, tollerance, RECORDER_SAMPLERATE);
            // dispatcher.addAudioProcessor(p);

            // Output
            RandomAccessFile outputFile = new RandomAccessFile(getTempFilename(), "rw");
            TarsosDSPAudioFormat outputFormat = new TarsosDSPAudioFormat(44100, 16, 1, true, false);
            WriterProcessor writer = new WriterProcessor(outputFormat, outputFile);
            dispatcher.addAudioProcessor(writer);

            recordingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dispatcher.run();
                }
            }, "Crowd_Speech Thread");

            recordingThread.start();
        }
        catch (FileNotFoundException e) { Log.e("StartRecording: ", "archivo no encontrado"); }
    }

    // ---------------------------------------------------------------------------------------------

    private void stopRecording(){

        if(null !=dispatcher)
        {
            dispatcher.stop();
            recordingThread = null;
        }

        copyWaveFile(getTempFilename(), getFilename());

        deleteTempFile();
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
