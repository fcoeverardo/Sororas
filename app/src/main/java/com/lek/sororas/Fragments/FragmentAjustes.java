package com.lek.sororas.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lek.sororas.Adapters.AnuncioRecyclerView;
import com.lek.sororas.EditActivity;
import com.lek.sororas.MainActivity;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.Denuncia;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.R;
import com.lek.sororas.ShowAnuncioActivity;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FragmentAjustes extends Fragment {


    private View view;
    Context context;
    MainActivity main;

    ConstraintLayout constraintLayout,layoutHistorico,layoutMensagens,layoutAlertas;
    FirebaseDatabase database;
    DatabaseReference myRef;

    CheckBox mensagens,alertas;
    ProgressBar progress;

    Dialog dialog;

    public MaterialRatingBar materialRatingBar;
    public TextView evaluationCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_ajustes, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (MainActivity) context;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

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

        constraintLayout = view.findViewById(R.id.constraintLayout3);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openEditActivity();
            }
        });

        layoutHistorico = view.findViewById(R.id.constraintLayout5);
        layoutHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogLimpa();
            }
        });

        mensagens = view.findViewById(R.id.checkBox);
        alertas = view.findViewById(R.id.checkBox1);

        layoutMensagens = view.findViewById(R.id.constraintLayout2);
        layoutMensagens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mensagens.setChecked(!mensagens.isChecked());

            }
        });

        layoutAlertas = view.findViewById(R.id.layoutalerta);
        layoutAlertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertas.setChecked(!alertas.isChecked());

            }
        });
    }

    public void openEditActivity(){

        Intent intent = new Intent(context,EditActivity.class);
        startActivity(intent);
    }


    public void openDialogLimpa(){


        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_limpa_historico);

        TextView confirm = dialog.findViewById(R.id.confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getPhotoFromGallery();
                    //Toast.makeText(context,"Histórico Limpo")
                    dialog.dismiss();
                }
            });

        TextView cancel = dialog.findViewById(R.id.confirm2);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPhotoFromGallery();
                dialog.dismiss();
            }
        });


            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

    }


}