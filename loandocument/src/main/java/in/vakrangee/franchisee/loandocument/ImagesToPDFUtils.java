package in.vakrangee.franchisee.loandocument;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;
import in.vakrangee.franchisee.loandocument.model.AgreementImageDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class ImagesToPDFUtils {

    private static final String TAG = "ImagesToPDFUtils";
    private static String dirPath = "/sdcard/Download/PRDownloader/";

    public static String getPDFDestPath(Context context, String loanDocumentId) {

       // String enquiryId = CommonUtils.getEnquiryId(context);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                LoanDocumentConstants.DIR_NAME_LOAN_UPLOAD + File.separator + loanDocumentId);
        mediaStorageDir.mkdirs();

        String fileName = loanDocumentId + ".pdf";

        String DEST = mediaStorageDir.getAbsolutePath() + File.separator + fileName;
        return DEST;
    }

    public static boolean createPdf(String[] IMAGES, Context context,String loanDocumentId) throws IOException, DocumentException {

        try {
            String dest = getPDFDestPath(context, loanDocumentId);
            File file = new File(dest);
            file.getParentFile().mkdirs();

            Image img = Image.getInstance(IMAGES[0]);
            Document document = new Document(img);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            writer.setFullCompression();
            document.open();
            for (String image : IMAGES) {
                img = Image.getInstance(image);
				img.setAlignment(Image.MIDDLE);
                document.setPageSize(PageSize.A4);
                //document.setMargins(60,60,30,30);

               /* img.setAbsolutePosition(10f, 10f);
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / img.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                img.scalePercent(scaler);
                img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);*/


                float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
                float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
                img.scaleToFit(documentWidth, documentHeight);

                document.newPage();
                //img.setAbsolutePosition(0, 0);
                //document.setPageSize(PageSize.LETTER.rotate());
                document.setPageSize(PageSize.A4);
                document.add(img);
            }
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void viewPdf(Context context, File myPDF) {
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW);
        Log.w(TAG, "Opening:  " + myPDF.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", myPDF);

            downloadIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            downloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            downloadIntent.setDataAndType(apkUri, "application/pdf");

            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(downloadIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, apkUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

        } else {
            downloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            downloadIntent.setDataAndType(Uri.fromFile(myPDF), "application/pdf");
        }
        downloadIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(downloadIntent);
    }

    public static List<AgreementImageDto> pdfToBitmap(Context context, File pdfFile) {
        List<AgreementImageDto> imgsList = new ArrayList<AgreementImageDto>();

        try {

            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_WRITE));

            final int pageCount = renderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);

                int width = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
                int height = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                //Prepare Image Data
                AgreementImageDto imageDto = new AgreementImageDto();
                imageDto.setName(String.valueOf(i + 1));
                imageDto.setBitmap(bitmap);
                imageDto.setImgBase64(CommonUtils.convertBitmapToString(bitmap));
                imgsList.add(imageDto);

                // close the page
                page.close();
            }
            // close the renderer
            renderer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imgsList;
    }

    public void pdf2Image(Context context, File pdfFile) {
    }

    public static Uri compressPdfFileSize(File filePath, Context context,String loanDocumentId) {
        try {
            String destPDFPath = getPDFDestPath(context,loanDocumentId);

            FileInputStream fis = new FileInputStream(filePath);
            PdfReader reader = new PdfReader(fis);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destPDFPath));
            int total = reader.getNumberOfPages() + 1;
            for (int i = 1; i < total; i++) {
                reader.setPageContent(i + 1, reader.getPageContent(i + 1));
            }
            stamper.setFullCompression();
            stamper.close();

            return Uri.fromFile(new File(destPDFPath));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
