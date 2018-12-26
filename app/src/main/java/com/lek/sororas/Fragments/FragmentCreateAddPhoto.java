package com.lek.sororas.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lek.sororas.CreateActivity;
import com.lek.sororas.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FragmentCreateAddPhoto extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    CreateActivity main;

    static final int OPEN_MEDIA_PICKER = 1;
    static final int OPEN_MEDIA_CAMERA = 2;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 6;

    Button photoAdvanceBtn;

    public ArrayList<ImageView> photos;
    ArrayList<ImageView> deleteBtns;
    ArrayList<Uri> images;
    Dialog dialog;

    String mCurrentPhotoPath;
    String imageFilePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.create_photo, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (CreateActivity) context;

        images = new ArrayList<>();
        photos = new ArrayList<>();
        deleteBtns = new ArrayList<>();

        findViews();



        return view;
    }

    public void findViews(){

        photos.add((ImageView) view.findViewById(R.id.photo1));
        photos.add((ImageView) view.findViewById(R.id.photo2));
        photos.add((ImageView) view.findViewById(R.id.photo3));
        photos.add((ImageView) view.findViewById(R.id.photo4));
        photos.add((ImageView) view.findViewById(R.id.photo5));

        for(int i =0; i < photos.size();i++)
            photos.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogAddPhoto();
                }
            });

        deleteBtns.add((ImageView) view.findViewById(R.id.delete1));
        deleteBtns.add((ImageView) view.findViewById(R.id.delete2));
        deleteBtns.add((ImageView) view.findViewById(R.id.delete3));
        deleteBtns.add((ImageView) view.findViewById(R.id.delete4));
        deleteBtns.add((ImageView) view.findViewById(R.id.delete5));

        for(int i =0; i < deleteBtns.size();i++)
            deleteBtns.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index = Integer.parseInt((String)v.getTag());

                    //if()
                    images.remove(index);
                    updateImages();
                }
            });

        photoAdvanceBtn = view.findViewById(R.id.photoadvanceBtn);
        photoAdvanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // imagesToStrings();
                main.nextPage(v);
                main.images = images;
            }
        });

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.previusPage(v);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        dialog.dismiss();

        switch(requestCode) {
            case OPEN_MEDIA_PICKER:
                if(resultCode == RESULT_OK){

                    //imageUri =
                    images.add(data.getData());
                    //Glide.with(getApplicationContext()).load(imageUri).into(photoViewer);
                    updateImages();

                }

                break;
            case OPEN_MEDIA_CAMERA:
                if(resultCode == RESULT_OK){

                    //imageFilePath
                    images.add( Uri.fromFile(new File(imageFilePath)));
                    galleryAddPic();
//                    Uri tempUri = getImageUri(context, imageBitmap);
//                    images.add (tempUri);
//
                    updateImages();

                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    public void updateImages(){

        for(int i =0; i < photos.size(); i++){

            photos.get(i).setScaleX(0.4f);
            photos.get(i).setScaleY(0.4f);

            ((FrameLayout)photos.get(i).getParent()).setBackground(context.getDrawable(R.drawable.circularcamerabackground));

            photos.get(i).setImageResource(0);
            photos.get(i).setBackgroundResource(R.drawable.ic_camera2);
            //Glide.with(this).load(R.drawable.ic_camera2).into(photos.get(i));
            //photos.get(i).setBackgroundResource(android.R.color.transparent);
            deleteBtns.get(i).setVisibility(View.GONE);

        }

        for(int i =0; i < images.size(); i++){

            photos.get(i).setScaleX(1f);
            photos.get(i).setScaleY(1f);
            photos.get(i).setScaleType(ImageView.ScaleType.CENTER_CROP);

            ((FrameLayout)photos.get(i).getParent()).setBackgroundColor(context.getResources().getColor(R.color.fundo_branco));
            //photos.get(i).setBackgroundResource(R.drawable.ic_camera2);

            Glide.with(this).load(images.get(i)).apply(new RequestOptions().encodeQuality(100)).into(photos.get(i));

            deleteBtns.get(i).setVisibility(View.VISIBLE);

        }

        dialog.dismiss();

        if(images.size() > 0)
            photoAdvanceBtn.setVisibility(View.VISIBLE);
        else
            photoAdvanceBtn.setVisibility(View.GONE);

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
}