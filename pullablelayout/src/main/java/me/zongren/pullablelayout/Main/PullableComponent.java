package me.zongren.pullablelayout.Main;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import me.zongren.pullablelayout.Constant.Config;
import me.zongren.pullablelayout.Constant.Result;
import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Constant.Status;
import me.zongren.pullablelayout.Inteface.OnSizeChangeListener;
import me.zongren.pullablelayout.Inteface.OnStatusChangeListener;
import me.zongren.pullablelayout.R;

/**
 * Created by 宗仁 on 16/6/13.
 * All Rights Reserved By 宗仁.
 */
public class PullableComponent {

    private static final int DELAYING_WHEN_FINISHED = 1000;
    private static final float POSITION_DEGREE_RATIO = 1.0f;
    private static final int MINIMUM_ANIMATION_DURATION = 200;

    private View mView;
    private ImageView mIconImageView;
    private TextView mStatusTextView;
    private RotateAnimation mRotateAnimation;
    private OnSizeChangeListener mOnSizeChangeListener;
    private OnStatusChangeListener mOnStatusChangeListener;
    private Status mStatus = Status.INADEQUATE;
    private Side mSide;
    private int mTouchDownSize = 0;
    private int mSize = 0;
    private float mAdequateSize;
    private boolean mTouching;
    private Result mResult;

