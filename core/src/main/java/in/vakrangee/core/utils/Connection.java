package in.vakrangee.core.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.vakrangee.core.application.VakrangeeKendraApplication;
import in.vakrangee.core.model.FranchiseeMasterDto;
import in.vakrangee.core.model.GeoCordinates;
import in.vakrangee.core.model.My_vakranggekendra_image;
import in.vakrangee.core.model.ServiceProvider;

/**
 * Created by Nileshd on 4/18/2016.
 */
@SuppressWarnings("ALL")
public class Connection extends SQLiteOpenHelper {

    public static SQLiteDatabase VKMSDatabase;
    Context context;

    public Connection(Context context) {

        super(context, Constants.DB_NAME, null, Constants.DATABASE_VERSION);

        if (VakrangeeKendraApplication.getStrictMode() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        this.context = context;
        Constants.DB_PATH = context.getDatabasePath(Constants.DB_NAME).getAbsolutePath().replace("databases/"+Constants.DB_NAME, ""); //"data/data/" + context.getPackageName() + "/";
        //Log.e("Connection","DB Path: "+Constants.DB_PATH);
        creatadatabase();
    }

    //region Database File Operations
    public void creatadatabase() {
        try {
            boolean dbexist = checkdatabase();
            //this.getReadableDatabase();
            if (!dbexist) {
                copydatabase();
            }


        } catch (Exception e) {
            System.out.println("Error Creating Database: " + e.getMessage());
        }
    }

    private boolean checkdatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = Constants.DB_PATH + Constants.DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            System.out.println("Error Checking Database: Database doesn't exist");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    public final void openDatabase() {
        try {
            if(VKMSDatabase == null || !VKMSDatabase.isOpen()) {
                String mypath = Constants.DB_PATH + Constants.DB_NAME;
                VKMSDatabase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
            }
        } catch (Exception e) {
            System.out.println("Error Opening Database: " + e.getMessage());
        }

    }

    private void copydatabase() throws IOException {
        InputStream myinput = context.getAssets().open(Constants.DB_NAME);
        String outfilename = Constants.DB_PATH + Constants.DB_NAME;
        OutputStream myoutput = new FileOutputStream(outfilename);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }
        myoutput.flush();
        myoutput.close();
        myinput.close();

    }

    private void deleteFile() {
        try {
            new File(Constants.DB_PATH + Constants.DB_NAME).delete();
        } catch (Exception ignored) {
        }
    }
    //endregion

