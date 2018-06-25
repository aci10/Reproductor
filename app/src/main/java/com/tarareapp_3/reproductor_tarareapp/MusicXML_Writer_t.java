package com.tarareapp_3.reproductor_tarareapp;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MusicXML_Writer_t
{
    private OutputStreamWriter a_escritor;
    private Context a_ctx;

    private String a_texto_xml;

    // ---------------------------------------------------------------------------------------------

    public MusicXML_Writer_t (Context p_ctx)
    {
        a_escritor = null;

        a_ctx = p_ctx;

        a_texto_xml = "";
    }

    // ---------------------------------------------------------------------------------------------

    private void i_escribe_cabecera(String p_nombre_fichero, String p_autor, String p_fecha)
    {
        a_texto_xml = "<?xml version=\"1.0\" enconding=\"UTF-8\" standalone=\"no\"?> \n" +
                "<!DOCTYPE score-partwise PUBLIC " +
                "\"-//Recordare//DTD MusicXML 3.0 Partwise//EN\"" +
                "\"http://www.musicxml.org/dtds/partwise.dtd\"> \n" +
                "<Movement-title>" + p_nombre_fichero + "</Movement-title> \n";

        if (p_autor != null && p_autor.length() > 0)
            a_texto_xml += "<Identification>" + p_autor + "</Identification> \n";

        a_texto_xml += "<encoding> \n";

        if (p_fecha != null && p_fecha.length() > 0)
            a_texto_xml+= "<encoding-date>" + p_fecha + "</encoding-date>\n";

        a_texto_xml += "<encoder>Antonio Candela</encoder> \n" +
                "<software>Tarareapp</software> \n" +
                "<encoding-description>Anroid application for music transcription.</encoding-description> \n" +
                "</encoding> \n";

        a_texto_xml += "<score-partwise version=\"3.0\"> \n" +
                        "<part-list> \n" +
                            "<score-part id=\"P1\"> \n" +
                                "<part-name>Primera parte</part-name> \n" +
                            "</score-part> \n" +
                        "</part-list> \n";
    }

    // ---------------------------------------------------------------------------------------------

    public String mxmlw_inicializar_escritor(String p_nombre_fichero, String p_autor, String p_fecha)
    {
        try
        {
            if (p_nombre_fichero != null && p_nombre_fichero.length() > 0)
            {
                for (String tmp : a_ctx.fileList()) {
                    System.out.println(tmp);
                }

                a_escritor = new OutputStreamWriter(a_ctx.openFileOutput(p_nombre_fichero + ".musicxml", Context.MODE_PRIVATE));

                i_escribe_cabecera(p_nombre_fichero, p_autor, p_fecha);
            }
        }catch (Exception e)
        {
            Log.e("Fallo durante la escritura", "Inicializacion escritor");
        }

        return a_texto_xml;
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_inicio_partitura(
                        int p_bits_compas,
                        int p_quintas,
                        int p_beats, int p_beat_type,
                        String p_clave_nombre, int p_clave_octava,
                        int p_pentagramas)
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
        {
            if (p_bits_compas > 0
                    && p_quintas >= 0
                    && p_beats > 0 && p_beat_type > 0
                    && p_clave_nombre != null && p_clave_nombre.length() > 0
                    && p_clave_octava > 0
                    && p_pentagramas > 0)
            {
                a_texto_xml += "<part id=\"P1\"> \n" +
                                    "<attributes> \n" +
                                        "<divisions> " + p_bits_compas + " </divisions> \n" +
                                        "<key> \n" +
                                            "<fifths> " + p_quintas + " </fifths> \n" +
                                        "</key> \n" +
                                        "<time> \n" +
                                            "<beats> " + p_beats + " </beats> \n" +
                                            "<beat-type> " + p_beat_type + " </beat-type> \n" +
                                        "</time> \n" +
                                        "<clef> \n" +
                                            "<sign> " + p_clave_nombre + " </sign> \n" +
                                            "<line> " + p_clave_octava + " </line> \n" +
                                        "</clef> \n" +
                                        "<staves> " + p_pentagramas + " </staves> \n" +
                                    "</attributes> \n";
            }
            else
                Log.e("Escribe inicio partitura", "atributo incorrecto.");
        }
        else
            Log.e("Escribe inicio partitura", "escritura no inicializada.");
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_cierre_partitura()
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
        {
            a_texto_xml += "</part> \n" +
                            "</score-partwise>";
            if (a_escritor != null)
            {
                try
                {
                    System.out.println(a_texto_xml);
                    a_escritor.write(a_texto_xml);
                    a_escritor.close();

                    for (String tmp : a_ctx.fileList()) {
                        System.out.println(tmp);
                    }
                }
                catch (Exception e)
                {
                    Log.e("Cierre partitura", "error escritura");
                }
            }
        }
        else
            Log.e("Escribe cierre partitura", "escritura no inicializada.");
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_inicio_compas(int p_numero_compas)
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
        {
            a_texto_xml += "<measure number=\" " + p_numero_compas + " \"> \n";
        }
        else
            Log.e("Escribe inicio compas", "escritura no inicializada.");
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_cierre_compas()
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
        {
            a_texto_xml += "</measure>\n";
        }
        else
            Log.e("Escribe cierre compas", "escritura no inicializada.");
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_nota(String p_nombre, int p_octava, double p_duracion, int p_estado_ligadura)
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
        {
            a_texto_xml += "<note> \n";

            if (p_estado_ligadura > 0)
                a_texto_xml += "<tie type = \"start\"/> \n";
            else if (p_estado_ligadura < 0)
                a_texto_xml += "<tie type = \"stop\"/> \n";

            a_texto_xml += "<pitch> \n" +
                                "<step> " + p_nombre + " </step> \n" +
                                "<octave> " + p_octava + " </octave> \n" +
                            "</pitch> \n" +
                            "<duration> " + p_duracion + " </duration> \n" +
                            "<type> whole </type> \n" +
                        "</note> \n";
        }
        else
            Log.e("Escribe escribe nota", "escritura no inicializada.");
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_silencio(double p_duracion)
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
        {
            a_texto_xml += "</note>\n" +
                                "<rest measure=\"yes\"/> \n" +
                                "<duration> " + p_duracion + " </duration> \n" +
                            "<note> \n";
        }
        else
            Log.e("Escribe escribe silencio", "escritura no inicializada.");
    }
}
