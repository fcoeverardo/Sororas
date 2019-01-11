package com.lek.sororas.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lek.sororas.Adapters.AnuncioRecyclerView;
import com.lek.sororas.Adapters.TrabalhoRecyclerView;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.R;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.io.ByteArrayOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static android.app.Activity.RESULT_OK;

public class FragmentCategorias extends Fragment {


    private View view;
    Context context;
    MainActivity main;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentPerfilTrabalhos tabTrabalho;
    FragmentPerfilAvaliacoes tabAvaliacoes;
    FragmentPerfilFavoritos tabFavoritos;
    Dialog dialog;

    FirebaseDatabase database;
    DatabaseReference myRef;

    UserEvaluation userEvaluation;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Anuncio> anuncios;
    ArrayList<String> anunciosIds;
    String key;

    ProgressBar progress;

    public MaterialRatingBar materialRatingBar;
    public TextView evaluationCount;

    ImageView perfilPhoto;
    ImageView bannerPhoto;
    ImageView target;
    ImageView changeBanner,changePerfil;
    TextView tvCategorias;
    MaterialSearchBar searchBar;

    TextView count;
    TextView nome,cidade;

    boolean perfil = false;

    static final int OPEN_MEDIA_PICKER = 1;
    static final int OPEN_MEDIA_CAMERA = 2;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 6;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_categoria, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (MainActivity) context;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        anuncios = new ArrayList<>();
        anunciosIds = new ArrayList<>();

        main.blankLayout.setVisibility(View.GONE);
        //main.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        findViews();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        //this.onCreate(null);
    }

    public void findViews(){

        mRecyclerView =  view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(context,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        count = view.findViewById(R.id.resultcont);

        progress = view.findViewById(R.id.progress);
        final String [] categoriasText = getResources().getStringArray(R.array.categorias);
        tvCategorias = view.findViewById(R.id.tvcategorias);

        tvCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchBar.showSuggestionsList();

            }
        });
        searchBar = view.findViewById(R.id.searchBar);
        searchBar.setLastSuggestions(Arrays.asList(categoriasText));

        searchBar.showSuggestionsList();
        //main.blankLayout.setVisibility(View.INVISIBLE);
        //searchBar.icon

        searchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {

                searchBar.setText(categoriasText[position]);
                searchBar.hideSuggestionsList();
                tvCategorias.setText(categoriasText[position]);

                key = categoriasText[position];
                loadAnuncios();


            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });
    }

    public void hideKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)main.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void loadAnuncios(){

        //keyTv.setText(key);
        //toolbarSearch.setVisibility(View.VISIBLE);

        CollectionReference docRef = main.db.collection("advertisement");

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

                    AnuncioRecyclerView adapter = new AnuncioRecyclerView(context,anuncios,anunciosIds,progress);
                    mRecyclerView.setAdapter(adapter);

                    count.setText(anuncios.size() + " resultados encontrados");

                }
            }
        });

    }


}