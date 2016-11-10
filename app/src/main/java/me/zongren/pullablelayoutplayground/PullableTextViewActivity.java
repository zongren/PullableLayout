package me.zongren.pullablelayoutplayground;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import me.zongren.pullablelayout.Constant.Result;
import me.zongren.pullablelayout.Inteface.OnSizeChangeListener;
import me.zongren.pullablelayout.Inteface.OnStatusChangeListener;
import me.zongren.pullablelayout.Main.PullableComponent;
import me.zongren.pullablelayout.Main.PullableLayout;

/**
 * Created by 宗仁 on 2016/11/10.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class PullableTextViewActivity extends AppCompatActivity {
    final int MESSAGE_SUCCEED = 1;

    PullableLayout mPullableLayout;
    PullableComponent mTopComponent;
    TextView mTextView;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == MESSAGE_SUCCEED) {
                mTextView.setText(R.string.succeed);
                mTopComponent.finish(Result.SUCCEED);
            }
        }
    };

    OnSizeChangeListener mOnSizeChangeListener = new OnSizeChangeListener() {
        @Override
        public void onSizeChanged(PullableComponent pullableComponent, int size) {
            Log.i(this.getClass().toString(),
                    "Size of " + pullableComponent.getSide().toString() + "component is " + size);
        }
    };

    OnStatusChangeListener mOnStatusChangeListener = new OnStatusChangeListener() {
        @Override
        public void onCanceled(PullableComponent component) {
            mTextView.setText(R.string.canceled);
            mHandler.removeMessages(MESSAGE_SUCCEED);
        }

        @Override
        public void onLoading(final PullableComponent component) {
            mTextView.setText(R.string.loading);
            mHandler.sendEmptyMessageDelayed(MESSAGE_SUCCEED, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullable_text_view);

        mTextView = (TextView) findViewById(R.id.activity_pullable_text_view_textView);
        mPullableLayout = (PullableLayout) findViewById(
                R.id.activity_pullable_text_view_pullableLayout);
        mTopComponent = mPullableLayout.topComponent;
        mTopComponent.setOnSizeChangeListener(mOnSizeChangeListener);
        mTopComponent.setOnStatusChangeListener(mOnStatusChangeListener);
        mTopComponent.autoLoad();
    }
}
