package com.tarareapp_3.reproductor_tarareapp.CanvasTapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout;

import com.tarareapp_3.reproductor_tarareapp.R;

public class Motor_Canvas_t extends View{

    private double a_zoom;

    private Paint a_pincel;

    public enum colors
    {
        AZUL,
        AZUL_CLARO,
        AZUL_SATURADO,
        MORADO,
        VIOLETA,
        GRIS
    }

    Bitmap iconoBitmap;
    Drawable iconoDrawable;
    RelativeLayout layot;

    // ---------------------------------------------------------------------------------------------

    public Motor_Canvas_t(Context p_context)
    {
        super(p_context);
        a_zoom = 0;

        a_pincel = new Paint();

        iconoDrawable = p_context.getResources().getDrawable(R.drawable.nota);
        iconoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nota);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        a_pincel.setColor(Color.WHITE);
        a_pincel.setStrokeWidth(100);
        a_pincel.setTextSize(30);
        a_pincel.setTypeface(Typeface.MONOSPACE);
        //fondo de color del Canvas
        canvas.drawColor(Color.RED);
        //dibujamos un circulo//x,y,radio,paint
        canvas.drawCircle(200, 500, 55, a_pincel);
        //dibujamos rectas...
        canvas.drawRect(0,10,canvas.getWidth(), 20, a_pincel);
        //dibujamos texto...
        canvas.drawText("ancho Canvas: "+canvas.getWidth(),35,50,a_pincel);
        canvas.drawRect(5,0,15, canvas.getHeight(), a_pincel);
        canvas.drawText("alto Canvas: "+canvas.getHeight(),35,200,a_pincel);

///dibujo de bitmap y drawable
        canvas.drawBitmap(iconoBitmap,150,50, null);
        iconoDrawable.setBounds(150, 300, 150+iconoDrawable.getIntrinsicWidth(), 300+iconoDrawable.getIntrinsicHeight());
        iconoDrawable.draw(canvas);
    }
}
