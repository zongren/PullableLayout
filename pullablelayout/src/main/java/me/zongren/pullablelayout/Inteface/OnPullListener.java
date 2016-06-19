package me.zongren.pullablelayout.Inteface;

import me.zongren.pullablelayout.Main.PullableComponent;

/**
 * Created by 宗仁 on 16/6/12.
 * All Rights Reserved By 宗仁.
 */
public interface OnPullListener {
    void onCanceled(PullableComponent pullableComponent);

    void onLoading(PullableComponent pullableComponent);

    void onSizeChanged(PullableComponent pullableComponent, int mSize);
}
