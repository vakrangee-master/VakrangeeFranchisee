package in.vakrangee.franchisee.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.MyVakrangeeKendraPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.adapter.MyVakrangeeKendraImageAdapter;
import in.vakrangee.franchisee.task.AsyncGetmyVakrangeeKendraTimingsResponse;
import in.vakrangee.franchisee.task.AsyncGetmyVakrangeeKendraTimingsResponseParticular;
import in.vakrangee.supercore.franchisee.model.MyVKMaster;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by Nileshd on 12/30/2016.
 */
@SuppressLint("ValidFragment")
public class MyVakrangeeKendraPhotoFragment extends Fragment {


    String TAG = "Response";

    SimpleDateFormat sdf;
    String getlati;
    String currentDateandTime;

    String diplayServerResopnse;
    TelephonyManager telephonyManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    public MyVakrangeeKendraImageAdapter adapter;
    Connection connection;
    Spinner spinner;
    private ImageView imageViewa1;
    ImageView btnCapture;

    byte[] byteArray;

    Button btnAddphoto, btnSubmitPhoto;

    private RecyclerView recyclerView;
    private MyVakrangeeKendraImageAdapter adapterR;

    // MyVKMaster myVKMaster;
    MyVKMaster myVKMasterMain;
    List<MyVKMaster> rowItems;
    ProgressDialog progress;
    String Frontage, LeftWall, FrontWall, RightWall, BackWall, Ceiling, Floor, Extra1, Extra2, Extra3;
    final int CAMERA_CAPTURE = 1;

    int PIC_CROP = 3;
    private Uri picUri;
    InternetConnection internetConnection;
    String imgetype;
    List<My_vakranggekendra_image> myvakranggekendraimageList;

    @SuppressLint("ValidFragment")
    public MyVakrangeeKendraPhotoFragment(MyVKMaster myVKMaster) {

        this.myVKMasterMain = myVKMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vakrangeekendra_photo, container, false);

        try {
            camerapermission();
            telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);


            connection = new Connection(getActivity());

            // listView = (ListView) view.findViewById(R.id.listkendraphoto);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyle_view_search);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            //contactList = connection.getAllAdpaterImage();

