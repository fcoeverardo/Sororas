package com.lek.sororas.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.storage.UploadTask;
import com.lek.sororas.CreateActivity;
import com.lek.sororas.EditActivity;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.R;
import com.lek.sororas.Utils.CurrentUser;
import com.lek.sororas.Utils.FirebaseHelper;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static android.app.Activity.RESULT_OK;

public class FragmentPerfil extends Fragment {


    private View view;
    Context context;
    MainActivity main;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentPerfilTrabalhos tabTrabalho;
    FragmentPerfilAvaliacoes tabAvaliacoes;
    FragmentPerfilFavoritos tabFavoritos;
    Dialog dialog;

    FloatingActionButton create;

    UserEvaluation userEvaluation;
    String imageFilePath;

    public MaterialRatingBar materialRatingBar;
    public TextView evaluationCount;

    ImageView perfilPhoto;
    ImageView bannerPhoto;
    ImageView target;
    ImageView changeBanner,changePerfil;

    ImageView perfilMenu;

    TextView nome,cidade;

    boolean perfil = false;

    static final int OPEN_MEDIA_PICKER = 1;
    static final int OPEN_MEDIA_CAMERA = 2;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 6;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_perfil, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (MainActivity) context;

        findViews();
        inicializeTabs();
        loadAvaliacoes();

