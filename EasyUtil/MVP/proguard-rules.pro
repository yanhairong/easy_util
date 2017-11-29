# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Tool/android/android-sdk-macosx/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassname

-dontnote **
-dontwarn **
-ignorewarnings
-target 1.7

-verbose
-repackageclasses com.broaddeep.safe.base.internal
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!method/propagation/parameter
-keepattributes *Annotation*,InnerClasses,Signature,SourceFile,EnclosingMethod

-keep class com.broaddeep.safe.base.** { *; }