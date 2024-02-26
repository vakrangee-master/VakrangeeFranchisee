package in.vakrangee.core.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class Logger {

    private final static String LOG_DIR_NAME = "Log";
    private static String LOG_FILE_NAME = "Logger.txt";
    private static String LOG_FILE_PATH;
    private static Logger logger = null;
    private static Context mContext;
    private static int LOG_LEVEL = 3;
    private final static int MAX_LENGTH = 1024 * 5;     // 5MB Max Size

    // LOG LEVEL
    private int LOG_TYPE_ERROR = 1;
    private int LOG_TYPE_DEBUG = 2;
    private int LOG_TYPE_INFO = 3;

    private final static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    private Logger() {}

    public static Logger getInstance(@NonNull Context context) {

        mContext = context;
        if(logger == null) {
            logger = new Logger();
        }

        // Create Dir If not exists and Set Log File Path
        String logDir = mContext.getFilesDir().getAbsolutePath() + File.separator + LOG_DIR_NAME;
        File file = new File(logDir);
        if(!file.exists()) {
            file.mkdirs();
        }
        LOG_FILE_PATH = logDir + File.separator + LOG_FILE_NAME;

        return logger;
    }

    //region Write to Log
    public void writeError(@NonNull String TAG, @NonNull String msg) {
        write(TAG, msg, LOG_TYPE_ERROR);
    }

    public void writeDebug(@NonNull String TAG, @NonNull String msg) {
        write(TAG, msg, LOG_TYPE_DEBUG);
    }

    public void writeInfo(@NonNull String TAG, @NonNull String msg) {
        write(TAG, msg, LOG_TYPE_INFO);
    }

    // Write Log into File
    private void write(@NonNull String TAG, @NonNull String msg, int type) {

        // Check Mesasge is empty
        if(msg == null && msg.trim().length() == 0) {
            return;
        }

        // Set Type Log Type
        String logType = "";
        switch (type) {
            case 1:
                logType = "ERR  ";
                break;
            case 2:
                logType = "DEBUG";
                break;
            case 3:
                logType = "INFO ";
                break;
            default:
                logType = "INFO ";
                break;
        }

        // Check LOG_LEVEL
        if(type <= LOG_LEVEL) {
            // Create log format and write into File
            String log = dateFormatter.format(new Date()) + " " + logType + " ["+TAG+"] " + msg + "\n";
            Log.d("Logger", log);
            FileOutputStream fos = null;
            try {
                File file = new File(LOG_FILE_PATH);

                // Check File Size to Create Backup
                int file_size = Integer.parseInt(String.valueOf(file.length()));
                if(file_size >= MAX_LENGTH) {
                    file.renameTo(new File(LOG_FILE_PATH+".backup"));
                    file.createNewFile();
                }

                // Write into file
                fos = new FileOutputStream(file, true) ;
                fos.write(log.getBytes());

                Log.e("Logger", log);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
    //endregion
}
