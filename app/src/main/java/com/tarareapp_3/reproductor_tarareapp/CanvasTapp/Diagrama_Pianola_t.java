package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.content.Context;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

public class Diagrama_Pianola_t{

    private Motor_Canvas_t a_motor;

    private Partitura_t partitura;

    private double a_altura_lienzo;
    private double a_ancho_lienzo;

    private double a_altura_vista;
    private double a_ancho_vista;

    private i_fila_nota [] av_filas;

    // ---------------------------------------------------------------------------------------------

    public Diagrama_Pianola_t(Context p_context)
    {

    }

    // ---------------------------------------------------------------------------------------------

    public void dp_dibuja()
    {

    }

    // ---------------------------------------------------------------------------------------------

    private class i_fila_nota
    {
        private double a_linea_superior;
        private double a_linea_inferior;
        private String a_nombre;

        public i_fila_nota(double p_linea_sup, double p_linea_inf, String p_nombre)
        {
            a_linea_superior = p_linea_sup;
            a_linea_inferior = p_linea_inf;
            a_nombre = p_nombre;
        }
    }
}