            myvakranggekendraimageList = new ArrayList<My_vakranggekendra_image>();
            if (myVKMasterMain != null && myVKMasterMain.getFrontageThumb() != null && !myVKMasterMain.getFrontageThumb().isEmpty() && !myVKMasterMain.getFrontageThumb().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimage = new My_vakranggekendra_image();
                myvakranggekendraimage.setID(0);
                myvakranggekendraimage.setImgetype("Frontage Image");
                Bitmap img = StringToBitMap(myVKMasterMain.getFrontageThumb());
                myvakranggekendraimage.setImage(img);

                myvakranggekendraimageList.add(myvakranggekendraimage);
            }
            if (myVKMasterMain != null && myVKMasterMain.getLeftWallThumb() != null && !myVKMasterMain.getLeftWallThumb().isEmpty() && !myVKMasterMain.getLeftWallThumb().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(1);
                myvakranggekendraimagea.setImgetype("Left Wall Image");
                Bitmap img = StringToBitMap(myVKMasterMain.getLeftWallThumb());
                myvakranggekendraimagea.setImage(img);

                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (myVKMasterMain != null && myVKMasterMain.getFrontWallThumb() != null && !myVKMasterMain.getFrontWallThumb().isEmpty() && !myVKMasterMain.getFrontWallThumb().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(2);
                myvakranggekendraimagea.setImgetype("Front Wall Image");
                Bitmap img = StringToBitMap(myVKMasterMain.getFrontWallThumb());
                myvakranggekendraimagea.setImage(img);

                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (myVKMasterMain != null && myVKMasterMain.getRightWallThumb() != null && !myVKMasterMain.getRightWallThumb().isEmpty() && !myVKMasterMain.getRightWallThumb().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(3);
                myvakranggekendraimagea.setImgetype("Right Wall Image");
                Bitmap img = StringToBitMap(myVKMasterMain.getRightWallThumb());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (myVKMasterMain != null && myVKMasterMain.getBackWallThumb() != null && !myVKMasterMain.getBackWallThumb().isEmpty() && !myVKMasterMain.getBackWallThumb().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(4);
                myvakranggekendraimagea.setImgetype("Back Wall Image");
                Bitmap img = StringToBitMap(myVKMasterMain.getBackWallThumb());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (myVKMasterMain != null && myVKMasterMain.getCeilingThumb() != null && !myVKMasterMain.getCeilingThumb().isEmpty() && !myVKMasterMain.getCeilingThumb().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(5);
                myvakranggekendraimagea.setImgetype("Ceiling Image");
                Bitmap img = StringToBitMap(myVKMasterMain.getCeilingThumb());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }

            if (myVKMasterMain != null && myVKMasterMain.getFloorThumb() != null && !myVKMasterMain.getFloorThumb().isEmpty() && !myVKMasterMain.getFloorThumb().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(6);
                myvakranggekendraimagea.setImgetype("Floor Image");
                Bitmap img = StringToBitMap(myVKMasterMain.getFloorThumb());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (myVKMasterMain != null && myVKMasterMain.getExtraThumb1() != null && !myVKMasterMain.getExtraThumb1().isEmpty() && !myVKMasterMain.getExtraThumb1().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(7);
                myvakranggekendraimagea.setImgetype("Extra Image 1");
                Bitmap img = StringToBitMap(myVKMasterMain.getExtraThumb1());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (myVKMasterMain != null && myVKMasterMain.getExtraThumb2() != null && !myVKMasterMain.getExtraThumb2().isEmpty() && !myVKMasterMain.getExtraThumb2().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(8);
                myvakranggekendraimagea.setImgetype("Extra Image 2");
                Bitmap img = StringToBitMap(myVKMasterMain.getExtraThumb2());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (myVKMasterMain != null && myVKMasterMain.getExtraThumb3() != null && !myVKMasterMain.getExtraThumb3().isEmpty() && !myVKMasterMain.getExtraThumb3().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(9);
                myvakranggekendraimagea.setImgetype("Extra Image 3");
                Bitmap img = StringToBitMap(myVKMasterMain.getExtraThumb3());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }


            internetConnection = new InternetConnection(getActivity());

            // adapterR.setAdapter(adapter);
            // contactList = myvakranggekendraimageList;

            adapter = new MyVakrangeeKendraImageAdapter(getContext(), myvakranggekendraimageList);
            recyclerView.setAdapter(adapter);
            // adapter = new MyVakrangeeKendraImageAdapter(getActivity(), contactList);
            //listView.setAdapter(adapter);

            btnSubmitPhoto = (Button) view.findViewById(R.id.btnSubmitPhoto);
            btnAddphoto = (Button) view.findViewById(R.id.btnAddphoto);
            btnAddphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageCapturePopup();
                }
            });

            btnSubmitPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadtoserver();


                }
            });

            return view;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    private void uploadtoserver() {
        try {

            if (myVKMasterMain.getLatitude() == null) {
                Toast.makeText(getActivity(), "Please Turn on GPS setting", Toast.LENGTH_SHORT).show();
            } else if (internetConnection.isConnectingToInternet() == false) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.internetCheck));

            } else {
                //byteArray=null;
                AsyncMyVakrangeeKendra myRequest = new AsyncMyVakrangeeKendra();
                myRequest.execute();

                ((MyVakrangeeKendraPhotoViewPager) getActivity()).selectFragment(1);
            }
        } catch (Exception e) {
            e.getMessage();
        }


    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void camerapermission() {
        try {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity()
                    ,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } catch (Exception e) {
            e.getMessage();

        }
    }


    private void ImageCapturePopup() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //inflate layout from xml. you must create an xml layout file in res/layout first


            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View layout = inflater.inflate(R.layout.popupaddimage, null);
            builder.setPositiveButton("ok", null);
            builder.setNegativeButton("cancel", null);
            builder.setView(layout);
            builder.setCancelable(false);
            btnCapture = (ImageView) layout.findViewById(R.id.btncapture);
            imageViewa1 = (ImageView) layout.findViewById(R.id.imageView1main);
            //  imageViewa1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            spinner = (Spinner) layout.findViewById(R.id.spinnerCategory);
            final AlertDialog mAlertDialog = builder.create();


            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
            currentDateandTime = sdf.format(new Date());

            mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(final DialogInterface dialog) {

                    Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            try {


                                //String vk = connection.getVkid();
                                getlati = myVKMasterMain.getLatitude();
                                imgetype = (String) spinner.getSelectedItem();

                                if (imageViewa1.getDrawable() == null) {
                                    Toast.makeText(getActivity(), "please Capture image", Toast.LENGTH_SHORT).show();
                                } else if (spinner.getSelectedItemPosition() == 0) {
                                    Toast.makeText(getActivity(), "please Select ", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {

                                    if (imgetype.equals("Frontage Image")) {

                                        byte[] bytea = byteArray;
                                        Frontage = bytea != null ? (EncryptionUtil.encodeBase64(bytea)) : "";
                                        myVKMasterMain.setFrontageThumb(Frontage);

                                    } else {
                                        myVKMasterMain.setFrontageThumb(null);
                                    }
                                    if (imgetype.equals("Left Wall Image")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        LeftWall = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setLeftWallThumb(LeftWall);

                                    } else {
                                        myVKMasterMain.setLeftWallThumb(null);
                                        Log.e("Front Image ", "Null");
                                    }
                                    if (imgetype.equals("Front Wall Image")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        FrontWall = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setFrontWallThumb(FrontWall);

                                    } else {
                                        myVKMasterMain.setFrontWallThumb(null);
                                    }
                                    if (imgetype.equals("Right Wall Image")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        RightWall = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setRightWallThumb(RightWall);

                                    } else {
                                        myVKMasterMain.setRightWallThumb(null);
                                    }
                                    if (imgetype.equals("Back Wall Image")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        BackWall = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setBackWallThumb(BackWall);

                                    } else {
                                        myVKMasterMain.setBackWallThumb(null);
                                    }
                                    if (imgetype.equals("Ceiling Image")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        Ceiling = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setCeilingThumb(Ceiling);

                                    } else {
                                        myVKMasterMain.setCeilingThumb(null);
                                    }
                                    if (imgetype.equals("Floor Image")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        Floor = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setFloorThumb(Floor);

                                    } else {
                                        myVKMasterMain.setFloorThumb(null);
                                    }
                                    if (imgetype.equals("Extra Image 1")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        Extra1 = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setExtraThumb1(Extra1);

                                    } else {
                                        myVKMasterMain.setExtraThumb1(null);

                                    }
                                    if (imgetype.equals("Extra Image 2")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        Extra2 = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setExtraThumb2(Extra2);

                                    } else {
                                        myVKMasterMain.setExtraThumb2(null);
                                    }
                                    if (imgetype.equals("Extra Image 3")) {
                                        byte[] bytea = byteArray;
                                        byte[] encodeByte1 = bytea;
                                        Extra3 = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                                        myVKMasterMain.setExtraThumb3(Extra3);

                                    } else {

                                        myVKMasterMain.setExtraThumb3(null);
                                    }

                                    uploadtoserver();
                                }
                                //DashboardActivity.myVKMaster a=myVKMaster;
                                //DashboardActivity.myVKMaster =myVKMayster;
                                imageViewa1.setImageBitmap(null);
                                spinner.setSelection(0);
                                //  dialog.dismiss();
                            } catch (Exception e) {
                                e.getMessage();
                            }


                            // TODO Do something
/*
                                String imgetype = (String) spinner.getSelectedItem();
                                byte[] encodeByte1 = connection.getImagewithname("frontage");
                                if (imageViewa1.getDrawable() == null) {
                                    Toast.makeText(getActivity(), "please Capture image", Toast.LENGTH_SHORT).show();
                                } else if (spinner.getSelectedItemPosition() == 0) {
                                    Toast.makeText(getActivity(), "please Select ", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {

                                    String vk = connection.getVkid();
                                    sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
                                    currentDateandTime = sdf.format(new Date());

                                    byte[] bytea = byteArray;

                                    // connection.insertaddImage(bytea, vk, currentDateandTime, getlati, imgetype);
                                    int id = connection.addImageIndb(new My_vakranggekendra_image(bytea, vk, currentDateandTime, getlati, imgetype));
                                    if (id == 0) {
                                        Toast.makeText(getActivity(), "Image already added", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }

                                    imageViewa1.setImageBitmap(null);
                                    spinner.setSelection(0);
                                    dialog.dismiss();
                                }
*/
                        }
                    });


                    Button cancle = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    cancle.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // TODO Do something

                            dialog.dismiss();

                        }
                    });
                }
            });
            mAlertDialog.show();


            btnCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = getOutputMediaFile(1);
                    picUri = Uri.fromFile(file); // create
                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                    startActivityForResult(i, CAMERA_CAPTURE);


                  /*  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                    File imageFile = new File(imageFilePath);
                    picUri = Uri.fromFile(imageFile); // convert path to Uri
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    startActivityForResult(takePictureIntent, CAMERA_CAPTURE);*/
                }
            });


        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        SubMenu subMenu1 = menu.addSubMenu("Action Item");

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.addod);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        subMenu1Item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ImageCapturePopup();
                return false;
            }
        });


    }

    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "VakrangeePhoto");

        mediaStorageDir.mkdirs();

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
                Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);


                // Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(picUri));
                imageViewa1.setImageBitmap(bitmapNew);

                ExifInterface exif = new ExifInterface(picUri.getPath());
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotationInDegrees = exifToDegrees(rotation);


                int width = imageViewa1.getDrawable().getIntrinsicWidth();
                int height = imageViewa1.getDrawable().getIntrinsicHeight();
                if (rotation == 6 && rotationInDegrees == 90) {
                    imageViewa1.setVisibility(View.GONE);
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    imageViewa1.setImageDrawable(null);

                } else if (height > width) {
                    imageViewa1.setVisibility(View.GONE);
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    imageViewa1.setImageDrawable(null);

                } else {


                    imageViewa1.setVisibility(View.VISIBLE);


                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    String a = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    byte[] dataa = Base64.decode(a, Base64.DEFAULT);
                    //------------------

                    // byte[] byteArrayaa = byteArrayOutputStream.toByteArray();
                    byteArray = dataa;
                }

            }


