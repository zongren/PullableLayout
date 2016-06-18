package me.zongren.pullablelayout.Main;

import android.os.Looper;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.zongren.pullablelayout.Constant.Result;
import me.zongren.pullablelayout.Constant.Side;
import me.zongren.pullablelayout.Constant.Status;
import me.zongren.pullablelayout.Inteface.OnPullListener;
import me.zongren.pullablelayout.Other.ReverseInterpolator;
import me.zongren.pullablelayout.R;
import me.zongren.pullablelayout.Util.Utils;

/**
 * Created by 宗仁 on 16/6/13.
 * All Rights Reserved By 宗仁.
 */
public class PullableComponent {
    private RotateAnimation mFlipAnimation;
    private RotateAnimation mRotateAnimation;
    private RotateAnimation mReverseFlipAnimation;
    private Status mStatus = Status.INADEQUATE;
    private OnPullListener mOnPullListener;
    private View mView;
    private ImageView mIconImageView;
    private ImageView mLoadingImageView;
    private ImageView mResultImageView;
    private TextView mStatusTextView;
    private TextView mPreviousUpdateTextView;
    private Date mPreviousUpdateDate;
    private Side mSide;
    private int mTouchDownSize = 0;
    private int mSize = 0;
    private float mAdequateSize;
    private boolean mTouching;
    private Result mResult;

    public PullableComponent(ViewGroup parent, int layoutId, Side side) {
        mView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mSide = side;
        mAdequateSize = Utils.getPixel(parent.getContext(), 100);
        mFlipAnimation = (RotateAnimation) AnimationUtils.loadAnimation(parent.getContext(), R.anim.pullablelayout_flip_animation);
        mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(parent.getContext(), R.anim.pullablelayout_rotate_animation);
        mReverseFlipAnimation = (RotateAnimation) AnimationUtils.loadAnimation(parent.getContext(), R.anim.pullablelayout_flip_animation);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setInterpolator(new ReverseInterpolator());
        //These view may be null
        mIconImageView = (ImageView) mView.findViewById(R.id.pullable_icon_imageView);
        mLoadingImageView = (ImageView) mView.findViewById(R.id.pullable_loading_imageView);
        mResultImageView = (ImageView) mView.findViewById(R.id.pullable_result_imageView);
        mStatusTextView = (TextView) mView.findViewById(R.id.pullable_status_textView);
        mPreviousUpdateTextView = (TextView) mView.findViewById(R.id.pullable_previous_update_textView);

        mPreviousUpdateDate = new Date();
        setPreviousUpdateDate();
    }

