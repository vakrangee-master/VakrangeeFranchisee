package in.vakrangee.core.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.vakrangee.core.R;
import in.vakrangee.core.application.VakrangeeKendraApplication;
import in.vakrangee.core.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.core.oauth.OAuthUtils;
import in.vakrangee.core.utils.CommonUtils;
import in.vakrangee.core.utils.Logger;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;

/**
 * Created by Vasundhara on 8/1/2017.
 */

public class OkHttpService extends OkHttpClient {

    //region Variables
    private static final String TAG = "OkHttpService";
    private static final MediaType MEDIA_TYPE_PLAINTEXT = MediaType.parse("text/plain; charset=utf-8");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private Context context;
    private String SERVER_URL;
    private Headers REQ_HEADERS = null;
    private Logger logger;
    private VakrangeeKendraApplication vkApp;
    private String responseData = null;

    private final int HTTP_OK = 200;                            // HTTP Ok Status
    private final int HTTP_UNAUTHORIZED = 401;                  // HTTP Unauthorized access
    private final int HTTP_MAX_TOKEN_RETRY = 3;                 // HTTP Token Max Retry Option
    private final int CACHE_FILE_SIZE = 20 * 1024 * 1024;       // 20MB
    private final String FILE_NETWORK_LOG = "NetLog";
    //MAX_AGE and MAX_STALE for services
    public static final int MAX_AGE_60s = 60;                           //In Seconds
    public static final int MAX_STALE_1day = 60 * 60 * 24;              //In Seconds
    public static final int MAX_STALE_7day = 60 * 60 * 24 * 7;
    public static final int MAX_STALE_15day = 60 * 60 * 24 * 15;              //In Seconds
    private static final int TIME_OUT = 45;
    private OAuthUtils oAuthUtils;
    //endregion

    //region OkHttpServer Constructor & init
    private OkHttpService() { // No need to initialize.
    }

    public OkHttpService(Context c, String url, Map<String, String> headers) {
        this.SERVER_URL = url;
        this.context = c;
        init(); // Initialize require objects.
    }

    public OkHttpService(Context c, String url) {
        this.SERVER_URL = url;
        this.context = c;
        init(); // Initialize require objects.

    }

    public OkHttpService(Context c) {
        this.context = c;
        init(); // Initialize require objects.
    }

    private void init() {
        logger = Logger.getInstance(context);
        vkApp = (VakrangeeKendraApplication) context.getApplicationContext();
        oAuthUtils = OAuthUtils.getInstance(context);
        REQ_HEADERS = Headers.of(getAllImpHeaders());

        // HttpLoggerInterceptor Level - Body
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    }
    //endregion OkHttpServer Constructor & init

    //region Headers Detail
    public Map<String, String> getAllHeaders(Map<String, String> headers) {
        Map<String, String> allHeaders = new HashMap<String, String>();
        if (headers != null)
            allHeaders.putAll(headers);
        return allHeaders;
    }

    /**
     * Get all headers that are compulsory to add
     *
     * @return
     */
    public Map<String, String> getAllImpHeaders() {

        //1. VKMS Agent
        String vkmsAgent = vkApp.getVkmsAgent();

        //Add all headers
        Map<String, String> allHeaders = new HashMap<String, String>();
        if (vkmsAgent != null)
            allHeaders.put("VKMSAgent", vkmsAgent);

        allHeaders.put("ContentType", "application/json");

        if (oAuthUtils.getToken() != null)
            allHeaders.put("ContentType", oAuthUtils.getToken());

        return allHeaders;
    }

    public Map<String, String> getAllImpHeadersWithoutCacheControl() {
        Map<String, String> allHeaders = getAllImpHeaders();
        allHeaders.put("Cache-Control", "no-cache");

        return allHeaders;
    }

    //endregion END Headers Detail

    //region Interceptor
    private class RetryTokenInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request newRequest;

            //Try the request
            Response response = chain.proceed(request);

