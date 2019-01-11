package com.lek.sororas;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lek.sororas.Fragments.FragmentActivityPerfilAvaliacoes;
import com.lek.sororas.Fragments.FragmentActivityPerfilFavoritos;
import com.lek.sororas.Fragments.FragmentActivityPerfilTrabalhos;
import com.lek.sororas.Models.Evaluation;
import com.lek.sororas.Models.User;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.Utils.CurrentUser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ss.com.bannerslider.Slider;

public class PerfilActivity extends BasicActivity {


    TabLayout tabLayout;
    ViewPager viewPager;

    public String id;
    String foto64;
    String currentUserId;
    public ArrayList<String> anunciosIds;

    UserEvaluation userEvaluation;

    FragmentActivityPerfilTrabalhos tabTrabalho;
    FragmentActivityPerfilAvaliacoes tabAvaliacoes;
    FragmentActivityPerfilFavoritos tabFavoritos;

    ImageView favorite;

    ImageView perfilPhoto;
    ImageView bannerPhoto;
    ImageView chat;
    ImageView target;
    ImageView changeBanner,changePerfil;
    ConstraintLayout container;

    Bitmap fotoPerfil;

    TextView nome,cidade;

    public MaterialRatingBar materialRatingBar;
    public TextView evaluationCount;

    Dialog dialog;
    String comment;
    String nota = "0";

    public User perfilUser;

    private Slider slider;

    FirebaseDatabase database;
    public DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        findViews();

        id = getIntent().getStringExtra("id");
        if(mAuth.getCurrentUser()!= null)
            currentUserId = mAuth.getCurrentUser().getUid();

        if(id.equals(currentUserId))
            chat.setVisibility(View.GONE);


        loadUser();

