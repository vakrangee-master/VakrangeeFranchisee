package in.vakrangee.core.utils;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * Created by Nileshd on 5/31/2017.
 */

public class MultiDexApplication  extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());


    }
}
