package me.zongren.example.pullablelayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import me.zongren.pullablelayout.Constant.Result;
import me.zongren.pullablelayout.Inteface.OnPullListener;
import me.zongren.pullablelayout.Main.PullableComponent;
import me.zongren.pullablelayout.Main.PullableLayout;

public class PullableTextViewActivity extends ActionBarActivity {
    private int mLoadCount = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullable_text_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView textView = (TextView) findViewById(R.id.activity_pullable_textView);
        final PullableLayout pullableLayout = (PullableLayout) findViewById(R.id.activity_pullableLayout);
        textView.setText(String.format(getResources().getString(R.string.load_count_format), mLoadCount));
        pullableLayout.topComponent.setOnPullListener(new OnPullListener() {
            @Override
            public void onCanceled(PullableComponent pullableComponent) {

            }

            @Override
            public void onLoading(PullableComponent pullableComponent) {

                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadCount++;
                        textView.setText(String.format(getResources().getString(R.string.load_count_format), mLoadCount));
                        pullableLayout.topComponent.finish(Result.SUCCEED);
                    }
                }, 1000);
            }

            @Override
            public void onSizeChanged(PullableComponent pullableComponent, int mSize) {

            }
        });
    }
}
