## [English introduction](https://github.com/zongren/PullableLayout/blob/master/README.md)

## PullableLayout [![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/zongren/PullableLayout/blob/master/LICENSE.md) [![GitHub release](https://img.shields.io/github/release/zongren/PullableLayout.svg)](https://github.com/zongren/PullableLayout/releases) [![Build Status](https://travis-ci.org/zongren/PullableLayout.svg?branch=master)](https://travis-ci.org/zongren/PullableLayout)

`PullableLayout`帮助快速添加拉动并且刷新的特性，并且支持上下左右四个方向。

## 屏幕截图
![](https://github.com/zongren/PullableLayout/blob/master/screen.gif)

## 支持所有视图

## 如何使用
* 使用 JitPack [![](https://jitpack.io/v/zongren/PullableLayout.svg)](https://jitpack.io/#zongren/PullableLayout)
* Use jcenter [ ![](https://img.shields.io/bintray/v/zongren/maven/PullableLayout.svg)](https://bintray.com/zongren/maven/PullableLayout/_latestVersion)

### 添加依赖
```build.gradle
dependencies {
    ...
    compile 'me.zongren:pullablelayout:x.x.x'
}
```

### 创建视图文件
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

### 设置pullable component回调方法
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