package me.zongren.pullablelayout.Inteface;

import me.zongren.pullablelayout.Main.PullableComponent;

/**
 * Created by zongren on 16/6/12.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public interface OnPullListener {
    void onCanceled(PullableComponent pullableComponent);

    void onLoading(PullableComponent pullableComponent);

    void onSizeChanged(PullableComponent pullableComponent, int mSize);
}
