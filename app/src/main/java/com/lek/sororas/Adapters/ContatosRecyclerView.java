package com.lek.sororas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lek.sororas.ChatActivity;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Contato;
import com.lek.sororas.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ContatosRecyclerView extends RecyclerView.Adapter{

    private Context context;
    private  ArrayList<Contato> contatos;


    public ContatosRecyclerView(Context context) {
        //this.mDataset = mDataset;
        this.context = context;
    }

    public ContatosRecyclerView(Context context, ArrayList<Contato> contatos) {
        this.contatos = contatos;
        this.context = context;
    }

    public ContatosRecyclerView(){
        //this.mDataset = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_contatos, parent, false);

        final materialViewHolder holder = new materialViewHolder(view);


        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

//        final materialViewHolder materialHolder = (materialViewHolder) holder;
//
//        if(contatos.get(position).getLast() != null)
//            materialHolder.lastMsn.setText(contatos.get(position).getLast());
//        else
//            materialHolder.lastMsn.setVisibility(View.GONE);
//
//
//        materialHolder.name.setText(contatos.get(position).getNome());
//
//
//        if(contatos.get(position).getFoto64() != null){
//
//            if(position < contatos.size()-1)
//                Glide.with(context).load(StringToBitMap(contatos.get(position).getFoto64())).into(materialHolder.imageView);
//
//            else{
//
//                RequestBuilder<Drawable> requestBuilder = Glide.with(context)
//                        .load(StringToBitMap(contatos.get(position).getFoto64()));
//
//                requestBuilder
//                        .load(StringToBitMap(contatos.get(position).getFoto64()))
//                        .listener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//
//                                Animation expandCard = AnimationUtils.loadAnimation(context,R.anim.fade_in);
//                                expandCard.setAnimationListener(new Animation.AnimationListener() {
//                                    @Override
//                                    public void onAnimationStart(Animation animation) {
//                                        MainActivity main = (MainActivity) context;
//                                        main.blankLayout.setVisibility(View.GONE);
//                                        //materialHolder.card.setVisibility(View.VISIBLE);
//                                    }
//
//                                    @Override
//                                    public void onAnimationEnd(Animation animation) {
//
//                                    }
//
//                                    @Override
//                                    public void onAnimationRepeat(Animation animation) {
//
//                                    }
//                                });
//
//                                materialHolder.item.startAnimation(expandCard);
//
//
//
//                                return false;
//                            }
//                        })
//                        .into(materialHolder.imageView);
//
//            }
//
//        }
//
//
//
//
//
//        materialHolder.item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(context,ChatActivity.class);
//
//                Bundle b = new Bundle();
//                b.putString("id",contatos.get(position).getId());
//                b.putString("foto",contatos.get(position).getFoto64());
//                b.putString("nome",contatos.get(position).getNome());
//
//                i.putExtras(b);
//
//                context.startActivity(i);
//            }
//        });
//
//



    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }


    public class materialViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView name;
        final TextView lastMsn;
        final FrameLayout item;

        public materialViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.perfil_photo3);

            name = view.findViewById(R.id.title);
            lastMsn = view.findViewById(R.id.lastmensage);

            item = view.findViewById(R.id.item);




        }

    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);

            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
