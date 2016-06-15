package me.zongren.pullablelayout.Util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by 宗仁 on 16/6/13.
 * All Rights Reserved By 宗仁.
 */
public class Utils {
    /**
     * Convert dp to pixel.
     *
     * @param context The context which provides resources.
     * @param dp      The dp value.
     * @return Pixel value equivalent to db.
     */
    public static float getPixel(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
