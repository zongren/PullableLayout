package me.zongren.pullablelayout.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Inteface.Pullable;

/**
 * Created by 宗仁 on 16/5/31.
 * All Rights Reserved By 宗仁.
 */
public class PullableImageView extends ImageView implements Pullable {

    public PullableImageView(Context context) {
        super(context);
    }

    public PullableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean reachEdgeOfSide(Side side) {
        return true;
    }
}
