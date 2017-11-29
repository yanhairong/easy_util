package yhr.git.com.easyutil;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import yhr.git.com.easyutil.util.AppCrashCatcher;

/**
 * Description:
 *
 * @author YanHaiRong
 * @version 2.0
 * @since 2017/11/20
 */
public class App {
    private static SoftReference<Context> sContextRef; //软引用内存不够时候会先回收
    private static final String TAG = "yan";

    public static void setup(Context context) {
        sContextRef = new SoftReference<Context>(context);
        INSTANCE = new App();
        AppCrashCatcher.setup(App.instance().getContext());  //添加崩溃日志收集
        Logger.init(TAG).logLevel(LogLevel.FULL); //正式版本中LogLevel.NONE 就不打印日志了

    }

    public Context getContext() {
        return sContextRef.get();
    }

    private static App INSTANCE;

    public static App instance() {
        return INSTANCE;
    }



    //======================线程池=====================================================

    private Executor MAIN_EXECUTOR = Executors.newFixedThreadPool(15);

    /**
     * 使用方法：
     *  App.instance().getMainExecutor().execute(new Runnable(){});
     * @return
     */
    public Executor getMainExecutor() {
        return MAIN_EXECUTOR;
    }

    //===========================================================================

    //=======================Handler====================================================

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 使用方法：
     * post 或者 postDelayed 延迟消息
     * App.instance().getMainHandler().postDelayed(new Runnable() {
        @Override
        public void run() {

        }
    }, 5000);
     * @return
     */
    public Handler getMainHandler() {
        return mHandler;
    }

    //===========================================================================

    //===========================================================================

    /**
     * sd卡中创建文件夹
     * @return
     */
    public String getPhotoDir() {
        return String.format("%s%s%s%s%s",
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                File.separator,
                "EasyUtil",
                File.separator,
                "photo"
        );
    }

    public String getCrashDir() {
        return String.format("%s%s%s%s%s",
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                File.separator,
                "EasyUtil",
                File.separator,
                "crash"
        );
    }
    //===========================================================================



}
