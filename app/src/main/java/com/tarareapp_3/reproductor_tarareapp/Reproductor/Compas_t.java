package com.tarareapp_3.reproductor_tarareapp.Reproductor;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Compas_Canvas_t;
import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Diagrama_Pianola_t;
import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Nota_Canvas_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Compas_t
{
    private static int a_ids = 0;
    private int a_id;
    private Partitura_t a_partitura;

    private ArrayList<Nota_t>[] av_notas;

    // ---------------------------------------------------------------------------------------------

    public Compas_t(Partitura_t p_partitura)
    {
        a_id = a_ids;
        a_ids++;

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

    public int compas_get_pos_bit_inicial()
    {
        return a_id * a_partitura.partitura_get_num_bits_en_compas();
    }

    // ---------------------------------------------------------------------------------------------

    public Partitura_t compas_get_partitura()
    {
        return a_partitura;
    }

    // ---------------------------------------------------------------------------------------------

    public int compas_get_id()
    {
        return a_id;
    }

    // ---------------------------------------------------------------------------------------------

    public boolean compas_comparar(Compas_t p_compas)
    {
        boolean son_iguales = false;

        if (p_compas != null && a_id == p_compas.compas_get_id())
            son_iguales = true;

        return son_iguales;
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_append_nota(
            int p_bit_inicial,
            int p_num_bits,
            Nota_t p_nota_padre,
            String p_nombre,
            int p_octava)
    {
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

    public Nota_t compas_get_nota(int p_bit, String p_nombre, int p_octava)
    {
        Nota_t nota = null;

        if(av_notas != null && p_bit >= 0 && p_bit < av_notas.length &&
                av_notas[p_bit] != null && !av_notas[p_bit].isEmpty())
        {
            for (int i = 0; i < av_notas[p_bit].size(); i++)
            {
                if (av_notas[p_bit].get(i).nota_compara_nombre_octava(p_nombre, p_octava))
                {
                    nota = av_notas[p_bit].get(i);
                    break;
                }
            }
        }
        else
            Log.e("compas_borra_nota", "Fallo en borrado de nota");

        return nota;
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_borra_nota(int p_bit, double p_frecuencia, boolean remove)
    {
        if(av_notas != null && p_bit >= 0 && p_bit < av_notas.length && av_notas[p_bit] != null)
        {
            for (int i = 0; i < av_notas[p_bit].size(); i++)
            {
                if (av_notas[p_bit].get(i).nota_get_frecuencia() == p_frecuencia)
                {
                    if (remove)
                        av_notas[p_bit].remove(i);
                    else
                        av_notas[p_bit].get(i).nota_borrar();

                    break;
                }
            }
        }
        else
            Log.e("compas_borra_nota", "Fallo en borrado de nota");
    }

    // ---------------------------------------------------------------------------------------------

    public void compas_inicializar_notas(
                        int p_indice_compas,
                        int p_bit_inicial,
                        boolean p_es_primer_compas)
    {
        if (av_notas != null)
        {
            boolean es_primer_bit = false;

            if (p_es_primer_compas)
                es_primer_bit = true;

            double double_delay = a_partitura.partitura_get_duracion_bit() * 1000;

            long delay_bit_inicial = (long) Math.floor(double_delay * p_bit_inicial +
                    (p_indice_compas * av_notas.length * double_delay));

            long delay_primer_bit_compas = (long) Math.floor(p_indice_compas * av_notas.length * double_delay);

            for (int i = 0; i < av_notas.length; i++)
            {
                if (av_notas[i] != null)
                {
                    long delay = (long) Math.floor(double_delay * i +
                            (p_indice_compas * av_notas.length * double_delay));

                    for (int j = 0; j < av_notas[i].size(); j++)
                    {
                        if (delay >= delay_bit_inicial)
                        {
                            System.out.println("Delay_1: " + delay);
                            System.out.println("Primer_bit: " + es_primer_bit);
                            av_notas[i].get(j).nota_inicializar_hilo(delay, p_bit_inicial, p_es_primer_compas, es_primer_bit);
                            es_primer_bit = false;
                        }
                        else if (av_notas[i].get(j).nota_ocupa_rejilla(p_bit_inicial))
                        {
                            System.out.println("Delay_2: " + delay_bit_inicial);
                            System.out.println("Primer_bit: " + es_primer_bit);
                            av_notas[i].get(j).nota_inicializar_hilo(delay_primer_bit_compas, p_bit_inicial, p_es_primer_compas, es_primer_bit);
                            es_primer_bit = false;
                        }
                        else
                        {
                            Log.e("nota compas no reproducida", "" + es_primer_bit);
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

    // En principio no se tendrán en cuenta los acordes.
    public void compas_exportar_mxml(MusicXML_Writer_t p_exportador_mxml)
    {
        if (p_exportador_mxml != null && av_notas != null)
        {
            for (int i = 0; i < av_notas.length; i++)
            {
                for (int j = 0; j < av_notas[i].size(); j++)
                {
                    av_notas[i].get(j).nota_exportar_mxml(p_exportador_mxml);
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

    // ---------------------------------------------------------------------------------------------

    public Compas_Canvas_t compas_crea_para_canvas(
                        Diagrama_Pianola_t p_dp,
                        int p_indicador,
                        float [] p_coordenadas,
                        float p_width_celda_compas,
                        int p_num_rejillas, float p_tamanyo_rejilla)
    {
        Compas_Canvas_t compas_c;
        float left, right;
        float[] x0;

        if (p_indicador <= 0) {
            left = p_coordenadas[0];
            right = p_coordenadas[2];
        }
        else
        {
            left = p_coordenadas[0] + p_width_celda_compas * p_indicador;
            right = left + p_width_celda_compas;
        }

        x0 = new float[2];
        x0[0] = left;
        x0[1] = right;

        if (p_indicador <= 0)
        {
            float [] yf;

            yf = new float[2];
            yf[0] = p_coordenadas[1];
            yf[1] = p_coordenadas[3];

            compas_c = new Compas_Canvas_t(this, x0, yf, p_num_rejillas, p_indicador);
        }
        else
            compas_c = new Compas_Canvas_t(this, x0, null, p_num_rejillas, p_indicador);

        for (int i = 0; i < av_notas.length; i++)
        {
            for (int j = 0; j < av_notas[i].size(); j++)
            {
                if (av_notas[i].get(j) != null)
                    compas_c.cmp_canvas_set_nota(av_notas[i].get(j), i, p_dp.dp_get_top_bottom_nota(av_notas[i].get(j)), p_tamanyo_rejilla);
            }
        }

        return compas_c;
    }
}


