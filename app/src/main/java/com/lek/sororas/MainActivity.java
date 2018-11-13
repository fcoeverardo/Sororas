package com.lek.sororas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.lek.sororas.Fragments.FragmentHome;
import com.lek.sororas.Models.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //MaterialSearchBar searchBar;
    FrameLayout searchLayout;
    TextView tv;
    Button create;

//    FirebaseDatabase database;
//    public DatabaseReference myRef;
    FirebaseAuth auth;

    public FrameLayout blankLayout;

//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;

    TextView logo,title;
    public Bitmap fotoPerfil,fotoBanner;

    //ArrayList<Anuncio> anuncios;
    public ArrayList<String> anunciosIds;

    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment currentFragment;
    public User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);

        blankLayout = findViewById(R.id.blank);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.LEFT);
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        getCurrentUser();

        setFragment(new FragmentHome());

    }

    public void findViews() throws IllegalAccessException, InstantiationException {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //anuncios = new ArrayList<>();

//        searchBar = findViewById(R.id.searchBar);
//        searchBar.setHint("O que você precisa?");
//        searchLayout = findViewById(R.id.searchLayout);
//
//        tv = findViewById(R.id.textView);
//        tv.setVisibility(View.GONE);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                searchLayout.setVisibility(View.VISIBLE);
//                searchBar.onClick(searchBar);
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        });

//        mRecyclerView =  findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
//
//        mLayoutManager = new GridLayoutManager(this,2);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setFocusable(false);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.LEFT);
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        try {
//            setFragment(FragmentHome.class.newInstance());
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        //navigationView.Navi
    }


    @Override
    public void onResume(){
        super.onResume();



    }

    public void clickCreate(View v){

//        Intent i = new Intent(this,CreateActivity.class);
//        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

//        Intent i = new Intent(this,SearchActivity.class);
//        i.putExtra("tag",tag);
//
//        startActivity(i);


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
            //logo.setVisibility(View.VISIBLE);
            title.setText(getString(R.string.app_name));
            //title.setVisibility(View.GONE);

            //cl = FragmentHome.class;
        } else if (id == R.id.nav_perfil) {

            //logo.setVisibility(View.GONE);
            title.setText("Perfil");
            //title.setVisibility(View.VISIBLE);

           // cl = FragmentPerfil.class;

        } else if (id == R.id.nav_mensages) {

            //logo.setVisibility(View.GONE);
            title.setText("Mensagens");
            //title.setVisibility(View.VISIBLE);

            //cl = FragmentMensagens.class;


        } else if (id == R.id.nav_category) {

            //logo.setVisibility(View.GONE);
            title.setText("Categorias");
            //title.setVisibility(View.VISIBLE);

            //cl = FragmentCategorias.class;

        } else if (id == R.id.nav_support) {

        } else if (id == R.id.nav_configs) {

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
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();

        }


    }

    public void clickCategorias(View v){

//        title.setText("Categorias");
//
//        changeNavigationFragment(FragmentCategorias.class);
//        setFragment(currentFragment);
    }


    public void getCurrentUser(){

//        myRef.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                currentUser = new User((HashMap<String, Object>) dataSnapshot.getValue());
//                CurrentUser.getUser(currentUser);
//
//                updateNavigationView();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        getPhotos();

    }

    public void updateNavigationView(){

        TextView name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        name.setText(currentUser.getNome());

        TextView city = navigationView.getHeaderView(0).findViewById(R.id.user_city);
        city.setText(currentUser.getCidade());

    }

    public void getPhotos(){

//        myRef.child("photos").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.hasChildren()){
//
//                    HashMap<String,String> data = (HashMap<String, String>) dataSnapshot.getValue();
//
//                    User u = CurrentUser.getUser();
//
//                    if(data.get("fotoperfil") != null){
//
//                        String foto64 = data.get("fotoperfil");
//                        fotoPerfil = StringToBitMap(foto64);
//
//                        ImageView foto = navigationView.getHeaderView(0).findViewById(R.id.perfil_photo);
//                        Glide.with(getApplicationContext()).load(fotoPerfil).into(foto);
//
//                        u.setFoto(fotoPerfil);
//                        u.setFotoperfil(foto64);
//                    }
//
//                    if(data.get("fotobanner") != null){
//
//                        String banner64 = data.get("fotobanner");
//                        fotoBanner = StringToBitMap(banner64);
//
//                        ImageView banner = navigationView.getHeaderView(0).findViewById(R.id.banner);
//                        Glide.with(getApplicationContext()).load(fotoBanner).into(banner);
//
//                        u.setBanner(fotoBanner);
//                        u.setFotobanner(banner64);
//                    }
//
//                    CurrentUser.getUser(u);
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


    public void backToLogin(){


        openConfirmaDialog();

    }

    public void openConfirmaDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        FirebaseAuth.getInstance().signOut();

//                        Intent i = new Intent(getApplicationContext(),TesteLoginActivity.class);
//                        startActivity(i);
//                        finish();


                    }
                })
                .setNegativeButton("Cancelar", null);

        builder.create();
        builder.show();
    }
}
