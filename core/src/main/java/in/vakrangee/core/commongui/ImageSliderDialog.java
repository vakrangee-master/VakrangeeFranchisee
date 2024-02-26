package in.vakrangee.core.commongui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

import in.vakrangee.core.R;

import in.vakrangee.core.commongui.imagegallery.ImageDto;
import in.vakrangee.core.model.My_vakranggekendra_image;
import in.vakrangee.core.model.PhotoDto;
import in.vakrangee.core.utils.CommonUtils;
import in.vakrangee.core.utils.Constants;

public class ImageSliderDialog extends Dialog {

    private static final String TAG = "ImageSliderDialog";
    private Context context;

    //Controls
    private Button btnClose, btnSave;
    private ViewPager viewPager;
    private ImageSliderPagerAdapter imageSliderPagerAdapter;
    private LinearLayout parentLayout;

    //private List<My_vakranggekendra_image> my_vakranggekendra_images;
    private List<Object> my_vakranggekendra_images;
    private ISliderClickHandler iSliderClickHandler;
    private boolean isAllow = false;
    private boolean isAllowChangePhoto = false;

    public interface ISliderClickHandler {
        public void captureClick(int position);

        public void saveClick(List<Object> objectList);
    }

    public void notifyViewPagerAdapter(List<Object> objectList) {
        this.my_vakranggekendra_images = objectList;
        imageSliderPagerAdapter.notifyDataSetChanged();
    }

    public ImageSliderDialog(@NonNull Context context, @NonNull final List<Object> my_vakranggekendra_images, int selectedPos, final ISliderClickHandler iSliderClickHandler) {
        super(context);
        this.context = context;
        this.my_vakranggekendra_images = my_vakranggekendra_images;
        this.iSliderClickHandler = iSliderClickHandler;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_image_slider);

