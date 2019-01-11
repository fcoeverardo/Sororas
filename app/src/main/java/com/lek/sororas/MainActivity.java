package com.lek.sororas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.lek.sororas.Fragments.FragmentAjustes;
import com.lek.sororas.Fragments.FragmentCategorias;
import com.lek.sororas.Fragments.FragmentHome;
import com.lek.sororas.Fragments.FragmentMessages;
import com.lek.sororas.Fragments.FragmentPerfil;
import com.lek.sororas.Models.User;
import com.lek.sororas.Utils.CurrentUser;

import java.util.ArrayList;

public class MainActivity extends BasicActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //MaterialSearchBar searchBar;
    FrameLayout searchLayout;
    FrameLayout blur;
    TextView tv;
    Button create;

    public FrameLayout blankLayout;

//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;

    TextView logo,title;
    ImageView logo2;
    TextView entrar;
    public Bitmap fotoPerfil,fotoBanner;

    //ArrayList<Anuncio> anuncios;
    public ArrayList<String> anunciosIds;

    TextView name,city;
    ImageView photo,banner;

    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment currentFragment;
    public User currentUser;
    public boolean changesPhoto = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        setFragment(new FragmentHome());

    }

    public void findViews(){

        Toolbar toolbar = findViewById(R.id.toolbar);

        logo2 = findViewById(R.id.imageView20);
        Glide.with(this).load(R.drawable.letring4).into(logo2);
        title = findViewById(R.id.title);

        blankLayout = findViewById(R.id.blank);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.LEFT);
                if(changesPhoto)
                    updateNavigationView();
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        city = navigationView.getHeaderView(0).findViewById(R.id.user_city);
        photo = navigationView.getHeaderView(0).findViewById(R.id.perfil_photo);
        banner = navigationView.getHeaderView(0).findViewById(R.id.banner);
        blur = navigationView.getHeaderView(0).findViewById(R.id.blurlayout);
        entrar = navigationView.getHeaderView(0).findViewById(R.id.entrar);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.closeDrawer(GravityCompat.START);

                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);

            }
        });

        updateNavigationView();

    }


    @Override
    public void onResume(){
        super.onResume();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }


        if(mAuth.getCurrentUser() == null){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_logout);
        }else{

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);

            updateNavigationView();
            setPhotoUriCurrentUser();
        }

    }

    public void clickCreate(View v){

        if(mAuth.getCurrentUser() !=null){

            Intent i = new Intent(this,CreateActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this,"Você precisa estar logado para criar um anuncio",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean checkLogin(){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        return (mAuth.getCurrentUser() != null);

    }

    public void clickCategoria(View v){

        String tag = (String) v.getTag();

        Intent i = new Intent(this,SearchActivity.class);
        i.putExtra("tag",tag);

        startActivity(i);


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        //openBlack()

        Class cl = null;
        currentFragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            logo2.setVisibility(View.VISIBLE);
            title.setText("");

            //title.setVisibility(View.GONE);

            cl = FragmentHome.class;
        } else if (id == R.id.nav_perfil) {

            //logo.setVisibility(View.GONE);
            title.setText("Perfil");
            title.setVisibility(View.VISIBLE);

            logo2.setVisibility(View.INVISIBLE);

            cl = FragmentPerfil.class;

        } else if (id == R.id.nav_mensages) {

            //logo.setVisibility(View.GONE);
            title.setText("Mensagens");
            //title.setVisibility(View.VISIBLE);

            logo2.setVisibility(View.INVISIBLE);

            cl = FragmentMessages.class;


        } else if (id == R.id.nav_category) {

            //logo.setVisibility(View.GONE);
            title.setText("Categorias");
            //title.setVisibility(View.VISIBLE);
            logo2.setVisibility(View.INVISIBLE);
            cl = FragmentCategorias.class;

        } else if (id == R.id.nav_support) {

        } else if (id == R.id.nav_configs) {

            title.setText("Configurações");
            //title.setVisibility(View.VISIBLE);
            logo2.setVisibility(View.INVISIBLE);

            cl = FragmentAjustes.class;

        } else if(id == R.id.nav_exit){

            backToLogin();
        }

        changeNavigationFragment(cl);

        return true;
    }


    public void changeNavigationFragment(Class cl){

        try {

            currentFragment = (Fragment) cl.newInstance();
            blankLayout.setVisibility(View.VISIBLE);

        } catch (Exception e) {

            e.printStackTrace();

        }

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                if(currentFragment != null){

                    setFragment(currentFragment);
                }

                drawer.removeDrawerListener(this);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        // Insert the fragment by replacing any existing fragment
        drawer.closeDrawer(GravityCompat.START);

    }

    public void setFragment(Fragment fragment) {
        if(fragment != null){

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerChange, fragment);
            fragmentTransaction.commit();

        }


    }

    public void clickCategorias(View v){

        Class cl = null;
        currentFragment = null;

        title.setText("Categorias");
        //title.setVisibility(View.VISIBLE);
        logo2.setVisibility(View.INVISIBLE);

        cl = FragmentCategorias.class;
        //changeNavigationFragment(cl);

        try {

            currentFragment = (Fragment) cl.newInstance();
            blankLayout.setVisibility(View.VISIBLE);
            setFragment(currentFragment);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//        title.setText("Categorias");
//
//        changeNavigationFragment(FragmentCategorias.class);
//        setFragment(currentFragment);
    }

    public void getCurrentUser(){

           DocumentReference docRef = db.collection("users").document(mAuth.getUid());
           docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       if (document.exists()) {
                           Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                           //User user = document.toObject(User.class);
                           CurrentUser.setUser(document.toObject(User.class));

                           updateNavigationView();
                           Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                       } else {
                           Log.d("getUser", "No such document");
                       }
                   } else {
                       Log.d("getUser", "get failed with ", task.getException());
                   }
               }
           });

    }

    public void updateNavigationView(){

        if(mAuth.getCurrentUser() != null){

            if(CurrentUser.getUser().getNome() == null ){

                DocumentReference docRef = db.collection("users").document(mAuth.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                                //User user = document.toObject(User.class);
                                CurrentUser.setUser(document.toObject(User.class));
                                CurrentUser.getUser().setId(mAuth.getUid());

                                setTextPerfil();
                                Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                            }

                        } else {
                            Log.d("getUser", "get failed with ", task.getException());
                        }
                    }
                });
            }

            else{
                setTextPerfil();
            }
            //setPhotoUriCurrentUser();
        }
        else{

            blur.setVisibility(View.INVISIBLE);
        }

    }

    public void setTextPerfil(){

        name.setVisibility(View.VISIBLE);
        city.setVisibility(View.VISIBLE);

        entrar.setVisibility(View.GONE);

        User user = CurrentUser.getUser();
        name.setText(CurrentUser.getUser().getNome());
        city.setText(CurrentUser.getUser().getCidade());

        if(CurrentUser.getUser().bannerPhoto != null){
            blur.setVisibility(View.VISIBLE);
        }
        else{
            blur.setVisibility(View.INVISIBLE);
        }

        changesPhoto = false;

    }

    public void backToLogin(){

        openConfirmaDialog();

    }

    public void openConfirmaDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        logOut();

