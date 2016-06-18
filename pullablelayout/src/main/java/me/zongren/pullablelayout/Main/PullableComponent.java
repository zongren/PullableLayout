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
import java.util.Locale;

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
    private Date mPreviousUpdateDate;
    private Side mSide;
    private int mTouchDownSize = 0;
    private int mSize = 0;
    private float mAdequateSize;
    private boolean mTouching;
    private Result mResult;
    private int mAdequateAnimationId = R.anim.pullablelayout_flip_animation;
    private int mLoadingAnimationId = R.anim.pullablelayout_rotate_animation;
    private int mPreviousUpdateFormatStringId = R.string.pullablelayout_previous_update_format;
    private int mPreviousUpdateDateFormatStringId = R.string.pullablelayout_previous_update_date_format;
    private int mPullToLoadStringId = R.string.pullablelayout_pull_to_load;
    private int mReleaseToLoadStringId = R.string.pullablelayout_release_to_load;
    private int mLoadingStringId = R.string.pullablelayout_loading;
    private int mLoadSucceedStringId = R.string.pullablelayout_load_succeed;
    private int mLoadFailedStringId = R.string.pullablelayout_load_failed;
    private String mPreviousUpdateFormatString;
    private String mPreviousUpdateDateFormatString;
    private String mPullToLoadString;
    private String mReleaseToLoadString;
    private String mLoadingString;
    private String mLoadSucceedString;
    private String mLoadFailedString;
    private int mIconImageViewId = R.id.pullablelayout_icon_imageView;
    private int mLoadingImageViewId = R.id.pullablelayout_loading_imageView;
    private int mResultImageViewId = R.id.pullablelayout_result_imageView;
    private int mStatusTextViewId = R.id.pullablelayout_status_textView;
    private int mPreviousUpdateTextViewId = R.id.pullablelayout_previous_update_textView;
    private ImageView mIconImageView;
    private ImageView mLoadingImageView;
    private ImageView mResultImageView;
    private TextView mStatusTextView;
    private TextView mPreviousUpdateTextView;
    private int mIconImageId = R.mipmap.pullablelayout_ic_arrow_down;
    private int mLoadingImageId = R.mipmap.pullablelayout_ic_loading;
    private int mSucceedImageId = R.mipmap.pullablelayout_ic_succeed;
    private int mFailedImageId = R.mipmap.pullablelayout_ic_failed;

    public PullableComponent(ViewGroup parent, int layoutId, Side side) {
        mView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mSide = side;
        mAdequateSize = Utils.getPixel(parent.getContext(), 100);

        mFlipAnimation = (RotateAnimation) AnimationUtils.loadAnimation(parent.getContext(), mAdequateAnimationId);
        mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(parent.getContext(), mLoadingAnimationId);
        mReverseFlipAnimation = (RotateAnimation) AnimationUtils.loadAnimation(parent.getContext(), mAdequateAnimationId);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setInterpolator(new ReverseInterpolator());

        mIconImageView = provideIconImageView();
        mLoadingImageView = provideLoadingImageView();
        mResultImageView = provideResultImageView();
        mStatusTextView = provideStatusTextView();
        mPreviousUpdateTextView = providePreviousUpdateTextView();

        mPreviousUpdateFormatString = parent.getContext().getResources().getString(mPreviousUpdateFormatStringId);
        mPreviousUpdateDateFormatString = parent.getContext().getResources().getString(mPreviousUpdateDateFormatStringId);
        mPullToLoadString = parent.getContext().getResources().getString(mPullToLoadStringId);
        mReleaseToLoadString = parent.getContext().getResources().getString(mReleaseToLoadStringId);
        mLoadingString = parent.getContext().getResources().getString(mLoadingStringId);
        mLoadSucceedString = parent.getContext().getResources().getString(mLoadSucceedStringId);
        mLoadFailedString = parent.getContext().getResources().getString(mLoadFailedStringId);

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

    public void setFailedImageId(int imageId) {
        mFailedImageId = imageId;
    }

    public void setIconImageId(int imageId) {
        mIconImageId = imageId;
        mIconImageView.setImageResource(imageId);
    }

    public void setLoadFailedString(String loadFailedString) {
        mLoadFailedString = loadFailedString;
    }

    public void setLoadSucceedString(String loadSucceedString) {
        mLoadSucceedString = loadSucceedString;
    }

    public void setLoadingImageId(int imageId) {
        mLoadingImageId = imageId;
        mLoadingImageView.setImageResource(imageId);

    }

    public void setLoadingString(String loadingString) {
        mLoadingString = loadingString;
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
            SimpleDateFormat dateFormat = new SimpleDateFormat(mPreviousUpdateDateFormatString, Locale.getDefault());
            String text = String.format(mPreviousUpdateFormatString, dateFormat.format(mPreviousUpdateDate));
            mPreviousUpdateTextView.setText(text);
        }
    }

    public void setPreviousUpdateDateFormatString(String previousUpdateDateFormatString) {
        mPreviousUpdateDateFormatString = previousUpdateDateFormatString;
    }

    public void setPreviousUpdateFormat(String previousUpdateFormat) {
        mPreviousUpdateFormatString = previousUpdateFormat;
    }

    public void setPullToLoadString(String pullToLoadString) {
        mPullToLoadString = pullToLoadString;
    }

    public void setReleaseToLoadString(String releaseToLoadString) {
        mReleaseToLoadString = releaseToLoadString;
    }

    public void setSucceedImageId(int imageId) {
        mSucceedImageId = imageId;
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
            setStatusText(mReleaseToLoadString);
        }
        if (fromStatus == Status.ADEQUATE && toStatus == Status.INADEQUATE) {
            startIconAnimation(mReverseFlipAnimation);
            hideLoadingImageView();
            hideResultImageView();
            setStatusText(mPullToLoadString);
        }
        if (fromStatus == Status.ADEQUATE && toStatus == Status.LOAD) {
            hideIconImageView();
            hideResultImageView();
            startLoadingAnimation(mRotateAnimation);
            setStatusText(mLoadingString);
            if (mOnPullListener != null) {
                mOnPullListener.onLoading(this);
            }
        }
        if (fromStatus == Status.LOAD && toStatus == Status.ADEQUATE) {
            startIconAnimation(mFlipAnimation);
            hideResultImageView();
            hideLoadingImageView();
            setStatusText(mReleaseToLoadString);
            if (mOnPullListener != null) {
                mOnPullListener.onCanceled(this);
            }
        }
        if (fromStatus == Status.LOAD && toStatus == Status.INADEQUATE) {
            showIconImageView();
            hideResultImageView();
            hideLoadingImageView();
            setStatusText(mPullToLoadString);
            if (mOnPullListener != null) {
                mOnPullListener.onCanceled(this);
            }
        }
        if (fromStatus == Status.LOAD && toStatus == Status.FINISH) {
            hideIconImageView();
            hideLoadingImageView();
            if (mResult == Result.SUCCEED) {
                setPreviousUpdateDate();
                setResultImage(mSucceedImageId);
                setStatusText(mLoadSucceedString);
            } else {
                setResultImage(mFailedImageId);
                setStatusText(mLoadFailedString);
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
            setStatusText(mPullToLoadString);
        }
        //This situation happens when the status is finish and you touch the screen and move
        if (fromStatus == Status.FINISH && toStatus == Status.ADEQUATE) {
            startIconAnimation(mFlipAnimation);
            hideResultImageView();
            hideLoadingImageView();
            setStatusText(mReleaseToLoadString);
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

    void setStatusText(String text) {
        if (mStatusTextView != null) {
            mStatusTextView.setText(text);
            showStatusTextView();
        }
    }

    ImageView provideIconImageView() {
        View view = mView.findViewById(mIconImageViewId);
        if (view instanceof ImageView) {
            return (ImageView) view;
        } else {
            return null;
        }
    }

    ImageView provideLoadingImageView() {
        View view = mView.findViewById(mLoadingImageViewId);
        if (view instanceof ImageView) {
            return (ImageView) view;
        } else {
            return null;
        }
    }

    ImageView provideResultImageView() {
        View view = mView.findViewById(mResultImageViewId);
        if (view instanceof ImageView) {
            return (ImageView) view;
        } else {
            return null;
        }
    }

    TextView provideStatusTextView() {
        View view = mView.findViewById(mStatusTextViewId);
        if (view instanceof TextView) {
            return (TextView) view;
        } else {
            return null;
        }
    }

    TextView providePreviousUpdateTextView() {
        View view = mView.findViewById(mPreviousUpdateTextViewId);
        if (view instanceof TextView) {
            return (TextView) view;
        } else {
            return null;
        }
    }
}
