package com.tarareapp_3.reproductor_tarareapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Image_Picker_t extends View
{
    private Bitmap a_bitmap;
    private float [] a_pos;
    private Paint a_pincel;

    public enum icon_t
    {
        SAVE,
        EDIT,
        PLAY,
        STOP,
        PAUSE,
        UNDO,
        REDO,
        DELETE,
        AJUSTES,
        NEXT
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (a_bitmap != null && a_pos != null && a_pincel != null)
            canvas.drawBitmap(a_bitmap, a_pos[0], a_pos[1], a_pincel);
    }

    // ---------------------------------------------------------------------------------------------

    public Image_Picker_t (Context context) {
        super(context);

        a_bitmap = null;
        a_pos = null;
        a_pincel = null;
    }

    // ---------------------------------------------------------------------------------------------

    public void setPos(float [] p_pos)
    {
        a_pos = p_pos;
    }

    // ---------------------------------------------------------------------------------------------

    public void initialize(icon_t p_icon, Color_Picker_t.type_color_t p_color)
    {
        Color_Picker_t picker = new Color_Picker_t();

        a_pincel = picker.getPincel(p_color);

        switch (p_icon)
        {
            case SAVE:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.save);
                break;
            case EDIT:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.edit);
                break;
            case NEXT:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.next);
                break;

            case PLAY:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play);
                break;

            case STOP:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stop);
                break;

            case PAUSE:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
                break;

            case REDO:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.redo);
                break;

            case UNDO:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.undo);
                break;

            case DELETE:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                break;

            case AJUSTES:
            default:

                a_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ajustes);
                break;
        }
    }
}
