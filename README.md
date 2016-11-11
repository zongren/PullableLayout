## [中文介绍](https://github.com/zongren/PullableLayout/blob/master/README_zh.md)

## PullableLayout

PullableLayout makes it easy to add pull-to-load feature to your app on top,left,bottom and right side.

## Screen capture
![](https://github.com/zongren/PullableLayout/blob/master/screenshot.gif)

## Support all views

## How to use
* Use JitPack [![JitPack](https://jitpack.io/v/me.zongren/pullablelayout.svg)](https://jitpack.io/#me.zongren/pullablelayout)
* Use JCenter [![JCenter](https://api.bintray.com/packages/zongren/maven/PullableLayout/images/download.svg)](https://bintray.com/zongren/maven/PullableLayout/_latestVersion)

### Add dependency
```build.gradle
dependencies {
    ...
    compile 'me.zongren:pullablelayout:x.x.x'
}
```

### Create layout
```
<?xml version="1.0" encoding="utf-8"?>
<me.zongren.pullablelayout.Main.PullableLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/activity_pullable_text_view_pullableLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:side="TOP"
    >

    <me.zongren.pullablelayout.View.PullableTextView
        android:id="@+id/activity_pullable_text_view_textView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:textColor="#666"
        android:textSize="18sp"
        />

</me.zongren.pullablelayout.Main.PullableLayout>

```

### Set up pullable component
```
public class PullableTextViewActivity extends AppCompatActivity {
    ...
    
    OnSizeChangeListener mOnSizeChangeListener = new OnSizeChangeListener() {
        @Override
        public void onSizeChanged(PullableComponent pullableComponent, int size) {
            // do stuff
        }
    };
    
    OnStatusChangeListener mOnStatusChangeListener = new OnStatusChangeListener() {
        @Override
        public void onCanceled(PullableComponent component) {
            // do something like canceling the request.
        }
    
        @Override
        public void onLoading(final PullableComponent component) {
            // do something link make a request.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullable_text_view);

        PullableLayout pullableLayout = (PullableLayout) findViewById(
                R.id.activity_pullable_text_view_pullableLayout);
        
        pullableLayout.topComponent.setOnSizeChangeListener(mOnSizeChangeListener);
        pullableLayout.topComponent.setOnStatusChangeListener(mOnStatusChangeListener);
        pullableLayout.topComponent.autoLoad();
    }
```