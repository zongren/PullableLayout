package me.zongren.example.pullablelayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.webkit.WebView;

import me.zongren.pullablelayout.Constant.Result;
import me.zongren.pullablelayout.Inteface.OnPullListener;
import me.zongren.pullablelayout.Main.PullableComponent;
import me.zongren.pullablelayout.Main.PullableLayout;

public class PullableWebViewActivity extends ActionBarActivity {
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
        setContentView(R.layout.activity_pullable_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final WebView webView = (WebView) findViewById(R.id.activity_pullable_webView);
        final PullableLayout pullableLayout = (PullableLayout) findViewById(R.id.activity_pullableLayout);
        webView.loadUrl("https://github.com/");
        pullableLayout.topComponent.setOnPullListener(new OnPullListener() {
            @Override
            public void onCanceled(PullableComponent pullableComponent) {

            }

            @Override
            public void onLoading(PullableComponent pullableComponent) {

                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoadCount % 2 > 0) {
                            webView.loadUrl("https://github.com/");
                        } else {
                            webView.loadUrl("http://zongren.me/");
                        }
                        mLoadCount++;
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
