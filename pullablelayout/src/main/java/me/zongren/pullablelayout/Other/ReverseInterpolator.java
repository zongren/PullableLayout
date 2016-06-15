package me.zongren.pullablelayout.Other;

import android.view.animation.Interpolator;

/**
 * Created by 宗仁 on 16/6/14.
 * All Rights Reserved By 宗仁.
 */
public class ReverseInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float paramFloat) {
        return Math.abs(paramFloat - 1f);
    }
}