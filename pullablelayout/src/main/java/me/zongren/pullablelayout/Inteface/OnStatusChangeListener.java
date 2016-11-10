package me.zongren.pullablelayout.Inteface;

import me.zongren.pullablelayout.Main.PullableComponent;

/**
 * Created by 宗仁 on 2016/11/10.
 * All Rights Reserved By 宗仁.
 */

public interface OnStatusChangeListener {
    void onCanceled(PullableComponent component);

    void onLoading(PullableComponent component);
}
