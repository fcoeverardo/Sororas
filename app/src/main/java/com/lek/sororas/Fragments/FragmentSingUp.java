package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lek.sororas.LoginActivity;
import com.lek.sororas.R;
import com.lek.sororas.LoginActivity;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FragmentSingUp extends Fragment {


    private View view;
    Context context;
    LoginActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.layout_singup, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (LoginActivity) context;

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        //this.onCreate(null);
    }

    public void findViews() {

    }
}