        viewPager.setOffscreenPageLimit(tabLayout.getTabCount());
        final PagerAdapter adapter = new PagerAdapter
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        setCustomFont();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position != 0){

                    Animation anim = AnimationUtils.loadAnimation(context,R.anim.scale_fadeout);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                                create.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    if(create.getVisibility() == View.VISIBLE)
                        create.startAnimation(anim);
                }
                else{

                    Animation anim = AnimationUtils.loadAnimation(context,R.anim.scale_fadein);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            create.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    if(create.getVisibility() == View.GONE)
                         create.startAnimation(anim);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        //this.onCreate(null);
    }

    public void findViews(){

        create = view.findViewById(R.id.createBtn);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);

        nome = view.findViewById(R.id.name);
        nome.setText(CurrentUser.getUser().getNome());

        cidade = view.findViewById(R.id.city);
        cidade.setText(CurrentUser.getUser().getCidade());

        perfilPhoto = view.findViewById(R.id.perfil_photo);
        bannerPhoto = view.findViewById(R.id.banner);

        //changeBanner = view.findViewById(R.id.changebannerBtn);
        changePerfil = view.findViewById(R.id.changeperfilBtn);

        materialRatingBar = view.findViewById(R.id.materialRatingBar);
        evaluationCount = view.findViewById(R.id.evaluationCount);

        if(CurrentUser.getUser().perfilPhoto != null){
            Glide.with(context).load(CurrentUser.getUser().perfilPhoto).into(perfilPhoto);
            perfilPhoto.setVisibility(View.VISIBLE);
        }

        if(CurrentUser.getUser().bannerPhoto != null)
            Glide.with(context).load(CurrentUser.getUser().bannerPhoto).into(bannerPhoto);
        //FirebaseHelper.setPhotoInImageView(context,CurrentUser.getUser().getPhotoPerfil(),perfilPhoto);
        //getPhotos();

        View.OnClickListener clickChanggePhoto = new View.OnClickListener(){

             @Override
            public void onClick(View v) {

               perfil = true;
               target = perfilPhoto;
               openDialogAddPhoto();
            }
        };

        View.OnClickListener clickChanggeBanner = new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                perfil = false;
                target = bannerPhoto;
                openDialogAddPhoto();
            }
        };

        changePerfil.setOnClickListener(clickChanggePhoto);
        //changeBanner.setOnClickListener(clickChanggeBanner);

        perfilMenu = view.findViewById(R.id.perfilmenu);
        perfilMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openOptionsMenu(v);
            }
        });

        main.blankLayout.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);


    }

    public void getPhotos(){


        if(main.fotoPerfil != null)
            Glide.with(context).load(main.fotoPerfil).into(perfilPhoto);
        else
            Glide.with(context).load(R.drawable.ic_account).into(perfilPhoto);

        if(main.fotoBanner != null){
            RequestBuilder<Drawable> requestBuilder = Glide.with(context)
                    .load(main.fotoBanner);

            requestBuilder
                    .load(main.fotoBanner)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            main.blankLayout.setVisibility(View.GONE);


                            Animation fadein = AnimationUtils.loadAnimation(context,R.anim.fadein);
                            fadein.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    view.setVisibility(View.VISIBLE);                                                                                                                        
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            view.startAnimation(fadein);

                            return false;
                        }
                    })
                    .into(bannerPhoto);

        }
        else{

            RequestBuilder<Drawable> requestBuilder = Glide.with(context)
                    .load(R.drawable.bg_default);

            requestBuilder
                    .load(R.drawable.bg_default)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            main.blankLayout.setVisibility(View.GONE);


                            Animation fadein = AnimationUtils.loadAnimation(context,R.anim.fadein);
                            fadein.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    view.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            view.startAnimation(fadein);

                            return false;
                        }
                    })
                    .into(bannerPhoto);

        }


    }

    public void inicializeTabs() {

        //tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        String[] tabNames = getResources().getStringArray(R.array.tabsNames);

        for (int i = 0; i < tabNames.length; i++)
            tabLayout.addTab(tabLayout.newTab());


    }

    public void setCustomFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    Typeface face = Typeface.createFromAsset(main.getAssets(), "roboto_light.ttf");

                    ((TextView) tabViewChild).setTypeface(face);
                    ((TextView) tabViewChild).setAllCaps(false);
                }
            }
        }
    }

    public void openDialogAddPhoto(){

        dialog = new Dialog(main);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addphoto);

        Button camera = dialog.findViewById(R.id.camerabtn);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPhotoFromCamera();
            }
        });

        Button galeria = dialog.findViewById(R.id.galeriabtn);
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPhotoFromGallery();
            }
        });

        Button cancel = dialog.findViewById(R.id.cancelarbtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                //openCamera()
            }
        });

        dialog.show();
    }

    public void getPhotoFromCamera(){

        if (ContextCompat.checkSelfPermission(main,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(main,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_USE_CAMERA);
            }
        }else{

            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePicture.resolveActivity(context.getPackageManager()) != null){
                //Create a file to store the image
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(context,"com.lek.sororas.provider", photoFile);
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(takePicture,
                            OPEN_MEDIA_CAMERA);
                }
            }

        }

    }

    public void getPhotoFromGallery(){


        if (ContextCompat.checkSelfPermission(main,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(main,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }else{

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Selecione ate 5 imagens"), OPEN_MEDIA_PICKER);

        }



    }

    public String convertToBase64(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);


        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return base64;

    }

    public void loadAvaliacoes(){

//        myRef.child("userevaluation").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if(dataSnapshot.hasChildren()){
//
//                            userEvaluation = new UserEvaluation((HashMap<String, Object>) dataSnapshot.getValue());
//
//                            tabAvaliacoes.setEvaluations(userEvaluation.getAvalicaoes());
//                            materialRatingBar.setRating(Float.parseFloat(userEvaluation.getMedia()));
//                            evaluationCount.setText("(" + userEvaluation.getAvalicaoes().size() + ")");
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



    }

    public void openOptionsMenu(View v){

        PopupMenu popup = new PopupMenu(context, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.perfil_options, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.alterardados) {

                    Intent intent = new Intent(context,EditActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.alterarfotoperfil) {

                    perfil = true;
                    target = perfilPhoto;
                    openDialogAddPhoto();

                }
                else if (id == R.id.alterarfotobanner) {

                    perfil = false;
                    target = bannerPhoto;
                    openDialogAddPhoto();

                }
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        dialog.dismiss();

        switch(requestCode) {
            case OPEN_MEDIA_PICKER:
                if(resultCode == RESULT_OK){

                    Uri uri = data.getData();
                    setPhoto(uri);

                }

                break;
            case OPEN_MEDIA_CAMERA:
                if(resultCode == RESULT_OK){

                    setPhoto(Uri.fromFile(new File(imageFilePath)));

                }
                break;
        }
    }

    public void setPhoto(final Uri uri){

        RequestBuilder<Drawable>  requestBuilder = Glide.with(context)
                .load(uri);

        requestBuilder
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> t, DataSource dataSource, boolean isFirstResource) {

                        String id = main.mAuth.getUid();
                        if(perfil){
                            id = id+ "_perfil";
                            CurrentUser.getUser().perfilPhoto = uri;
                        }
                        else{
                            id = id+ "_banner";
                            CurrentUser.getUser().bannerPhoto = uri;
                        }




                        final Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] data = baos.toByteArray();

                        main.storageRef.child(id).putBytes(data);
                        main.changesPhoto = true;

                        return false;
                    }
                })
                .into(target);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        dialog.dismiss();

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Selecione ate 5 imagens"), OPEN_MEDIA_PICKER);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_USE_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(takePicture.resolveActivity(context.getPackageManager()) != null){
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(context,"com.lek.sororas.provider", photoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoURI);
                            startActivityForResult(takePicture,
                                    OPEN_MEDIA_CAMERA);
                        }
                    }

                } else {

                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imageFilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;


            try {
                tabTrabalho = FragmentPerfilTrabalhos.class.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            try {
                tabAvaliacoes = FragmentPerfilAvaliacoes.class.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            try {
                tabFavoritos = FragmentPerfilFavoritos.class.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return tabTrabalho;
                case 1:
                    return tabAvaliacoes;

                case 2:
                    return tabFavoritos;

                default:
                    return null;
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {

            String[] tabNames = getResources().getStringArray(R.array.tabsNames);


            return tabNames[position];
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }



}