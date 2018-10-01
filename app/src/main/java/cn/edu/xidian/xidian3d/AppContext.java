package cn.edu.xidian.xidian3d;

import android.app.Application;

/**
 * @author Yao Keqi
 * @date 2018/8/5
 */
public class AppContext extends Application {

    public static AppContext baseContext;

    @Override
    public void onCreate() {
        super.onCreate();
        baseContext = this;
    }
}
