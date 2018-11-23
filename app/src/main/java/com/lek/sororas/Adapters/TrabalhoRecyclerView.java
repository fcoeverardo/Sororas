package com.lek.sororas.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.R;

import java.util.ArrayList;

public class TrabalhoRecyclerView extends RecyclerView.Adapter{

    private Context context;
    private  ArrayList<Anuncio> anuncios;
    private ArrayList<String> anuncioIds;
    private boolean myPerfil = true;


    public TrabalhoRecyclerView(Context context) {
        //this.mDataset = mDataset;
        this.context = context;
    }

    public TrabalhoRecyclerView(Context context, ArrayList<Anuncio> anuncios) {
        this.anuncios = anuncios;
        this.context = context;
    }

    public TrabalhoRecyclerView(Context context, ArrayList<Anuncio> anuncios,ArrayList<String> anuncioIds) {
        this.anuncios = anuncios;
        this.context = context;
        this.anuncioIds = anuncioIds;
    }

    public TrabalhoRecyclerView(Context context, ArrayList<Anuncio> anuncios,ArrayList<String> anuncioIds,Boolean myPerfil) {
        this.anuncios = anuncios;
        this.context = context;
        this.anuncioIds = anuncioIds;
        this.myPerfil = myPerfil;
    }

    public TrabalhoRecyclerView(){
        //this.mDataset = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_trabalho, parent, false);

        final materialViewHolder holder = new materialViewHolder(view);


        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final materialViewHolder materialHolder = (materialViewHolder) holder;

        materialHolder.title.setText(anuncios.get(position).getTitulo());
        materialHolder.description.setText(anuncios.get(position).getDescricao());


        String encodedImage = anuncios.get(position).getFotos().get(0);

        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Glide.with(context).load(decodedByte).into(materialHolder.imageView);

        RequestBuilder<Drawable> requestBuilder = Glide.with(context)
                .load(decodedByte);

        requestBuilder
                .load(decodedByte)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                        Animation expandCard = AnimationUtils.loadAnimation(context,R.anim.fade_in);
                        expandCard.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                materialHolder.card.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        materialHolder.card.startAnimation(expandCard);



                        return false;
                    }
                })
                .into(materialHolder.imageView);


        materialHolder.saibamais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//
//                Intent i = new Intent(context, ShowAnuncioActivity.class);
//                i.putExtra("anuncio",anuncioIds.get(position));
//                i.putExtra("userid",anuncios.get(position).getProprietaria());
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
            }
        });

        if(myPerfil){

            materialHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(context).setTitle("Deletando anúncio")
                            .setMessage("Tem certeza que deseja deletar esse anúncio?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                    DatabaseReference myRef = database.getReference();
//
//                                    myRef.child("advertisement").child(anuncioIds.get(position)).removeValue();
                                }
                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            })
                            .show();

                }
            });
        }
        else
            materialHolder.delete.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }


    public class materialViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView title;
        final TextView description;
        final Button saibamais;
        ImageView delete;

        final CardView card;

        public materialViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.image);

            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);

            card = view.findViewById(R.id.card);

            card.setVisibility(View.INVISIBLE);

            saibamais = view.findViewById(R.id.saibamais);
            delete = view.findViewById(R.id.delete);

        }

    }

}
