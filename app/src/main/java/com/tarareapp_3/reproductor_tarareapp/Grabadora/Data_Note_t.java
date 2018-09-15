package com.tarareapp_3.reproductor_tarareapp.Grabadora;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Data_Note_t implements Serializable
{
    private int a_id;
    private String a_nombre;
    private int a_octava;
    private int a_id_compas;
    private int a_bit_inicial;
    private int a_num_bits;

    // ---------------------------------------------------------------------------------------------

    public Data_Note_t(double p_frecuency)
    {
        i_calcula_nombre_y_octava(p_frecuency);

        a_id_compas = -1;
        a_bit_inicial = -1;
        a_num_bits = -1;
    }

    // ---------------------------------------------------------------------------------------------

    public Data_Note_t(int p_id)
    {
        i_get_nota(p_id);

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

    // ---------------------------------------------------------------------------------------------

    public Data_Note_t dn_calcula_nota_media(ArrayList<Data_Note_t> p_notas)
    {
        Data_Note_t nota = null;

        if (p_notas != null)
        {
            int nota_top = 0;
            int nota_bottom = 1000;
            int num_bits = 0;

            ArrayList<int[]> q_ids = new ArrayList<>();

            for (int i = 0; i < p_notas.size(); i++)
            {
                int id = p_notas.get(i).a_id;
                boolean founded = false;

                if (id > nota_top)
                    nota_top = id;
                else if (id < nota_bottom)
                    nota_bottom = id;

                for (int j = 0; !founded && j < q_ids.size(); j++)
                {
                    if (q_ids.get(j)[0] == id)
                    {
                        q_ids.get(j)[1]++;
                        founded = true;
                    }
                }

                if (!founded)
                {
                    q_ids.add(new int [2]);
                    q_ids.get(q_ids.size() - 1)[0] = id;
                    q_ids.get(q_ids.size() - 1)[1] = 1;
                }

                num_bits += p_notas.get(i).a_num_bits;
            }

            if (nota_top - nota_bottom > 1)
            {
                nota = new Data_Note_t(nota_top - 1);
            }
            else
            {
                int final_id = 0;
                int total = 0;

                for (int i = 0; i < q_ids.size(); i++)
                {
                    if (q_ids.get(i)[1] > total)
                    {
                        final_id = q_ids.get(i)[0];
                        total = q_ids.get(i)[1];
                    }
                }

                nota = new Data_Note_t(final_id);
            }

            if (nota != null)
            {
                nota.a_num_bits = num_bits;
                nota.a_id_compas = p_notas.get(0).a_id_compas;
                nota.a_bit_inicial = p_notas.get(0).a_bit_inicial;
            }
        }

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    private boolean i_anyade_nota(ArrayList<Data_Note_t> p_notas)
    {
        boolean added = false;

        if (p_notas != null)
        {
            p_notas.add(this);
            added = true;
        }

        return added;
    }

    // ---------------------------------------------------------------------------------------------

    public ArrayList<Data_Note_t> dn_compara_ids(ArrayList<Data_Note_t> p_notas)
    {
        ArrayList<Data_Note_t> notas = null;
        boolean added = false;

        if (p_notas != null)
        {
            notas = new ArrayList<>();

            if (p_notas.size() > 0)
            {
                int diferencia = p_notas.get(0).a_id - a_id;

                if (diferencia >= -1 && diferencia <= 1)
                    added = i_anyade_nota(notas);
                else if (diferencia >= -2 && diferencia <= 2)
                {
                    if (p_notas.size() > 1)
                    {
                        for (int i = 0; !added && i < p_notas.size(); i++)
                        {
                            int diferencia_aux = p_notas.get(i).a_id - a_id;

                            if (diferencia_aux >= -1 && diferencia_aux <= 1)
                                added = i_anyade_nota(notas);
                        }
                    }
                    else
                        added = i_anyade_nota(notas);
                }
            }
            else
                added = i_anyade_nota(notas);
        }

        if (!added)
            notas = null;

        return notas;
    }

    // ---------------------------------------------------------------------------------------------

    private void i_calcula_nombre_y_octava(double p_frecuencia)
    {
        if (p_frecuencia <= 0)
        {
            a_id = -1;
            a_nombre = "NONE";
            a_octava = 0;
        }
        else if (p_frecuencia >= 31 && p_frecuencia < 33.6755)
        {
            a_id = 0;
            a_octava = 1;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 63.5705 && p_frecuencia < 67.351)
        {
            a_id = 1;
            a_octava = 2;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 127.14 && p_frecuencia < 134.7)
        {
            a_id = 2;
            a_octava = 3;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 257.27 && p_frecuencia < 269.39)
        {
            a_id = 3;
            a_octava = 4;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 508.565 && p_frecuencia < 538.81)
        {
            a_id = 4;
            a_octava = 5;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 1017.7 && p_frecuencia < 1077.26)
        {
            a_id = 5;
            a_octava = 6;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 2034.23 && p_frecuencia < 2155.24)
        {
            a_id = 6;
            a_octava = 7;
            a_nombre = "C";
        }
        else if (p_frecuencia >= 4068.52 && p_frecuencia < 4310.48)
        {
            a_id = 7;
            a_octava = 8;
            a_nombre = "C";
        }

        // -----------------------------

        else if (p_frecuencia >= 33.6755 && p_frecuencia < 35.678)
        {
            a_id = 8;
            a_octava = 1;
            a_nombre = "C#";

        }
        else if (p_frecuencia >= 67.351 && p_frecuencia < 71.356)
        {
            a_id = 9;
            a_octava = 2;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 134.7 && p_frecuencia < 142.71)
        {
            a_id = 10;
            a_octava = 3;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 269.39 && p_frecuencia < 285.425)
        {
            a_id = 11;
            a_octava = 4;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 538.81 && p_frecuencia < 570.85)
        {
            a_id = 12;
            a_octava = 5;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 1077.62 && p_frecuencia < 1141.7)
        {
            a_id = 13;
            a_octava = 6;
            a_nombre = "C#";
        }
        else if (p_frecuencia >= 2155.24 && p_frecuencia < 2283.4)
        {
            a_id = 14;
            a_octava = 7;
            a_nombre = "C#";
        }

        // -----------------------------

        else if (p_frecuencia >= 35.678 && p_frecuencia < 37.4995)
        {
            a_id = 15;
            a_octava = 1;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 71.356 && p_frecuencia < 75.599)
        {
            a_id = 16;
            a_octava = 2;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 142.71 && p_frecuencia < 151.195)
        {
            a_id = 17;
            a_octava = 3;
            a_nombre = "D";
        }
        else if(p_frecuencia >= 285.425 && p_frecuencia < 302.4)
        {
            a_id = 18;
            a_octava = 4;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 570.85 && p_frecuencia < 604.8)
        {
            a_id = 19;
            a_octava = 5;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 1114.7 && p_frecuencia < 1209.6)
        {
            a_id = 20;
            a_octava = 6;
            a_nombre = "D";
        }
        else if (p_frecuencia >= 2283.4 && p_frecuencia < 2419.2)
        {
            a_id = 21;
            a_octava = 7;
            a_nombre = "D";
        }

        // -----------------------------

        else if (p_frecuencia >= 37.4995 && p_frecuencia < 39.747)
        {
            a_id = 22;
            a_octava = 1;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 75.599 && p_frecuencia < 80.0945)
        {
            a_id = 23;
            a_octava = 2;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 151.195 && p_frecuencia < 160.185)
        {
            a_id = 24;
            a_octava = 3;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 302.4 && p_frecuencia < 320.38)
        {
            a_id = 25;
            a_octava = 4;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 604.8 && p_frecuencia < 640.76)
        {
            a_id = 26;
            a_octava = 5;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 1209.6 && p_frecuencia < 1281.52)
        {
            a_id = 27;
            a_octava = 6;
            a_nombre = "D#";
        }
        else if (p_frecuencia >= 2419.2 && p_frecuencia < 2563.04)
        {
            a_id = 28;
            a_octava = 7;
            a_nombre = "D#";
        }

        // -----------------------------

        else if (p_frecuencia >= 39.747 && p_frecuencia < 42.4285)
        {
            a_id = 29;
            a_octava = 1;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 80.0945 && p_frecuencia < 84.857)
        {
            a_id = 30;
            a_octava = 2;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 160.185 && p_frecuencia < 169.71)
        {
            a_id = 31;
            a_octava = 3;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 320.38 && p_frecuencia < 339.43)
        {
            a_id = 32;
            a_octava = 4;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 640.76 && p_frecuencia < 678.86)
        {
            a_id = 33;
            a_octava = 5;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 1281.52 && p_frecuencia < 1357.72)
        {
            a_id = 34;
            a_octava = 6;
            a_nombre = "E";
        }
        else if (p_frecuencia >= 2563.04 && p_frecuencia < 2715.44)
        {
            a_id = 35;
            a_octava = 7;
            a_nombre = "E";
        }

        // -----------------------------

        else if (p_frecuencia >= 42.4285 && p_frecuencia < 44.9515)
        {
            a_id = 36;
            a_octava = 1;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 84.857 && p_frecuencia < 89.983)
        {
            a_id = 37;
            a_octava = 2;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 169.71 && p_frecuencia < 179.805)
        {
            a_id = 38;
            a_octava = 3;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 339.43 && p_frecuencia < 359.51)
        {
            a_id = 39;
            a_octava = 4;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 678.86 && p_frecuencia < 719.02)
        {
            a_id = 40;
            a_octava = 5;
            a_nombre = "F";
        }
        else if (p_frecuencia == 1357.72 && p_frecuencia < 1438.04)
        {
            a_id = 41;
            a_octava = 6;
            a_nombre = "F";
        }
        else if (p_frecuencia >= 2715.44 && p_frecuencia < 2876.08)
        {
            a_id = 42;
            a_octava = 7;
            a_nombre = "F";
        }

        // -----------------------------

        else if (p_frecuencia >= 44.9515 && p_frecuencia < 47.624)
        {
            a_id = 43;
            a_octava = 1;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 89.983 && p_frecuencia < 95.249)
        {
            a_id = 44;
            a_octava = 2;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 179.805 && p_frecuencia < 190.5)
        {
            a_id = 45;
            a_octava = 3;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 359.51 && p_frecuencia < 380.995)
        {
            a_id = 46;
            a_octava = 4;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 719.02 && p_frecuencia < 761.99)
        {
            a_id = 47;
            a_octava = 5;
            a_nombre = "F#";
        }
        else if (p_frecuencia >= 1438.04 && p_frecuencia < 1523.98)
        {
            a_id = 48;
            a_octava = 6;
            a_nombre = "F#";
        }
        else if(p_frecuencia >= 2876.08 && p_frecuencia < 3047.96)
        {
            a_id = 49;
            a_octava = 7;
            a_nombre = "F#";
        }
        // -----------------------------

        else if (p_frecuencia >= 47.624 && p_frecuencia < 50.456)
        {
            a_id = 50;
            a_octava = 1;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 95.249 && p_frecuencia < 100.9145)
        {
            a_id = 51;
            a_octava = 2;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 190.5 && p_frecuencia < 201.825)
        {
            a_id = 52;
            a_octava = 3;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 380.995 && p_frecuencia < 403.65)
        {
            a_id = 53;
            a_octava = 4;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 761.99 && p_frecuencia < 807.3)
        {
            a_id = 54;
            a_octava = 5;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 1523.98 && p_frecuencia < 1614.6)
        {
            a_id = 55;
            a_octava = 6;
            a_nombre = "G";
        }
        else if (p_frecuencia >= 3047.96 && p_frecuencia < 3229.2)
        {
            a_id = 56;
            a_octava = 7;
            a_nombre = "G";
        }

        // -----------------------------

        else if (p_frecuencia >= 50.456 && p_frecuencia < 53.4565)
        {
            a_id = 57;
            a_octava = 1;
            a_nombre = "G#";
        }
        else if (p_frecuencia >= 100.9145 && p_frecuencia < 106.915)
        {
            a_id = 58;
            a_octava = 2;
            a_nombre = "G#";
        }
        else if (p_frecuencia >= 201.825 && p_frecuencia < 213.5)
        {
            a_id = 59;
            a_octava = 3;
            a_nombre = "G#";
        }
        else if(p_frecuencia >= 403.65 && p_frecuencia < 427.65)
        {
            a_id = 60;
            a_octava = 4;
            a_nombre = "G#";
        }
        else if(p_frecuencia >= 807.3 && p_frecuencia < 855.3)
        {
            a_id = 61;
            a_octava = 5;
            a_nombre = "G#";
        }
        else if(p_frecuencia >= 1614.6 && p_frecuencia < 1710.6)
        {
            a_id = 62;
            a_octava = 6;
            a_nombre = "G#";
        }
        else if(p_frecuencia >= 3229.2 && p_frecuencia < 3421.2)
        {
            a_id = 63;
            a_octava = 7;
            a_nombre = "G#";
        }

        // -----------------------------

        else if (p_frecuencia >= 53.4565 && p_frecuencia < 56.635)
        {
            a_id = 64;
            a_octava = 1;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 106.915 && p_frecuencia < 113.27)
        {
            a_id = 65;
            a_octava = 2;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 213.5 && p_frecuencia < 226.54)
        {
            a_id = 66;
            a_octava = 3;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 427.65 && p_frecuencia < 453.08)
        {
            a_id = 67;
            a_octava = 4;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 855.3 && p_frecuencia < 906.16)
        {
            a_id = 68;
            a_octava = 5;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 1710.6 && p_frecuencia < 1812.32)
        {
            a_id = 69;
            a_octava = 6;
            a_nombre = "A";
        }
        else if (p_frecuencia >= 3421.2 && p_frecuencia < 3624.64)
        {
            a_id = 70;
            a_octava = 7;
            a_nombre = "A";
        }

        // -----------------------------

        else if(p_frecuencia >= 56.635 && p_frecuencia < 60.0025)
        {
            a_id = 71;
            a_octava = 1;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 113.27 && p_frecuencia < 120.005)
        {
            a_id = 72;
            a_octava = 2;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 226.54 && p_frecuencia < 240.01)
        {
            a_id = 73;
            a_octava = 3;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 453.08 && p_frecuencia < 480.02)
        {
            a_id = 74;
            a_octava = 4;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 906.16 && p_frecuencia < 960.04)
        {
            a_id = 75;
            a_octava = 5;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 1812.32 && p_frecuencia < 1920.08)
        {
            a_id = 76;
            a_octava = 6;
            a_nombre = "A#";
        }
        else if(p_frecuencia >= 3624.64 && p_frecuencia < 3840.16)
        {
            a_id = 77;
            a_octava = 7;
            a_nombre = "A#";
        }

        // -----------------------------

        else if (p_frecuencia >= 60.0025 && p_frecuencia < 63.5705)
        {
            a_id = 78;
            a_octava = 1;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 120.005 && p_frecuencia < 123.14)
        {
            a_id = 79;
            a_octava = 2;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 240.01 && p_frecuencia < 257.27)
        {
            a_id = 80;
            a_octava = 3;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 480.02 && p_frecuencia < 508.565)
        {
            a_id = 81;
            a_octava = 4;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 960.04 && p_frecuencia < 1017.13)
        {
            a_id = 82;
            a_octava = 5;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 1920.08 && p_frecuencia < 2034.26)
        {
            a_id = 83;
            a_octava = 6;
            a_nombre = "B";
        }
        else if (p_frecuencia >= 3840.16 && p_frecuencia < 4068.52)
        {
            a_id = 84;
            a_octava = 7;
            a_nombre = "B";
        }
        else
        {
            a_id = 0;
            a_nombre = "NONE";
            a_octava = 0;
            Log.e("nota_i_calcula_nombre", "a_frecuencia no registrada en BD");
        }
    }

    // ---------------------------------------------------------------------------------------------

    private void i_get_nota(int p_id)
    {
        if (p_id < 0)
        {
            a_id = -1;
            a_nombre = "NONE";
            a_octava = 0;
        }
        else if (p_id == 0)
        {
            a_id = 0;
            a_octava = 1;
            a_nombre = "C";
        }
        else if (p_id == 1)
        {
            a_id = 1;
            a_octava = 2;
            a_nombre = "C";
        }
        else if (p_id == 2)
        {
            a_id = 2;
            a_octava = 3;
            a_nombre = "C";
        }
        else if (p_id == 3)
        {
            a_id = 3;
            a_octava = 4;
            a_nombre = "C";
        }
        else if (p_id == 4)
        {
            a_id = 4;
            a_octava = 5;
            a_nombre = "C";
        }
        else if (p_id == 5)
        {
            a_id = 5;
            a_octava = 6;
            a_nombre = "C";
        }
        else if (p_id == 6)
        {
            a_id = 6;
            a_octava = 7;
            a_nombre = "C";
        }
        else if (p_id == 7)
        {
            a_id = 7;
            a_octava = 8;
            a_nombre = "C";
        }

        // -----------------------------

        else if (p_id == 8)
        {
            a_id = 8;
            a_octava = 1;
            a_nombre = "C#";

        }
        else if (p_id == 9)
        {
            a_id = 9;
            a_octava = 2;
            a_nombre = "C#";
        }
        else if (p_id == 10)
        {
            a_id = 10;
            a_octava = 3;
            a_nombre = "C#";
        }
        else if (p_id == 11)
        {
            a_id = 11;
            a_octava = 4;
            a_nombre = "C#";
        }
        else if (p_id == 12)
        {
            a_id = 12;
            a_octava = 5;
            a_nombre = "C#";
        }
        else if (p_id == 13)
        {
            a_id = 13;
            a_octava = 6;
            a_nombre = "C#";
        }
        else if (p_id == 14)
        {
            a_id = 14;
            a_octava = 7;
            a_nombre = "C#";
        }

        // -----------------------------

        else if (p_id == 15)
        {
            a_id = 15;
            a_octava = 1;
            a_nombre = "D";
        }
        else if (p_id == 16)
        {
            a_id = 16;
            a_octava = 2;
            a_nombre = "D";
        }
        else if (p_id == 17)
        {
            a_id = 17;
            a_octava = 3;
            a_nombre = "D";
        }
        else if (p_id == 18)
        {
            a_id = 18;
            a_octava = 4;
            a_nombre = "D";
        }
        else if (p_id == 19)
        {
            a_id = 19;
            a_octava = 5;
            a_nombre = "D";
        }
        else if (p_id == 20)
        {
            a_id = 20;
            a_octava = 6;
            a_nombre = "D";
        }
        else if (p_id == 21)
        {
            a_id = 21;
            a_octava = 7;
            a_nombre = "D";
        }

        // -----------------------------

        else if (p_id == 22)
        {
            a_id = 22;
            a_octava = 1;
            a_nombre = "D#";
        }
        else if (p_id == 23)
        {
            a_id = 23;
            a_octava = 2;
            a_nombre = "D#";
        }
        else if (p_id == 24)
        {
            a_id = 24;
            a_octava = 3;
            a_nombre = "D#";
        }
        else if (p_id == 25)
        {
            a_id = 25;
            a_octava = 4;
            a_nombre = "D#";
        }
        else if (p_id == 26)
        {
            a_id = 26;
            a_octava = 5;
            a_nombre = "D#";
        }
        else if (p_id == 27)
        {
            a_id = 27;
            a_octava = 6;
            a_nombre = "D#";
        }
        else if (p_id == 28)
        {
            a_id = 28;
            a_octava = 7;
            a_nombre = "D#";
        }

        // -----------------------------

        else if (p_id == 29)
        {
            a_id = 29;
            a_octava = 1;
            a_nombre = "E";
        }
        else if (p_id == 30)
        {
            a_id = 30;
            a_octava = 2;
            a_nombre = "E";
        }
        else if (p_id == 31)
        {
            a_id = 31;
            a_octava = 3;
            a_nombre = "E";
        }
        else if (p_id == 32)
        {
            a_id = 32;
            a_octava = 4;
            a_nombre = "E";
        }
        else if (p_id == 33)
        {
            a_id = 33;
            a_octava = 5;
            a_nombre = "E";
        }
        else if (p_id == 34)
        {
            a_id = 34;
            a_octava = 6;
            a_nombre = "E";
        }
        else if (p_id == 35)
        {
            a_id = 35;
            a_octava = 7;
            a_nombre = "E";
        }

        // -----------------------------

        else if (p_id == 36)
        {
            a_id = 36;
            a_octava = 1;
            a_nombre = "F";
        }
        else if (p_id == 37)
        {
            a_id = 37;
            a_octava = 2;
            a_nombre = "F";
        }
        else if (p_id == 38)
        {
            a_id = 38;
            a_octava = 3;
            a_nombre = "F";
        }
        else if (p_id == 39)
        {
            a_id = 39;
            a_octava = 4;
            a_nombre = "F";
        }
        else if (p_id == 40)
        {
            a_id = 40;
            a_octava = 5;
            a_nombre = "F";
        }
        else if (p_id == 41)
        {
            a_id = 41;
            a_octava = 6;
            a_nombre = "F";
        }
        else if (p_id == 42)
        {
            a_id = 42;
            a_octava = 7;
            a_nombre = "F";
        }

        // -----------------------------

        else if (p_id == 43)
        {
            a_id = 43;
            a_octava = 1;
            a_nombre = "F#";
        }
        else if (p_id == 44)
        {
            a_id = 44;
            a_octava = 2;
            a_nombre = "F#";
        }
        else if (p_id == 45)
        {
            a_id = 45;
            a_octava = 3;
            a_nombre = "F#";
        }
        else if (p_id == 46)
        {
            a_id = 46;
            a_octava = 4;
            a_nombre = "F#";
        }
        else if (p_id == 47)
        {
            a_id = 47;
            a_octava = 5;
            a_nombre = "F#";
        }
        else if (p_id == 48)
        {
            a_id = 48;
            a_octava = 6;
            a_nombre = "F#";
        }
        else if (p_id == 49)
        {
            a_id = 49;
            a_octava = 7;
            a_nombre = "F#";
        }
        // -----------------------------

        else if (p_id == 50)
        {
            a_id = 50;
            a_octava = 1;
            a_nombre = "G";
        }
        else if (p_id == 51)
        {
            a_id = 51;
            a_octava = 2;
            a_nombre = "G";
        }
        else if (p_id == 52)
        {
            a_id = 52;
            a_octava = 3;
            a_nombre = "G";
        }
        else if (p_id == 53)
        {
            a_id = 53;
            a_octava = 4;
            a_nombre = "G";
        }
        else if (p_id == 54)
        {
            a_id = 54;
            a_octava = 5;
            a_nombre = "G";
        }
        else if (p_id == 55)
        {
            a_id = 55;
            a_octava = 6;
            a_nombre = "G";
        }
        else if (p_id == 56)
        {
            a_id = 56;
            a_octava = 7;
            a_nombre = "G";
        }

        // -----------------------------

        else if (p_id == 57)
        {
            a_id = 57;
            a_octava = 1;
            a_nombre = "G#";
        }
        else if (p_id == 58)
        {
            a_id = 58;
            a_octava = 2;
            a_nombre = "G#";
        }
        else if (p_id == 59)
        {
            a_id = 59;
            a_octava = 3;
            a_nombre = "G#";
        }
        else if (p_id == 60)
        {
            a_id = 60;
            a_octava = 4;
            a_nombre = "G#";
        }
        else if (p_id == 61)
        {
            a_id = 61;
            a_octava = 5;
            a_nombre = "G#";
        }
        else if (p_id == 62)
        {
            a_id = 62;
            a_octava = 6;
            a_nombre = "G#";
        }
        else if (p_id == 63)
        {
            a_id = 63;
            a_octava = 7;
            a_nombre = "G#";
        }

        // -----------------------------

        else if (p_id == 64)
        {
            a_id = 64;
            a_octava = 1;
            a_nombre = "A";
        }
        else if (p_id == 65)
        {
            a_id = 65;
            a_octava = 2;
            a_nombre = "A";
        }
        else if (p_id == 66)
        {
            a_id = 66;
            a_octava = 3;
            a_nombre = "A";
        }
        else if (p_id == 67)
        {
            a_id = 67;
            a_octava = 4;
            a_nombre = "A";
        }
        else if (p_id == 68)
        {
            a_id = 68;
            a_octava = 5;
            a_nombre = "A";
        }
        else if (p_id == 69)
        {
            a_id = 69;
            a_octava = 6;
            a_nombre = "A";
        }
        else if (p_id == 70)
        {
            a_id = 70;
            a_octava = 7;
            a_nombre = "A";
        }

        // -----------------------------

        else if (p_id == 71)
        {
            a_id = 71;
            a_octava = 1;
            a_nombre = "A#";
        }
        else if (p_id == 72)
        {
            a_id = 72;
            a_octava = 2;
            a_nombre = "A#";
        }
        else if (p_id == 73)
        {
            a_id = 73;
            a_octava = 3;
            a_nombre = "A#";
        }
        else if (p_id == 74)
        {
            a_id = 74;
            a_octava = 4;
            a_nombre = "A#";
        }
        else if (p_id == 75)
        {
            a_id = 75;
            a_octava = 5;
            a_nombre = "A#";
        }
        else if (p_id == 76)
        {
            a_id = 76;
            a_octava = 6;
            a_nombre = "A#";
        }
        else if (p_id == 77)
        {
            a_id = 77;
            a_octava = 7;
            a_nombre = "A#";
        }

        // -----------------------------

        else if (p_id == 78)
        {
            a_id = 78;
            a_octava = 1;
            a_nombre = "B";
        }
        else if (p_id == 79)
        {
            a_id = 79;
            a_octava = 2;
            a_nombre = "B";
        }
        else if (p_id == 80)
        {
            a_id = 80;
            a_octava = 3;
            a_nombre = "B";
        }
        else if (p_id == 81)
        {
            a_id = 81;
            a_octava = 4;
            a_nombre = "B";
        }
        else if (p_id == 82)
        {
            a_id = 82;
            a_octava = 5;
            a_nombre = "B";
        }
        else if (p_id == 83)
        {
            a_id = 83;
            a_octava = 6;
            a_nombre = "B";
        }
        else if (p_id == 84)
        {
            a_id = 84;
            a_octava = 7;
            a_nombre = "B";
        }
        else
        {
            a_id = 0;
            a_nombre = "NONE";
            a_octava = 0;
            Log.e("nota_i_calcula_nombre", "a_frecuencia no registrada en BD");
        }
    }
}
