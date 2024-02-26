package in.vakrangee.franchisee.activity;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardNewActivity extends AppCompatActivity {
/*
//please use this --- public class DashboardNewActivity extends AppCompatActivity implements View.OnClickListener, Drawer.OnDrawerItemClickListener
    private static final String TAG = "DashboardNewActivity";
    private Context context;
    private Drawer drawer = null;
    private AccountHeader headerResult = null;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int CAMERA_CAPTURE = 1;
    int PIC_CROP = 3;
    private PermissionHandler permissionHandler;
    private InternetConnection internetConnection;
    private Connection connection;
    private TelephonyManager telephonyManager;
    private int widthg = 0;
    private final Timer timer = new Timer();
    private final static int DELAY = 10;
    private Uri picUri;
    private String titleCaseValue;
    int k = 0;
    private ProgressDialog progress;
    IProfile profile;
    String getVkid;
    String vkid;
    String vkidd;
    String TokenID;
    String diplayServerResopnse;

    //Drawer
    List<IDrawerItem> iDrawerItems = new LinkedList<>();
    List<ExpandableBadgeDrawerItem> expandableBadgeDrawerItems = new LinkedList<>();

    //Views
    private TextView vlId, vlName;
    private Toolbar toolbar;
    private ImageView imgEditProfile, imgSetProfile;
    private LinearLayout mobilerechagre, dthrechagre, mahavitran, getbillinfo, MyhelpLine, MyRecharge, MyTraction, myvkphoto, btnCustomerProflie, MyOutlettime, Myoutletsa, btnCustomer, bkdisplay, imgprofile;

    //regionDrawer Items with icon
    private final int IDENTIFIER_HOME = 1;
    private final int IDENTIFIER_SERVICES = 2;
    private final int IDENTIFIER_MY_VKMS = 3;
    private final int IDENTIFIER_NEXTGEN_SITE_VISIT = 5;
    private final int IDENTIFIER_NEXTGEN_SITE_COMMENCEMENT = 201;
    private final int IDENTIFIER_NEXTGEN_SITE_WORK_IN_PROGRESS = 202;
    private final int IDENTIFIER_SUPPORT_TICKET = 203;
    private final int IDENTIFIER_NEXTGEN_SITE_COMPLETION_INTIMATION = 205;
    private final int IDENTIFIER_NEXTGEN_SITE_READINESS_VERIFICATION = 204;

    private int[] defaultDrawerItemNames = {R.string.service, R.string.mystatement};
    private int[] defaultDrawerItemIcon = {-1, -1};
    private int[] defaultDrawerItemIdentifier = {IDENTIFIER_SERVICES, IDENTIFIER_MY_VKMS};

    //VA and VL Drawer Items with icon
    private int[] VAVLSpecificDrawerItemIcon = {-1, -1, -1};
    private int[] VAVLSpecificDrawerItemNames = {R.string.next_gen_site_visit, R.string.next_gen_site_commencement, R.string.next_gen_site_work_in_progress};
    private int[] VAVLSpecificDrawerItemIdentifier = {IDENTIFIER_NEXTGEN_SITE_VISIT, IDENTIFIER_NEXTGEN_SITE_COMMENCEMENT, IDENTIFIER_NEXTGEN_SITE_WORK_IN_PROGRESS};
    //endregion

    private LinearLayout layoutOtherServices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_dashboard);

        //Initializing views
        initializeViews();
        layoutOtherServices = findViewById(R.id.layoutOtherServices);

        //Initialize data
        context = this;
        permissionHandler = new PermissionHandler(this);
        connection = new Connection(getApplicationContext());
        internetConnection = new InternetConnection(this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //Permission Check
        locationpermission();
        camerapermission();
        startTimer();

        // Set Image to Profile.
        setProfileImg();
        iniliazeAllDashboardControls();

        try {
            byte[] encodeByte1 = connection.getProfileImage(1);
            Bitmap bitmap = null;

            if (encodeByte1 != null) {
                bitmap = BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                imgSetProfile.setImageBitmap(bitmap);
            }

            profile = new ProfileDrawerItem().withName(getVkid).withEmail(titleCaseValue).withIcon(bitmap).withIdentifier(105);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
            }

            //Create the drawer
            prepareDrawerItems();
            int pixels = CommonUtils.getScreenDensityWidth(this);
            int width = (int) (pixels * 0.65);
            drawer = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar).withDrawerWidthDp(width).withSliderBackgroundColorRes(R.color.navigationcolor)
                    .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                    .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                        @Override
                        public boolean onNavigationClickListener(View clickedView) {
                            DashboardNewActivity.this.finish();
                            return true;
                        }
                    })
                    .addStickyDrawerItems(new SecondaryDrawerItem().withName(R.string.SignOut).withIcon(FontAwesome.Icon.faw_sign_out)).addDrawerItems()
                    .withOnDrawerItemClickListener(this)
                    .withSavedInstance(savedInstanceState)
                    .build();

            for (IDrawerItem iDrawerItem : iDrawerItems) {
                drawer.addItem(iDrawerItem);
            }

            for (ExpandableBadgeDrawerItem expandableBadgeDrawerItema : expandableBadgeDrawerItems) {
                drawer.addItem(expandableBadgeDrawerItema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Hide Other Services In FRANCHISEE MODE
        if (Constants.ENABLE_FRANCHISEE_MODE) {
            layoutOtherServices.setVisibility(View.GONE);
        } else {
            layoutOtherServices.setVisibility(View.VISIBLE);
        }
    }

    public void prepareDrawerItems() {
        iDrawerItems.add(new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(IDENTIFIER_HOME));

        //Set Default Drawer
        if (!Constants.ENABLE_FRANCHISEE_MODE) {
            for (int i = 0; i < defaultDrawerItemNames.length; i++) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(defaultDrawerItemNames[i]).withIcon(defaultDrawerItemIcon[i]).withIdentifier(defaultDrawerItemIdentifier[i]));
            }
        }

        //if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {

            //Set VA VL Drawer Item Drawer
            for (int i = 0; i < VAVLSpecificDrawerItemNames.length; i++) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(VAVLSpecificDrawerItemNames[i]).withIcon(VAVLSpecificDrawerItemIcon[i]).withIdentifier(VAVLSpecificDrawerItemIdentifier[i]));
            }

           *//*  iDrawerItems.add(new SecondaryDrawerItem().withName(R.string.next_gen_site_visit).withIdentifier(5));
            iDrawerItems.add(new SecondaryDrawerItem().withName(R.string.next_gen_site_commencement).withIdentifier(201));     // 201 - Code
            iDrawerItems.add(new SecondaryDrawerItem().withName(R.string.next_gen_site_work_in_progress).withIdentifier(202));     // 202 - Code
           iDrawerItems.add(new SecondaryDrawerItem().withName(R.string.support_ticker).withIdentifier(203));     // 203 - Code
            iDrawerItems.add(new SecondaryDrawerItem().withName(R.string.site_completion_intimation).withIdentifier(205));
            iDrawerItems.add(new SecondaryDrawerItem().withName(R.string.site_readiness_verification).withIdentifier(204));*//*

        //}
    }

    public void startTimer() {
        final Handler handler = new Handler();
        final TimerTask task = new TimerTask() {
            private int counter = 0;

            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                LinearLayout layoutl1 = (LinearLayout) findViewById(R.id.l1);
                                LinearLayout layoutl2 = (LinearLayout) findViewById(R.id.l2);

                                assert layoutl1 != null;
                                int widtha = layoutl1.getWidth();
                                int heighta = layoutl1.getWidth() / 3;
                                int getHeigh = layoutl1.getHeight();

                                assert layoutl2 != null;
                                int width2 = layoutl2.getWidth();
                                widthg = width2;
                                int height2 = layoutl2.getWidth() / 3;
                                int getHeig2 = layoutl2.getHeight();


                                if (getHeigh < heighta) {
                                    LinearLayout.LayoutParams parmsa = new LinearLayout.LayoutParams(widtha, heighta);
                                    layoutl1.setLayoutParams(parmsa);
//

                                    int heightl = layoutl1.getWidth() / 3;
                                    LinearLayout.LayoutParams parmsl = new LinearLayout.LayoutParams(heightl, heightl);
                                    assert mobilerechagre != null;
                                    mobilerechagre.setLayoutParams(parmsl);

                                    assert dthrechagre != null;
                                    dthrechagre.setLayoutParams(parmsl);
                                    assert mahavitran != null;
                                    mahavitran.setLayoutParams(parmsl);


                                }


                                if (getHeig2 < height2) {
                                    LinearLayout.LayoutParams parmsa = new LinearLayout.LayoutParams(width2, height2);
                                    layoutl2.setLayoutParams(parmsa);
//

                                    int heightl = layoutl2.getWidth() / 3;
                                    LinearLayout.LayoutParams parmsl = new LinearLayout.LayoutParams(heightl, heightl);
                                    assert getbillinfo != null;
                                    getbillinfo.setLayoutParams(parmsl);
                                    if (MyRecharge != null) {
                                        MyRecharge.setLayoutParams(parmsl);
                                    }
                                    MyhelpLine.setLayoutParams(parmsl);

                                    MyTraction.setLayoutParams(parmsl);
                                    myvkphoto.setLayoutParams(parmsl);
                                    btnCustomerProflie.setLayoutParams(parmsl);

                                    MyOutlettime.setLayoutParams(parmsl);
                                    Myoutletsa.setLayoutParams(parmsl);

                                    btnCustomer.setLayoutParams(parmsl);

                                }


                            }
                        }, 10);


                    }
                });
                if (++counter == 1) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, DELAY, DELAY);
    }

    public void initializeViews() {
        mobilerechagre = (LinearLayout) findViewById(R.id.btnmobilerecharge);
        dthrechagre = (LinearLayout) findViewById(R.id.btndthrechagre);
        mahavitran = (LinearLayout) findViewById(R.id.btnMahavitran);
        getbillinfo = (LinearLayout) findViewById(R.id.getbillinfo);
        MyhelpLine = (LinearLayout) findViewById(R.id.btnMyHelpLine);
        MyRecharge = (LinearLayout) findViewById(R.id.btnMyRecharge);
        MyTraction = (LinearLayout) findViewById(R.id.btnMyTraction);
        myvkphoto = (LinearLayout) findViewById(R.id.btnvkphoto);
        btnCustomerProflie = (LinearLayout) findViewById(R.id.btnCustomerProflie);
        MyOutlettime = (LinearLayout) findViewById(R.id.btnMyOutletsTiming);
        Myoutletsa = (LinearLayout) findViewById(R.id.btnChangeMap);
        btnCustomer = (LinearLayout) findViewById(R.id.btnCustomer);
        bkdisplay = (LinearLayout) findViewById(R.id.bkdisplay);
        imgprofile = (LinearLayout) findViewById(R.id.imgprofile);
        imgEditProfile = (ImageView) findViewById(R.id.editprofile);
        imgSetProfile = (ImageView) findViewById(R.id.imagepro);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        //Listeners
        imgSetProfile.setOnClickListener(this);
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "VKMS");

        *//**Create the storage directory if it does not exist*//*
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        *//**Create a media file name*//*
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }
        return mediaFile;
    }

    private void camerapermission() {
        try {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(DashboardNewActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardNewActivity.this, Manifest.permission.CAMERA)) {
                } else {
                    ActivityCompat.requestPermissions(DashboardNewActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
        } catch (Exception e) {
            e.getMessage();
            AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.permissionnoallow));
        }
    }

    private void locationpermission() {
        if (ContextCompat.checkSelfPermission(DashboardNewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardNewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(DashboardNewActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    public void showImgProfileChangeDialog() {
        final Dialog dialog = new Dialog(DashboardNewActivity.this);

        dialog.setContentView(R.layout.userprofile);
        dialog.show();

        TextView txtGallerya = (TextView) dialog.findViewById(R.id.txtGallery);
        TextView txtCapture = (TextView) dialog.findViewById(R.id.txtCapture);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);

        txtCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                File file = getOutputMediaFile(1);
                picUri = Uri.fromFile(file); // create
                i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file

                startActivityForResult(i, CAMERA_CAPTURE);
                dialog.dismiss();

            }
        });

        txtGallerya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent GalIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);
                dialog.dismiss();
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    public void setProfileImg() {
        try {
            byte[] encodeByte1 = connection.getProfileImage(1);
            if (encodeByte1 == null) {
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                imgSetProfile.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniliazeAllDashboardControls() {
        assert btnCustomerProflie != null;
        btnCustomerProflie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);

                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, MyCustomerProflie.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });


        assert myvkphoto != null;
        myvkphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardNewActivity.this, UploadImageActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });


        assert getbillinfo != null;
        getbillinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, MyWalltetViewPager.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

        assert MyTraction != null;
        MyTraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, MyTransactionActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });


        assert MyRecharge != null;
        MyRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, RechargeHistroyActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });


        assert MyhelpLine != null;
        MyhelpLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    //Intent i = new Intent(context, MyhelpLineActivity.class);
                    Intent i = new Intent(DashboardNewActivity.this, MyhelpLineActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }

            }
        });


        assert MyOutlettime != null;
        MyOutlettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, MyoutletTimingActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

        assert Myoutletsa != null;
        Myoutletsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, MyOutletsActitvity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

        assert mobilerechagre != null;
        mobilerechagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, MobileRechargActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

        assert mahavitran != null;
        mahavitran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, MahavitranActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

        assert dthrechagre != null;
        dthrechagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(DashboardNewActivity.this);
                if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
                } else {
                    Intent i = new Intent(DashboardNewActivity.this, DTHRechargeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

        String username = connection.getUsernameVkid();
        vlId = (TextView) findViewById(R.id.vlid);
        vlName = (TextView) findViewById(R.id.vlname);
        try {

            String[] words = username.toString().split(" ");
            StringBuilder sb = new StringBuilder();
            if (words[0].length() > 0) {
                sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                for (int i = 1; i < words.length; i++) {
                    sb.append(" ");
                    sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                }
            }
            titleCaseValue = sb.toString();

        } catch (Exception e) {
            e.getMessage();
        }
        final String vkid = connection.getVkid();
        String name = connection.getUsernameVkid();

        vlId.setText(vkid);

        if (titleCaseValue == null) {
            vlName.setText(name);
        } else {
            vlName.setText(titleCaseValue);
        }

        Crashlytics.setUserIdentifier(vkid);
        Crashlytics.setUserName(vkid);
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.imagepro) {
            showImgProfileChangeDialog();
        }

    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (drawerItem != null) {
            int identifier = (int) drawerItem.getIdentifier();

            if (position == -1) {
                logoutAccount();
            }

            switch (identifier) {
                case IDENTIFIER_HOME:
                    fragmentTransaction.replace(R.id.main, new Fragment_Main_Dashboard());
                    break;

                case IDENTIFIER_SERVICES:
                    switchTOMyServiceActivity();
                    break;

                case IDENTIFIER_MY_VKMS:
                    switchTOMyStatement();
                    break;

                case IDENTIFIER_NEXTGEN_SITE_VISIT:
                    switchTOSiteVisit();
                    break;

                case IDENTIFIER_NEXTGEN_SITE_COMMENCEMENT:
                    switchTOSiteCommencement();
                    break;

                case IDENTIFIER_NEXTGEN_SITE_WORK_IN_PROGRESS:
                    switchToNextGenWorkInProgress();
                    break;

                case IDENTIFIER_SUPPORT_TICKET:
                    Intent intent = new Intent(context, SupportTicketActivity.class);
                    intent.putExtra("IsBackVisible", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;

                case IDENTIFIER_NEXTGEN_SITE_READINESS_VERIFICATION:
                    switcingTOLocationDetails(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
                    break;

                case IDENTIFIER_NEXTGEN_SITE_COMPLETION_INTIMATION:
                    switchTOSiteCompletionIntimation();
                    break;


                default:
                    break;

            }
*//*
            if (drawerItem.getIdentifier() == IDENTIFIER_HOME) {
                fragmentTransaction.replace(R.id.main, new Fragment_Main_Dashboard());
            }

            if (drawerItem.getIdentifier() == IDENTIFIER_SERVICES) {
                switchTOMyServiceActivity();
            }
            if (drawerItem.getIdentifier() == IDENTIFIER_MY_VKMS) {
                switchTOMyStatement();
            }

            if (drawerItem.getIdentifier() == IDENTIFIER_NEXTGEN_SITE_VISIT) {
                switchTOSiteVisit();
            }


            // Handling Click of NextGen Site Readiness. [11-06-2018 By Dpk]
            if (drawerItem.getIdentifier() == IDENTIFIER_NEXTGEN_SITE_COMMENCEMENT) {
                switchTOSiteCommencement();
            }

            //Work In Progress
            if (drawerItem.getIdentifier() == IDENTIFIER_NEXTGEN_SITE_WORK_IN_PROGRESS) {
                switchToNextGenWorkInProgress();
            }

            //Support Ticket
            if (drawerItem.getIdentifier() == IDENTIFIER_SUPPORT_TICKET) {
                Intent intent = new Intent(context, SupportTicketActivity.class);
                intent.putExtra("IsBackVisible", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            // Site Readiness
            if (drawerItem.getIdentifier() == IDENTIFIER_NEXTGEN_SITE_READINESS_VERIFICATION) {
                switcingTOLocationDetails(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
            }

            // Work Completion Intimation
            if (drawerItem.getIdentifier() == IDENTIFIER_NEXTGEN_SITE_COMPLETION_INTIMATION) {
                switchTOSiteCompletionIntimation();
            }*//*
        }
        return false;
    }

    private void switchTOMyStatement() {
        if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));

        } else {
            Intent intent = new Intent(context, MyStatementActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Connection connection = new Connection(this);
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_CAPTURE) {

                Uri uri = picUri;
                Log.d("picUri", uri.toString());
                performCrop(uri);
            } else if (requestCode == 2) {
                if (data != null) {
                    picUri = data.getData();
                    performCrop(picUri);

                }
            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();

                if (extras == null) {
                    Uri uri = picUri;
                    Log.d("picUri", uri.toString());
                    InputStream image_stream = getContentResolver().openInputStream(picUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picUri));
                    imgSetProfile.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
                    byte[] byteArrayaa = stream.toByteArray();
                    connection.insertIntoProflieImage(1, byteArrayaa);

                } else {
                    Bitmap thePic = (Bitmap) extras.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thePic.compress(Bitmap.CompressFormat.PNG, 60, stream);
                    byte[] byteArrayaa = stream.toByteArray();
                    imgSetProfile.setImageBitmap(thePic);
                    connection.insertIntoProflieImage(1, byteArrayaa);
                }
            } else {
                Toast.makeText(getApplicationContext(), "image not capture", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private void performCrop(Uri picUri) {
        try {


            Intent cropIntent = new Intent("com.android.camera.action.CROP");   //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");       //set crop properties
            cropIntent.putExtra("crop", "true");        //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);          //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);        //retrieve data on return
            cropIntent.putExtra("return-data", true);   //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                try {


                    //update the profile2 and set a new image.
                    profile.withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_android).backgroundColorRes(R.color.accent).sizeDp(48).paddingDp(4));
                    headerResult.updateProfileByIdentifier(profile);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                    return true;

                } catch (Exception e) {
                    e.getMessage();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            Log.e("My Tags", "onBackPressed");
            k++;
            if (k == 1) {
                if (drawer != null)
                    drawer.closeDrawer();
                Toast.makeText(context, "Please press again to exit.", Toast.LENGTH_SHORT).show();
            } else {
                AsyncLogout task = new AsyncLogout();
                task.execute();

                Connection c = new Connection(context);
                c.setTokenIdnull();
                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                Log.e("logout App", "onBackPressed");
            }
        }
    }

    private class AsyncLogout extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            logout();
            return null;
        }

        private void logout() {
            try {
                String vkid = EncryptionUtil.encryptString(getVkid, getApplicationContext());
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String imei = EncryptionUtil.encryptString(telephonyManager.getDeviceId().toString(), getApplicationContext());
                final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());
                String simopertaor = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(), getApplicationContext());

                diplayServerResopnse = WebService.logOutFranchisee(vkid, imei, deviceid, simopertaor);
                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                Log.d(TAG, "Catch" + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Log.e(TAG + "Already Register ", diplayServerResopnse);

            try {

                *//**
     * METHOD: logOutFranchisee
     * RESPONSE:
     * 1. Log Out Successfull. :
     *    ELSE : Warning! Oops, some error encountered. Please try again.
     *//*

                if (diplayServerResopnse == null) {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));

                } else if (diplayServerResopnse.equals("Log Out Successfull.")) {
                    Log.e(TAG + "OKAY ", diplayServerResopnse);

                } else {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    Log.e(TAG + "Issue in Server ", diplayServerResopnse);

                }
            } catch (Exception e) {
                Log.d(TAG, "Error" + e);
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));

            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void switchToNextGenWorkInProgress() {

        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            vkid = connection.getVkid();
            vkidd = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());
            TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getApplicationContext());

            permissionHandler.requestPermission(imgEditProfile, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        DeviceInfo deviceInfo = DeviceInfo.getInstance(context);

                        String imei = EncryptionUtil.encryptString(deviceInfo.getIMEI(), getApplicationContext());
                        String deviceid = EncryptionUtil.encryptString(deviceInfo.getDeviceId(), getApplicationContext());
                        String simserialnumber = EncryptionUtil.encryptString(deviceInfo.getSimNo(), getApplicationContext());

                        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
                        } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {

                            Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
                            intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    }
                }
            });
        }
    }

    public boolean switcingTOLocationDetails(String mode) {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
        } else {
            Connection connection = new Connection(getApplicationContext());
            String vkid = connection.getVkid();
            String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());
            String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getApplicationContext());

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            String imei = EncryptionUtil.encryptString(telephonyManager.getDeviceId(), getApplicationContext());
            String deviceIdget = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());
            String simserialnumber = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(), getApplicationContext());

            if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));

            } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {
                Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
                intent.putExtra("MODE", mode);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {
                new AsyncGetmyVakrangeeKendraTimingsResponseNextGen(context).execute(vkidd, TokenID, imei, deviceid, simserialnumber);
            }
        }
        return false;
    }

    public boolean switchTOSiteCompletionIntimation() {
        //Site Completion Intimation
        switcingTOLocationDetails(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION);
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            // connection.openDatabase();

            String vkid = connection.getVkid();
            String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());
            String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getApplicationContext());

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return true;
            }
            String imei = EncryptionUtil.encryptString(telephonyManager.getDeviceId(), getApplicationContext());
            String deviceIdget = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());
            String simserialnumber = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(),
                    getApplicationContext());

            if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
            } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {

                // Start Kendra Location List - with Mode Work Completion Intimation.
                Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
                intent.putExtra("MODE", Constants.NEXTGEN_WORK_COMPLETION_INTIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {
                // If its franchisee then directly open its Kendra detail.
                // Here franchisee application will be fetch via vkId.
                new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION, context, vkidd).execute(vkidd, vkidd, TokenID, imei, deviceid, simserialnumber);
            }
        }
        return false;
    }

    public boolean switchTOSiteCommencement() {

        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            // connection.openDatabase();

            String vkid = connection.getVkid();
            String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());
            String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getApplicationContext());

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return true;
            }
            String imei = EncryptionUtil.encryptString(telephonyManager.getDeviceId(), getApplicationContext());
            String deviceIdget = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());
            String simserialnumber = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(),
                    getApplicationContext());
            // myVKMaster =new MyVKMaster();

            if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
            } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {
                // Intent intent = new Intent(context, NextGenPhotoViewPager.class);

                Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
                //  Intent intent = new Intent(context, NextGenPhotoViewPager.class);
                intent.putExtra("MODE", Constants.NEXTGEN_SITE_WORK_COMMENCEMENT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {
                // new AsyncGetmyVakrangeeKendraTimingsResponseNextGen(context).execute(vkidd, TokenID, imei, deviceid, simserialnumber);
            }
        }
        return false;
    }

    private void logoutAccount() {
        AsyncLogout task = new AsyncLogout();
        task.execute();

        Connection c = new Connection(context);
        c.setTokenIdnull();
        progress = new ProgressDialog(context);
        progress.setTitle(getResources().getString(R.string.LogoutyourAccount));
        progress.setMessage(getResources().getString(R.string.PleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        Intent i = new Intent(context, MainActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
        finish();
    }

    private void switchTOMyServiceActivity() {
        if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {
            AlertDialogBoxInfo.alertDialogShow(DashboardNewActivity.this, getResources().getString(R.string.internetCheck));
        } else {
            Intent i = new Intent(context, MySerivcesActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    private boolean switchTOSiteVisit() {

        if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));

        } else {
            Connection connection = new Connection(getApplicationContext());
            String vkid = connection.getVkid();
            String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());
            String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getApplicationContext());

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            String imei = EncryptionUtil.encryptString(telephonyManager.getDeviceId(), getApplicationContext());
            String deviceIdget = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());
            String simserialnumber = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(), getApplicationContext());

            if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));

            } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {
                Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {
                new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_SITE_VISIT_ADHOC,DashboardNewActivity.this, vkid).execute(vkid);
                //new AsyncGetmyVakrangeeKendraTimingsResponseNextGen(context).execute(vkidd, TokenID, imei, deviceid, simserialnumber);

            }
        }
        return false;
    }*/
}
