package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.text.TextUtils;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import android.widget.Toast;

import com.lek.sororas.LoginActivity;
import com.lek.sororas.R;

import com.lek.sororas.Utils.NetworkConnection;


public class FragmentSingIn extends Fragment {


    private View view;
    Context context;
    LoginActivity main;

    public EditText loginEmail,loginPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.layout_singin, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (LoginActivity) context;

        findViews();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        //this.onCreate(null);
    }

    public void findViews() {

        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);

    }

    public boolean validateLoginForm() {
        boolean valid = true;

        if(NetworkConnection.isNetworkConnected(context)){
            String email = loginEmail.getText().toString();
            if (TextUtils.isEmpty(email)) {
                loginEmail.setError("Campo não preenchido");
                valid = false;
            } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                loginEmail.setError("Email inválido!");
                valid = false;
            } else {
                loginEmail.setError(null);
            }

            String password = loginPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                loginPassword.setError("Campo não preenchido");
                valid = false;
            } else {
                loginPassword.setError(null);
            }
        } else{
            valid = false;
            Toast.makeText(context, R.string.auth_no_internet_connection,
                    Toast.LENGTH_SHORT).show();
        }

        return valid;
    }



}
