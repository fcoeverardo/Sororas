package com.lek.sororas.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lek.sororas.Models.Evaluation;
import com.lek.sororas.R;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AvalicaoesRecyclerView extends RecyclerView.Adapter{

    private Context context;
    private  ArrayList<Evaluation> evaluations;


    public AvalicaoesRecyclerView(Context context) {
        //this.mDataset = mDataset;
        this.context = context;
    }

    public AvalicaoesRecyclerView(Context context, ArrayList<Evaluation> anuncios) {
        this.evaluations = anuncios;
        this.context = context;
    }

    public AvalicaoesRecyclerView(){
        //this.mDataset = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_avaliacao, parent, false);

        final materialViewHolder holder = new materialViewHolder(view);


        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final materialViewHolder materialHolder = (materialViewHolder) holder;

        //materialHolder.name.setText(evaluations.get(position).getNome());
        materialHolder.comment.setText(evaluations.get(position).getComentario());

        materialHolder.ratingBar.setRating(Float.parseFloat(evaluations.get(position).getNota()));


//        if(evaluations.get(position).getFoto() != null){
//
//            String encodedImage = evaluations.get(position).getFoto();
//
//            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//            RequestBuilder<Drawable> requestBuilder = Glide.with(context)
//                    .load(decodedByte);
//
//            requestBuilder
//                    .load(decodedByte)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//
//                            Animation expandCard = AnimationUtils.loadAnimation(context,R.anim.fade_in);
//                            expandCard.setAnimationListener(new Animation.AnimationListener() {
//                                @Override
//                                public void onAnimationStart(Animation animation) {
//                                    materialHolder.card.setVisibility(View.VISIBLE);
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
//
//                            materialHolder.card.startAnimation(expandCard);
//
//
//
//                            return false;
//                        }
//                    })
//                    .into(materialHolder.imageView);
//
//        }
//
//        else{
//
//            Animation expandCard = AnimationUtils.loadAnimation(context,R.anim.scale_fadein);
//            expandCard.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                    materialHolder.card.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//
//            materialHolder.card.startAnimation(expandCard);
//        }






    }

    @Override
    public int getItemCount() {
        return evaluations.size();
    }


    public class materialViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView name;
        final TextView comment;
        final MaterialRatingBar ratingBar;

        final CardView card;

        public materialViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.perfil_photo3);

            name = view.findViewById(R.id.title);
            comment = view.findViewById(R.id.description);

            ratingBar = view.findViewById(R.id.materialRatingBar2);

            card = view.findViewById(R.id.card);

            card.setVisibility(View.INVISIBLE);

        }

    }

}