//                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
//                        startActivity(i);
//                        finish();


                    }
                })
                .setNegativeButton("Cancelar", null);

        builder.create();
        builder.show();
    }

    public void logOut(){

        FirebaseAuth.getInstance().signOut();
        //onNavigationItemSelected(navigationView.getMenu().getItem(0));

        CurrentUser.setUser(new User());

        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer_logout);

        name.setVisibility(View.INVISIBLE);
        city.setVisibility(View.INVISIBLE);

        entrar.setVisibility(View.VISIBLE);
        navigationView.getHeaderView(0).findViewById(R.id.perfil_photo).setVisibility(View.GONE);

        Glide.with(this).load(R.drawable.ic_account).into(photo);
        banner.setImageResource(R.drawable.background_perfil);

        logo2.setVisibility(View.VISIBLE);
        title.setText("");

        Toast.makeText(this,"Usuário desconectado",Toast.LENGTH_SHORT).show();
        setFragment(new FragmentHome());

    }

    public void setPhotoUriCurrentUser(){

        storageRef = FirebaseStorage.getInstance().getReference();
        String id = mAuth.getCurrentUser().getUid() + "_perfil";
        storageRef.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                CurrentUser.getUser().perfilPhoto = uri;
                ImageView photo = navigationView.getHeaderView(0).findViewById(R.id.perfil_photo);
                if(CurrentUser.getUser().perfilPhoto != null){
                    Glide.with(getApplicationContext()).load(CurrentUser.getUser().perfilPhoto).into(photo);
                    photo.setVisibility(View.VISIBLE);
                }

                else
                    photo.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("getUser", "DocumentSnapshot data: ");

            }
        });

        id = mAuth.getCurrentUser().getUid() + "_banner";
        storageRef.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                CurrentUser.getUser().bannerPhoto = uri;
                ImageView banner = navigationView.getHeaderView(0).findViewById(R.id.banner);
                if(CurrentUser.getUser().bannerPhoto != null){
                    Glide.with(getApplicationContext()).load(CurrentUser.getUser().bannerPhoto).into(banner);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("getUser", "DocumentSnapshot data: ");

            }
        });

    }
}