//            if (requestCode == CAMERA_CAPTURE) {
//                Uri uri = picUri;
//                Log.d("picUri", uri.toString());
//
//                performCrop(uri);
//
//            } else if (requestCode == PIC_CROP) {
//
//
//                Bundle extras = data.getExtras();
//
//                if (extras == null) {
//                    Uri uri = picUri;
//                    Log.d("picUri", uri.toString());
//                    InputStream image_stream = getActivity().getContentResolver().openInputStream(picUri);
//                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(picUri));
//                    imageViewa1.setImageBitmap(bitmap);
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
//                    byte[] byteArrayaa = stream.toByteArray();
//                    byteArray = byteArrayaa;
//
//                } else {
//                    Bitmap thePic = (Bitmap) extras.get("data");
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    thePic.compress(Bitmap.CompressFormat.PNG, 60, stream);
//                    byte[] byteArrayaa = stream.toByteArray();
//                    byteArray = byteArrayaa;
//                    imageViewa1.setImageBitmap(thePic);
//                }
////get the cropped bitmap
//
//            } else {
//                Toast.makeText(getActivity(), "image not capture", Toast.LENGTH_LONG).show();
//            }


        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void performCrop(Uri picUri) {
        try {


            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            // cropIntent.setClassName("com.android.camera", "com.android.camera.CropImage");

            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("outputX", 96);
            cropIntent.putExtra("outputY", 96);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("return-data", true);


//start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private class AsyncMyVakrangeeKendra extends AsyncTask<Void, Void, Void> {

        String vkLatitude = EncryptionUtil.encryptString(myVKMasterMain.getLatitude(), getActivity());
        String vkLongitude = EncryptionUtil.encryptString(myVKMasterMain.getLongtitude(), getActivity());

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");


            progress = new ProgressDialog(getActivity());
            progress.setTitle(R.string.updateTiming);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                String checkboxMon;
                String checkboxTue;
                String checkboxWed;
                String checkboxThu;
                String checkboxFri;
                String checkboxSat;
                String checkboxSun;


                String checkm = myVKMasterMain.getMondayTimings();
                if (checkm.equals("uncheck")) {
                    checkboxMon = "Y";
                } else {
                    checkboxMon = "N";
                }

                String checktue = myVKMasterMain.getTuesdayTimings();
                if (checktue.equals("uncheck")) {
                    checkboxTue = "Y";
                } else {
                    checkboxTue = "N";
                }

                String checkWed = myVKMasterMain.getWednesdayTimings();
                if (checkWed.equals("uncheck")) {
                    checkboxWed = "Y";
                } else {
                    checkboxWed = "N";
                }

                String checkthu = myVKMasterMain.getThursdayTimings();
                if (checkthu.equals("uncheck")) {
                    checkboxThu = "Y";
                } else {
                    checkboxThu = "N";
                }
                String checkfri = myVKMasterMain.getFridayTimings();
                if (checkfri.equals("uncheck")) {
                    checkboxFri = "Y";
                } else {
                    checkboxFri = "N";
                }

                String checksat = myVKMasterMain.getSaturdayTimings();
                if (checksat.equals("uncheck")) {
                    checkboxSat = "Y";
                } else {
                    checkboxSat = "N";
                }
                String checksun = myVKMasterMain.getSundayTimings();
                if (checksun.equals("uncheck")) {
                    checkboxSun = "Y";
                } else {
                    checkboxSun = "N";
                }


                String mondayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenmMon(), getActivity());
                String mondayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseMon(), getActivity());
                String tuesdayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenTue(), getActivity());
                String tuesdayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseTue(), getActivity());
                String wednesdayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenWed(), getActivity());
                String wednesdayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseWed(), getActivity());
                String thursdayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenThu(), getActivity());
                String thursdayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseThu(), getActivity());
                String fridayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenFri(), getActivity());
                String fridayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseFri(), getActivity());
                String saturdayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenSat(), getActivity());
                String saturdayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseSat(), getActivity());
                String sundayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenSun(), getActivity());
                String sundayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseSun(), getActivity());


                //byte[] bytea = byteArray;
                // String frontage = bytea != null ? (EncryptionUtil.encodeBase64(bytea)) : "";


                String isMondayClosed = EncryptionUtil.encryptString(checkboxMon, getActivity());
                String isTuesdayClosed = EncryptionUtil.encryptString(checkboxTue, getActivity());
                String isWednesdayClosed = EncryptionUtil.encryptString(checkboxWed, getActivity());
                String isThursdayClosed = EncryptionUtil.encryptString(checkboxThu, getActivity());
                String isFridayClosed = EncryptionUtil.encryptString(checkboxFri, getActivity());
                String isSaturdayClosed = EncryptionUtil.encryptString(checkboxSat, getActivity());
                String isSundayClosed = EncryptionUtil.encryptString(checkboxSun, getActivity());

                String leftWall = myVKMasterMain.getLeftWallThumb();
                String frontage = myVKMasterMain.getFrontageThumb();
                String frontWall = myVKMasterMain.getFrontWallThumb();
                String rightWall = myVKMasterMain.getRightWallThumb();
                String backWall = myVKMasterMain.getBackWallThumb();
                String ceiling = myVKMasterMain.getCeilingThumb();
                String floor = myVKMasterMain.getFloorThumb();
                String extra1 = myVKMasterMain.getExtraThumb1();
                String extra2 = myVKMasterMain.getExtraThumb2();
                String extra3 = myVKMasterMain.getExtraThumb3();


                Connection connection = new Connection(getActivity());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();

                String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());

                String UserID = EncryptionUtil.encryptString(vkid, getActivity());
                String VKID = EncryptionUtil.encryptString(myVKMasterMain.getUserId(), getActivity());


                if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {

                    diplayServerResopnse = WebService.myVakrangeeKendraParticular(UserID, VKID, TokenId, imei, deviceid, simserialnumber,
                            isMondayClosed, isTuesdayClosed, isWednesdayClosed, isThursdayClosed, isFridayClosed, isSaturdayClosed, isSundayClosed,
                            mondayO, mondayC, tuesdayO, tuesdayC, wednesdayO, wednesdayC, thursdayO, thursdayC, fridayO, fridayC, saturdayO, saturdayC,
                            sundayO, sundayC, vkLatitude, vkLongitude, frontage, leftWall, frontWall, rightWall, backWall, ceiling, floor, extra1, extra2, extra3);


                } else {
                    diplayServerResopnse = WebService.myVakrangeeKendra(UserID, TokenId, imei, deviceid, simserialnumber,
                            isMondayClosed, isTuesdayClosed, isWednesdayClosed, isThursdayClosed, isFridayClosed, isSaturdayClosed, isSundayClosed,
                            mondayO, mondayC, tuesdayO, tuesdayC, wednesdayO, wednesdayC, thursdayO, thursdayC, fridayO, fridayC, saturdayO, saturdayC,
                            sundayO, sundayC, vkLatitude, vkLongitude, frontage, leftWall, frontWall, rightWall, backWall, ceiling, floor, extra1, extra2, extra3);

                }

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                e.printStackTrace();


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            // progress.dismiss();
            try {


                if (diplayServerResopnse == null) {

                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));


                    //  Log.e(TAG + "Please Null error", diplayServerResopnse);
                } else if (diplayServerResopnse.equals("Vakrangee Kendra data updated successfully.")) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));
                    String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getActivity());
                    String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getActivity());

                    final String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceid = EncryptionUtil.encryptString(deviceId, getActivity());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                    String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                    String VKID = EncryptionUtil.encryptString(myVKMasterMain.getUserId(), getActivity());
                    if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
                        new AsyncGetmyVakrangeeKendraTimingsResponseParticular(getActivity(), myVKMasterMain.getUserId()).execute(vkidd, VKID, TokenID, imei, deviceid, simserialnumber);

                    } else {
                        new AsyncGetmyVakrangeeKendraTimingsResponse(getActivity()).execute(vkidd, TokenID, imei, deviceid, simserialnumber);

                    }


                    // new AsyncGetmyVakrangeeKendraTimingsResponse(getActivity()).execute(vkidd, TokenID, imei, deviceid, simserialnumber);


                } else if (diplayServerResopnse.equals("My Vakrangee Kendra information added successfully.")) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));
                    String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getActivity());
                    String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getActivity());

                    final String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceid = EncryptionUtil.encryptString(deviceId, getActivity());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                    String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                    String VKID = EncryptionUtil.encryptString(myVKMasterMain.getUserId(), getActivity());

                    if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
                        new AsyncGetmyVakrangeeKendraTimingsResponseParticular(getActivity(), myVKMasterMain.getUserId()).execute(vkidd, VKID, TokenID, imei, deviceid, simserialnumber);

                    } else {
                        new AsyncGetmyVakrangeeKendraTimingsResponse(getActivity()).execute(vkidd, TokenID, imei, deviceid, simserialnumber);

                    }

                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                }
                ((MyVakrangeeKendraPhotoViewPager) getActivity()).viewPager.setCurrentItem(1, true);


            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                e.printStackTrace();
            }

        }

    }

}
