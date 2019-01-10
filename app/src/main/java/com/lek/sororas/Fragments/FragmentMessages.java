package com.lek.sororas.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lek.sororas.Adapters.AvalicaoesRecyclerView;
import com.lek.sororas.Adapters.ContatosRecyclerView;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Contato;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static android.app.Activity.RESULT_OK;

public class FragmentMessages extends Fragment {


    private View view;
    Context context;
    MainActivity main;

    ArrayList<Contato> contatos;


    FirebaseDatabase database;
    DatabaseReference myRef;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.layout_contatlist, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (MainActivity) context;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        contatos = new ArrayList<>();

        findViews();

        mRecyclerView =  view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        getContatosList();
        //this.onCreate(null);
    }

    public void findViews(){

        progressBar = view.findViewById(R.id.progress);
    }

    public void getContatosList(){

        myRef.child("contats").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                contatos.clear();

                if(dataSnapshot.hasChildren()){

                    for(DataSnapshot contatoSnapshot : dataSnapshot.getChildren()){

                        Contato contato = contatoSnapshot.getValue(Contato.class);
                        contatos.add(contato);
                    }


                    ContatosRecyclerView adapter = new ContatosRecyclerView(context,contatos,progressBar);
                    mRecyclerView.setAdapter(adapter);

                    //main.blankLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

//    public void getPhotos(){
//
//
//        if(main.fotoPerfil != null)
//            Glide.with(context).load(main.fotoPerfil).into(perfilPhoto);
//        else
//            Glide.with(context).load(R.drawable.ic_account).into(perfilPhoto);
//
//        if(main.fotoBanner != null){
//            RequestBuilder<Drawable> requestBuilder = Glide.with(context)
//                    .load(main.fotoBanner);
//
//            requestBuilder
//                    .load(main.fotoBanner)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                            main.blankLayout.setVisibility(View.GONE);
//
//
//                            Animation fadein = AnimationUtils.loadAnimation(context,R.anim.fadein);
//                            fadein.setAnimationListener(new Animation.AnimationListener() {
//                                @Override
//                                public void onAnimationStart(Animation animation) {
//                                    view.setVisibility(View.VISIBLE);
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animation animation) {
//
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animation animation) {
//
//                                }
//                            });
//                            view.startAnimation(fadein);
//
//                            return false;
//                        }
//                    })
//                    .into(bannerPhoto);
//
//        }
//        else{
//
//            RequestBuilder<Drawable> requestBuilder = Glide.with(context)
//                    .load(R.drawable.bg_default);
//
//            requestBuilder
//                    .load(R.drawable.bg_default)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                            main.blankLayout.setVisibility(View.GONE);
//
//
//                            Animation fadein = AnimationUtils.loadAnimation(context,R.anim.fadein);
//                            fadein.setAnimationListener(new Animation.AnimationListener() {
//                                @Override
//                                public void onAnimationStart(Animation animation) {
//                                    view.setVisibility(View.VISIBLE);
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animation animation) {
//
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animation animation) {
//
//                                }
//                            });
//                            view.startAnimation(fadein);
//
//                            return false;
//                        }
//                    })
//                    .into(bannerPhoto);
//
//        }
//
//
//    }

    public String convertToBase64(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);


        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return base64;

    }

}