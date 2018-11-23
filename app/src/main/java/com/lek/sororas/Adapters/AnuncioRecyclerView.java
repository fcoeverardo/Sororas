package com.lek.sororas.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.User;
import com.lek.sororas.R;
import com.lek.sororas.Utils.FirebaseHelper;

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

        final Anuncio currentAnuncio = anuncios.get(position);

        DocumentReference ref = currentAnuncio.getProprietaria();
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                materialHolder.userName.setText(user.getNome());
                materialHolder.cidadeTv.setText(user.getCidade());

                FirebaseHelper.setPhotoInImageView(context,user.getPhotoPerfil(),materialHolder.perfilPhoto);
                FirebaseHelper.setPhotoInImageView(context,currentAnuncio.getFotos().get(0)
                        ,materialHolder.imageView);

                Log.d("loadingAnuncio", "Error getting documents: ");
            }
        });

        materialHolder.servicoTv.setText(anuncios.get(position).getTitulo());

        if(anuncios.get(position).getTags() == null)
            materialHolder.userName.setVisibility(View.GONE);
        else
            materialHolder.userName.setText(anuncios.get(position).getTags());

        materialHolder.card.setVisibility(View.VISIBLE);

        final MainActivity main = (MainActivity) context;

        materialHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }


    public class materialViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final ImageView perfilPhoto;
        final TextView servicoTv;
        final TextView cidadeTv;
        final CardView card;
        final ImageView favorito;
        final TextView userName;

        public materialViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.image);
            perfilPhoto = view.findViewById(R.id.perfil_photo);
            favorito = view.findViewById(R.id.favorite);

            servicoTv = view.findViewById(R.id.servicoTv);
            cidadeTv = view.findViewById(R.id.cidadeTv);

            card = view.findViewById(R.id.card);

            card.setVisibility(View.INVISIBLE);

            userName = view.findViewById(R.id.username);

        }

    }

}
