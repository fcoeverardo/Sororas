package com.lek.sororas.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lek.sororas.EditActivity;
import com.lek.sororas.MainActivity;
import com.lek.sororas.R;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FragmentAjuda extends Fragment {


    private View view;
    Context context;
    MainActivity main;

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_ajuda, container, false);
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


    }


}