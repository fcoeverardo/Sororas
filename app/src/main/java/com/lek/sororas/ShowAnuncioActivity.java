package com.lek.sororas;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lek.sororas.Adapters.MainSliderAdapter;
import com.lek.sororas.Fragments.FragmentHome;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.Contato;
import com.lek.sororas.Models.Denuncia;
import com.lek.sororas.Models.User;
import com.lek.sororas.Utils.CurrentAnuncio;
import com.lek.sororas.Utils.CurrentUser;
import com.lek.sororas.Utils.GlideImageLoadingService;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ss.com.bannerslider.Slider;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ShowAnuncioActivity extends BasicActivity {

    ImageView favorite;
    ImageView denuncia;
    ImageView perfilPhoto;
    FrameLayout favoriteLayout;

    Anuncio anuncio;
    ArrayList<Uri> fotos;

    Dialog dialog;

    FrameLayout foreground;
    TextView name,title,descripion,city,tags;

    String anuncioId;
    //ArrayList<String>

    String proprietariaId;
    private Slider slider;

    ImageView defaultPerfil;
    public MaterialRatingBar materialRatingBar;
    public TextView evaluationCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showanuncio);

        anuncio = CurrentAnuncio.getAnuncio();

        Slider.init(new GlideImageLoadingService(this));

        findViews();
        loadAnuncio();

//        if(CurrentUser.getUser().getFavoritosIds() == null)
//            CurrentUser.getUser().setFavoritosIds(new ArrayList<String>());
//        else if(CurrentUser.getUser().getFavoritosIds().indexOf(anuncio.id) != -1){
//
//            favorite.setImageResource(R.drawable.ic_favoritorosa);
//            favorite.setTag("rosa");
//        }


//        denuncia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog = new Dialog(ShowAnuncioActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.dialog_denuncia);
//
//                final EditText comment = dialog.findViewById(R.id.comment);
//
//                Button confirm = dialog.findViewById(R.id.confirmbtn);
//                confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        String text = comment.getText().toString();
//                        myRef.child("denuncias").push().setValue(new Denuncia(id,text));
//                        dialog.dismiss();
//
//                        Toast.makeText(ShowAnuncioActivity.this,"Sua denuncia será analisada",Toast.LENGTH_SHORT).show();
//
//                        //getPhotoFromGallery();
//                    }
//                });
//
//                Button cancel = dialog.findViewById(R.id.cancelarbtn);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//                        //openCamera()
//                    }
//                });
//
//                dialog.show();
//                Window window = dialog.getWindow();
//                window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//
//
//            }
//        });

