package com.lek.sororas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lek.sororas.Fragments.FragmentSingIn;
import com.lek.sororas.Fragments.FragmentSingUp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    //private FusedLocationProviderClient mFusedLocationClient;


    TabLayout tabLayout;
    ViewPager viewPager;

    String id;

    ProgressDialog mProgressDialog;

    FragmentSingIn fragmentSingIn;
    FragmentSingUp fragmentSingUp;

    private PlaceDetectionClient mPlaceDetectionClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        findViews();
        inicializeTabs();
        viewPager.setOffscreenPageLimit(tabLayout.getTabCount());
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        setCustomFont("roboto_medium.ttf");

    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            Log.i("login","logado");
        else
            Log.i("login","nao logado");

        mGoogleSignInClient.signOut();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public void clickGoogleLogin(View v){

        showProgressDialog();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            //Log.i("login","logou");
            //account.
            login(account);


        } catch (ApiException e) {
            Log.w("login", "signInResult:failed code=" + e.getStatusCode());
            Log.i("login","nao logou");

            Toast.makeText(getApplicationContext(),"Falha no login",Toast.LENGTH_SHORT).show();

        }
    }


    public void findViews(){

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);

    }

    public boolean login(GoogleSignInAccount account){
        boolean sucess = true;

        hideProgressDialog();

        return  sucess;
    }

    public void inicializeTabs(){


        String[] tabNames = getResources().getStringArray(R.array.loginTabsNames);

        for (int i = 0; i < tabNames.length; i++)
            tabLayout.addTab(tabLayout.newTab());
    }

    public void setCustomFont(String font) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    Typeface face = Typeface.createFromAsset(getAssets(), font);

                    ((TextView) tabViewChild).setTypeface(face);
                    ((TextView) tabViewChild).setAllCaps(false);
                }
            }
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Carregando...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;


        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    fragmentSingIn = new FragmentSingIn();
                    return fragmentSingIn;
                case 1:
                    fragmentSingUp = new FragmentSingUp();
                    return fragmentSingUp;

                default:
                    return null;
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {

            String[] tabNames = getResources().getStringArray(R.array.loginTabsNames);

            return tabNames[position];
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
