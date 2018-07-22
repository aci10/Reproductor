package com.tarareapp_3.reproductor_tarareapp.Reproductor;

import android.content.Context;
import android.util.Log;

import com.tarareapp_3.reproductor_tarareapp.Reproductor.Partitura_t;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Lector_MXML_t
{
    private BufferedReader a_lector;
    private Context a_ctx;

    // ---------------------------------------------------------------------------------------------

    public Lector_MXML_t(Context p_ctx)
    {
        a_lector = null;

        if (p_ctx != null)
            a_ctx = p_ctx;
    }

    // ---------------------------------------------------------------------------------------------

    private Partitura_t i_lectura_fichero(Partitura_t p_partitura)
    {
        Partitura_t partitura = null;

        if (a_lector != null && p_partitura != null)
            partitura = p_partitura;

        return partitura;
    }

    // ---------------------------------------------------------------------------------------------

    public Partitura_t lector_mxml_abrir_fichero(String p_nombre_fichero)
    {
        Partitura_t partitura = null;

        if (p_nombre_fichero != null && p_nombre_fichero.length() > 0)
        {
            boolean existe_fichero = false;

            for (String tmp : a_ctx.fileList()) {
                System.out.println(tmp);
                if (tmp.equals(p_nombre_fichero + ".musicxml"))
                {
                    existe_fichero = true;
                    break;
                }
            }

            if (existe_fichero)
            {
                try
                {
                    a_lector = new BufferedReader(new InputStreamReader(a_ctx.openFileInput(p_nombre_fichero + ".musicxml")));

                    String linea = a_lector.readLine();

                    a_lector.close();
                }
                catch (Exception ex)
                {
                    Log.e("Ficheros", "Error al leer fichero");
                }
            }
            else
                Log.e("Lector MXML", "fichero " + p_nombre_fichero + ".musicxml no encontrado");
        }

        return partitura;
    }
}
