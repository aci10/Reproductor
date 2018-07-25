package com.tarareapp_3.reproductor_tarareapp.Reproductor;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Nota_Canvas_t;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Nota_t
{
    private Hilo_Reproduccion_t a_hilo_rep;

    private String a_nombre;
    private int a_octava;
    private double a_frecuencia;

    private int a_bit_inicial;
    private int a_num_bits;

    private Nota_t a_nota_ligada;
    private Nota_t a_nota_padre;
    private boolean a_es_ultima;

    private boolean a_en_reproduccion;

    private Compas_t a_compas;

    // ---------------------------------------------------------------------------------------------

    private double i_calcula_duracion(int p_num_bits)
    {
        double duracion = 0.;

        if (a_compas != null)
        {
            duracion = p_num_bits * a_compas.compas_get_partitura().partitura_get_duracion_bit();

            if (a_nota_ligada != null)
                duracion += a_nota_ligada.nota_get_duracion();
        }

        return duracion;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_inicializa_variables_constructor(
            Compas_t p_compas,
            int p_bit_inicial,
            int p_num_bits,
            Nota_t p_nota_padre)
    {
        a_compas = p_compas;

        if (a_compas == null)
            Log.e("Nota Constructor", "Compas NULL");

        if(p_bit_inicial >= 0)
            a_bit_inicial = p_bit_inicial;
        else
        {
            a_bit_inicial = 0;
            Log.e("Nota Constructor", "p_bit_inicial es menor a 0");
        }

        if(p_num_bits > 0)
            a_num_bits = p_num_bits;
        else
        {
            a_num_bits = 1;
            Log.e("Nota Constructor", "p_bit_final es menor a p_bit_inicial");
        }

        a_nota_ligada = null;

        a_nota_padre = p_nota_padre;

        a_es_ultima = false;

        double duracion = i_calcula_duracion(a_num_bits);

        if(a_nota_padre == null)
            a_hilo_rep = new Hilo_Reproduccion_t(this, duracion, a_frecuencia);
        else
        {
            a_hilo_rep = null;
            Log.e("Nota Constructor", "ligadura creada");
        }
    }

    // ---------------------------------------------------------------------------------------------

    private double i_calcula_frecuencia(String p_nombre, int p_octava)
    {
        double f_resultante = 0.0;

        switch (p_nombre)
        {
            case "C":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 32.703;
                        break;

                    case 2:
                        f_resultante = 65.406;
                        break;

                    case 3:
                        f_resultante = 130.81;
                        break;

                    case 4:
                        f_resultante = 261.6;
                        break;

                    case 5:
                        f_resultante = 523.25;
                        break;

                    case 6:
                        f_resultante = 1045.5;
                        break;

                    case 7:
                        f_resultante = 2093.0;
                        break;

                    case 8:
                        f_resultante = 4186.0;
                        break;
                }
                break;

            case "C#":
            case "Db":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 34.648;
                        break;

                    case 2:
                        f_resultante = 69.296;
                        break;

                    case 3:
                        f_resultante = 138.59;
                        break;

                    case 4:
                        f_resultante = 277.18;
                        break;

                    case 5:
                        f_resultante = 554.37;
                        break;

                    case 6:
                        f_resultante = 1108.7;
                        break;

                    case 7:
                        f_resultante = 2217.5;
                        break;
                }
                break;

            case "D":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 36.708;
                        break;

                    case 2:
                        f_resultante = 73.416;
                        break;

                    case 3:
                        f_resultante = 146.83;
                        break;

                    case 4:
                        f_resultante = 293.67;
                        break;

                    case 5:
                        f_resultante = 587.33;
                        break;

                    case 6:
                        f_resultante = 1174.7;
                        break;

                    case 7:
                        f_resultante = 2349.3;
                        break;
                }
                break;

            case "D#":
            case "Eb":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 38.291;
                        break;

                    case 2:
                        f_resultante = 77.782;
                        break;

                    case 3:
                        f_resultante = 155.56;
                        break;

                    case 4:
                        f_resultante = 311.13;
                        break;

                    case 5:
                        f_resultante = 622.25;
                        break;

                    case 6:
                        f_resultante = 1244.5;
                        break;

                    case 7:
                        f_resultante = 2489.0;
                        break;
                }
                break;

            case "E":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 41.203;
                        break;

                    case 2:
                        f_resultante = 82.407;
                        break;

                    case 3:
                        f_resultante = 64.81;
                        break;

                    case 4:
                        f_resultante = 329.63;
                        break;

                    case 5:
                        f_resultante = 659.26;
                        break;

                    case 6:
                        f_resultante = 1318.5;
                        break;

                    case 7:
                        f_resultante = 2637.0;
                        break;
                }
                break;

            case "F":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 43.654;
                        break;

                    case 2:
                        f_resultante = 87.307;
                        break;

                    case 3:
                        f_resultante = 174.61;
                        break;

                    case 4:
                        f_resultante = 349.23;
                        break;

                    case 5:
                        f_resultante = 698.43;
                        break;

                    case 6:
                        f_resultante = 1396.9;
                        break;

                    case 7:
                        f_resultante = 2793.8;
                        break;
                }
                break;

            case "F#":
            case "Gb":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 46.249;
                        break;

                    case 2:
                        f_resultante = 92.499;
                        break;

                    case 3:
                        f_resultante = 185.0;
                        break;

                    case 4:
                        f_resultante = 369.99;
                        break;

                    case 5:
                        f_resultante = 739.99;
                        break;

                    case 6:
                        f_resultante = 1480.0;
                        break;

                    case 7:
                        f_resultante = 2960.0;
                        break;
                }
                break;

            case "G":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 48.999;
                        break;

                    case 2:
                        f_resultante = 97.999;
                        break;

                    case 3:
                        f_resultante = 196.0;
                        break;

                    case 4:
                        f_resultante = 392.0;
                        break;

                    case 5:
                        f_resultante = 783.99;
                        break;

                    case 6:
                        f_resultante = 1568.0;
                        break;

                    case 7:
                        f_resultante = 3136.0;
                        break;
                }
                break;

            case "G#":
            case "Ab":
                switch (p_octava)
                {
                    case 1:
                        f_resultante = 51.913;
                        break;

                    case 2:
                        f_resultante = 103.83;
                        break;

                    case 3:
                        f_resultante = 207.65;
                        break;

                    case 4:
                        f_resultante = 415.30;
                        break;

                    case 5:
                        f_resultante = 830.61;
                        break;

                    case 6:
                        f_resultante = 1661.2;
                        break;

                    case 7:
                        f_resultante = 3322.4;
                        break;
                }
                break;

            case "A":
                switch (p_octava)
                {
                    case 0:
                        f_resultante = 27.5;
                        break;

                    case 1:
                        f_resultante = 55.0;
                        break;

                    case 2:
                        f_resultante = 110.0;
                        break;

                    case 3:
                        f_resultante = 220.0;
                        break;

                    case 4:
                        f_resultante = 440.0;
                        break;

                    case 5:
                        f_resultante = 880.0;
                        break;

                    case 6:
                        f_resultante = 1760.0;
                        break;

                    case 7:
                        f_resultante = 3520.0;
                        break;
                }
                break;

            case "A#":
            case "Bb":
                switch (p_octava)
                {
                    case 0:
                        f_resultante = 29.135;
                        break;

                    case 1:
                        f_resultante = 58.270;
                        break;

                    case 2:
                        f_resultante = 116.54;
                        break;

                    case 3:
                        f_resultante = 233.08;
                        break;

                    case 4:
                        f_resultante = 466.16;
                        break;

                    case 5:
                        f_resultante = 932.33;
                        break;

                    case 6:
                        f_resultante = 1864.7;
                        break;

                    case 7:
                        f_resultante = 3729.3;
                        break;
                }
                break;

            case "B":
                switch (p_octava)
                {
                    case 0:
                        f_resultante = 30.868;
                        break;

                    case 1:
                        f_resultante = 61.735;
                        break;

                    case 2:
                        f_resultante = 123.47;
                        break;

                    case 3:
                        f_resultante = 246.94;
                        break;

                    case 4:
                        f_resultante = 493.88;
                        break;

                    case 5:
                        f_resultante = 987.77;
                        break;

                    case 6:
                        f_resultante = 1975.5;
                        break;

                    case 7:
                        f_resultante = 3951.1;
                        break;
                }
                break;

        }

        return f_resultante;
    }

    // ---------------------------------------------------------------------------------------------
    // Este metodo por ahora te pide la frecuencia exacta, ...
    // si el algoritmo utilizado aporta simplemente una aproximacion de esta, habra que modificarlo.
    // Tambien podria optimizarse el metodo de busqueda.

    private void i_calcula_nombre_y_octava()
    {
        if(a_frecuencia == 32.703)
        {
            a_octava = 1;
            a_nombre = "C";
        }
        else if(a_frecuencia == 65.406)
        {
            a_octava = 2;
            a_nombre = "C";
        }
        else if(a_frecuencia == 130.81)
        {
            a_octava = 3;
            a_nombre = "C";
        }
        else if(a_frecuencia == 261.6)
        {
            a_octava = 4;
            a_nombre = "C";
        }
        else if(a_frecuencia == 523.25)
        {
            a_octava = 5;
            a_nombre = "C";
        }
        else if(a_frecuencia == 1045.5)
        {
            a_octava = 6;
            a_nombre = "C";
        }
        else if(a_frecuencia == 2093.0)
        {
            a_octava = 7;
            a_nombre = "C";
        }
        else if(a_frecuencia == 4186.0)
        {
            a_octava = 8;
            a_nombre = "C";
        }

        // -----------------------------

        else if(a_frecuencia == 34.648)
        {
            a_octava = 1;
            a_nombre = "C#";

        }
        else if(a_frecuencia == 69.296)
        {
            a_octava = 2;
            a_nombre = "C#";
        }
        else if(a_frecuencia == 138.59)
        {
            a_octava = 3;
            a_nombre = "C#";
        }
        else if(a_frecuencia == 277.18)
        {
            a_octava = 4;
            a_nombre = "C#";
        }
        else if(a_frecuencia == 554.37)
        {
            a_octava = 5;
            a_nombre = "C#";
        }
        else if(a_frecuencia == 1108.7)
        {
            a_octava = 6;
            a_nombre = "C#";
        }
        else if(a_frecuencia == 2217.5)
        {
            a_octava = 7;
            a_nombre = "C#";
        }

        // -----------------------------

        else if(a_frecuencia == 36.708)
        {
            a_octava = 1;
            a_nombre = "D";
        }
        else if(a_frecuencia == 73.416)
        {
            a_octava = 2;
            a_nombre = "D";
        }
        else if(a_frecuencia == 146.83)
        {
            a_octava = 3;
            a_nombre = "D";
        }
        else if(a_frecuencia == 293.67)
        {
            a_octava = 4;
            a_nombre = "D";
        }
        else if(a_frecuencia == 587.33)
        {
            a_octava = 5;
            a_nombre = "D";
        }
        else if(a_frecuencia == 1174.7)
        {
            a_octava = 6;
            a_nombre = "D";
        }
        else if(a_frecuencia == 2349.3)
        {
            a_octava = 7;
            a_nombre = "D";
        }

        // -----------------------------

        else if(a_frecuencia == 38.291)
        {
            a_octava = 1;
            a_nombre = "D#";
        }
        else if(a_frecuencia == 77.782)
        {
            a_octava = 2;
            a_nombre = "D#";
        }
        else if(a_frecuencia == 155.56)
        {
            a_octava = 3;
            a_nombre = "D#";
        }
        else if(a_frecuencia == 311.13)
        {
            a_octava = 4;
            a_nombre = "D#";
        }
        else if(a_frecuencia == 622.25)
        {
            a_octava = 5;
            a_nombre = "D#";
        }
        else if(a_frecuencia == 1244.5)
        {
            a_octava = 6;
            a_nombre = "D#";
        }
        else if(a_frecuencia == 2489.0) {
            a_octava = 7;
            a_nombre = "D#";
        }

        // -----------------------------

        else if(a_frecuencia == 41.203)
        {
            a_octava = 1;
            a_nombre = "E";
        }
        else if(a_frecuencia == 82.407)
        {
            a_octava = 2;
            a_nombre = "E";
        }
        else if(a_frecuencia == 64.81)
        {
            a_octava = 3;
            a_nombre = "E";
        }
        else if(a_frecuencia == 329.63)
        {
            a_octava = 4;
            a_nombre = "E";
        }
        else if(a_frecuencia == 659.26)
        {
            a_octava = 5;
            a_nombre = "E";
        }
        else if(a_frecuencia == 1318.5)
        {
            a_octava = 6;
            a_nombre = "E";
        }
        else if(a_frecuencia == 2637.0)
        {
            a_octava = 7;
            a_nombre = "E";
        }

        // -----------------------------

        else if(a_frecuencia == 43.654)
        {
            a_octava = 1;
            a_nombre = "F";
        }
        else if(a_frecuencia == 87.307)
        {
            a_octava = 2;
            a_nombre = "F";
        }
        else if(a_frecuencia == 174.61)
        {
            a_octava = 3;
            a_nombre = "F";
        }
        else if(a_frecuencia == 349.23)
        {
            a_octava = 4;
            a_nombre = "F";
        }
        else if(a_frecuencia == 659.26)
        {
            a_octava = 5;
            a_nombre = "F";
        }
        else if(a_frecuencia == 1396.9)
        {
            a_octava = 6;
            a_nombre = "F";
        }
        else if(a_frecuencia == 2793.8)
        {
            a_octava = 7;
            a_nombre = "F";
        }

        // -----------------------------

        else if(a_frecuencia == 46.249)
        {
            a_octava = 1;
            a_nombre = "F#";
        }
        else if(a_frecuencia == 92.499)
        {
            a_octava = 2;
            a_nombre = "F#";
        }
        else if(a_frecuencia == 185.0)
        {
            a_octava = 3;
            a_nombre = "F#";
        }
        else if(a_frecuencia == 369.99)
        {
            a_octava = 4;
            a_nombre = "F#";
        }
        else if(a_frecuencia == 739.99)
        {
            a_octava = 5;
            a_nombre = "F#";
        }
        else if(a_frecuencia == 1480.0)
        {
            a_octava = 6;
            a_nombre = "F#";
        }
        else if(a_frecuencia == 2960.0)
        {
            a_octava = 7;
            a_nombre = "F#";
        }
        // -----------------------------

        else if(a_frecuencia == 48.999)
        {
            a_octava = 1;
            a_nombre = "G";
        }
        else if(a_frecuencia == 97.999)
        {
            a_octava = 2;
            a_nombre = "G";
        }
        else if(a_frecuencia == 196.0)
        {
            a_octava = 3;
            a_nombre = "G";
        }
        else if(a_frecuencia == 392)
        {
            a_octava = 4;
            a_nombre = "G";
        }
        else if(a_frecuencia == 783.99)
        {
            a_octava = 5;
            a_nombre = "G";
        }
        else if(a_frecuencia == 1568.0)
        {
            a_octava = 6;
            a_nombre = "G";
        }
        else if(a_frecuencia == 3136.0) {
            a_octava = 7;
            a_nombre = "G";
        }

        // -----------------------------

        else if(a_frecuencia == 51.913)
        {
            a_octava = 1;
            a_nombre = "G#";
        }
        else if(a_frecuencia == 103.83)
        {
            a_octava = 2;
            a_nombre = "G#";
        }
        else if(a_frecuencia == 207.65)
        {
            a_octava = 3;
            a_nombre = "G#";
        }
        else if(a_frecuencia == 415.30)
        {
            a_octava = 4;
            a_nombre = "G#";
        }
        else if(a_frecuencia == 830.61)
        {
            a_octava = 5;
            a_nombre = "G#";
        }
        else if(a_frecuencia == 1661.2)
        {
            a_octava = 6;
            a_nombre = "G#";
        }
        else if(a_frecuencia == 3322.4)
        {
            a_octava = 7;
            a_nombre = "G#";
        }

        // -----------------------------

        else if(a_frecuencia == 27.5)
        {
            a_octava = 0;
            a_nombre = "A";
        }
        else if(a_frecuencia == 55.0)
        {
            a_octava = 1;
            a_nombre = "A";
        }
        else if(a_frecuencia == 110.0)
        {
            a_octava = 2;
            a_nombre = "A";
        }
        else if(a_frecuencia == 220.0)
        {
            a_octava = 3;
            a_nombre = "A";
        }
        else if(a_frecuencia == 440.0)
        {
            a_octava = 4;
            a_nombre = "A";
        }
        else if(a_frecuencia == 880.0)
        {
            a_octava = 5;
            a_nombre = "A";
        }
        else if(a_frecuencia == 1760.0)
        {
            a_octava = 6;
            a_nombre = "A";
        }
        else if(a_frecuencia == 3520.0)
        {
            a_octava = 7;
            a_nombre = "A";
        }

        // -----------------------------

        else if(a_frecuencia == 29.135)
        {
            a_octava = 0;
            a_nombre = "A#";
        }
        else if(a_frecuencia == 58.270)
        {
            a_octava = 1;
            a_nombre = "A#";
        }
        else if(a_frecuencia == 116.54)
        {
            a_octava = 2;
            a_nombre = "A#";
        }
        else if(a_frecuencia == 233.08)
        {
            a_octava = 3;
            a_nombre = "A#";
        }
        else if(a_frecuencia == 466.16)
        {
            a_octava = 4;
            a_nombre = "A#";
        }
        else if(a_frecuencia == 932.33)
        {
            a_octava = 5;
            a_nombre = "A#";
        }
        else if(a_frecuencia == 1864.7)
        {
            a_octava = 6;
            a_nombre = "A#";
        }
        else if(a_frecuencia == 3729.3)
        {
            a_octava = 7;
            a_nombre = "A#";
        }

        // -----------------------------

        else if(a_frecuencia == 30.868)
        {
            a_octava = 0;
            a_nombre = "B";
        }
        else if(a_frecuencia == 61.735)
        {
            a_octava = 1;
            a_nombre = "B";
        }
        else if(a_frecuencia == 123.47)
        {
            a_octava = 2;
            a_nombre = "B";
        }
        else if(a_frecuencia == 246.94)
        {
            a_octava = 3;
            a_nombre = "B";
        }
        else if(a_frecuencia == 493.88)
        {
            a_octava = 4;
            a_nombre = "B";
        }
        else if(a_frecuencia == 987.77)
        {
            a_octava = 5;
            a_nombre = "B";
        }
        else if(a_frecuencia == 1975.5)
        {
            a_octava = 6;
            a_nombre = "B";
        }
        else if(a_frecuencia == 3951.1)
        {
            a_octava = 7;
            a_nombre = "B";
        }
        else
        {
            Log.e("nota_i_calcula_nombre", "a_frecuencia no registrada en BD");
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_t(
            Compas_t p_compas,
            int p_bit_inicial,
            int p_num_bits,
            Nota_t p_nota_padre,
            String p_nombre, int p_octava)
    {
        a_frecuencia = i_calcula_frecuencia(p_nombre, p_octava);

        if(a_frecuencia > 0.0)
        {
            a_nombre = p_nombre;
            a_octava = p_octava;
        }
        else
        {
            Log.e("Nota Constructor", "Nota no existente");
        }

        i_inicializa_variables_constructor(p_compas, p_bit_inicial, p_num_bits, p_nota_padre);
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_t(
            Compas_t p_compas,
            int p_bit_inicial,
            int p_num_bits,
            Nota_t p_nota_padre,
            double p_frecuencia)
    {
        a_frecuencia = p_frecuencia;
        i_calcula_nombre_y_octava();

        i_inicializa_variables_constructor(p_compas, p_bit_inicial, p_num_bits, p_nota_padre);
    }



    // ---------------------------------------------------------------------------------------------

    private boolean i_en_reproduccion()
    {
        return a_en_reproduccion;
    }

    // ---------------------------------------------------------------------------------------------

    private int i_get_num_bit_final(int p_bit_inicial)
    {
        int num_bit_final;

        num_bit_final = p_bit_inicial + a_num_bits;

        if (a_nota_ligada != null)
            num_bit_final = a_nota_ligada.i_get_num_bit_final(num_bit_final);

        return num_bit_final;
    }

    // ---------------------------------------------------------------------------------------------

    public void nota_inicia_crono()
    {
        Log.e("nota inicia crono", "Inicia");
        if (a_compas != null)
            a_compas.compas_get_partitura().partitura_inicia_crono();
    }

    // ---------------------------------------------------------------------------------------------

    public double nota_get_frecuencia()
    {
        return a_frecuencia;
    }

    // ---------------------------------------------------------------------------------------------

    public int nota_get_num_bit_final()
    {
        int num_bit_final;

        num_bit_final = a_compas.compas_get_pos_bit_inicial() + a_num_bits;

        if (a_nota_ligada != null)
            num_bit_final = a_nota_ligada.i_get_num_bit_final(num_bit_final);

        return num_bit_final;
    }

    // ---------------------------------------------------------------------------------------------

    public void nota_set_hijo(Nota_t p_hijo)
    {
        a_nota_ligada = p_hijo;
    }

    // ---------------------------------------------------------------------------------------------

    public void nota_set_es_ultima(boolean es_ultima)
    {
        a_es_ultima = es_ultima;

        if (a_nota_ligada != null)
            a_nota_ligada.nota_set_es_ultima(es_ultima);
    }

    // ---------------------------------------------------------------------------------------------

    public boolean nota_ocupa_rejilla(int p_bit_rejilla)
    {
        boolean ocupa_rejilla = false;

        if (a_bit_inicial <= p_bit_rejilla)
        {
            if (p_bit_rejilla < a_bit_inicial + a_num_bits)
                ocupa_rejilla = true;
        }

        return ocupa_rejilla;
    }

    // ---------------------------------------------------------------------------------------------

    public void nota_finaliza_reproduccion()
    {
        a_en_reproduccion = false;

        if (a_es_ultima)
            a_compas.compas_get_partitura().partitura_finaliza_reproduccion();
        else if (a_nota_padre != null)
            a_nota_padre.nota_finaliza_reproduccion();

    }

    // ---------------------------------------------------------------------------------------------

    public void nota_detener_reproduccion()
    {
        if (a_en_reproduccion) {
            Log.e("Inicio detencion nota_2", "detencion");
            a_hilo_rep.hilo_reproduccion_detener();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public boolean nota_compara_nombre_octava(String p_nombre, int p_octava)
    {
        boolean es_nota = false;

        if (p_nombre != null && p_octava > 0)
            es_nota = (a_nombre.equals(p_nombre) && p_octava == a_octava);

        return es_nota;
    }

    // ---------------------------------------------------------------------------------------------

    public void nota_inicializar_hilo(
                        long p_delay,
                        int p_bit_inicio_rep,
                        boolean p_es_compas_inicial,
                        boolean p_es_primera_nota)
    {
        double nueva_duracion;

        // System.out.println("nota_inicializar");

        if (!a_en_reproduccion )
        {
            if (a_nota_padre == null || (!a_nota_padre.i_en_reproduccion() && p_es_compas_inicial))
            {
                a_en_reproduccion = true;

                if (p_bit_inicio_rep > a_bit_inicial)
                {
                    int num_bits = a_num_bits - p_bit_inicio_rep;

                    nueva_duracion = i_calcula_duracion(num_bits);

                    Log.e("inicializa hilo de nota", "nueva duracion: " + nueva_duracion);
                }
                else
                {
                    System.out.println("nota_inicializar_hilo: " + p_bit_inicio_rep + " " + a_bit_inicial);
                    nueva_duracion = -1;
                }

                if (a_hilo_rep == null && nueva_duracion > 0.)
                    a_hilo_rep = new Hilo_Reproduccion_t(this, nueva_duracion, a_frecuencia);

                if (a_hilo_rep != null)
                    a_hilo_rep.hilo_reproduccion_inicializar(p_delay, p_es_primera_nota, nueva_duracion);
            }
            else
            {
                Log.e("nota_inicializar_hilo", "Padre en reproduccion");
            }
        }
        else
        {
            Log.e("nota_inicializar_hilo", "ya en reproduccion");
        }
    }

    // ---------------------------------------------------------------------------------------------

    public double nota_get_duracion()
    {
        double duracion;

        if (a_compas != null)
        {
            duracion = a_num_bits * a_compas.compas_get_partitura().partitura_get_duracion_bit();

            if (a_nota_ligada != null)
                duracion += a_nota_ligada.nota_get_duracion();
        }
        else
            duracion = 0.;

        return duracion;
    }

    // ---------------------------------------------------------------------------------------------

    public void nota_recalcula_duracion()
    {
        if (a_compas != null)
        {
            double duracion;

            duracion = a_num_bits * a_compas.compas_get_partitura().partitura_get_duracion_bit();

            if (a_nota_ligada != null)
                duracion += a_nota_ligada.nota_get_duracion();

            a_hilo_rep.hilo_reproduccion_set_duracion(duracion);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void nota_exportar_mxml(MusicXML_Writer_t p_exportador_mxml)
    {
        if (p_exportador_mxml != null)
        {
            double duracion = a_num_bits * a_compas.compas_get_partitura().partitura_get_duracion_bit();

            int estado_ligadura;

            if (a_nota_padre != null)
            {
                if (a_nota_ligada != null)
                    estado_ligadura = 0;
                else
                    estado_ligadura = -1;

            }
            else
            {
                if (a_nota_ligada != null)
                    estado_ligadura = 1;
                else
                    estado_ligadura = 0;
            }

            p_exportador_mxml.mxmlw_escribe_nota(a_nombre, a_octava, duracion, estado_ligadura);
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void nota_muestra_vista()
    {
        System.out.println("Nombre: " + a_nombre + a_octava);
        System.out.println("Bit Inicial: " + a_bit_inicial);
        System.out.println("Num Bits: " + a_num_bits);
        System.out.println("Frecuencia: " + a_frecuencia);
        System.out.println("Duracion: " + this.nota_get_duracion());
        System.out.println("Ultima nota: " + a_es_ultima);

        if (a_nota_ligada != null)
            System.out.println("Notas_ligada: " + a_nota_ligada.nota_get_duracion());
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_Canvas_t nota_crear_canvas_nota(float p_left, float p_top, float p_bottom, float tamanyo_rejilla)
    {
        Nota_Canvas_t nota = null;

        if (a_nota_padre == null)
        {
            int num_rejillas;
            float right;

            num_rejillas = a_num_bits;

            if (a_nota_ligada != null)
                num_rejillas = a_nota_ligada.i_get_num_bit_final(num_rejillas);

            right = num_rejillas * tamanyo_rejilla + p_left;

            nota = new Nota_Canvas_t(p_left, p_top, right, p_bottom);
        }

        return nota;
    }
}


