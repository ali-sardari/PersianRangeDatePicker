package com.sardari.daterangepicker.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.SimpleArrayMap;

public class FontUtils {
    private static String FONT_NAME = "IranYekan";

    private static final SimpleArrayMap<String, Typeface> cache = new SimpleArrayMap<>();

    public static Typeface Default(Context context) {
        synchronized (cache) {
            if (!cache.containsKey(FONT_NAME)) {
                Typeface t = Typeface.createFromAsset(context.getAssets(), "fonts/" + FONT_NAME + ".ttf");
                cache.put(FONT_NAME, t);
                return t;
            }
            return cache.get(FONT_NAME);
        }
    }
}
