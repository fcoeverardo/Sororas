package com.lek.sororas;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lek.sororas.Adapters.AnuncioRecyclerView;
import com.lek.sororas.Adapters.TrabalhoRecyclerView;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Utils.ResizeAnimation;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ss.com.bannerslider.Slider;

public class SearchActivity extends BasicActivity {

    ImageView favorite;
    ImageView back;
    FrameLayout favoriteLayout;
    MaterialSearchView searchView;
    Spinner spinner;

    NavigationView navigationView;

    ConstraintLayout toolbarSearch;

    TextView count,keyTv,filtrar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ProgressBar progress;

    ArrayList<Anuncio> anuncios;
    ArrayList<String> anunciosIds;
    String key;

    String id;
    String userId;
    String foto64;
    String nome;
    String currentUserId;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private Slider slider;

    DrawerLayout drawer;

    ImageView clasificaoarrow;

    HashMap<String,Integer> categoryCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        anuncios = new ArrayList<>();
        anunciosIds = new ArrayList<>();

        initFirebase();
        findViews();

    }


    public void findViews(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.fundo));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        back = findViewById(R.id.back);

        mRecyclerView =  findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);

        progress = findViewById(R.id.progress);

        count = findViewById(R.id.resultcont);
        keyTv = findViewById(R.id.key);

        toolbarSearch = findViewById(R.id.toolbarSearch);
        spinner = findViewById(R.id.spinner);

        filtrar = findViewById(R.id.textView24);
        filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.END);
            }
        });


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.LEFT);
//                if(changesPhoto)
//                    updateNavigationView();
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        clasificaoarrow = navigationView.getHeaderView(0).findViewById(R.id.classificacaoarrow);

        clasificaoarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResizeAnimation animation = new ResizeAnimation(navigationView.getHeaderView(0).findViewById(R.id.classificacaolayout),
                        v,2,300);

                navigationView.getHeaderView(0).findViewById(R.id.classificacaolayout).startAnimation(animation);

            }
        });

    }


    @Override
    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getActionBar().setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusearch, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView =  findViewById(R.id.search_view);

        if(getIntent().getStringExtra("tag") != null){

            key = getIntent().getStringExtra("tag");
            loadAnuncios();
        }
        else{

            searchView.showSearch();
            searchView.setMenuItem(item);

            searchView.setSuggestions(getCategoryTag());

            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    key = query;
                    loadAnuncios();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return true;
                }
            });

            searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                @Override
                public void onSearchViewShown() {

                    back.setVisibility(View.GONE);


                }

                @Override
                public void onSearchViewClosed() {

                    back.setVisibility(View.VISIBLE);
                }
            });
        }


        return true;
    }


    public String[] getCategoryTag(){

        ArrayList<String> all = new ArrayList<>();

        all.addAll(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.categorias))));

        all.addAll(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.moda))));
        all.addAll(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.artesanato))));
        all.addAll(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.servicos))));
        all.addAll(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.veiculos))));
        all.addAll(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.beleza))));

        String[] allString = new String[all.size()];
        allString = all.toArray(allString);

        return allString;
        //return getResources().getStringArray(R.array.categorias);
    }

    public void loadAnuncios(){

        categoryCount = new HashMap<>();

        keyTv.setText(key);
        toolbarSearch.setVisibility(View.VISIBLE);

        CollectionReference docRef = db.collection("advertisement");

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {

                    anuncios.clear();
                    anunciosIds.clear();

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d("loadingAnuncio", document.getId() + " => " + document.getData());
                        Anuncio anuncio = document.toObject(Anuncio.class);

                        if (anuncio.getAllText().contains(key.toLowerCase())) {
                            anuncios.add(anuncio);
                            anuncio.id = document.getId();

                            if(categoryCount.containsKey(anuncio.getCategoria())){

                                int count = categoryCount.get(anuncio.getCategoria()) + 1;

                                categoryCount.put(anuncio.getCategoria(),count);
                            }
                            else
                                categoryCount.put(anuncio.getCategoria(),1);

                            //anunciosIds.add(anuncioSnapshot.getKey());
                        }


                    }

                    AnuncioRecyclerView adapter = new AnuncioRecyclerView(getApplicationContext(),anuncios,anunciosIds,progress);
                    mRecyclerView.setAdapter(adapter);

                    count.setText(anuncios.size() + " resultados encontrados");

                }
            }
        });
    }

    public void clickBack(View v){

        finish();
    }
}
