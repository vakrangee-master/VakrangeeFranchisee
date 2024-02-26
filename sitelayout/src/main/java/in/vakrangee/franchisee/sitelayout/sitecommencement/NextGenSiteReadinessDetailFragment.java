package in.vakrangee.franchisee.sitelayout.sitecommencement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class NextGenSiteReadinessDetailFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "NextGenProfileFragment";

    FranchiseeDetails franchiseeDetails;
    View view;

    Switch switchInterior;
    Switch switchGlassDoor;
    Switch switchFurniture;
    Switch swtichStorage;
    Switch switchCustomerBench;
    Switch switchAtmEnclosure;
    Switch switchElectricConnectionAvailable;

    /*@BindView(R.id.interiorPhotos) TextView interiorPhotos;
    @BindView(R.id.glassDoorPhoto) TextView glassDoorPhoto;
    @BindView(R.id.furniturePhoto) TextView furniturePhoto;
    @BindView(R.id.storagePhoto) TextView storagePhoto;
    @BindView(R.id.customerBenchPhoto) TextView customerBenchPhoto;
    @BindView(R.id.atmEnclosurePhoto) TextView atmEnclosurePhoto;
    @BindView(R.id.electricBillPhoto) TextView electricBillPhoto;*/

    EditText electricPhase;
    EditText electricSanctionLoad;
    LinearLayout layoutElectricConnectionDetail;

    // Include Layout
    View includeInteriorPhotos;

    ImageView imageView1;                    // Frontage Image
    ImageView imageView2;                    // Left Wall Image
    ImageView imageView3;                    // Front Wall Image
    ImageView imageView4;                    // Right Wall Image
    ImageView imageView5;                    // Back Wall Image
    ImageView imageView6;                    // Ceiling Image
    ImageView imageView7;                    // Floor Image

    View includeGlassDoorPhoto;
    View includeFurniturePhoto;
    View includeStoragePhoto;
    View includeBenchPhoto;
    View includeAtmPhoto;
    View includeElectricBillPhoto;

    final int CAMERA_CAPTURE = 201;     // REQUEST CODE
    private Uri picUri;                 // Picture URI

    private int selectedImageViewId = 0;

    public NextGenSiteReadinessDetailFragment(FranchiseeDetails franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nextgen_site_readiness_detail_fragment, container, false);

        // Bind GUI


        // ButterKnife.bind(this, includeInteriorPhotos);
        switchInterior = view.findViewById(R.id.switchInterior);
        switchGlassDoor = view.findViewById(R.id.switchGlassDoor);
        switchFurniture = view.findViewById(R.id.switchFurniture);
        swtichStorage = view.findViewById(R.id.swtichStorage);
        switchCustomerBench = view.findViewById(R.id.switchCustomerBench);
        switchAtmEnclosure = view.findViewById(R.id.switchAtmEnclosure);
        switchElectricConnectionAvailable = view.findViewById(R.id.switchElectricConnectionAvailable);
        electricPhase = view.findViewById(R.id.electricPhase);
        electricSanctionLoad = view.findViewById(R.id.electricPhase);
        layoutElectricConnectionDetail = view.findViewById(R.id.layoutElectricConnectionDetail);
        includeInteriorPhotos = view.findViewById(R.id.includeInteriorPhotos);

        includeGlassDoorPhoto = view.findViewById(R.id.includeGlassDoorPhoto);
        includeFurniturePhoto = view.findViewById(R.id.includeFurniturePhoto);
        includeStoragePhoto = view.findViewById(R.id.includeStoragePhoto);
        includeBenchPhoto = view.findViewById(R.id.includeBenchPhoto);
        includeAtmPhoto = view.findViewById(R.id.includeAtmPhoto);
        includeElectricBillPhoto = view.findViewById(R.id.includeElectricBillPhoto);

        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        imageView4 = (ImageView) view.findViewById(R.id.imageView4);
        imageView5 = (ImageView) view.findViewById(R.id.imageView5);
        imageView6 = (ImageView) view.findViewById(R.id.imageView6);
        imageView7 = (ImageView) view.findViewById(R.id.imageView7);


        imageView1 = (ImageView) includeInteriorPhotos.findViewById(R.id.imageView1);

        // Hide Layout Electric Connection Detail
        layoutElectricConnectionDetail.setVisibility(View.GONE);

        // Hide Photo/s Icons TextView
       /* interiorPhotos.setVisibility(View.INVISIBLE);
        glassDoorPhoto.setVisibility(View.INVISIBLE);
        furniturePhoto.setVisibility(View.INVISIBLE);
        storagePhoto.setVisibility(View.INVISIBLE);
        customerBenchPhoto.setVisibility(View.INVISIBLE);
        atmEnclosurePhoto.setVisibility(View.INVISIBLE);*/
        //electricBillPhoto.setVisibility(View.INVISIBLE);

        // Add Listener On Switch
        switchInterior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    includeInteriorPhotos.setVisibility(View.VISIBLE);
                } else {
                    includeInteriorPhotos.setVisibility(View.GONE);
                }
            }
        });

        // Add Listeners
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);

        switchGlassDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    includeGlassDoorPhoto.setVisibility(View.VISIBLE);
                } else {
                    includeGlassDoorPhoto.setVisibility(View.GONE);
                }
            }
        });

        switchFurniture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    includeFurniturePhoto.setVisibility(View.VISIBLE);
                } else {
                    includeFurniturePhoto.setVisibility(View.GONE);
                }
            }
        });

        swtichStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    includeStoragePhoto.setVisibility(View.VISIBLE);
                } else {
                    includeStoragePhoto.setVisibility(View.GONE);
                }
            }
        });

        switchCustomerBench.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    includeBenchPhoto.setVisibility(View.VISIBLE);
                } else {
                    includeBenchPhoto.setVisibility(View.GONE);
                }
            }
        });

        switchAtmEnclosure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    includeAtmPhoto.setVisibility(View.VISIBLE);
                } else {
                    includeAtmPhoto.setVisibility(View.GONE);
                }
            }
        });

        switchElectricConnectionAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutElectricConnectionDetail.setVisibility(View.VISIBLE);
                    includeElectricBillPhoto.setVisibility(View.VISIBLE);
                } else {
                    layoutElectricConnectionDetail.setVisibility(View.GONE);
                    includeElectricBillPhoto.setVisibility(View.GONE);
                }
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Frontage Image Clicked.", Toast.LENGTH_SHORT).show();

                File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                picUri = Uri.fromFile(file); // create
                i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                i.putExtra("ImageId", picUri); // set the image file
                startActivityForResult(i, CAMERA_CAPTURE);

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imageView1 || id == R.id.imageView2 || id == R.id.imageView3 || id == R.id.imageView4 || id == R.id.imageView5 || id == R.id.imageView6 || id == R.id.imageView7) {
            File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            picUri = Uri.fromFile(file); // create
            i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
            i.putExtra("ImageId", picUri); // set the image file
            selectedImageViewId = id;
            startActivityForResult(i, CAMERA_CAPTURE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
                Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                // Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(picUri));
                if (selectedImageViewId != 0) {
                    ImageView imageView = (ImageView) view.findViewById(selectedImageViewId);
                    imageView.setImageBitmap(bitmapNew);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
