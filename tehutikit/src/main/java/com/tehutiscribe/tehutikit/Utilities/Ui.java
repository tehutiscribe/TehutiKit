package com.tehutiscribe.tehutikit.Utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class Ui {

    // Convert any view to a bitmap
    public static Bitmap createBitmapFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        view.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);

        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(
                view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
