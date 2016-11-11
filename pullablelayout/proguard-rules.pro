# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zongren/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#-keepclassmembers class me.zongren.pullablelayout.Constant.Result {
#   public *;
#}
#
#-keepclassmembers class me.zongren.pullablelayout.Constant.Side {
#   public *;
#}
#
#-keepclassmembers class me.zongren.pullablelayout.Interface.OnSizeChangeListener {
#   public *;
#}
#
#-keepclassmembers class me.zongren.pullablelayout.Interface.OnStatusChangeLisntener {
#   public *;
#}
#
#-keepclassmembers class me.zongren.pullablelayout.Interface.Pullable {
#   public *;
#}
#
#-keepclassmembers class me.zongren.pullablelayout.Main.PullabeLayout {
#   public *;
#}
#
#-keepclassmembers class me.zongren.pullablelayout.Main.PullableComponent {
#   public *;
#}