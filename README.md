# easy_util
userful tools

android studio 3.0 issues
If you update android stuido 3.0,you can run project to install apk ,no problem,but this apk in build/output,you will find you can't via cmd to use "adb intstall xxx" to install apk in your phone.how to resolve this problem.
first way:
debug apk    bulid->build APK(s)   
release apk  bulid-> generate signed apk(s)   choose v1   (if you choose v2 you will find install without certificate)

second way:
adb install -t xxx
        
        
