package com.lek.sororas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lek.sororas.Adapters.AnuncioRecyclerView;
import com.lek.sororas.Adapters.TrabalhoRecyclerView;
import com.lek.sororas.Models.Anuncio;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;

import ss.com.bannerslider.Slider;

public class SearchActivity extends BasicActivity {

    ImageView favorite;
    ImageView back;
    FrameLayout favoriteLayout;
    MaterialSearchView searchView;

    TextView count;

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


    private Slider slider;


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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
