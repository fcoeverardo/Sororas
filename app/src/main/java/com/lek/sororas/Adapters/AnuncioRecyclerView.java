package com.lek.sororas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.R;
import com.lek.sororas.Utils.CurrentUser;

import java.util.ArrayList;

public class AnuncioRecyclerView extends RecyclerView.Adapter{

    private Context context;
    private  ArrayList<Anuncio> anuncios;
    private ArrayList<String> anuncioIds;
    private ProgressBar progress;


    public AnuncioRecyclerView(Context context) {
        //this.mDataset = mDataset;
        this.context = context;
    }

    public AnuncioRecyclerView(Context context,ArrayList<Anuncio> anuncios) {
        this.anuncios = anuncios;
        this.context = context;
    }

    public AnuncioRecyclerView(Context context,ArrayList<Anuncio> anuncios,ArrayList<String> anuncioIds) {
        this.anuncios = anuncios;
        this.context = context;
        this.anuncioIds = anuncioIds;
    }

    public AnuncioRecyclerView(Context context,ArrayList<Anuncio> anuncios,ArrayList<String> anuncioIds,ProgressBar progress) {
        this.anuncios = anuncios;
        this.context = context;
        this.anuncioIds = anuncioIds;
        this.progress = progress;
    }

    public AnuncioRecyclerView(Context context,ArrayList<Anuncio> anuncios,ProgressBar progress) {
        this.anuncios = anuncios;
        this.context = context;
        this.progress = progress;
    }

    public AnuncioRecyclerView(){
        //this.mDataset = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_guia, parent, false);

        final materialViewHolder holder = new materialViewHolder(view);


        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final materialViewHolder materialHolder = (materialViewHolder) holder;

        materialHolder.servicoTv.setText(anuncios.get(position).getTitulo());
        String encodedImage = anuncios.get(position).getFotos().get(0);

        if(anuncios.get(position).getTags() == null)
            materialHolder.tags.setVisibility(View.GONE);
        else
            materialHolder.tags.setText(anuncios.get(position).getTags());

        materialHolder.card.setVisibility(View.VISIBLE);

//        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//        RequestBuilder<Drawable> requestBuilder = Glide.with(context)
//                .load(decodedByte);
//
//        requestBuilder
//                .load(decodedByte)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//
//                        Animation expandCard = AnimationUtils.loadAnimation(context,R.anim.fadein);
//                        expandCard.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//                                materialHolder.card.setVisibility(View.VISIBLE);
//
//                                if(progress!= null && position == 0)
//                                    progress.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                    //materialHolder.card.setScaleX(1);
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
//
//                        materialHolder.card.startAnimation(expandCard);
//
//
//
//                        return false;
//                    }
//                })
//                .into(materialHolder.imageView);


        //Glide.with(context).load(decodedByte).into(materialHolder.imageView);

        final MainActivity main = (MainActivity) context;

        materialHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                Intent i = new Intent(context, ShowAnuncioActivity.class);
//                i.putExtra("anuncio",anuncioIds.get(position));
//                i.putExtra("userid",anuncios.get(position).getProprietaria());
//
//                context.startActivity(i);
            }
        });


//        if(CurrentUser.getUser().getFavoritos() != null)
//            if(CurrentUser.getUser().getFavoritos().indexOf(anuncioIds.get(position)) != -1){
//
//                materialHolder.favorito.setImageResource(R.drawable.ic_favoritorosa);
//                materialHolder.favorito.setTag("rosa");
//
//            }
//
//        materialHolder.favorito.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                MainActivity main = (MainActivity) context;
//                String id = anuncioIds.get(position);
//
//                if(v.getTag().equals("branco")){
//
//                    materialHolder.favorito.setColorFilter( context.getResources().getColor(R.color.rosa), PorterDuff.Mode.SRC_IN);
//                    CurrentUser.getUser().getFavoritos().add(id);
//                    main.myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(id).setValue(true);
//
//                    v.setTag("rosa");
//                }
//
//                else{
//
//                    materialHolder.favorito.setColorFilter( context.getResources().getColor(R.color.preto), PorterDuff.Mode.SRC_IN);
//
//                    CurrentUser.getUser().getFavoritos().remove(id);
//                    //favorite.setColorFilter( getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//                    main.myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(id).removeValue();
//
//                    v.setTag("branco");
//                }
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }


    public class materialViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView servicoTv;
        final CardView card;
        final ImageView favorito;
        final TextView tags;

        public materialViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.image);
            favorito = view.findViewById(R.id.favorite);

            servicoTv = view.findViewById(R.id.servicoTv);

            card = view.findViewById(R.id.card);

            card.setVisibility(View.INVISIBLE);

            tags = view.findViewById(R.id.tags);

        }

    }

}
