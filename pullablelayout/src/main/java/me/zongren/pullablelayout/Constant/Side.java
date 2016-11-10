package me.zongren.pullablelayout.Constant;

/**
 * Created by 宗仁 on 16/6/13.
 * All Rights Reserved By 宗仁.
 */
public enum Side {
    TOP(1),
    LEFT(2),
    BOTTOM(4),
    RIGHT(8),;

    private int mValue;

    Side(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

}
