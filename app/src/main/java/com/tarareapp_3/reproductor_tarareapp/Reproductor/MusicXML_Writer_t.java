package com.tarareapp_3.reproductor_tarareapp.Reproductor;

import android.content.Context;
import android.util.Log;

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
            a_texto_xml+= "\t<encoding-date>" + p_fecha + "</encoding-date>\n";

        a_texto_xml += "\t<encoder>Antonio Candela</encoder> \n" +
                "\t<software>Tarareapp</software> \n" +
                "\t<encoding-description>Anroid application for music transcription.</encoding-description> \n" +
                "</encoding> \n";

        a_texto_xml += "<score-partwise version=\"3.0\"> \n" +
                        "\t<part-list> \n" +
                            "\t\t<score-part id=\"P1\"> \n" +
                                "\t\t\t<part-name>Primera parte</part-name> \n" +
                            "\t\t</score-part> \n" +
                        "\t</part-list> \n";
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
                a_texto_xml += "\t<part id=\"P1\"> \n" +
                                    "\t\t<attributes> \n" +
                                        "\t\t\t<divisions> " + p_bits_compas + " </divisions> \n" +
                                        "\t\t\t<key> \n" +
                                            "\t\t\t\t<fifths> " + p_quintas + " </fifths> \n" +
                                        "\t\t\t</key> \n" +
                                        "\t\t\t<time> \n" +
                                            "\t\t\t\t<beats> " + p_beats + " </beats> \n" +
                                            "\t\t\t\t<beat-type> " + p_beat_type + " </beat-type> \n" +
                                        "\t\t\t</time> \n" +
                                        "\t\t\t<clef> \n" +
                                            "\t\t\t\t<sign> " + p_clave_nombre + " </sign> \n" +
                                            "\t\t\t\t<line> " + p_clave_octava + " </line> \n" +
                                        "\t\t\t</clef> \n" +
                                        "\t\t\t<staves> " + p_pentagramas + " </staves> \n" +
                                    "\t\t</attributes> \n";
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
            a_texto_xml += "\t</part> \n" +
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
            a_texto_xml += "\t\t<measure number=\"" + p_numero_compas + "\"> \n";
        else
            Log.e("Escribe inicio compas", "escritura no inicializada.");
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_cierre_compas()
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
            a_texto_xml += "\t\t</measure>\n";
        else
            Log.e("Escribe cierre compas", "escritura no inicializada.");
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_nota(String p_nombre, int p_octava, double p_duracion, int p_estado_ligadura)
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
        {
            a_texto_xml += "\t\t\t<note> \n";

            if (p_estado_ligadura > 0)
                a_texto_xml += "\t\t\t\t<tie type = \"start\"/> \n";
            else if (p_estado_ligadura < 0)
                a_texto_xml += "\t\t\t\t<tie type = \"stop\"/> \n";

            a_texto_xml += "\t\t\t\t<pitch> \n" +
                                "\t\t\t\t\t<step> " + p_nombre + " </step> \n" +
                                "\t\t\t\t\t<octave> " + p_octava + " </octave> \n" +
                            "\t\t\t\t</pitch> \n" +
                            "\t\t\t\t<duration> " + p_duracion + " </duration> \n" +
                            "\t\t\t\t<type> whole </type> \n" +
                        "\t\t\t</note> \n";
        }
        else
            Log.e("Escribe escribe nota", "escritura no inicializada.");
    }

    // ---------------------------------------------------------------------------------------------

    public void mxmlw_escribe_silencio(double p_duracion)
    {
        if (a_texto_xml != null && a_texto_xml.length() > 0)
        {
            a_texto_xml += "\t\t\t</note>\n" +
                                "\t\t\t\t<rest measure=\"yes\"/> \n" +
                                "\t\t\t\t<duration> " + p_duracion + " </duration> \n" +
                            "\t\t\t<note> \n";
        }
        else
            Log.e("Escribe escribe silencio", "escritura no inicializada.");
    }
}
