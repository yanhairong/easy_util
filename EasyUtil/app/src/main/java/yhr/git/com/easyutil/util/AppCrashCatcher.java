package yhr.git.com.easyutil.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;

import yhr.git.com.easyutil.App;
import yhr.git.com.easyutil.home.MainActivity;

/**
 * Description: 应用程序崩溃捕捉
 *
 * @author Zhang Fangtao
 * @version 1.0
 * @since 16/4/27
 */
public class AppCrashCatcher implements UncaughtExceptionHandler {
    private static final String TAG = "AppCrashCatcher";

    private static final String CRASH_PATH = App.instance().getCrashDir();

    private UncaughtExceptionHandler mDefaultHandler;


    private static AppCrashCatcher INSTANCE = new AppCrashCatcher();

    public static AppCrashCatcher get() {
        return INSTANCE;
    }

    private void init(Context context) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static void setup(Context context) {
        get().init(context.getApplicationContext());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    private void handleException(Throwable ex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
//                Toast.makeText(App.instance().getContext(), "程序异常退出了！", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();

        long now = System.currentTimeMillis();
        String time = DateFormatUtil.format(DateFormatUtil.Format.yyyyMMddHHmmss, now);

        try {
            StringBuilder sb = collectCrash(time, ex);
            String detail = ex.getLocalizedMessage();
            saveToFile(time, sb);
            Thread.sleep(1500);
//            System.exit(0);
            Intent intent = new Intent(App.instance().getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.instance().getContext().startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private StringBuilder collectCrash(String time, Throwable ex) throws PackageManager.NameNotFoundException {
        StringBuilder sb = new StringBuilder();
        String format = "Time=%s\n" +
                "PkgName=%s\n" +
                "VersionCode=%d\n" +
                "Manufacturer=%s\n" +
                "Model=%s\n" +
                "VersionOS=%s\n";
        sb.append(String.format(Locale.getDefault(),
                format,
                time,
                App.instance().getContext().getPackageName(),
                App.instance().getContext().getPackageManager().getPackageInfo(App.instance().getContext().getPackageName(), 0).versionCode,
                Build.MANUFACTURER,
                Build.MODEL,
                Build.VERSION.RELEASE));
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        Throwable cause = ex.getCause() == null ? ex : ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append(writer.toString());
        return sb;
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveToFile(String fileName, StringBuilder sb) {
        try {
            fileName = fileName + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(CRASH_PATH);
                if (!dir.exists() && !dir.mkdirs()) {
                    return "";
                }
                String path = CRASH_PATH + File.separator + fileName;
                //noinspection ResultOfMethodCallIgnored
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
                return path;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//    private void autoClearCrashLogs(File dir) {
//        File[] files = dir.listFiles();
//        if (files == null) return;
//        long delta = TimeUnit.DAYS.toMillis(7);
//        for (File file : files) {
//            if (file.isFile() && Math.abs(System.currentTimeMillis() - file.lastModified()) > delta) {
//                file.deleteOnExit();
//            }
//        }
//    }
}
