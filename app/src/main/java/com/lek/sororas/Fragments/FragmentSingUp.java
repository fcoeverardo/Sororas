package com.lek.sororas.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lek.sororas.LoginActivity;
import com.lek.sororas.R;
import com.lek.sororas.LoginActivity;
import com.lek.sororas.Utils.DateInputMask;

import java.util.concurrent.Executor;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FragmentSingUp extends Fragment {

    private FusedLocationProviderClient mFusedLocationClient;


    private View view;
    Context context;
    LoginActivity main;

    public EditText nome, email, senha, local, dataNascimento;
    public Uri photoPerfil;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        verifyPermission();
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                        }
//                    }
//                });

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



    public void verifyPermission(){

        if (ContextCompat.checkSelfPermission(main,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(main,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }else{

            mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(main, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {


                            Log.i("local","teste");
                        }
                    }
                });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(main, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {

                                        Log.i("local","teste");
                                    }
                                }
                            });

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

}