        LinearLayout layout = findViewById(R.id.parentLayout);
        CommonUtils.setDialog(context, layout);
        //clearGlildeCache();

        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + " Close "));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setTypeface(font);
        btnSave.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + context.getResources().getString(R.string.save)));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSliderClickHandler.saveClick(my_vakranggekendra_images);
                dismiss();
            }
        });

        viewPager = findViewById(R.id.viewPager);
        imageSliderPagerAdapter = new ImageSliderPagerAdapter(context, my_vakranggekendra_images, iSliderClickHandler);
        viewPager.setAdapter(imageSliderPagerAdapter);
        viewPager.setCurrentItem(selectedPos);

        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);

        //int pixelsWidth = CommonUtils.getScreenDensityWidth(context);
        //int pixelsHeight = CommonUtils.getScreenDensityHeight(context);
        CommonUtils.setDialog(context, parentLayout);
        /*int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.60);
        getWindow().setLayout(width, height);*/
    }

    public void refresh(List<Object> objectList, int pos) {
        this.my_vakranggekendra_images = objectList;
        imageSliderPagerAdapter = new ImageSliderPagerAdapter(context, my_vakranggekendra_images, iSliderClickHandler);
        viewPager.setAdapter(imageSliderPagerAdapter);
        viewPager.setCurrentItem(pos);
    }

    public void allowRemarks(boolean isAllow) {
        this.isAllow = isAllow;
    }

    public void allowChangePhoto(boolean isAllow) {
        this.isAllowChangePhoto = isAllow;
    }

    //region ImageSlider Adapter
    private class ImageSliderPagerAdapter extends PagerAdapter {
        Context context;
        //int images[];
        List<Object> my_vakranggekendra_images;
        LayoutInflater layoutInflater;
        ISliderClickHandler iSliderClickHandler;
        ImageView imageView;
        Object object;

        public ImageSliderPagerAdapter(Context context, List<Object> my_vakranggekendra_images, ISliderClickHandler iSliderClickHandler) {
            this.context = context;
            this.my_vakranggekendra_images = my_vakranggekendra_images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.iSliderClickHandler = iSliderClickHandler;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return my_vakranggekendra_images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            object = my_vakranggekendra_images.get(position);
            View itemView = layoutInflater.inflate(R.layout.item_image_slider, container, false);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            TextView textViewImageTitle = (TextView) itemView.findViewById(R.id.textViewImageTitle);
            LinearLayout layoutRemarks = (LinearLayout) itemView.findViewById(R.id.layoutRemarks);
            final EditText editTextRemarks = itemView.findViewById(R.id.editTextRemarks);
            editTextRemarks.setTag(object);
            editTextRemarks.addTextChangedListener(new MyTextWatcher(itemView));

            //Change Photo button
            LinearLayout parentLinearlytCaptureButton = itemView.findViewById(R.id.parentLinearlytCaptureButton);
            parentLinearlytCaptureButton.setTag(position);
            parentLinearlytCaptureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    iSliderClickHandler.captureClick(pos);
                }
            });


            if (object instanceof My_vakranggekendra_image) {

                final String imageId = ((My_vakranggekendra_image) object).getImageId();
                boolean IsChangedPhoto = ((My_vakranggekendra_image) object).isChangedPhoto();
                if (TextUtils.isEmpty(imageId) || imageId.equals("0") || IsChangedPhoto)
                    Glide.with(context).asBitmap().load(((My_vakranggekendra_image) object).getImage()).into(imageView);
                else {
                    String imageUrl = Constants.DownloadImageUrl + imageId;
                    Log.d(TAG, "Testing: Image Name: " + ((My_vakranggekendra_image) object).getImgetype() + " Url: " + imageUrl);

                   /* Glide.with(context)
                            .load(imageUrl)
                            .apply(new RequestOptions().signature(new ObjectKey(((My_vakranggekendra_image) object).getImageHash())))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Glide.with(context).asBitmap().load(((My_vakranggekendra_image) object).getImage()).into(imageView);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imageView);*/

                    Glide.with(context)
                            .load(imageUrl)
                            .apply(new RequestOptions().signature(new ObjectKey(((My_vakranggekendra_image) object).getImageHash())))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1000);
                                                Glide.with(context).asBitmap().load(((My_vakranggekendra_image) object).getImage()).into(imageView);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    });
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imageView);


                }
                //imageView.setImageBitmap(((My_vakranggekendra_image) object).getImage());
                textViewImageTitle.setText(((My_vakranggekendra_image) object).getImgetype());
                editTextRemarks.setText(((My_vakranggekendra_image) object).getRemarks());

            } else if (object instanceof ImageDto) {
                imageView.setImageBitmap(((ImageDto) object).getBitmap());
                if (!TextUtils.isEmpty(((ImageDto) object).getBranding_element_name()))
                    textViewImageTitle.setText(((ImageDto) object).getBranding_element_name());
                else
                    textViewImageTitle.setText(((ImageDto) object).getName());
                editTextRemarks.setText(((ImageDto) object).getRemarks());

            } else if (object instanceof PhotoDto) {
                imageView.setImageBitmap(((PhotoDto) object).getBitmap());
                textViewImageTitle.setText(((PhotoDto) object).getName());
                editTextRemarks.setText(((PhotoDto) object).getRemarks());

            } else {
                textViewImageTitle.setText(object.toString());
            }

            // Allow Remarks
            if (isAllow) {
                //editTextRemarks.setVisibility(View.VISIBLE);
                layoutRemarks.setVisibility(View.VISIBLE);
            } else {
                //editTextRemarks.setVisibility(View.GONE);
                layoutRemarks.setVisibility(View.GONE);
            }
            // Allow Change Photo Option
            if (isAllowChangePhoto) {
                parentLinearlytCaptureButton.setVisibility(View.VISIBLE);
            } else {
                parentLinearlytCaptureButton.setVisibility(View.GONE);
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        private class MyTextWatcher implements TextWatcher {

            private View view;

            private MyTextWatcher(View view) {
                this.view = view;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }

            public void afterTextChanged(Editable s) {
                String data = s.toString().trim();

                EditText editText = (EditText) view.findViewById(R.id.editTextRemarks);
                Object obj = (Object) editText.getTag();

                if (obj instanceof My_vakranggekendra_image) {
                    ((My_vakranggekendra_image) obj).setRemarks(data);

                } else if (obj instanceof ImageDto) {
                    ((ImageDto) obj).setRemarks(data);

                }
            }
        }
    }
    //endregion


}
