package in.vakrangee.franchisee.gwr.evidence;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import in.vakrangee.supercore.franchisee.utils.Connection;

public class UploadEvidenceFilesService extends Service {

    private static final String TAG = "UploadEvidenceFilesService";
    private Context context;
    private UploadEvidenceData uploadEvidenceData = null;
    private EvidenceRepository evidenceRepository;
    private Connection connection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize
        this.context = UploadEvidenceFilesService.this;
        evidenceRepository = new EvidenceRepository(context);
        connection = new Connection(context);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Call Async
        String vkId = connection.getVkid();
        if (!TextUtils.isEmpty(vkId)) {
            uploadEvidenceData = new UploadEvidenceData(vkId);
            uploadEvidenceData.execute("");
        }
        return START_STICKY;
    }

    /**
     * Upload Evidences files Data
     */
    class UploadEvidenceData extends AsyncTask<String, Integer, String> {

        private String vkId;

        public UploadEvidenceData(String vkId) {
            this.vkId = vkId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                EvidenceDto evidenceDto;
                while ((evidenceDto = evidenceRepository.getNextImage()) != null) {

                    if (evidenceDto != null) {

                        Gson gson = new Gson();
                        String jsonData = gson.toJson(evidenceDto, EvidenceDto.class);

                        String response = evidenceRepository.uploadFileDataService(vkId, jsonData);
                        if (TextUtils.isEmpty(response) || response.startsWith("ERROR"))
                            break;

                        //Delete On Success
                        if (!evidenceRepository.deleteEvidence(evidenceDto))
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (uploadEvidenceData != null && !uploadEvidenceData.isCancelled()) {
            uploadEvidenceData.cancel(true);
        }
    }
}