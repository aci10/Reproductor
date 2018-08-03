package com.tarareapp_3.reproductor_tarareapp.Reproductor;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Compas_Canvas_t;
import com.tarareapp_3.reproductor_tarareapp.CanvasTapp.Diagrama_Pianola_t;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Partitura_t
{
    private String a_id_partitura;

    private int a_pulsos_compas;
    private double a_unidad_pulso_compas;
    private int a_bpm;
    private double a_duracion_bit;
    private int a_num_bits_en_compas;
    private double a_precision;
    private String a_escala;

    private Nota_t a_ultima_nota;

    private Crono_t a_cronometro;
    private MusicXML_Writer_t a_exportador_mxml;

    private ArrayList<Compas_t> av_compases;

    // ---------------------------------------------------------------------------------------------

    private void i_inicializa_variables_bits_compases(String p_nombre, int p_bpm, int p_pulsos_compas, double p_unidad_pulso_compas)
    {
        double valor_unidad_negra;
        double valor_pulso;
        double tamano_compas;

        a_id_partitura = p_nombre;

        a_bpm = p_bpm;
        a_pulsos_compas = p_pulsos_compas;
        a_unidad_pulso_compas = p_unidad_pulso_compas;
        a_escala = "G";

        valor_unidad_negra = a_bpm/60;

        if (a_unidad_pulso_compas > 0)
            valor_pulso = 4 / a_unidad_pulso_compas;
        else
            valor_pulso = 1;

        tamano_compas = valor_pulso * a_pulsos_compas;

        if (a_num_bits_en_compas <= 0) {
            a_duracion_bit = valor_unidad_negra * a_precision;
            a_num_bits_en_compas = (int) Math.floor(tamano_compas / a_duracion_bit);
        }
        else {
            a_precision = tamano_compas / a_num_bits_en_compas;
            a_duracion_bit = valor_unidad_negra * a_precision;
        }

        a_cronometro = new Crono_t(a_duracion_bit);

        a_exportador_mxml = null;

        a_ultima_nota = null;

        av_compases = new ArrayList<Compas_t>();

        System.out.println("Numero bits en compas: " + a_num_bits_en_compas);
        System.out.println("Unidad pulso compas: " + a_unidad_pulso_compas);
        System.out.println("Pulsos compas: " + a_pulsos_compas);
        System.out.println("Duracion bit: " + a_duracion_bit);
        System.out.println("Valor pulso: " + valor_pulso);
        System.out.println("Tamano compas: " + tamano_compas);
        System.out.println("Valor unidad negra: " + valor_unidad_negra);
    }

    // ---------------------------------------------------------------------------------------------

    public Partitura_t(
                        String p_nombre,
                        int p_bpm,
                        int p_pulsos_compas,
                        int p_unidad_pulso_compas,
                        String p_precision)
    {
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

        a_num_bits_en_compas = 0;

        i_inicializa_variables_bits_compases(p_nombre, p_bpm, p_pulsos_compas, p_unidad_pulso_compas);
    }

    // ---------------------------------------------------------------------------------------------

    public Partitura_t(
                        String p_nombre,
                        int p_bpm,
                        int p_pulsos_compas,
                        int p_unidad_pulso_compas,
                        int p_bits_compas)
    {
        if (p_bits_compas > 0)
            a_num_bits_en_compas = p_bits_compas;
        else
        {
            a_num_bits_en_compas = 0;
            a_precision = 0.25;
        }

        i_inicializa_variables_bits_compases(p_nombre, p_bpm, p_pulsos_compas, p_unidad_pulso_compas);
    }

    // ---------------------------------------------------------------------------------------------

    private void i_redimensiona_vec_compases(int p_indice_compas)
    {
        int diferencia;

        if(p_indice_compas >= av_compases.size())
        {
            diferencia = (p_indice_compas - av_compases.size()) + 1;

            for(int i = 0; i < diferencia; i++)
            {
                av_compases.add(new Compas_t(av_compases.size() + i,this));
            }

        }else if(p_indice_compas < 0)
            Log.e("partitura_append_nota", "p_indice_compas es menor que 0: " + p_indice_compas);

    }

    // ---------------------------------------------------------------------------------------------

    private void i_reproducir_partitura()
    {
        if (av_compases != null && !av_compases.isEmpty())
        {
            boolean es_primer_compas = true;

            int compas_inicial, bit_inicial;

            if (a_cronometro.crono_get_bit_actual() >= a_num_bits_en_compas)
            {
                compas_inicial = (int) Math.floor(a_cronometro.crono_get_bit_actual() / a_num_bits_en_compas);
                bit_inicial = a_cronometro.crono_get_bit_actual() % a_num_bits_en_compas;
            }
            else
            {
                compas_inicial = 0;
                bit_inicial = a_cronometro.crono_get_bit_actual();
            }

            // a_cronometro.crono_iniciar();

            System.out.println("compas_inicial " + compas_inicial);
            System.out.println("bit_inicial " + bit_inicial);

            if (compas_inicial < av_compases.size())
            {
                int indice_compas_reproduccion = 0;

                for (int i = compas_inicial; i < av_compases.size(); i++)
                {
                    if (i > compas_inicial)
                    {
                        es_primer_compas = false;
                        bit_inicial = 0;
                    }

                    if (av_compases.get(i) != null)
                        av_compases.get(i).compas_inicializar_notas(indice_compas_reproduccion, bit_inicial, es_primer_compas);
                    else
                        Log.e("partitura_rep_compases", "av_compas[i] es NULL");

                    indice_compas_reproduccion++;
                }
            }
            else
            {
                a_cronometro.crono_reiniciar();
                i_reproducir_partitura();
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public int partitura_get_num_bits_en_compas()
    {
        return a_num_bits_en_compas;
    }

    // ---------------------------------------------------------------------------------------------

    public double partitura_get_duracion_bit()
    {
        return a_duracion_bit;
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_finaliza_reproduccion()
    {
        a_cronometro.crono_reiniciar();
    }

    // ---------------------------------------------------------------------------------------------

    public Compas_Canvas_t partitura_add_compas_vacio(
                        Diagrama_Pianola_t p_dp,
                        int p_indicador,
                        float [] p_x0, float [] p_yf,
                        float p_width_celda_compas)
    {
        Compas_Canvas_t compas_canvas;

        Compas_t compas = new Compas_t(av_compases.size(), this);

        float tamanyo_rejilla = p_width_celda_compas / a_num_bits_en_compas;

        if (av_compases == null)
            av_compases = new ArrayList<Compas_t>();

        av_compases.add(compas);

        compas_canvas = compas.compas_crea_para_canvas(
                                    p_dp,
                                    p_indicador,
                                    p_x0, p_yf,
                                    p_width_celda_compas,
                                    a_num_bits_en_compas,
                                    tamanyo_rejilla);

        return compas_canvas;
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_append_nota_a_compas(
            int p_indice_compas,
            int p_bit_inicial,
            int p_num_bits,
            String p_nombre,
            int p_octava)
    {
        int compases_extra, bits_restantes, tamanyo_en_ultimo_compas, bits_nota, indice_nota;
        Nota_t nota_padre, nota_raiz;

        bits_restantes = p_num_bits - (a_num_bits_en_compas - p_bit_inicial);

        if (bits_restantes > 0)
        {
            tamanyo_en_ultimo_compas = bits_restantes % a_num_bits_en_compas;

            compases_extra = (int) Math.floor(bits_restantes / a_num_bits_en_compas);

            if (tamanyo_en_ultimo_compas > 0)
                compases_extra++;

            bits_nota = a_num_bits_en_compas - p_bit_inicial;
        }
        else
        {
            tamanyo_en_ultimo_compas = 0;
            compases_extra = 0;
            bits_nota = p_num_bits;
        }

        i_redimensiona_vec_compases(p_indice_compas + compases_extra);

        compases_extra++;

        nota_padre = null;
        nota_raiz = null;
        indice_nota = p_bit_inicial;

        for (int i = 0; i < compases_extra; i++)
        {
            Nota_t nota_aux;

            if (i > 0)
            {
                bits_nota = a_num_bits_en_compas;
                indice_nota = 0;
            }

            if (i >= compases_extra - 1 && tamanyo_en_ultimo_compas > 0)
                bits_nota = tamanyo_en_ultimo_compas;

            av_compases.get(i + p_indice_compas).compas_append_nota(
                    indice_nota,
                    bits_nota,
                    nota_padre,
                    p_nombre,
                    p_octava);

            nota_aux = av_compases.get(i + p_indice_compas).compas_get_nota(indice_nota, p_nombre, p_octava);

            if (i == 0)
                nota_raiz = av_compases.get(i + p_indice_compas).compas_get_nota(indice_nota, p_nombre, p_octava);

            if (i == compases_extra - 1
                    && (a_ultima_nota == null
                        || (nota_raiz != null
                            && nota_raiz.nota_get_num_bit_final() > a_ultima_nota.nota_get_num_bit_final()
                            )
                        )
                )
            {
                if (a_ultima_nota != null)
                    a_ultima_nota.nota_set_es_ultima(false);

                a_ultima_nota = nota_raiz;
                a_ultima_nota.nota_set_es_ultima(true);
            }

            if (nota_padre != null)
                nota_padre.nota_set_hijo(nota_aux);

            nota_padre = nota_aux;
        }

        if (nota_raiz != null)
            nota_raiz.nota_recalcula_duracion();
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_borra_nota_de_compas(int p_indice_compas, int p_bit_nota, int p_frecuencia)
    {
        if (p_indice_compas > 0)
            p_indice_compas -= 1;

        av_compases.get(p_indice_compas ).compas_borra_nota(p_bit_nota, p_frecuencia);
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_inicia_crono()
    {
        if (a_cronometro != null)
            a_cronometro.crono_iniciar();
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_reproducir_notas_compases()
    {
        i_reproducir_partitura();
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_reproducir_notas_compases(int p_compas_inicial, int p_bit_inicial)
    {
        int compas_inicial = 0;
        int bit_inicial = 0;
        int bit_crono_inicial;

        if (p_compas_inicial >= 0)
            compas_inicial = p_compas_inicial;

        if (p_bit_inicial >= 0)
            bit_inicial = p_bit_inicial;

        bit_crono_inicial = compas_inicial * a_num_bits_en_compas + bit_inicial;

        a_cronometro.crono_set_bit_inicial(bit_crono_inicial);

        i_reproducir_partitura();
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_detener_reproduccion(boolean p_pausar)
    {
        int compas_inicial = 0;

        if (a_cronometro.crono_get_bit_rep_inicial() > 0)
            compas_inicial = (int) Math.floor((a_duracion_bit * a_num_bits_en_compas) / a_cronometro.crono_get_bit_rep_inicial());

        /*Log.e("Inicio detencion compas", "" + compas_inicial);
        Log.e("Inicio detencion compas", "" + a_duracion_bit);
        Log.e("Inicio detencion compas", "" + a_num_bits_en_compas);
        Log.e("Inicio detencion compas", "" + a_cronometro.crono_get_bit_inicial());*/

        if (a_cronometro.crono_get_en_reproduccion())
        {
            if (p_pausar)
                a_cronometro.crono_parar();
            else
                a_cronometro.crono_reiniciar();

            for (int i = compas_inicial; i < av_compases.size(); i++)
            {
                av_compases.get(i).compas_detener_reproduccion();
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_exportar_mxml(Context p_ctx)
    {
        if (p_ctx != null)
        {
            a_exportador_mxml = new MusicXML_Writer_t(p_ctx);

            a_exportador_mxml.mxmlw_inicializar_escritor(a_id_partitura, "anonimo", "01-01-2018");

            a_exportador_mxml.mxmlw_escribe_inicio_partitura(
                        a_num_bits_en_compas,
                        0,
                        a_pulsos_compas, (int) a_unidad_pulso_compas,
                        a_escala, 4,
                        1);

            if (av_compases != null && !av_compases.isEmpty())
            {
                for (int i = 0; i < av_compases.size(); i++)
                {
                    a_exportador_mxml.mxmlw_escribe_inicio_compas(i + 1);

                    av_compases.get(i).compas_exportar_mxml(a_exportador_mxml);

                    a_exportador_mxml.mxmlw_escribe_cierre_compas();
                }
            }

            a_exportador_mxml.mxmlw_escribe_cierre_partitura();
        }
    }

    // ---------------------------------------------------------------------------------------------

    public void partitura_muestra_vista()
    {
        System.out.println("******************* Vista Partitura ******************");
        if (av_compases != null)
        {
            if (!av_compases.isEmpty())
            {
                for (int i = 0; i < av_compases.size(); i++)
                {
                    System.out.println("************* Compas " + i + " ***********");

                    if (av_compases.get(i) != null)
                    {
                        av_compases.get(i).compas_muestra_vista();
                    } else {
                        Log.e("partitura_muestra_vista", "Compas " + i + " es NULL");
                    }
                }
            }else
            {
                System.out.println("Partitura Vacia");
            }
        }else
        {
            Log.e("partitura_muestra_vista", "av_compases es NULL");
        }
    }

    // ---------------------------------------------------------------------------------------------

    public ArrayList<Compas_Canvas_t> partitura_crea_compases_canvas(Diagrama_Pianola_t p_dp, float [] p_x0, float [] p_yf, float p_width_celda_compas)
    {
        ArrayList<Compas_Canvas_t> compases = new ArrayList<>();

        if (av_compases != null)
        {
            int num_compases;
            float tamanyo_rejilla;

            if (av_compases.size() > 2)
                num_compases = av_compases.size();
            else
                num_compases = 3;

            tamanyo_rejilla = p_width_celda_compas / a_num_bits_en_compas;

            for (int i = 0; i < num_compases; i++)
            {
                if (i >= av_compases.size())
                    av_compases.add(new Compas_t(i, this));

                Compas_Canvas_t nuevo_compas = av_compases.get(i).compas_crea_para_canvas(
                                                                                p_dp,
                                                                                i,
                                                                                p_x0, p_yf,
                                                                                p_width_celda_compas,
                                                                                a_num_bits_en_compas,
                                                                                tamanyo_rejilla);
                compases.add(nuevo_compas);
            }
        }

        return compases;
    }
}
