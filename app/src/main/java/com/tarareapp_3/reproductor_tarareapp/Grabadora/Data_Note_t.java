package com.tarareapp_3.reproductor_tarareapp.Grabadora;

import android.util.Log;

import java.io.Serializable;

public class Data_Note_t implements Serializable
{
    private String a_nombre;
    private int a_octava;
    private int a_id_compas;
    private int a_bit_inicial;
    private int a_num_bits;

    // ---------------------------------------------------------------------------------------------

    private void i_calcula_nombre_y_octava(double p_frecuencia)
    {
        if (p_frecuencia <= 0)
        {
            a_nombre = "NONE";
            a_octava = 0;
        }
        else if (p_frecuencia >= 31 && p_frecuencia < 33.6755)
        {
            a_octava = 1;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 63.5705 && p_frecuencia < 67.351)
        {
            a_octava = 2;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 127.14 && p_frecuencia < 134.7)
        {
            a_octava = 3;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 257.27 && p_frecuencia < 269.39)
        {
            a_octava = 4;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 508.565 && p_frecuencia < 538.81)
        {
            a_octava = 5;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 1017.7 && p_frecuencia < 1077.26)
        {
            a_octava = 6;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 2034.23 && p_frecuencia < 2155.24)
        {
            a_octava = 7;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 4068.52 && p_frecuencia < 4310.48)
        {
            a_octava = 8;
            a_nombre = "C";
        }

        // -----------------------------

        else if (p_frecuencia >= 33.6755 && p_frecuencia < 35.678)
        {
            a_octava = 1;
            a_nombre = "C#";

        }
        else if (p_frecuencia >= 67.351 && p_frecuencia < 71.356)
        {
            a_octava = 2;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 134.7 && p_frecuencia < 142.71)
        {
            a_octava = 3;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 269.39 && p_frecuencia < 285.425)
        {
            a_octava = 4;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 538.81 && p_frecuencia < 570.85)
        {
            a_octava = 5;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 1077.62 && p_frecuencia < 1141.7)
        {
            a_octava = 6;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 2155.24 && p_frecuencia < 2283.4)
        {
            a_octava = 7;
            a_nombre = "C#";
        }

        // -----------------------------

        else if (p_frecuencia >= 35.678 && p_frecuencia < 37.4995)
        {
            a_octava = 1;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 71.356 && p_frecuencia < 75.599)
        {
            a_octava = 2;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 142.71 && p_frecuencia < 151.195)
        {
            a_octava = 3;
            a_nombre = "D";
        }
        else if(p_frecuencia >= 285.425 && p_frecuencia < 302.4)
        {
            a_octava = 4;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 570.85 && p_frecuencia < 604.8)
        {
            a_octava = 5;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 1114.7 && p_frecuencia < 1209.6)
        {
            a_octava = 6;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 2283.4 && p_frecuencia < 2419.2)
        {
            a_octava = 7;
            a_nombre = "D";
        }

        // -----------------------------

        else if (p_frecuencia >= 37.4995 && p_frecuencia < 39.747)
        {
            a_octava = 1;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 75.599 && p_frecuencia < 80.0945)
        {
            a_octava = 2;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 151.195 && p_frecuencia < 160.185)
        {
            a_octava = 3;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 302.4 && p_frecuencia < 320.38)
        {
            a_octava = 4;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 604.8 && p_frecuencia < 640.76)
        {
            a_octava = 5;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 1209.6 && p_frecuencia < 1281.52)
        {
            a_octava = 6;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 2419.2 && p_frecuencia < 2563.04) {
            a_octava = 7;
            a_nombre = "D#";
        }

        // -----------------------------

        else if (p_frecuencia >= 39.747 && p_frecuencia < 42.4285)
        {
            a_octava = 1;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 80.0945 && p_frecuencia < 84.857)
        {
            a_octava = 2;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 160.185 && p_frecuencia < 169.71)
        {
            a_octava = 3;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 320.38 && p_frecuencia < 339.43)
        {
            a_octava = 4;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 640.76 && p_frecuencia < 678.86)
        {
            a_octava = 5;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 1281.52 && p_frecuencia < 1357.72)
        {
            a_octava = 6;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 2563.04 && p_frecuencia < 2715.44)
        {
            a_octava = 7;
            a_nombre = "E";
        }

        // -----------------------------

        else if (p_frecuencia >= 42.4285 && p_frecuencia < 44.9515)
        {
            a_octava = 1;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 84.857 && p_frecuencia < 89.983)
        {
            a_octava = 2;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 169.71 && p_frecuencia < 179.805)
        {
            a_octava = 3;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 339.43 && p_frecuencia < 359.51)
        {
            a_octava = 4;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 678.86 && p_frecuencia < 719.02)
        {
            a_octava = 5;
            a_nombre = "F";
        }
        else if (p_frecuencia == 1357.72 && p_frecuencia < 1438.04)
        {
            a_octava = 6;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 2715.44 && p_frecuencia < 2876.08)
        {
            a_octava = 7;
            a_nombre = "F";
        }

        // -----------------------------

        else if (p_frecuencia >= 44.9515 && p_frecuencia < 47.624)
        {
            a_octava = 1;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 89.983 && p_frecuencia < 95.249)
        {
            a_octava = 2;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 179.805 && p_frecuencia < 190.5)
        {
            a_octava = 3;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 359.51 && p_frecuencia < 380.995)
        {
            a_octava = 4;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 719.02 && p_frecuencia < 761.99)
        {
            a_octava = 5;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 1438.04 && p_frecuencia < 1523.98)
        {
            a_octava = 6;
            a_nombre = "F#";
        }
        else if(p_frecuencia >= 2876.08 && p_frecuencia < 3047.96)
        {
            a_octava = 7;
            a_nombre = "F#";
        }
        // -----------------------------

        else if (p_frecuencia >= 47.624 && p_frecuencia < 50.456)
        {
            a_octava = 1;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 95.249 && p_frecuencia < 100.9145)
        {
            a_octava = 2;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 190.5 && p_frecuencia < 201.825)
        {
            a_octava = 3;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 380.995 && p_frecuencia < 403.65)
        {
            a_octava = 4;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 761.99 && p_frecuencia < 807.3)
        {
            a_octava = 5;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 1523.98 && p_frecuencia < 1614.6)
        {
            a_octava = 6;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 3047.96 && p_frecuencia < 3229.2) {
            a_octava = 7;
            a_nombre = "G";
        }

        // -----------------------------

        else if (p_frecuencia >= 50.456 && p_frecuencia < 53.4565)
        {
            a_octava = 1;
            a_nombre = "G#";
        }
        else if (p_frecuencia >= 100.9145 && p_frecuencia < 106.915)
        {
            a_octava = 2;
            a_nombre = "G#";
        }
        else if (p_frecuencia >= 201.825 && p_frecuencia < 213.5)
        {
            a_octava = 3;
            a_nombre = "G#";
        }
        else if(p_frecuencia >= 403.65 && p_frecuencia < 427.65)
        {
            a_octava = 4;
            a_nombre = "G#";
        }
        else if(p_frecuencia >= 807.3 && p_frecuencia < 855.3)
        {
            a_octava = 5;
            a_nombre = "G#";
        }
        else if(p_frecuencia >= 1614.6 && p_frecuencia < 1710.6)
        {
            a_octava = 6;
            a_nombre = "G#";
        }
        else if(p_frecuencia >= 3229.2 && p_frecuencia < 3421.2)
        {
            a_octava = 7;
            a_nombre = "G#";
        }

        // -----------------------------

        else if (p_frecuencia >= 53.4565 && p_frecuencia < 56.635)
        {
            a_octava = 1;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 106.915 && p_frecuencia < 113.27)
        {
            a_octava = 2;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 213.5 && p_frecuencia < 226.54)
        {
            a_octava = 3;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 427.65 && p_frecuencia < 453.08)
        {
            a_octava = 4;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 855.3 && p_frecuencia < 906.16)
        {
            a_octava = 5;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 1710.6 && p_frecuencia < 1812.32)
        {
            a_octava = 6;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 3421.2 && p_frecuencia < 3624.64)
        {
            a_octava = 7;
            a_nombre = "A";
        }

        // -----------------------------

        else if(p_frecuencia >= 56.635 && p_frecuencia < 60.0025)
        {
            a_octava = 1;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 113.27 && p_frecuencia < 120.005)
        {
            a_octava = 2;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 226.54 && p_frecuencia < 240.01)
        {
            a_octava = 3;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 453.08 && p_frecuencia < 480.02)
        {
            a_octava = 4;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 906.16 && p_frecuencia < 960.04)
        {
            a_octava = 5;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 1812.32 && p_frecuencia < 1920.08)
        {
            a_octava = 6;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 3624.64 && p_frecuencia < 3840.16)
        {
            a_octava = 7;
            a_nombre = "A#";
        }

        // -----------------------------

        else if (p_frecuencia >= 60.0025 && p_frecuencia < 63.5705)
        {
            a_octava = 1;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 120.005 && p_frecuencia < 123.14)
        {
            a_octava = 2;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 240.01 && p_frecuencia < 257.27)
        {
            a_octava = 3;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 480.02 && p_frecuencia < 508.565)
        {
            a_octava = 4;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 960.04 && p_frecuencia < 1017.13)
        {
            a_octava = 5;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 1920.08 && p_frecuencia < 2034.26)
        {
            a_octava = 6;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 3840.16 && p_frecuencia < 4068.52)
        {
            a_octava = 7;
            a_nombre = "B";
        }
        else
        {
            a_nombre = "NONE";
            a_octava = 0;
            Log.e("nota_i_calcula_nombre", "a_frecuencia no registrada en BD");
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Data_Note_t(double p_frecuency)
    {
        i_calcula_nombre_y_octava(p_frecuency);

        a_id_compas = -1;
        a_bit_inicial = -1;
        a_num_bits = -1;
    }

    // ---------------------------------------------------------------------------------------------

    public String dn_get_nombre()
    {
        return a_nombre;
    }

    // ---------------------------------------------------------------------------------------------

    public int dn_get_octava()
    {
        return a_octava;
    }

    // ---------------------------------------------------------------------------------------------

    public int dn_get_id_compas()
    {
        return a_id_compas;
    }

    // ---------------------------------------------------------------------------------------------

    public int dn_get_bit_inicial()
    {
        return a_bit_inicial;
    }

    // ---------------------------------------------------------------------------------------------

    public int dn_get_num_bits()
    {
        return a_num_bits;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean dn_compare(Data_Note_t p_dn)
    {
        boolean equals = false;

        if (p_dn != null)
            equals = (a_nombre + a_octava).equals(p_dn.a_nombre + p_dn.a_octava);

        return equals;
    }

    // ---------------------------------------------------------------------------------------------

    public void dn_set_data(int p_bit_inicial, int p_id_compas, int p_num_bits)
    {
        a_bit_inicial = p_bit_inicial;
        a_id_compas = p_id_compas;
        a_num_bits = p_num_bits;
    }

    // ---------------------------------------------------------------------------------------------

    public void dn_increase_num_bits()
    {
        if (a_num_bits > 0)
            a_num_bits++;
        else
            a_num_bits = 1;
    }
}
