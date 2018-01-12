package com.example.administrator.activitycollector.other;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class GetColor {

    public int[] getPixColor(Bitmap image, int x, int y) {
        int A, R, G, B;
        int pixelColor;
        pixelColor = image.getPixel(x, y);
        A = Color.alpha(pixelColor);
        R = Color.red(pixelColor);
        G = Color.green(pixelColor);
        B = Color.blue(pixelColor);
        int[] array = new int[4];
        array[0] = A;
        array[1] = R;
        array[2] = G;
        array[3] = B;
        Log.e("Main2Activity", "A=" + A + "");
        Log.e("Main2Activity:", "R" + R + "");
        Log.e("Main2Activity:", "G" + G + "");
        Log.e("Main2Activity:", "B" + B + "");
        return array;
    }
}



