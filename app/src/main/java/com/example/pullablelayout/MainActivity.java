package com.example.pullablelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.zongren.pullablelayout.PullabelListView;
import me.zongren.pullablelayout.PullableLayout;
import me.zongren.pullablelayout.PullableTextView;

public class MainActivity extends AppCompatActivity {
    private int mRefreshedTimes = 0;
    private int mLoadedTimes = 0;
    private PullableLayout mPullableLayout;
    private PullableTextView mPullableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPullableLayout = (PullableLayout)findViewById(R.id.activity_main_pullableLayout);
        mPullableTextView = (PullableTextView) findViewById(R.id.activity_main_pullableTextView);
        mPullableLayout.setOnPullDownListener(new PullableLayout.OnPullDownListener() {
            @Override
            public void onPullDown(float scrollY) {

            }

            @Override
            public void onPullDownCanceled() {

            }

            @Override
            public void onPullDownFinished() {

            }
        });
        mPullableLayout.setOnPullUpListener(new PullableLayout.OnPullUpListener() {
            @Override
            public void onPullUp() {

            }

            @Override
            public void onPullUpCanceled() {

            }

            @Override
            public void onPullUpFinished() {

            }
        });
        mPullableLayout.setOnRefreshListener(new PullableLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullableLayout pullableLayout) {
                new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            mPullableLayout.refreshFinish(PullableLayout.SUCCEED);
                            mRefreshedTimes ++;
                            updateTextView();
                        }
                    },
                1000);
            }

            @Override
            public void onLoadMore(PullableLayout pullableLayout) {
                new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            mPullableLayout.loadMoreFinish(PullableLayout.SUCCEED);
                            mLoadedTimes ++;
                            updateTextView();
                        }
                    },
                1000);
            }
        });
        updateTextView();
    }

    private void updateTextView() {
        mPullableTextView.setText(String.format(getResources().getString(R.string.refresh_and_load_times),mRefreshedTimes,mLoadedTimes));
    }


}