//        if(CurrentUser.getUser().getFavoritos().indexOf(id) != -1){
//
//            favorite.setImageResource(R.drawable.ic_favoritorosa);
//            favoriteLayout.setTag("rosa");
//
//        }

    }

    public void findViews(){

        slider = findViewById(R.id.banner_slider1);

        perfilPhoto = findViewById(R.id.perfil_photo);
        foreground = findViewById(R.id.foreground);
        name = findViewById(R.id.name);
        descripion = findViewById(R.id.description);
        title = findViewById(R.id.title);

        favorite = findViewById(R.id.imageView8);
        favoriteLayout = findViewById(R.id.favoriteLayout);

        tags = findViewById(R.id.tags);

        defaultPerfil = findViewById(R.id.imageView17);

        materialRatingBar = findViewById(R.id.materialRatingBar4);
        evaluationCount = findViewById(R.id.evaluationCount2);


    }

    public void addFavorite(View v){

        if(mAuth.getCurrentUser() !=null){

            User user = CurrentUser.getUser();

            if(v.getTag().equals("branco")){

                favorite.setImageResource(R.drawable.ic_favorito);

                //favorite.setColorFilter( getResources().getColor(R.color.vermelho), PorterDuff.Mode.SRC_IN);
                if(user.getFavoritosIds() == null)
                    user.setFavoritosIds(new ArrayList<String>());

                user.getFavoritosIds().add(anuncio.id);
                user.setBannerPhoto(null);
                user.setPerfilPhoto(null);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user);
//            myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(id).setValue(true);

                v.setTag("rosa");
            }

            else{

                favorite.setImageResource(R.drawable.ic_heart);

                user.getFavoritosIds().remove(anuncio.id);
                user.setBannerPhoto(null);
                user.setPerfilPhoto(null);

                db.collection("users").document(mAuth.getCurrentUser().getUid()).set( user);
//            //favorite.setColorFilter( getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//            myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoritos").child(id).removeValue();
//
                v.setTag("branco");
            }

            CurrentUser.setUser(user);
        }
        else{
            Toast.makeText(this,"Você precisa estar logado para favoritar",Toast.LENGTH_SHORT).show();
        }
        //favorite.setFor(getResources().getColor(R.color.vermelho));
    }

    public void clickBack(View v){

        finish();
    }

    public void openOptionsMenu(View v){

        PopupMenu popup = new PopupMenu(this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.anuncio_options, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.denunciar) {

                    openDialogDenuncia();
                }
                return true;
            }
        });

        popup.show();
    }

    public void openDialogDenuncia(){

        dialog = new Dialog(ShowAnuncioActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_denuncia);

        final EditText comment = dialog.findViewById(R.id.comment);

        TextView confirm = dialog.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = comment.getText().toString();
                // .child("denuncias").push().setValue(new Denuncia(id,text));
                dialog.dismiss();

                Denuncia denuncia = new Denuncia(anuncio.id,text);

                db.collection("complaints").document(proprietariaId).collection(anuncio.id)
                        .add(denuncia);

                Toast.makeText(ShowAnuncioActivity.this,"Sua denúncia será analisada",Toast.LENGTH_SHORT).show();

                        //getPhotoFromGallery();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }

    public void loadAnuncio(){

        slider.setAdapter(new MainSliderAdapter(anuncio.getFotos()));

        if(anuncio.getTags() != null )
            tags.setText(anuncio.getTags());

        title.setText(anuncio.getTitulo());
        descripion.setText(anuncio.getDescricao());


        DocumentReference ref = anuncio.getProprietaria();
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                User proprietaria = documentSnapshot.toObject(User.class);
                setProprietariaFoto(proprietaria.getId());

                proprietariaId = proprietaria.getId();

                setAvaliacoesTexts(proprietariaId,materialRatingBar, evaluationCount);


                name.setText(proprietaria.getNome());
                city.setText(proprietaria.getCidade());
            }
        });

        city = findViewById(R.id.city);

        if(CurrentUser.getUser().getFavoritosIds() != null && CurrentUser.getUser().getFavoritosIds().contains(anuncio.id)){
            favorite.setImageResource(R.drawable.ic_favorito);
            favoriteLayout.setTag("rosa");
        }

    }

    public void openPefil(View v){

        Intent i = new Intent(getApplicationContext(), PerfilActivity.class);
        i.putExtra("id",proprietariaId);
        startActivity(i);


    }

    public void openChat(View v){

        if(mAuth.getCurrentUser() !=null){

            saveContat();

            Intent i = new Intent(this,ChatActivity.class);
            Bundle b = new Bundle();
            b.putString("id",proprietariaId);

            i.putExtras(b);

            startActivity(i);
        }
        else{
            Toast.makeText(this,"Você precisa estar logado para iniciar um chat",Toast.LENGTH_SHORT).show();
        }
    }

    public void saveContat(){

        myRef.child("contats").child(CurrentUser.getUser().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(proprietariaId)) {

                    DocumentReference proprietaria = anuncio.getProprietaria();
                    Contato contato = new Contato(proprietariaId,"", Calendar.getInstance().getTime().toString());

                    myRef.child("contats").child(CurrentUser.getUser().getId()).child(proprietariaId).setValue(contato);
                    contato = new Contato(mAuth.getCurrentUser().getUid(), "", Calendar.getInstance().getTime().toString());
                    myRef.child("contats").child(proprietariaId).child(CurrentUser.getUser().getId()).setValue(contato);
                    // run some code
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadInfos(Anuncio anuncio){

        final TextView name,title,descripion,city,tags;

        name = findViewById(R.id.name);

        title = findViewById(R.id.title);
        title.setText(anuncio.getTitulo());

        tags = findViewById(R.id.tags);

        if(anuncio.getTags() != null )
            tags.setText(anuncio.getTags());

        descripion = findViewById(R.id.description);
        descripion.setText(anuncio.getDescricao());

        city = findViewById(R.id.city);
        //city.setText(anuncio.get);

     //   String id = anuncio.getProprietaria();

//        myRef.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                User user = new User((HashMap<String,Object>) dataSnapshot.getValue());
//
//                nome = user.getName();
//                name.setText(nome);
//                city.setText(user.getCity());
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        myRef.child("photos").child(id).child("fotoperfil").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                foto64 = dataSnapshot.getValue(String.class);
//
//                byte [] encodeByte=Base64.decode(foto64,Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//
//                ImageView fotoperfil = (ImageView) findViewById(R.id.perfil_photo);
////                fotoperfil.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                        openPefil();
////                    }
////                });
//
//
//                Glide.with(getApplicationContext()).load(bitmap)
//                        .apply(new RequestOptions().dontTransform())
//                        .into(fotoperfil);
//
//                RequestBuilder<Drawable> requestBuilder = Glide.with(getApplicationContext())
//                        .load(bitmap);
//
//                requestBuilder
//                        .load(bitmap)
//                        .listener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                                FrameLayout foreground = findViewById(R.id.foreground);
//                                foreground.setVisibility(View.GONE);
//
//                                return false;
//                            }
//                        })
//                        .apply(new RequestOptions().dontTransform())
//                        .into((ImageView) findViewById(R.id.perfil_photo));
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    public void animateForeground(){

        Animation fadeOut = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                foreground.setVisibility(View.GONE);
                //materialHolder.card.setScaleX(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        foreground.startAnimation(fadeOut);

    }

    public void setProprietariaFoto(String id){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(id + "_perfil").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                RequestBuilder<Drawable> requestBuilder = Glide.with(getApplicationContext())
                        .load(uri);

                requestBuilder
                        .load(uri)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                animateForeground();
                                defaultPerfil.setVisibility(View.GONE);

                                return false;
                            }
                        })
                        .into(perfilPhoto);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                animateForeground();
            }
        });
    }

}