        //this.loadAvaliacoes(id, materialRatingBar, evaluationCount,tabAvaliacoes.);

    }

    public void findViews(){

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);

        nome = findViewById(R.id.name);
        //nome.setText(main.currentUser.getName());

        cidade = findViewById(R.id.city);
        //cidade.setText(main.currentUser.getCity());

        perfilPhoto = findViewById(R.id.perfil_photo);
        bannerPhoto = findViewById(R.id.banner);

        materialRatingBar = findViewById(R.id.materialRatingBar);
        evaluationCount = findViewById(R.id.evaluationCount);

        container = findViewById(R.id.containerChange);

        chat = findViewById(R.id.chat);

        //Glide.with(getApplicationContext()).load(fotoPerfil).into((ImageView) findViewById(R.id.perfil_photo3));
    }

    public void inicializeTabs() {


        String[] tabNames = getResources().getStringArray(R.array.tabsNames);
        for (int i = 0; i < tabNames.length; i++)
            tabLayout.addTab(tabLayout.newTab());

        viewPager.setOffscreenPageLimit(tabLayout.getTabCount());
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), 3);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        //tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setCustomFont();

    }

    public void loadAvaliacoes(){

        myRef.child("userevaluation").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChildren()){

                            userEvaluation = new UserEvaluation((HashMap<String, Object>) dataSnapshot.getValue());
                            tabAvaliacoes.setEvaluations(userEvaluation.getAvalicaoes());

                            if(userEvaluation.getMedia()!= null)
                                materialRatingBar.setRating(Float.parseFloat(userEvaluation.getMedia()));


                            evaluationCount.setText("(" + userEvaluation.getAvalicaoes().size() + ")");

                        }

                        else{
                            materialRatingBar.setRating(0);
                            evaluationCount.setText("(0)");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
                    Typeface face = Typeface.createFromAsset(getAssets(), "roboto_light.ttf");

                    ((TextView) tabViewChild).setTypeface(face);
                    ((TextView) tabViewChild).setAllCaps(false);
                }
            }
        }
    }

    public void addFavorite(View v){

//        if(v.getTag() == "false"){
//
//            favorite.setImageResource(R.drawable.ic_heart);
//            favorite.setColorFilter( getResources().getColor(R.color.vermelho), PorterDuff.Mode.SRC_IN);
//
//            v.setTag("true");
//        }
//
//        else{
//
//            favorite.setImageResource(R.drawable.ic_favorito);
//            favorite.setColorFilter( getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//
//            v.setTag("false");
//        }


        //favorite.setFor(getResources().getColor(R.color.vermelho));
    }

    public void clickBack(View v){

        finish();
    }

    public void saveEvaluation(View v){

            comment = ((EditText)dialog.findViewById(R.id.comment)).getText().toString();

            Evaluation evaluation = new Evaluation(nota,comment, CurrentUser.getUser().getId(), Calendar.getInstance().getTime().toString());

            myRef.child("userevaluation").child(id).child("evaluations")
                    .child(CurrentUser.getUser().getId()).setValue(evaluation);

            dialog.dismiss();

    }

    public void openDialogAvaliar(View view){

        if(mAuth.getCurrentUser() !=null){

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_avaliar2);
//        dialog.setTitle("Title...");

            ((TextView) dialog.findViewById(R.id.name2)).setText(("Avalie " + nome.getText().toString()).toUpperCase());

            MaterialRatingBar materialRatingBar = dialog.findViewById(R.id.materialRatingBar3);
            materialRatingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {

                    nota = rating + "";
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        }
        else{
            Toast.makeText(this,"Você precisa estar logado para avaliar",Toast.LENGTH_SHORT).show();
        }

    }

    public void loadUser(){

        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                        perfilUser = document.toObject(User.class);

                        inicializeTabs();
                        loadAvaliacoes();
                        loadInfos();

                        container.setVisibility(View.VISIBLE);
                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                    }

                } else {
                    Log.d("getUser", "get failed with ", task.getException());
                }
            }
        });

    }

    public void loadInfos(){

        nome.setText(perfilUser.getNome());
        cidade.setText(perfilUser.getCidade());

    }

    public void getPhotos(){

//        myRef.child("photos").child(id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.hasChildren()){
//
//                    HashMap<String,String> data = (HashMap<String, String>) dataSnapshot.getValue();
//
//                    if(data.get("fotoperfil") != null){
//
//                        foto64 = data.get("fotoperfil");
//                        fotoPerfil = StringToBitMap(foto64);
//
//                        Glide.with(getApplicationContext()).load(fotoPerfil).into(perfilPhoto);
//
//                    }
//
//                    if(data.get("fotobanner") != null){
//
//                        String banner64 = data.get("fotobanner");
//                        Bitmap fotoBanner = StringToBitMap(banner64);
//
//                        Glide.with(getApplicationContext()).load(fotoBanner).into(bannerPhoto);
//
//                        RequestBuilder<Drawable> requestBuilder = Glide.with(getApplicationContext())
//                                .load(fotoBanner);
//
//                        requestBuilder
//                                .load(fotoBanner)
//                                .listener(new RequestListener<Drawable>() {
//                                    @Override
//                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                                        //main.blankLayout.setVisibility(View.GONE);
//
//
//                                        Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
//                                        fadein.setAnimationListener(new Animation.AnimationListener() {
//                                            @Override
//                                            public void onAnimationStart(Animation animation) {
//                                                container.setVisibility(View.VISIBLE);
//                                            }
//
//                                            @Override
//                                            public void onAnimationEnd(Animation animation) {
//
//                                            }
//
//                                            @Override
//                                            public void onAnimationRepeat(Animation animation) {
//
//                                            }
//                                        });
//                                        container.startAnimation(fadein);
//
//                                        return false;
//                                    }
//                                })
//                                .into(bannerPhoto);
//                    }
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    public void openChat(View v){

//        saveContat();
//
//        Intent i = new Intent(this,ChatActivity.class);
//
//        Bundle b = new Bundle();
//        b.putString("id",id);
//        b.putString("foto",foto64);
//        b.putString("nome",nome.getText().toString());
//
//        i.putExtras(b);
//
//        startActivity(i);

    }

    public void saveContat(){

//        Contato contato = new Contato(id,nome.getText().toString(),foto64);
//        myRef.child("contats").child(currentUserId).child(id).setValue(contato);
//
//        contato = new Contato(currentUserId,CurrentUser.getUser().getName(),CurrentUser.getUser().getFotoperfil());
//        myRef.child("contats").child(id).child(currentUserId).setValue(contato);

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

    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;

            tabTrabalho = new FragmentActivityPerfilTrabalhos();
            tabAvaliacoes = new FragmentActivityPerfilAvaliacoes();
            tabFavoritos = new FragmentActivityPerfilFavoritos();


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