    public void addSize(int offset) {
        setSize(mTouchDownSize + offset);
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

    public void clearTouchDownSize() {
        mTouchDownSize = 0;
    }

    public void finish(Result result) {
        mTouching = false;
        mResult = result;
        setStatus(Status.FINISH);
    }

    public Side getSide() {
        return mSide;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
        if (mOnPullListener != null) {
            mOnPullListener.onSizeChanged(this, mSize);
        }
        //If you are not touching the screen,it will stay at status load and finish
        if ((!mTouching && mStatus == Status.LOAD) || (!mTouching && mStatus == Status.FINISH)) {
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

    public void recordTouchDownSize() {
        mTouchDownSize = mSize;
    }

    public void release() {
        //If current status is FINISH,then change to INADEQUATE no matter if adequate or not.
        if (mStatus == Status.FINISH) {
            mSize -= 1;
            hide();
        } else {
            if (isAdequate()) {
                setStatus(Status.LOAD);
                animateToPosition(mAdequateSize);
            } else {
                hide();
            }
        }
    }

    public void setOnPullListener(OnPullListener onPullListener) {
        mOnPullListener = onPullListener;
    }

    public void setPreviousUpdateDate() {
        setPreviousUpdateDate(Calendar.getInstance().getTime());
    }

    public void setPreviousUpdateDate(Date previousUpdateDate) {
        if (mPreviousUpdateTextView != null) {
            mPreviousUpdateDate = previousUpdateDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String text = String.format(mView.getContext().getResources().getString(R.string.pullablelayout_previous_update_time), dateFormat.format(mPreviousUpdateDate));
            mPreviousUpdateTextView.setText(text);
        }
    }

    public void setTouching(boolean touching) {
        mTouching = touching;
        if (mTouching) {
            mView.clearAnimation();
        }
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
        if (durationMillis < 200) {
            durationMillis = 200;
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
     * Called only when status changed.
     *
     * @param fromStatus The status before change
     * @param toStatus   The status after change
     */
    private void onStatusChanged(Status fromStatus, Status toStatus) {
        if (fromStatus == Status.INADEQUATE && toStatus == Status.ADEQUATE) {
            startIconAnimation(mFlipAnimation);
            hideLoadingImageView();
            hideResultImageView();
            setStatusText(R.string.pullablelayout_release_to_load);
        }
        if (fromStatus == Status.ADEQUATE && toStatus == Status.INADEQUATE) {
            startIconAnimation(mReverseFlipAnimation);
            hideLoadingImageView();
            hideResultImageView();
            setStatusText(R.string.pullablelayout_pull_to_load);
        }
        if (fromStatus == Status.ADEQUATE && toStatus == Status.LOAD) {
            hideIconImageView();
            hideResultImageView();
            startLoadingAnimation(mRotateAnimation);
            setStatusText(R.string.pullablelayout_loading);
            if (mOnPullListener != null) {
                mOnPullListener.onLoading(this);
            }
        }
        if (fromStatus == Status.LOAD && toStatus == Status.ADEQUATE) {
            startIconAnimation(mFlipAnimation);
            hideResultImageView();
            hideLoadingImageView();
            setStatusText(R.string.pullablelayout_release_to_load);
            if (mOnPullListener != null) {
                mOnPullListener.onCanceled(this);
            }
        }
        if (fromStatus == Status.LOAD && toStatus == Status.INADEQUATE) {
            showIconImageView();
            hideResultImageView();
            hideLoadingImageView();
            setStatusText(R.string.pullablelayout_pull_to_load);
            if (mOnPullListener != null) {
                mOnPullListener.onCanceled(this);
            }
        }
        if (fromStatus == Status.LOAD && toStatus == Status.FINISH) {
            hideIconImageView();
            hideLoadingImageView();
            if (mResult == Result.SUCCEED) {
                setPreviousUpdateDate();
                setResultImage(R.mipmap.pullablelayout_ic_succeed);
                setStatusText(R.string.pullablelayout_load_succeed);
            } else {
                setResultImage(R.mipmap.pullablelayout_ic_failed);
                setStatusText(R.string.pullablelayout_load_failed);
            }
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mTouching) {
                        //This line makes sure status change to ADEQUATE.
                        mSize -= 1;
                        animateToPosition(0);
                    }
                }
            }, 500);
        }
        //This situation happens when the status is finish and you touch the screen and move
        if (fromStatus == Status.FINISH && toStatus == Status.INADEQUATE) {
            showIconImageView();
            hideResultImageView();
            hideLoadingImageView();
            setStatusText(R.string.pullablelayout_pull_to_load);
        }
        //This situation happens when the status is finish and you touch the screen and move
        if (fromStatus == Status.FINISH && toStatus == Status.ADEQUATE) {
            startIconAnimation(mFlipAnimation);
            hideResultImageView();
            hideLoadingImageView();
            setStatusText(R.string.pullablelayout_release_to_load);
        }
    }

    private void setStatus(Status status) {
        if (mStatus != status) {
            Status previousStatus = mStatus;
            mStatus = status;
            onStatusChanged(previousStatus, mStatus);
        }
    }

    void showIconImageView() {
        if (mIconImageView != null && mIconImageView.getVisibility() != View.VISIBLE) {
            mIconImageView.setVisibility(View.VISIBLE);
        }
    }

    void hideIconImageView() {
        if (mIconImageView != null && mIconImageView.getVisibility() == View.VISIBLE) {
            mIconImageView.setVisibility(View.GONE);
            mIconImageView.clearAnimation();
        }
    }

    void startIconAnimation(Animation animation) {
        if (mIconImageView != null) {
            mIconImageView.startAnimation(animation);
            showIconImageView();
        }
    }

    void showLoadingImageView() {
        if (mLoadingImageView != null && mLoadingImageView.getVisibility() != View.VISIBLE) {
            mLoadingImageView.setVisibility(View.VISIBLE);
        }
    }

    void hideLoadingImageView() {
        if (mLoadingImageView != null && mLoadingImageView.getVisibility() == View.VISIBLE) {
            mLoadingImageView.setVisibility(View.GONE);
            mLoadingImageView.clearAnimation();
        }
    }

    void startLoadingAnimation(Animation animation) {
        if (mLoadingImageView != null) {
            mLoadingImageView.startAnimation(animation);
            showLoadingImageView();
        }
    }

    void showResultImageView() {
        if (mResultImageView != null && mResultImageView.getVisibility() != View.VISIBLE) {
            mResultImageView.setVisibility(View.VISIBLE);
        }
    }

    void hideResultImageView() {
        if (mResultImageView != null && mResultImageView.getVisibility() == View.VISIBLE) {
            mResultImageView.setVisibility(View.GONE);
        }
    }

    void setResultImage(int resourceId) {
        if (mResultImageView != null) {
            mResultImageView.setImageResource(resourceId);
            showResultImageView();
        }
    }

    void showStatusTextView() {
        if (mStatusTextView != null && mStatusTextView.getVisibility() != View.VISIBLE) {
            mStatusTextView.setVisibility(View.VISIBLE);
        }
    }

    void hideStatusTextView() {
        if (mStatusTextView != null && mStatusTextView.getVisibility() == View.VISIBLE) {
            mStatusTextView.setVisibility(View.GONE);
        }
    }

    void setStatusText(int stringId) {
        if (mStatusTextView != null) {
            mStatusTextView.setText(stringId);
            showStatusTextView();
        }
    }
}
