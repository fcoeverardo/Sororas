package com.lek.sororas.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.lek.sororas.LoginActivity;
import com.lek.sororas.R;
import com.lek.sororas.LoginActivity;
import com.lek.sororas.Utils.DateInputMask;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FragmentSingUp extends Fragment{


    private View view;
    Context context;
    LoginActivity main;

    public EditText nome,email,senha,local,dataNascimento;
    public Uri photoPerfil;

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

        findViews();

        return view;
    }

    public void findViews(){

        nome = view.findViewById(R.id.createname);
        email = view.findViewById(R.id.createEmail);
        senha = view.findViewById(R.id.createsenha);

        dataNascimento = view.findViewById(R.id.createdate);
        new DateInputMask(dataNascimento);

        local = view.findViewById(R.id.createlocal);

//        dataNascimento.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DialogFragment newFragment = new MyDatePickerFragment();
//                ((MyDatePickerFragment) newFragment).setDisplay(dataNascimento);
//                newFragment.show(main.getSupportFragmentManager(), "date picker");
//
//            }
//        });

    }

    public void writeInformations(GoogleSignInAccount account){

        email.setText(account.getEmail());
        nome.setText(account.getDisplayName());

        photoPerfil = account.getPhotoUrl();

    }



    @Override
    public void onResume() {

        super.onResume();
        //this.onCreate(null);
    }

}
