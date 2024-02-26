package in.vakrangee.franchisee.gwr.evidence;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Compress;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;

public class EvidenceRepository {

    private static final String TAG = EvidenceRepository.class.getSimpleName();
    private Context mContext;
    private String evidence_folder = ".GWR";
    private Compress compress;
    private OkHttpService okHttpService;

    private EvidenceRepository() {
    }

    public EvidenceRepository(Context context) {
        this.mContext = context;
        this.compress = new Compress(context);
        init();
        okHttpService = new OkHttpService(context);
    }

    private void init() {
        // Create Folder into Storage
        File f = new File(Environment.getExternalStorageDirectory(), evidence_folder);
        if (!f.exists()) {
            f.mkdirs();
            Log.e(TAG, "GWR Folder Created Successfully.");
        }

    }

    //region Copy Evidence Photos
    public boolean copyEvidencePhoto(String sourceFilePath, String photoName) {

        // Copy Evidence Photo
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(photoName)) {
            Log.e(TAG, "Source File Path or File Name can not be empty.");
            return false;
        }

        sourceFilePath = sourceFilePath.replace("file:/", "");
        File file = new File(sourceFilePath);
        String name = file.getName();
        photoName = photoName.toLowerCase().replace(" ", "_");
        photoName = photoName + "_" + name;
        //photoName = photoName + ".jpg";
        String outputPath = Environment.getExternalStorageDirectory() + File.separator + evidence_folder + File.separator + photoName;

        // Output File Name
        Log.e(TAG, "Photo File Name: " + photoName);
        Log.e(TAG, "Photo Output Path : " + outputPath);

        return copyFile(sourceFilePath, outputPath);
    }

    private boolean copyFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
//            File dir = new File(outputPath);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

            Log.e(TAG, outputPath + "Copied Successfully.");

            return true;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
            Log.e(TAG, outputPath + "Failed Successfully.");
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
            Log.e(TAG, outputPath + "Failed Successfully.");
        }
        return false;
    }
    //endregion

    //region Compress GWR Files
    public boolean compressGWRFiles(String vkId) {

        if (TextUtils.isEmpty(vkId))
            return false;

        Log.e(TAG, "Compressing GWRFiles");
        String sourceFolder = Environment.getExternalStorageDirectory() + File.separator + evidence_folder;
        String destination = Environment.getExternalStorageDirectory() + File.separator + vkId + "_" + evidence_folder + ".zip";
        Log.e(TAG, "Source Folder : " + sourceFolder);
        Log.e(TAG, "Destination Folder : " + destination);
        //
        File file = new File(destination);
        if (file.exists() && file.isFile()) {
            Log.e(TAG, "Compress File Exists");
            file.delete();
            Log.e(TAG, "Compress File Deleting : ");
        }


        boolean flag = compress.zipFileAtPath(sourceFolder, destination);
        Log.e(TAG, "Compress GWRFiles Status : " + flag);

        return flag;
    }
    //endregion

    public EvidenceDto getNextImage() {

        try {
            String sourcePath = Environment.getExternalStorageDirectory() + File.separator + evidence_folder;
            File file = new File(sourcePath);
            File imageFiles[] = file.listFiles();

            if (imageFiles.length > 0) {

                File image = imageFiles[0];

                EvidenceDto evidenceDto = new EvidenceDto();
                evidenceDto.setFileName(image.getName());
                evidenceDto.setFilePath(image.getAbsolutePath());

                Log.e(TAG, "Getting Next Image : ");

                // Read File data and convert into base64
                String imageData = ImageUtils.getStringFile(image);
                if (TextUtils.isEmpty(imageData)) {
                    return null;
                }

                evidenceDto.setFile(imageData);

                return evidenceDto;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteEvidence(EvidenceDto evidenceDto) {
        if (evidenceDto != null) {

            Log.e(TAG, "deleteEvidence: " + evidenceDto.getFilePath());
            File file = new File(evidenceDto.getFilePath());
            return file.delete();

        }
        return false;
    }

    //region Post Raw Image Files
    public String uploadFileDataService(String vkid, String jsonData) {

        String data = null;
        try {
            String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_SAVE_EVIDENCE_RAW_FILES;
            url = url.replace("{vkId}", vkid);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Save Evidence Info into File
    public boolean writeEvidenceInfo(String data) {

        if (TextUtils.isEmpty(data))
            return false;

        FileOutputStream fos = null;
        try {
            String fileName = "inauguration_detail.txt";
            String sourcePath = Environment.getExternalStorageDirectory() + File.separator + evidence_folder + File.separator + fileName;

            // Save Inauguration Data
            File file = new File(sourcePath);
            if (file.exists() && file.isFile())
                file.delete();

            // Save Data into File.
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
            }
        }

        return false;
    }
    //endregion

}
