package in.vakrangee.core.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.vakrangee.core.BuildConfig;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.TypefaceUtil;
import io.fabric.sdk.android.Fabric;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

@SuppressLint("LongLogTag")
public class VakrangeeKendraApplication extends Application {

    private final static String TAG = "VakrangeeKendraApplication";

    private static Context mContext;
    private static boolean isStrictMode = false;
    public static Typeface font_awesome;
    public static Typeface sans_serif;
    private CookieJar cookieJar;        // Cookie Manager for OkHttp3

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Raleway-Light.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "default", "fonts/Raleway-Light.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SANS", "fonts/Raleway-Bold.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "BOLD", "fonts/Raleway-Bold.ttf");

        // Initializing Crashlytics using Fabric
        if (!BuildConfig.DEBUG) {
            Fabric.with(getApplicationContext(), new Crashlytics());
        }

        //disable FileUri Exposure - for get image path form picker
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Fonts
        font_awesome = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        sans_serif = Typeface.SANS_SERIF;

        // Maintain Session Across App via HttpUrlConnection
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024)
                .build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

    }

    /**
     * Get Application Context
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    public static boolean getStrictMode() {
        return isStrictMode;
    }

    //region CookieJar For OkHttp3
    public CookieJar getCookieJar() {
        if (cookieJar == null) {
            cookieJar = new CookieJar() {
                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url, cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            };
        }

        return cookieJar;
    }
    //endregion

    //Method to get current AppName
    public String getApplicationName() {
        String appName = null;
        try {
            ApplicationInfo applicationInfo = mContext.getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            appName = stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : mContext.getString(stringId);
        } catch (Exception e) {
            Log.e(TAG, "getApplicationName: Error: " + e.toString());
        }
        return appName;
    }

    //Method to get current AppVersion
    public String getAppVersion() {
        String version = null;
        try {
            String name = getContext().getApplicationContext().getPackageName();
            //PackageInfo pInfo = getPackageManager().getPackageInfo(name, 0);
            //version = pInfo.versionName;
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (Exception e) {
            Log.e(TAG, "getAppVersion : Application Packge is not found.: " + e.toString());
        }
        return version;
    }

    //Method to get current App Package Name
    public String getAppPackageName() {
        String name = null;
        try {
            name = getContext().getApplicationContext().getPackageName();
            //name = mContext.getString(R.string.app_package_name);

        } catch (Exception e) {
            Log.e(TAG, "getAppPackageName : Application Packge is not found.: " + e.toString());
        }
        return name;
    }

    //Method to get VKMSAgent
    public String getVkmsAgent() {
        String agent = null;
        try {
            String packageName = getAppPackageName() == null ? "UNKNOWN" : getAppPackageName();
            String version = getAppVersion() == null ? "UNKNOWN" : getAppVersion();

            agent = Constants.PRODUCT_CODE + "|" + packageName + "|" + version;

        } catch (Exception e) {
            Log.e(TAG, "getVkmsAgent: Error: " + e.toString());
        }
        return agent;
    }

}
