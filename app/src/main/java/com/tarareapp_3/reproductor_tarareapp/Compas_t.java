package com.tarareapp_3.reproductor_tarareapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Compas_t
{
    private Partitura_t a_partitura;

    private ArrayList<Nota_t>[] av_notas;

    // ---------------------------------------------------------------------------------------------

    public Compas_t(Partitura_t p_partitura)
    {
        if(p_partitura != null)
            a_partitura = p_partitura;
        else
        {
            a_partitura = null;
            Log.e("Constructor Partitura: ", "p_partitura es NULL");
        }

        av_notas = new ArrayList[a_partitura.partitura_get_num_bits_en_compas()];

        for (int i = 0; i < av_notas.length; i++)
        {
            av_notas[i] = new ArrayList<>();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public Partitura_t compas_get_partitura()
    {
        return a_partitura;
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_append_nota(
            int p_bit_inicial,
            int p_num_bits,
            Nota_t p_nota_padre,
            String p_nombre,
            int p_octava)
    {
        boolean no_encontrado;

        if (av_notas != null && p_bit_inicial >= 0 && p_bit_inicial < av_notas.length)
        {
            Nota_t nota = new Nota_t(
                    this,
                    p_bit_inicial,
                    p_num_bits,
                    p_nota_padre,
                    p_nombre, p_octava);

            av_notas[p_bit_inicial].add(nota);
        }
        else
        {
            Log.e("compas_append_nota", "La nota no ha podido anadirse correctamente.");
            Log.e("av_notas", "" + av_notas);
            Log.e("av_notas.lenght", "" + av_notas.length);
            Log.e("p_bit_inicial", "" + p_bit_inicial);
            Log.e("p_nota_padre", "" + p_nota_padre);
        }

        System.out.println("compas_append_nota->av_notas: " + av_notas);
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_t compas_get_nota(int p_bit, int p_frecuencia)
    {
        Nota_t nota = null;

        if(av_notas != null && p_bit >= 0 && p_bit < av_notas.length &&
                av_notas[p_bit] != null && !av_notas[p_bit].isEmpty())

            for (int i = 0; i < av_notas[p_bit].size(); i++)
            {
                if (av_notas[p_bit].get(i).nota_get_frecuencia() == p_frecuencia)
                {
                    nota = av_notas[p_bit].get(i);
                    break;
                }
            }
        else
            Log.e("compas_borra_nota", "Fallo en borrado de nota");

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    public Nota_t compas_get_nota(int p_bit, String p_nombre, int p_octava)
    {
        Nota_t nota = null;

        if(av_notas != null && p_bit >= 0 && p_bit < av_notas.length &&
                av_notas[p_bit] != null && !av_notas[p_bit].isEmpty())

            for (int i = 0; i < av_notas[p_bit].size(); i++)
            {
                if (av_notas[p_bit].get(i).nota_compara_nombre_octava(p_nombre, p_octava))
                {
                    nota = av_notas[p_bit].get(i);
                    break;
                }
            }
        else
            Log.e("compas_borra_nota", "Fallo en borrado de nota");

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_borra_nota(int p_bit, int p_frecuencia)
    {
        if(av_notas != null && p_bit >= 0 && p_bit < av_notas.length &&
                av_notas[p_bit] != null && !av_notas[p_bit].isEmpty())

            for (int i = 0; i < av_notas[p_bit].size(); i++)
            {
                if (av_notas[p_bit].get(i).nota_get_frecuencia() == p_frecuencia)
                {
                    av_notas[p_bit].remove(p_frecuencia);
                    break;
                }
            }
        else
            Log.e("compas_borra_nota", "Fallo en borrado de nota");
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_inicializar_notas(int p_indice_compas, boolean p_es_ultimo_compas)
    {
        if (av_notas != null)
        {
            for (int i = 0; i < av_notas.length; i++)
            {
                if (av_notas[i] != null)
                {
                    for (int j = 0; j < av_notas[i].size(); j++)
                    {
                        double double_delay = a_partitura.partitura_get_duracion_bit() * 1000;

                        long delay = (long) Math.floor(double_delay * i +
                                (p_indice_compas * av_notas.length * double_delay)) + 500;

                        System.out.println("Delay: " + delay);

                        av_notas[i].get(j).nota_inicializar_hilo(delay);
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_inicializar_notas(
            int p_indice_compas,
            int p_bit_inicial,
            boolean p_es_primer_compas)
    {
        if (av_notas != null)
        {
            double double_delay = a_partitura.partitura_get_duracion_bit() * 1000;

            for (int i = 0; i < av_notas.length; i++)
            {
                if (av_notas[i] != null)
                {
                    long delay_bit_inicial = (long) Math.floor(double_delay * i +
                            (p_indice_compas * av_notas.length * double_delay)) + 500;

                    for (int j = 0; j < av_notas[i].size(); j++)
                    {
                        long delay = (long) Math.floor(double_delay * i +
                                (p_indice_compas * av_notas.length * double_delay)) + 500;

                        if (delay >= delay_bit_inicial
                                || av_notas[i].get(j).nota_ocupa_rejilla(p_bit_inicial))
                        {
                            System.out.println("Delay: " + delay);

                            av_notas[i].get(j).nota_inicializar_hilo(delay_bit_inicial, p_bit_inicial, p_es_primer_compas);
                        }
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_detener_reproduccion()
    {
        if (av_notas != null)
        {
            for (int i = 0; i < av_notas.length; i++)
            {
                for (int j = 0; av_notas[i] != null && j < av_notas[i].size(); j++)
                {
                    av_notas[i].get(j).nota_detener_reproduccion();
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_muestra_vista()
    {
        if (av_notas != null)
        {
            for (int i = 0; i<av_notas.length; i++)
            {
                if (av_notas[i] != null && !av_notas[i].isEmpty())
                {
                    for (int j = 0; j < av_notas[i].size(); j++)
                    {
                        System.out.println("************* Nota " + i + "/" + j + " ***********");
                        av_notas[i].get(j).nota_muestra_vista();
                    }
                }else
                {
                    System.out.println("-");
                }
            }
        }else
            Log.e("compas_muestra_vista", "av_notas es NULL");
    }
}


