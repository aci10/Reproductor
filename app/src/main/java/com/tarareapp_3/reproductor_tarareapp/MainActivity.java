package com.tarareapp_3.reproductor_tarareapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Dp_activity;
import com.tarareapp_3.reproductor_tarareapp.Grabadora.Recorder_t;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private int a_signatures;
    private int a_tempo;

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        crea_boton_enlace(R.id.actionGoRecorder, Recorder_t.class);
        crea_boton_enlace(R.id.actionGoEditor, Dp_activity.class);
        crea_boton_enlace(R.id.actionSetBpm, Bpm_Detector_t.class);

        i_create_action_button(R.id.actionPlus);
        i_create_action_button(R.id.actionRest);

        a_tempo = 60;

        Intent intent = getIntent();
        Bundle extras = null;

        if (intent != null)
            extras = intent.getExtras();

        System.out.println(extras);

        if (extras != null)
        {
            int tempo;
            tempo = extras.getInt("bpm");

            if (tempo > 0)
                a_tempo = tempo;
        }

        EditText text = findViewById(R.id.TempoEditText);
        text.setText("" + a_tempo);
    }

    // ---------------------------------------------------------------------------------------------

    private void crea_boton_enlace(final int p_id_vista, final Class<?> cls)
    {
        Button button = findViewById(p_id_vista);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(MainActivity.this, cls);
                EditText myEditText;

                myEditText = findViewById(R.id.titleEditText);
                String name = myEditText.getText().toString();

                myEditText = findViewById(R.id.TempoEditText);
                int tempo = Integer.parseInt(myEditText.getText().toString());

                myEditText = findViewById(R.id.bits);
                int pulsos = Integer.parseInt(myEditText.getText().toString());

                myEditText = findViewById(R.id.bitValue);
                int valorPulso = Integer.parseInt(myEditText.getText().toString());

                Spinner sp_nombre = findViewById(R.id.precision);
                String precision = sp_nombre.getSelectedItem().toString();

                myIntent.putExtra("nombre", name);
                myIntent.putExtra("bpm", tempo);
                myIntent.putExtra("pulsos_compas", pulsos);
                myIntent.putExtra("valor_pulso", valorPulso);
                myIntent.putExtra("precision", precision);
                myIntent.putExtra("clef", i_get_value_clef());
                myIntent.putExtra("type_signature", i_get_type_signature());
                myIntent.putExtra("q_signatures", a_signatures);

                startActivity(myIntent);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------

    private void i_create_action_button(final int p_id_button)
    {
        Button button = findViewById(p_id_button);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                switch (p_id_button)
                {
                    case R.id.actionPlus:
                    {
                        a_signatures++;
                        TextView text = findViewById(R.id.qSignatures);
                        text.setText("" + a_signatures);
                        break;
                    }
                    case R.id.actionRest:
                    {
                        if (a_signatures > 0)
                        {
                            a_signatures--;
                            TextView text = findViewById(R.id.qSignatures);
                            text.setText("" + a_signatures);
                        }
                        break;
                    }
                }
            }
        });
    }

    // ---------------------------------------------------------------------------------------------

    private String i_get_value_clef()
    {
        RadioGroup types = findViewById(R.id.typeClef);
        switch (types.getCheckedRadioButtonId()) {
            case R.id.radioA:
                return "A";
            case R.id.radioG:
            default:
                return "G";
        }
    }

    // ---------------------------------------------------------------------------------------------

    private String i_get_type_signature()
    {
        RadioGroup types = findViewById(R.id.typeSignature);

        switch (types.getCheckedRadioButtonId()) {
            case R.id.radioBemol:
                return "b";
            case R.id.radioSostenido:
            default:
                return "#";
        }
    }
}