    PullableComponent(Context context, ViewGroup parent, int layoutId, Side side) {
        mSide = side;
        mAdequateSize = context.getResources().getDimensionPixelSize(
                R.dimen.pullablelayout_default_adequate_size);

        mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context,
                R.anim.pullablelayout_rotate_animation);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mIconImageView = (ImageView) mView.findViewById(
                R.id.pullablelayout_component_iconImageView);
        mStatusTextView = (TextView) mView.findViewById(
                R.id.pullablelayout_component_statusTextView);
    }

    public void autoLoad() {
        setTouching(false);
        Animation animation = animateToPosition(mAdequateSize + 50);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                release();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void finish(Result result) {
        mTouching = false;
        mResult = result;
        setStatus(Status.FINISHED);
    }

    public Side getSide() {
        return mSide;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        int delta = size - mSize;
        float degree = delta * POSITION_DEGREE_RATIO;
        if (Config.DEBUG) {
            Log.i(this.getClass().toString(), "degree is " + degree);
        }
        final float currentDegree = mIconImageView.getRotation();
        mIconImageView.setRotation(currentDegree + degree);
        mSize = size;
        if (mOnSizeChangeListener != null) {
            mOnSizeChangeListener.onSizeChanged(this, mSize);
        }
        //If you are not touching the screen,it will stay at status load and finish
        if ((!mTouching && mStatus == Status.LOADING) || (!mTouching && mStatus == Status.FINISHED)) {
            mView.requestLayout();
        } else {
            if (isAdequate()) {
                setStatus(Status.ADEQUATE);
            } else {
                setStatus(Status.INADEQUATE);
            }
            mView.requestLayout();
        }
    }

    public View getView() {
        return mView;
    }

    public void setOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        mOnSizeChangeListener = onSizeChangeListener;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        mOnStatusChangeListener = onStatusChangeListener;
    }

    private Animation animateToPosition(final float position) {
        Interpolator easeInOutQuart = new DecelerateInterpolator();

        Animation animation = new Animation() {
            @Override
            public boolean willChangeBounds() {
                return true;
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int size = (int) (mSize - (mSize - position) * interpolatedTime);
                setSize(size);
            }
        };

        animation.setInterpolator(easeInOutQuart);

        int durationMillis = (int) (position / mView.getContext().getResources().getDisplayMetrics().density);
        if (durationMillis < MINIMUM_ANIMATION_DURATION) {
            durationMillis = MINIMUM_ANIMATION_DURATION;
        }
        animation.setDuration(durationMillis);

        mView.startAnimation(animation);
        return animation;
    }

    private void hide() {
        setTouching(true);
        animateToPosition(0);
    }

    private boolean isAdequate() {
        return mSize >= mAdequateSize;
    }

    /**
     * When status changed,this method will be called to change view's looking.
     *
     * @param fromStatus The status before changing.
     * @param toStatus   The status after changing.
     */
    private void onStatusChanged(Status fromStatus, Status toStatus) {
        if (fromStatus == Status.INADEQUATE && toStatus == Status.ADEQUATE) {
            mStatusTextView.setText(R.string.pullablelayout_release_to_load);
            return;
        }
        if (fromStatus == Status.ADEQUATE && toStatus == Status.INADEQUATE) {
            mStatusTextView.setText(R.string.pullablelayout_pull_to_load);
        }
        if (fromStatus == Status.ADEQUATE && toStatus == Status.LOADING) {
            if (mIconImageView.getAnimation() != null) {
                mIconImageView.clearAnimation();
            }
            mIconImageView.startAnimation(mRotateAnimation);
            mStatusTextView.setText(R.string.pullablelayout_loading);
            if (mOnStatusChangeListener != null) {
                mOnStatusChangeListener.onLoading(this);
            }
        }
        if (fromStatus == Status.LOADING && toStatus == Status.ADEQUATE) {
            if (mIconImageView.getAnimation() != null) {
                mIconImageView.clearAnimation();
            }
            mStatusTextView.setText(R.string.pullablelayout_release_to_load);
            if (mOnStatusChangeListener != null) {
                mOnStatusChangeListener.onCanceled(this);
            }
        }
        if (fromStatus == Status.LOADING && toStatus == Status.INADEQUATE) {
            if (mIconImageView.getAnimation() != null) {
                mIconImageView.clearAnimation();
            }
            mStatusTextView.setText(R.string.pullablelayout_pull_to_load);
            if (mOnStatusChangeListener != null) {
                mOnStatusChangeListener.onCanceled(this);
            }
        }
        if (fromStatus == Status.LOADING && toStatus == Status.FINISHED) {
            if (mIconImageView.getAnimation() != null) {
                mIconImageView.clearAnimation();
            }
            if (mResult == Result.SUCCEED) {
                mIconImageView.setImageResource(R.mipmap.pullablelayout_ic_succeed);
                mStatusTextView.setText(R.string.pullablelayout_load_succeed);
            } else {
                mIconImageView.setImageResource(R.mipmap.pullablelayout_ic_failed);
                mStatusTextView.setText(R.string.pullablelayout_load_failed);
            }
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mTouching) {
                        //To make sure that the status changes to ADEQUATE.
                        animateToPosition(0);
                    }
                }
            }, DELAYING_WHEN_FINISHED);
        }
        //This happens when the status is finishing and you touch the screen and move
        if (fromStatus == Status.FINISHED && toStatus == Status.INADEQUATE) {
            mIconImageView.setImageResource(R.mipmap.pullablelayout_icon_loading);
            mStatusTextView.setText(R.string.pullablelayout_pull_to_load);
        }
        //This happens when the status is finishing and you touch the screen and move
        if (fromStatus == Status.FINISHED && toStatus == Status.ADEQUATE) {
            mIconImageView.setImageResource(R.mipmap.pullablelayout_icon_loading);
            mStatusTextView.setText(R.string.pullablelayout_release_to_load);
        }
    }

    private void setStatus(Status status) {
        if (mStatus != status) {
            Status previousStatus = mStatus;
            mStatus = status;
            onStatusChanged(previousStatus, mStatus);
        }
    }

    void addSize(int offset) {
        setSize(mTouchDownSize + offset);
    }

    void clearTouchDownSize() {
        mTouchDownSize = 0;
    }

    void recordTouchDownSize() {
        mTouchDownSize = mSize;
    }

    void release() {
        //If the status is FINISHED,then change to INADEQUATE no matter what.
        if (mStatus == Status.FINISHED) {
            hide();
        } else {
            if (isAdequate()) {
                setStatus(Status.LOADING);
                animateToPosition(mAdequateSize);
            } else {
                hide();
            }
        }
    }

    void setTouching(boolean touching) {
        mTouching = touching;
        if (mTouching) {
            mView.clearAnimation();
        }
    }

}
