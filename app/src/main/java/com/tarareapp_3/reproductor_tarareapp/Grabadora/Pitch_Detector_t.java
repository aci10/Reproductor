package com.tarareapp_3.reproductor_tarareapp.Grabadora;

import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Pitch_Detector_t {

    private ArrayList<ArrayList<double[]>> av_data;
    private int a_current_bit;
    private double a_bit_duration;
    private boolean a_timer_init;

    private Timer a_timer;
    private Thread a_hilo;

    private boolean started;

    // ---------------------------------------------------------------------------------------------

    private void i_init_new_data_bit_list()
    {
        av_data.add(new ArrayList<double[]>());
        a_current_bit++;
    }

    // ---------------------------------------------------------------------------------------------

    public Pitch_Detector_t(double p_bit_duration)
    {
        a_current_bit = -1;

        av_data = new ArrayList<>();

        a_timer_init = false;

        a_timer = null;
        a_hilo = null;

        started = false;

        if (p_bit_duration <= 0)
            a_bit_duration = 250;
        else
            a_bit_duration = p_bit_duration * 1000;

        i_init_new_data_bit_list();
    }

    // ---------------------------------------------------------------------------------------------

    public boolean pd_get_timer_init()
    {
        return a_timer_init;
    }

    // ---------------------------------------------------------------------------------------------

    public void pd_init_time_thread()
    {
        final Thread hilo = new Thread(new Runnable()
        {
            Timer timer = new Timer();

            public void run()
            {
                timer.scheduleAtFixedRate(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        started = true;
                        i_init_new_data_bit_list();
                        Log.e("bit_actual", "" + a_current_bit);
                    }
                }, 7000, (long) a_bit_duration);

                a_timer = timer;
            }
        });

        a_hilo = hilo;
        a_timer_init = true;

        hilo.start();
    }

    // ---------------------------------------------------------------------------------------------

    public void pd_stop_time_thread()
    {
        a_timer_init = false;
        started = false;

        if (a_timer != null)
        {
            a_timer.cancel();
            a_timer = null;
        }

        if (a_hilo != null && !a_hilo.isInterrupted())
        {
            a_hilo.interrupt();
            a_hilo = null;
        }

        a_current_bit = 0;
    }

    // ---------------------------------------------------------------------------------------------

    public void pd_add_data_frecuency(double p_f, double p_dB)
    {
        if (started)
        {
            if (av_data != null && a_current_bit > -1
                    && av_data.get(a_current_bit) != null)
            {
                double [] aux = new double [2];
                aux[0] = p_f;
                aux[1] = p_dB;

                av_data.get(a_current_bit).add(aux);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------

    private Data_Note_t i_get_note(ArrayList<Data_Note_t> p_notes)
    {
        Data_Note_t note = null;

        if (p_notes != null)
        {
            ArrayList<Data_Note_t> type = new ArrayList<>();
            int [] quantity = new int [p_notes.size()];

            for (int i = 0; i < p_notes.size(); i++)
            {
                if (type.isEmpty())
                {
                    type.add(p_notes.get(i));
                    quantity[0] = 1;
                }
                else
                {
                    boolean found = false;
                    for (int j = 0; !found && j < type.size(); j++)
                    {
                        if (type.get(j).dn_compare(p_notes.get(i)))
                        {
                            found = true;
                            quantity[j]++;
                        }
                    }

                    if (!found)
                    {
                        type.add(p_notes.get(i));
                        quantity[type.size() - 1] = 1;
                    }
                }
            }

            int size = type.size();
            if (size == 1)
                note = type.get(0);
            else
            {
                int id_actual = 0;
                for (int i = 0; i < size; i++)
                {
                    if (i != id_actual && quantity[i] > quantity[id_actual])
                        id_actual = i;
                }

                note = type.get(id_actual);
            }
        }

        return note;
    }

    // ---------------------------------------------------------------------------------------------

    private ArrayList<Data_Note_t> i_init_notes_attributes(Data_Note_t p_nota)
    {
        ArrayList<Data_Note_t> notes_attributes = null;

        if (p_nota != null)
        {
            notes_attributes = new ArrayList<>();
            notes_attributes.add(p_nota);
            p_nota.dn_set_data(0, 0, 1);
        }

        return notes_attributes;
    }

    // ---------------------------------------------------------------------------------------------

    private ArrayList<Data_Note_t> i_suavizado_notas(ArrayList<Data_Note_t> p_notas)
    {
        ArrayList<Data_Note_t> notas = null;

        if (p_notas != null)
        {
            ArrayList<Data_Note_t> lista_notas_contiguas = new ArrayList<>();
            notas = new ArrayList<>();

            for (int i = 0; i < p_notas.size(); i++)
            {
                ArrayList<Data_Note_t> aux_lista;

                if (!p_notas.get(i).dn_get_nombre().equals("NONE"))
                {
                    aux_lista = p_notas.get(i).dn_compara_ids(lista_notas_contiguas);

                    if (aux_lista != null)
                    {
                        lista_notas_contiguas = aux_lista;
                    }
                    else if (lista_notas_contiguas != null && lista_notas_contiguas.size() > 0)
                    {
                        notas.add(lista_notas_contiguas.get(0).dn_calcula_nota_media(lista_notas_contiguas));

                        lista_notas_contiguas = new ArrayList<>();
                        lista_notas_contiguas.add(p_notas.get(i));
                    }
                }
                else
                {
                    if (lista_notas_contiguas != null && lista_notas_contiguas.size() > 0)
                    {
                        notas.add(lista_notas_contiguas.get(0).dn_calcula_nota_media(lista_notas_contiguas));

                        lista_notas_contiguas = new ArrayList<>();
                        lista_notas_contiguas.add(p_notas.get(i));

                        notas.add(p_notas.get(i));
                    }
                }
            }
        }

        return notas;
    }

    // ---------------------------------------------------------------------------------------------

    public ArrayList<Data_Note_t> pd_get_notes_attributes(int p_num_bits_compas)
    {
        ArrayList<Data_Note_t> notes_attributes = null;

        if (av_data != null)
        {
            int current_bit = 0;
            int current_compas = 0;
            Data_Note_t last_note = null;

            for (int i = 0; i < av_data.size(); i++)
            {
                Data_Note_t nota_obtenida = null;

                int size = av_data.get(i).size();

                int total_frecuencies = 0;
                boolean nota_en_principio = false;
                boolean nota_en_final = false;
                boolean silence_in_middle = false;

                ArrayList<Data_Note_t> notes_bit = new ArrayList<>();
                ArrayList<Data_Note_t> notes_bit2 = null;

                // *********************************************************************************
                // Separamos las frecuencias del bit en dos bloques en caso de que se produzca un
                // silencio en medio de este.
                for (int j = 0; j < size; j++)
                {
                    Data_Note_t nota = new Data_Note_t(av_data.get(i).get(j)[0]);

                    if (nota != null && nota.dn_get_nombre() != null && !nota.dn_get_nombre().equals("NONE"))
                    {
                        total_frecuencies++;

                        if (j == 0)
                            nota_en_principio = true;
                        else if (j == size - 1)
                            nota_en_final = true;

                        if (!silence_in_middle)
                            notes_bit.add(nota);
                        else
                        {
                            notes_bit2 = new ArrayList<>();
                            notes_bit2.add(nota);
                        }
                    }
                    else
                    {
                        if (j > 0 && j < size && !notes_bit.isEmpty())
                            silence_in_middle = true;
                    }
                }

                // *********************************************************************************
                // Si hay mas de una frecuencia detectada dentro del bit
                if (total_frecuencies > 0)
                {
                    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    // Habrian dos bloques de frecuencias en el bit
                    if (notes_bit2 != null)
                    {
                        // _________________________________________________________________________
                        // Si el primer bloque es mayor
                        if (notes_bit.size() >= notes_bit2.size())
                        {
                            nota_obtenida = i_get_note(notes_bit);

                            if (nota_obtenida != null)
                            {
                                if (notes_attributes == null)
                                    notes_attributes = i_init_notes_attributes(nota_obtenida);
                                else if (nota_en_principio && last_note != null && last_note.dn_compare(nota_obtenida))
                                    last_note.dn_increase_num_bits();
                                else
                                {
                                    notes_attributes.add(nota_obtenida);
                                    nota_obtenida.dn_set_data(current_bit, current_compas, 1);
                                }
                            }

                            last_note = null;
                        }
                        // _________________________________________________________________________
                        // Si el segundo bloque es mayor
                        else
                        {
                            nota_obtenida = i_get_note(notes_bit2);

                            if (nota_obtenida != null)
                            {
                                if (notes_attributes == null)
                                    notes_attributes = i_init_notes_attributes(nota_obtenida);
                                else
                                {
                                    notes_attributes.add(nota_obtenida);
                                    nota_obtenida.dn_set_data(current_bit, current_compas, 1);
                                }

                                if (!nota_en_final)
                                    last_note = null;
                                else
                                    last_note = nota_obtenida;
                            }
                            else
                                last_note = null;
                        }
                    }
                    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    // Habria un solo bloque de frecuencias en el bit
                    else
                    {
                        nota_obtenida = i_get_note(notes_bit);

                        if (nota_obtenida != null)
                        {
                            if (notes_attributes == null)
                                notes_attributes = i_init_notes_attributes(nota_obtenida);
                            else if (nota_en_principio && last_note != null && last_note.dn_compare(nota_obtenida))
                                last_note.dn_increase_num_bits();
                            else
                            {
                                notes_attributes.add(nota_obtenida);
                                nota_obtenida.dn_set_data(current_bit, current_compas, 1);
                            }

                            if (!nota_en_final)
                                last_note = null;
                            else
                                last_note = nota_obtenida;
                        }
                        else
                            last_note = null;
                    }
                }
                else if (current_bit > 0 || current_compas > 0)
                    last_note = null;

                // *********************************************************************************
                //
                if (notes_attributes != null)
                {
                    current_bit++;

                    if (current_bit >= p_num_bits_compas)
                    {
                        current_bit = 0;
                        current_compas++;
                    }
                }
            }
        }

        if (notes_attributes != null)
            notes_attributes = i_suavizado_notas(notes_attributes);

        return notes_attributes;
    }
}