    @Override
    public void onCreate(SQLiteDatabase sqld) {
        try {
            creatadatabase();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int oldVersion, int newVersion) {
        try {
            deleteFile();
            onCreate(sqld);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //region Database Query

    public List<ServiceProvider> getServiceProvider(int serviceId, int subServiceId) {
        String result = "";


        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        String SQLite = "select SubSubServiceId,SubSubServiceName  from ServiceProvider where ServiceId=6 and SubServiceId=6 ;";
        Cursor cursor = null;

        List<ServiceProvider> serviceList = new LinkedList<ServiceProvider>();
        try {


            cursor = VKMSDatabase.rawQuery(SQLite, null);
            if (cursor != null && cursor.getCount() != 0) {
                result = "MANY";
                int i = 1;
                while (cursor.moveToNext()) {
                    serviceList.add(new ServiceProvider(Integer.valueOf(cursor.getString(0)), cursor.getString(1)));
                    i++;
                }
            }
        } catch (Exception e) {
            return serviceList;
        } finally {
            if (cursor != null) {
                cursor.close();

            }
            this.close();
        }
        return serviceList;
    }

    public void insertIntoDB(String vkid, String Username, String Emailid, String mobilenumber) {
        try {

            String query = "insert or replace  into FranchiseeMaster (VKID,UserName,EmailId,MobileNumber,Status,MahavitranBillUnitNo) values ('" + vkid + "','" + Username + "','" + Emailid + "','" + mobilenumber + "','P','' )";
            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.getMessage();

        } finally {
            this.close();
        }
    }

    public void UpdateOtp(String vkid) {
        try {


            String query = "update FranchiseeMaster set Status='OTP_V'";
            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.getMessage();

        } finally {
            this.close();
        }
    }

    public void UpdatepasswordTokenId(String TokenId) {
        try {


            String query = "update FranchiseeMaster set TokenId='" + TokenId + "',Status='Y'";

            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.getMessage();

        } finally {
            this.close();
        }

    }

    public void setTokenIdnull()
    {
        try {

            String query = "update FranchiseeMaster set TokenId=null";
            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.getMessage();

        } finally {
            this.close();
        }
    }

    public String checkIsEmpty() {

        String sqlQuery = "select COUNT(*) from FranchiseeMaster";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    return cursor.getString(0);
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            this.close();
        }
        return null;
    }

    public String getVkid() {
        String sqlQuery = "select VKID from FranchiseeMaster";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    return cursor.getString(0);
                }
            }
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            this.close();
        }
        return "";
    }

    public String getTokenId() {

        String sqlQuery = "select TokenId from FranchiseeMaster";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    return cursor.getString(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            this.close();
        }
        return null;
    }

    public boolean getTokenIdisNull() {
        boolean bol = false;
        String sqlQuery = "select * from FranchiseeMaster where  Status='Y' and TokenId is not null";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    bol = true;
                }
            }
        } catch (Exception e) {
            bol = false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            this.close();
        }
        return bol;
    }

    public String getUsernameVkid() {

        String sqlQuery = "select UserName from  FranchiseeMaster";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    return cursor.getString(0);
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            this.close();
        }
        return null;
    }

    public void insertintoImageMaster(int i, byte[] byteArray) {
        try {
            SQLiteStatement p = VKMSDatabase.compileStatement("insert or replace into ImageMaster(ImageId, Image,VKID,CurrentDateTime,LatLong) values(?, ?,?,?,?)");
            p.bindString(1, String.valueOf(i));
            p.bindBlob(2, byteArray);
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public void insertintoImageMasterWithLatlong(int i, byte[] byteArray, String Vkid, String datetime, String latilong) {

        try {
            // SQLiteStatement p = VKMSDatabase.compileStatement("insert or replace into ImageMaster(ImageId, Image) values(?, ?)");
            SQLiteStatement p = VKMSDatabase.compileStatement("insert or replace into ImageMaster(ImageId, Image,VKID,CurrentDateTime,LatLong) values(?, ?,?,?,?)");
            p.bindString(1, String.valueOf(i));
            p.bindBlob(2, byteArray);
            p.bindString(3, Vkid);
            p.bindString(4, datetime);
            p.bindString(5, String.valueOf(latilong));


            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public void insertintoImageBitmap(int i, Bitmap img) {

        try {


            SQLiteStatement p = VKMSDatabase.compileStatement("insert or replace into ImageMaster(ImageId, Image) values(?, ?)");
            byte[] data = getBitmapAsByteArray(img);
            p.bindString(1, String.valueOf(i));
            p.bindBlob(2, data);
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }


        //  String query = "insert into ImageMaster(ImageId,Image) values ('"+i+"','"+byteArray+"');";
        // String query = "INSERT INTO persons (name,address) VALUES('"+name+"', '"+add+"');";
        //  VKMSDatabase.execSQL(query);

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public byte[] getImage(int i) {

        String sqlQuery = "select Image from  ImageMaster  where ImageId='" + i + "'";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {

                    return cursor.getBlob(0);

                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            this.close();
        }
        return null;
    }

    public void UpdateImageId(int id, byte[] byteArray) {

        try {


            String query = "update  ImageMaster set Image='" + byteArray + "'  where ImageId='" + id + "'";
            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }

    }

    public String[] getAllMahavitranName() {
        try (Cursor cursor = Connection.VKMSDatabase.rawQuery("select  BillUnitNo || ' - ' || SubDivisionName as SubDivisionName from MSEDCLMaster", null)) {

            if (cursor.getCount() > 0) {
                String[] str = new String[cursor.getCount()];
                int i = 0;


                while (cursor.moveToNext()) {

                    str[i] = cursor.getString(cursor.getColumnIndex("SubDivisionName"));

                    i++;
                }
                return str;
            } else {
                return new String[]{};
            }
        }
    }

    public void InsertSpineerBillUnitNo(int getSpineer) {
        try {


            String query = "update FranchiseeMaster set MahavitranBillUnitNo='" + getSpineer + "'";
            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public int getSelectedValues() {
        String sqlQuery = "select MahavitranBillUnitNo from FranchiseeMaster";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    return Integer.parseInt(cursor.getString(0));
                }
            }
        } catch (Exception e) {
            return 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            this.close();
        }
        return 0;
    }

    public void insertLatLong(String Date_Time, String Latitude, String Longitude, String Status) {
        try {


            String query = "insert into LocationTrackingMaster (Date_Time,Latitude,Longitude,Status) values ('" + Date_Time + "','" + Latitude + "','" + Longitude + "','" + Status + "')";
            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }

    }

    public ArrayList<GeoCordinates> GetAllValues() {
        String SQLite = "select Date_Time,Latitude,Longitude,Status  from LocationTrackingMaster";
        Cursor cursor = null;


        ArrayList<GeoCordinates> list = new ArrayList<>();
        cursor = VKMSDatabase.rawQuery(SQLite, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new GeoCordinates(cursor.getString(0), cursor.getString(2)));
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (VKMSDatabase != null) {
            this.close();
        }

        return list;
    }

    public void resetMpin() {
        String query = "update FranchiseeMaster set Status='P'";
        VKMSDatabase.execSQL(query);
    }

    public void insertIntoProflieImage(int i, byte[] byteArray) {

        try {
            SQLiteStatement p = VKMSDatabase.compileStatement("insert or replace into ProfileImageMaster(ImageId, ProfileImage) values(?,?)");
            p.bindString(1, String.valueOf(i));
            p.bindBlob(2, byteArray);
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }

    }

    public byte[] getProfileImage(int i) {
        String sqlQuery = "select ProfileImage from  ProfileImageMaster  where ImageId='" + i + "'";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {

                    return cursor.getBlob(0);

                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
                this.close();
            }

        }
        return null;
    }

    public void insertIntoFranchiseeOnUpgrade(String vkid, String name, String mail, String mobileid, String token) {
        try {


            String query = "insert into FranchiseeMaster (VKID,UserName,EmailId,MobileNumber,Status,MahavitranBillUnitNo,TokenId) values ('" + vkid + "','" + name + "','" + mail + "','" + mobileid + "','Y','','" + token + "' )";
            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    public void TrackingUpdate(String trackingstatus) {

        String query = "update FranchiseeMaster set Tracking='" + trackingstatus + "'";

        VKMSDatabase.execSQL(query);
        Log.e(query + "okay with Tracking Update -", trackingstatus);

    }

    public String getTrackingID() {
        String sqlQuery = "select Tracking from FranchiseeMaster";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    return cursor.getString(0);
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
                this.close();
            }
        }
        return null;
    }

    public void deleteimage(My_vakranggekendra_image myvakranggekendraimage) {
        Log.e("Sqldelete", String.valueOf(myvakranggekendraimage.getID()));
        String id = String.valueOf(myvakranggekendraimage.getID());

        //  update FranchiseeMaster set MahavitranBillUnitNo='" + getSpineer + "'"

        String query = "delete from  ImageMaster where ImageId='" + id + "'";
        VKMSDatabase.execSQL(query);

    }

    private int getID(String job) {
        Cursor c = VKMSDatabase.query(Constants.TABLE_CONTACTS, new String[]{"ImageType"}, "ImageType=?", new String[]{job}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("ImageType"));
        return -1;
    }

    public byte[] getImagewithname(String i) {
        String sqlQuery = "select Image from  ImageMaster  where ImageType='" + i + "'";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {

                    return cursor.getBlob(0);

                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
                this.close();
            }
        }
        return null;
    }

    public void insertTimingMaster(String fname, String day) {


        String query = "insert or replace into TimingMaster ('" + fname + "') values ('" + day + "')";
        VKMSDatabase.execSQL(query);

    }

    public byte[] getImageFromImg(String i) {
        String sqlQuery = "select Image from  ImageMaster  where ImageType='" + i + "'";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {

                    return cursor.getBlob(0);

                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
                this.close();
            }
        }
        return null;
    }

    public void deleteTableinfo() {
        try {
            String query = "delete from   FranchiseeMaster;delete from   ImageMaster ;delete from   LocationTrackingMaster;" +
                    "delete from   ProfileImageMaster;delete from   TimingMaster;";
            String DELETEPASSCODE_DETAIL = "DELETE FROM ProfileImageMaster;";
            VKMSDatabase.execSQL(DELETEPASSCODE_DETAIL);
            VKMSDatabase.execSQL(query);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            this.close();
        }
    }

    //endregion

    //region Database Query with data model
    public FranchiseeMasterDto getFrachiseeMaster(String vkid) {
        String sqlQuery = "select VKID as VKID, UserName as UserName, EmailId as EmailId, MobileNumber as MobileNumber, " +
                "TokenId as TokenId, Status as Status, Tracking as Tracking from FranchiseeMaster where VKID = '"+vkid+"'";
        Cursor cursor = null;
        Log.d("", sqlQuery);
        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                FranchiseeMasterDto fmDto = new FranchiseeMasterDto();
                while (cursor.moveToNext()) {
                    fmDto.setVKID(cursor.getString(cursor.getColumnIndex("VKID")));
                    fmDto.setUserName(cursor.getString(cursor.getColumnIndex("UserName")));
                    fmDto.setEmailId(cursor.getString(cursor.getColumnIndex("EmailId")));
                    fmDto.setMobileNumber(cursor.getString(cursor.getColumnIndex("MobileNumber")));
                    fmDto.setTokenId(cursor.getString(cursor.getColumnIndex("TokenId")));
                    fmDto.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                    fmDto.setTracking(cursor.getString(cursor.getColumnIndex("Tracking")));
                }
                return fmDto;
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            this.close();
        }
        return null;

    }
    //endregion

}