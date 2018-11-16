package com.lek.sororas.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lek.sororas.CreateActivity;
import com.lek.sororas.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentCreateReview extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    CreateActivity main;


    ArrayList<ImageView> photos;
    ArrayList<Uri> images;
    Dialog dialog;
    TextView title,description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.create_review, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (CreateActivity) context;

        images = main.images;
        photos = new ArrayList<>();

        findViews();



        return view;
    }

    public void findViews(){

        photos.add((ImageView) view.findViewById(R.id.photo1));
        photos.add((ImageView) view.findViewById(R.id.photo2));
        photos.add((ImageView) view.findViewById(R.id.photo3));
        photos.add((ImageView) view.findViewById(R.id.photo4));
        photos.add((ImageView) view.findViewById(R.id.photo5));

        for(int i = 0; i < images.size();i++){

            photos.get(i).setScaleX(0.95f);
            photos.get(i).setScaleY(0.95f);
            photos.get(i).setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(main).load(images.get(i)).into(photos.get(i));
        }


        title = view.findViewById(R.id.title);
        title.setText(main.anuncio.getTitulo());

        description = view.findViewById(R.id.descricao);
        description.setText(main.anuncio.getDescricao());


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.previusPage(v);
            }
        });

    }


}