            int tryCount = 0;
            while ((response.code() == HTTP_UNAUTHORIZED) && tryCount < HTTP_MAX_TOKEN_RETRY) {

                boolean IsTokenAuthorized = getOAuthAccessToken();
                if (IsTokenAuthorized) {
                    //Add Headers to the Request
                    Headers REQ_HEADERS = Headers.of(getAllImpHeaders());

                    if (request.method().equalsIgnoreCase("POST")) {
                        newRequest = new Request.Builder()
                                .url(request.url())
                                .headers(REQ_HEADERS)
                                .post(request.body())
                                .build();
                    } else {
                        newRequest = new Request.Builder()
                                .url(request.url())
                                .headers(REQ_HEADERS)
                                .build();
                    }

                    response = chain.proceed(newRequest);             // retry the request
                }
                tryCount++;
            }

            // otherwise just pass the original response on
            return response;
        }
    }

    private class RetryRequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request newRequest;

            //Try the request
            Response response = chain.proceed(request);

            int tryCount = 0;
            while ((response.code() != HTTP_UNAUTHORIZED && response.code() != HTTP_OK) && tryCount < HTTP_MAX_TOKEN_RETRY) {

                //Add Headers to the Request
                Headers REQ_HEADERS = Headers.of(getAllImpHeaders());

                //Prepare Http Request
                if (request.method().equalsIgnoreCase("POST")) {
                    newRequest = new Request.Builder()
                            .url(request.url())
                            .headers(REQ_HEADERS)
                            .post(request.body())
                            .build();
                } else {
                    newRequest = new Request.Builder()
                            .url(request.url())
                            .headers(REQ_HEADERS)
                            .build();
                }

                response = chain.proceed(newRequest);             // retry the request

                tryCount++;
            }

            // otherwise just pass the original response on
            return response;
        }
    }

    /**
     * Interceptor to cache data and maintain it for a minute.
     * <p>
     * If the same network request is sent within a minute,
     * the response is retrieved from cache.
     */
    private class ResponseCacheInterceptor implements Interceptor {

        private Context mContext;
        private int maxAge;

        public ResponseCacheInterceptor(Context context, int max_age) {
            this.mContext = context;
            this.maxAge = max_age;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            okhttp3.Response originalResponse = chain.proceed(chain.request());
            String maxAgeHeader = originalResponse.header("Cache-Control");     //"public, max-age=3600";

            if (maxAgeHeader != null) {
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", maxAgeHeader)
                        .build();
            }

            return originalResponse;
        }
    }

    /**
     * Interceptor to cache data and maintain it for four weeks.
     * <p>
     * If the device is offline, stale (at most four weeks old)
     * response is fetched from the cache.
     */
    private class OfflineResponseCacheInterceptor implements Interceptor {

        private Context context;
        private int maxStale;

        public OfflineResponseCacheInterceptor(Context mcontext, int max_stale) {
            this.context = mcontext;
            this.maxStale = max_stale;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            boolean isAvailable = isNetworkAvailable();
            if (!isAvailable) {
                request = request.newBuilder()
                        .header("Cache-Control",
                                "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return chain.proceed(request);
        }

        //Finding the network is exists or not
        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }
    }

    // Skips cache and forces full refresh. Used for service connection
    private class NoCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "no-cache")
                    .build();
        }
    }

    private HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            logger.writeDebug(TAG, message);
            Log.e("OkHttpLog", message);
        }
    });
    //endregion

    //region OkHttpService GET Request
    public OkHttpClient getOkHttpClientWithNetworkAndOfflineAndRetryTokenInterceptor(int max_age, int max_stale) {
        String path = context.getFilesDir() + context.getString(R.string.cache_path);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // Enable response caching
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Timeout
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Read Timeout
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Write Timeout
                .addNetworkInterceptor(new ResponseCacheInterceptor(context, max_age))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new OfflineResponseCacheInterceptor(context, max_stale))
                .addInterceptor(new RetryTokenInterceptor())
                .cookieJar(vkApp.getCookieJar())
                // Set the cache location and size (20 MB)
                .cache(new Cache(new File(path, "apiResponses"), CACHE_FILE_SIZE))
                .build();

        return okHttpClient;
    }

    /**
     * No Cache Interceptor
     *
     * @return
     */
    public OkHttpClient getOkHttpClientWithNoCacheAndRetryTokenIInterceptor() {
        String path = context.getFilesDir() + context.getString(R.string.cache_path);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // Enable response caching
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Timeout
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)                                                  // Added 45sec Read Timeout
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)                                                 // Added 45sec Write Timeout
                .addNetworkInterceptor(new ResponseCacheInterceptor(context, 0))                    // Cache the response
                .addInterceptor(loggingInterceptor)
                //.addInterceptor(new NoCacheInterceptor())
                .addInterceptor(new RetryTokenInterceptor())
                .cookieJar(vkApp.getCookieJar())
                .cache(new Cache(new File(path, "apiResponses"), CACHE_FILE_SIZE))
                .build();

        return okHttpClient;
    }

    // Used only for for Sync Framework
    public OkHttpClient getOkHttpClientWithNoCacheAndRetryTokenAndRequestInterceptor() {
        String path = context.getFilesDir() + context.getString(R.string.cache_path);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // Enable response caching
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Timeout
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Read Timeout
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Write Timeout
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new RetryRequestInterceptor())
                .addInterceptor(new NoCacheInterceptor())
                .addInterceptor(new RetryTokenInterceptor())
                .cookieJar(vkApp.getCookieJar())
                .build();

        return okHttpClient;
    }

    /**
     * int max_stale = 2419200
     * int max_age = Provided By Server
     *
     * @param max_stale
     * @return
     */
    public OkHttpClient getOkHttpClientWithNetworkAndOfflineInterceptor(int max_stale, int max_age) {
        String path = context.getFilesDir() + context.getString(R.string.cache_path);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // Enable response caching

                .addNetworkInterceptor(new ResponseCacheInterceptor(context, max_age))
                .addInterceptor(new OfflineResponseCacheInterceptor(context, max_stale))
                .cookieJar(vkApp.getCookieJar())
                // Set the cache location and size (20 MB)
                .cache(new Cache(new File(path, "apiResponses"), CACHE_FILE_SIZE))
                .build();

        return okHttpClient;
    }

    /**
     * No Cache Interceptor
     *
     * @return
     */
    public OkHttpClient getOkHttpClientWithNoCacheInterceptor() {
        String path = context.getFilesDir() + context.getString(R.string.cache_path);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(vkApp.getCookieJar())
                // Enable response caching
                .addInterceptor(new NoCacheInterceptor())
                .build();

        return okHttpClient;
    }

    //Synchronous Get Request
    public String get() throws IOException {
        String responseData = null;
        Request request = new Request.Builder()
                .url(SERVER_URL)
                //.header("User-Agent", "User Agent")
                .headers(REQ_HEADERS)
                .build();
        Response response = newCall(request).execute();
        if (response.isSuccessful()) {
            responseData = response.body().string();
        }
        return responseData;
    }

    //endregion OkHttpService GET Request

    //region OkHttpService GET Async Request
    //Asynchronous Get call
    public void asynchronousEnqueueGet(final String path, int maxCacheSize) throws Exception {

        //maxCacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(new File(path), maxCacheSize);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(vkApp.getCookieJar())
                .cache(cache)
                .build();

        Request request = new Request.Builder()
                .url(SERVER_URL)
                //.header("User-Agent", "User Agent")
                .headers(REQ_HEADERS)
                .cacheControl(new CacheControl.Builder().maxStale(3, TimeUnit.DAYS).build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                logger.writeError(TAG, "OnFailure: SERVER_URL: " + e.toString());
                //writeResponse(response, path);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //writeResponse(response.body().string(), path);
                logger.writeError(TAG, "Response: " + response.body().string());
            }
        });
    }
    //endregion

    //region OkHttpService POST Request
    public Response post(String json) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request = new Request.Builder()
                .url(SERVER_URL)
                //.header("User-Agent", "User Agent")
                .headers(REQ_HEADERS)
                .post(body)
                .build();
        Response response = newCall(request).execute();
        return response;
    }

    public OkHttpClient postOkHttpClientWithNoCacheAndRetryTokenInterceptor() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Timeout
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Read Timeout
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)                                               // Added 45sec Write Timeout
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new NoCacheInterceptor())
                .addInterceptor(new RetryTokenInterceptor())
                .cookieJar(vkApp.getCookieJar())
                .build();

        return okHttpClient;
    }
    //endregion

    //region File Upload & Download and Write Response

    //Request Body
    public RequestBody getPreparedRequestBody(final File file) {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_PLAINTEXT;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {

                int fileLength = (int) contentLength();
                byte[] buffer = new byte[fileLength];
                FileInputStream in = new FileInputStream(file);
                long total = 0;
                try {
                    int read;
                    while ((read = in.read(buffer)) != -1) {

                        total += read;
                        float tempProgress = (((float) total * (float) 100) / (float) fileLength);
                        int progress = (int) Math.round(tempProgress);
                        sink.write(buffer, 0, read);
                    }
                } finally {
                    in.close();
                }
            }
        };
        return requestBody;
    }

    public RequestBody uploadImageRequestBody(String title, String imgName, String imgPath) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", title)
                .addFormDataPart("image", imgName,
                        RequestBody.create(MEDIA_TYPE_PNG, new File(imgPath)))
                .build();
        return requestBody;
    }

    /**
     * Download File
     *
     * @param downloadUrl
     * @throws Exception
     */
    public void downloadFileSync(String downloadUrl, String downloadPath) {
        OkHttpClient client = new OkHttpClient();
        FileOutputStream fos = null;
        try {
            Request request = new Request.Builder().url(downloadUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                fos = new FileOutputStream(downloadPath);       //fos = new FileOutputStream("d:/tmp.txt");
                fos.write(response.body().bytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "downloadFileSync: Error: " + e.toString());
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.writeError(TAG, "downloadFileSync: Error: " + e.toString());
            }
        }
    }

    /**
     * Write JSON response in file
     *
     * @param response
     * @param path
     * @return
     */
    public boolean writeResponse(String response, String path) {


        if (response != null && path != null) {
            Writer output = null;
            FileWriter fileWriter = null;
            try {

                //Create File and directories if not exist
                File file = new File(path);
                if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
                    file.createNewFile();
                }

                fileWriter = new FileWriter(file);
                output = new BufferedWriter(fileWriter);
                output.write(response);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    //endregion

    //region Get Data From Service
    // Get Data from Service
    public String getDataFromService(boolean IsCached, String url) {
        String data = null;
        OkHttpClient okHttpClient;
        Response response;
        try {
            //Decision to take response from network
            Headers REQ_HEADERS;
            if (IsCached) {
                okHttpClient = getOkHttpClientWithNetworkAndOfflineAndRetryTokenInterceptor(MAX_AGE_60s, MAX_STALE_1day);
                REQ_HEADERS = Headers.of(getAllImpHeaders());
            } else {
                okHttpClient = getOkHttpClientWithNoCacheAndRetryTokenIInterceptor();
                REQ_HEADERS = Headers.of(getAllImpHeadersWithoutCacheControl());
            }

            //Add Headers to the Request
            //REQ_HEADERS = Headers.of(getAllImpHeaders());

            //Prepare Http Request
            Request request = new Request.Builder()
                    .url(url)
                    .headers(REQ_HEADERS)
                    .build();

            //Prepare Http Response
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                data = response.body().string();
            } else {
                int code = response.code();
                logger.writeError(TAG, "getDataFromService:  Error in request processing. Url : " + url + " (ErrCode : " + code + ")");
                return "ERROR|Unable to process your request. ErrCode: " + code;
            }
        } catch (IOException io) {
            io.printStackTrace();
            logger.writeError(TAG, "getDataFromService: IOException Occur: " + io.toString());
            return "ERROR|Server is busy. Please try again later. ";
        }

        return data;
    }

    //Get Response from Service
    public Response getResponseFromService(boolean IsCached, String url) {
        OkHttpClient okHttpClient;
        Response response = null;
        try {
            //Decision to take response from network
            if (IsCached) {
                okHttpClient = getOkHttpClientWithNetworkAndOfflineAndRetryTokenInterceptor(MAX_AGE_60s, MAX_STALE_1day);
            } else {
                okHttpClient = getOkHttpClientWithNoCacheAndRetryTokenIInterceptor();
            }

            //Add Headers to the Request
            Headers REQ_HEADERS = Headers.of(getAllImpHeaders());

            //Prepare Http Request
            Request request = new Request.Builder()
                    .url(url)
                    .headers(REQ_HEADERS)
                    .build();

            //Prepare Http Response
            response = okHttpClient.newCall(request).execute();

        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "getResponseFromService: Error: " + e.toString());
        }
        return response;
    }

    //endregion

    //region Post Data to Service
    @Deprecated
    public String postDataToService(boolean IsCached, String url, String json) {
        String data = null;
        OkHttpClient okHttpClient;
        Response response;
        try {
            //Decision to take response from network
            okHttpClient = postOkHttpClientWithNoCacheAndRetryTokenInterceptor();

            //Add Headers to the Request
            Headers REQ_HEADERS = Headers.of(getAllImpHeaders());
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);

            //Prepare Http Request
            Request request = new Request.Builder()
                    .url(url)
                    .headers(REQ_HEADERS)
                    .post(body)
                    .build();

            //Prepare Http Response
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                data = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "postDataToService: Error: " + e.toString());
        }
        return data;
    }

    public String postDataToService(String url, String json) {
        String data = null;
        OkHttpClient okHttpClient;
        Response response;
        try {
            //Decision to take response from network
            okHttpClient = postOkHttpClientWithNoCacheAndRetryTokenInterceptor();

            //Add Headers to the Request
            Headers REQ_HEADERS = Headers.of(getAllImpHeaders());
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);

            //Prepare Http Request
            Request request = new Request.Builder()
                    .url(url)
                    .headers(REQ_HEADERS)
                    .post(body)
                    .build();

            //Prepare Http Response
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                data = response.body().string();
            } else {
                int code = response.code();
                logger.writeError(TAG, "postDataToService:  Error in request processing. Url : " + url + " (ErrCode : " + code + ")");
                return "ERROR|Unable to process your request. ErrCode: " + code;
            }
        } catch (IOException io) {
            io.printStackTrace();
            logger.writeError(TAG, "postDataToService: IOException Occur: " + io.toString());
            return "ERROR|Server is busy. Please try again later. ";
        }
        return data;
    }

    public String postDataInFormToService(String url, Map<String, String> formField) {
        String data = null;
        OkHttpClient okHttpClient;
        Response response;
        try {
            //Decision to take response from network
            okHttpClient = postOkHttpClientWithNoCacheAndRetryTokenInterceptor();

            //Add Headers to the Request
            Headers REQ_HEADERS = Headers.of(getAllImpHeaders());

           /* RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("somParam", "someValue")
                    .build();*/

            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : formField.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }

            //Prepare Http Request
            Request request = new Request.Builder()
                    .url(url)
                    .headers(REQ_HEADERS)
                    .post(formBodyBuilder.build())
                    .build();

            //Prepare Http Response
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                data = response.body().string();
            } else {
                int code = response.code();
                logger.writeError(TAG, "postDataInFormToService:  Error in request processing. Url : " + url + " (ErrCode : " + code + ")");
                return "ERROR|Unable to process your request. ErrCode: " + code;
            }
        } catch (IOException io) {
            io.printStackTrace();
            logger.writeError(TAG, "postDataInFormToService: IOException Occur: " + io.toString());
            return "ERROR|Server is busy. Please try again later. ";
        }
        return data;
    }

    public Response postResponseToService(String url, String json) {
        OkHttpClient okHttpClient;
        Response response = null;
        try {
            //Decision to take response from network
            okHttpClient = postOkHttpClientWithNoCacheAndRetryTokenInterceptor();

            //Add Headers to the Request
            Headers REQ_HEADERS = Headers.of(getAllImpHeaders());
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);

            //Prepare Http Request
            Request request = new Request.Builder()
                    .url(url)
                    .headers(REQ_HEADERS)
                    .post(body)
                    .build();

            //Prepare Http Response
            response = okHttpClient.newCall(request).execute();

        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "postResponseToService: Error: " + e.toString());
        }
        return response;
    }

    //endregion

    //region Read and Clear && Delete OkHttp Cache

    //Method to clear cache using Evict all
    public boolean clearCacheUsingEvict() {

        String path = context.getFilesDir() + context.getString(R.string.cache_path);
        File httpCacheDirectory = new File(path, "apiResponses");
        httpCacheDirectory.getParentFile().mkdirs();
        Cache cache = new Cache(httpCacheDirectory, CACHE_FILE_SIZE);
        try {
            cache.initialize();

            // Read Urls from Cache
            logger.writeError(TAG, "BEFORE: ");
            readUrlsFromCache(cache);

            // Deletes all values stored in the cache. In-flight writes to the cache will complete normally, but the corresponding responses will not be stored.
            cache.evictAll();

            // Read Urls from Cache
            logger.writeError(TAG, "AFTER: ");
            readUrlsFromCache(cache);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "clearCacheUsingEvict: CACHE NOT INIT: " + e.toString());
        } finally {
            try {
                if (cache != null) {
                    cache.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "clearCacheUsingEvict: " + e.toString());
            }
        }
        return false;
    }

    //Method to clear cache using delete
    public boolean clearCacheUsingDelete() {

        String path = context.getFilesDir() + context.getString(R.string.cache_path);
        File httpCacheDirectory = new File(path, "apiResponses");
        httpCacheDirectory.getParentFile().mkdirs();
        Cache cache = new Cache(httpCacheDirectory, CACHE_FILE_SIZE);
        try {
            cache.initialize();

            // Read Urls from Cache
            readUrlsFromCache(cache);

            // Deletes all values stored in the cache. In-flight writes to the cache will complete normally, but the corresponding responses will not be stored.
            cache.delete();

            // Read Urls from Cache
            readUrlsFromCache(cache);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            logger.writeError(TAG, "clearCacheUsingDelete: CACHE NOT INIT: " + e.toString());
        } finally {
            try {
                if (cache != null) {
                    cache.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.writeError(TAG, "clearCacheUsingDelete: " + e.toString());
            }
        }
        return false;
    }

    // Read Urls from Cache
    public void readUrlsFromCache(Cache cache) {

        try {
            Iterator<String> iterator = cache.urls();
            while (iterator.hasNext()) {
                logger.writeError(TAG, "clearCache: Urls: " + iterator.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.writeError(TAG, "readUrlsFromCache: Error:" + e.toString());
        }
    }
    //endregion

    // region Get OAUTH Access Token
    public boolean getOAuthAccessToken() {

        String OAUTHToken = oAuthUtils.getToken();
        if (!TextUtils.isEmpty(OAUTHToken))
            return true;

        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(context);
        if (loginChecksDto == null || TextUtils.isEmpty(loginChecksDto.getUserName()))
            return false;

        return oAuthUtils.authenticate(loginChecksDto.getUserName(), "");
    }
    // endregion
}
