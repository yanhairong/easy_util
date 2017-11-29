package yhr.git.com.easyutil;

import org.litepal.LitePalApplication;

/**
 * Description:
 *
 * @author YanHaiRong
 * @version 2.0
 * @since 2017/11/20
 */
public class MainApp extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        App.setup(this);
    }
}
