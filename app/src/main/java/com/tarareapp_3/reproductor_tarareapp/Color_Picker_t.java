package com.tarareapp_3.reproductor_tarareapp;

import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Color_Picker_t {

    private Paint a_pincel;
    private final static int ALPHA = 250;

    // ---------------------------------------------------------------------------------------------

    public enum type_color_t
    {
        SALMON,
        MORADO,
        MORADO_LIGHT,
        VIOLETA,
        VIOLETA_LIGHT,
        GRIS,
        BLANCO,
        BLANCO_VIOLETA,
        BLANCO_VERDE,
        VERDE,
        VERDE_LIGTH,
        ROSA,
        ROSA_LIGTH
    }

    // ---------------------------------------------------------------------------------------------

    public Color_Picker_t() { a_pincel = null; }

    // ---------------------------------------------------------------------------------------------

    public Paint getPincel(type_color_t p_color)
    {
        a_pincel = new Paint();

        switch (p_color)
        {
            case SALMON:
                a_pincel.setARGB(ALPHA, 229, 68, 123);
                break;
            case MORADO:
                a_pincel.setARGB(ALPHA, 73, 24, 105);
                break;
            case MORADO_LIGHT:
                a_pincel.setARGB(ALPHA, 90, 30, 133);
                break;
            case VIOLETA:
                a_pincel.setARGB(ALPHA, 148, 74, 255);
                break;
            case VIOLETA_LIGHT:
                a_pincel.setARGB(ALPHA, 178, 144, 200);
                break;
            case GRIS:
                a_pincel.setARGB(ALPHA, 89, 82, 133);
                break;
            case BLANCO:
                a_pincel.setARGB(ALPHA, 191, 211, 218);
                break;
            case VERDE_LIGTH:
                a_pincel.setARGB(ALPHA, 135, 225, 218);
                break;
            case BLANCO_VERDE:
                a_pincel.setARGB(ALPHA, 220, 254, 240);
                break;
            case VERDE:
                a_pincel.setARGB(ALPHA, 33, 193, 183);
                break;
            case ROSA:
                a_pincel.setARGB(ALPHA, 231, 101, 147);
                break;
            case ROSA_LIGTH:
                a_pincel.setARGB(ALPHA, 229, 135, 168);
                break;
            case BLANCO_VIOLETA:
            default:
                a_pincel.setARGB(ALPHA, 125, 97, 143);
                break;
        }

        return a_pincel;
    }
}
