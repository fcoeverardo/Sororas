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
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lek.sororas.Fragments.FragmentSingIn;
import com.lek.sororas.Fragments.FragmentSingUp;
import com.lek.sororas.Models.User;
import com.lek.sororas.Utils.CurrentUser;
import com.lek.sororas.Utils.ImageHelper;
import com.lek.sororas.Utils.NetworkConnection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    private static final int RC_SIGN_UP = 1;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    //private FusedLocationProviderClient mFusedLocationClient;


    TabLayout tabLayout;
    ViewPager viewPager;

    String id;

    ProgressDialog mProgressDialog;

    FragmentSingIn fragmentSingIn;
    FragmentSingUp fragmentSingUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();

        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope("https://www.googleapis.com/auth/user.birthday.read"))
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
        if(currentUser != null){
            Log.i("login","logado");
            toMainActivity();
        }

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
        else if (requestCode == RC_SIGN_UP) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignUpResult(task);
        }
    }

    public void clickGoogleLogin(View v){

        showProgressDialog();

        int code = Integer.parseInt(v.getTag().toString());

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, code);

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("login", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            toMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            //account.
            firebaseAuthWithGoogle(account);


        } catch (ApiException e) {
            Log.w("login", "signInResult:failed code=" + e.getStatusCode());
            Log.i("login","nao logou");

            Toast.makeText(getApplicationContext(),"Falha no login",Toast.LENGTH_SHORT).show();

            hideProgressDialog();
        }
    }

    private void handleSignUpResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            fragmentSingUp.writeInformations(account);
            hideProgressDialog();

        } catch (ApiException e) {
            Log.w("login", "signInResult:failed code=" + e.getStatusCode());
            Log.i("login","nao logou");

            Toast.makeText(getApplicationContext(),"Falha em obter informações de cadastro",Toast.LENGTH_SHORT).show();

            hideProgressDialog();
        }
    }

    public void findViews(){

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);

    }

//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d("login", "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.i("login","logou");
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.i("login","nao logou");
//                            Toast.makeText(getApplicationContext(),"Falha no login",Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        hideProgressDialog();
//                        // ...
//                    }
//                });
//    }

    public void loginWithEmail(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            hideProgressDialog();
                            Log.d("login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            toMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithEmail:failure", task.getException());

//                            String errorCode = ((FirebaseAuthInvalidUserException)task.getException()).getErrorCode();
//
//                            if(errorCode.equals("ERROR_USER_NOT_FOUND")){
//                                Toast.makeText(LoginActivity.this, "Usuário não encontrado",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            else if(errorCode.equals("INVALID_PASSWORD")){
//                                Toast.makeText(LoginActivity.this, "Senha incorreta",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            else if(errorCode.equals("EMAIL_ALREADY_IN_USE")){
//                                Toast.makeText(LoginActivity.this, "Já existe uma conta associada a esse email",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//
//                                Toast.makeText(LoginActivity.this, "Falha no login",
//                                        Toast.LENGTH_SHORT).show();
//                            }

                            //updateUI(null);


                        }

                        // ...
                    }
                });
    }

    public void createUser(final String email, final String password, final String nome, final String dataNascimento,
                            final String local, final Uri photoPerfil){

        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            hideProgressDialog();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sing In", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            InputStream iStream = null;

                            User newUser = new User(nome,email,dataNascimento,local);
                            newUser.setId(user.getUid());

                            if(photoPerfil != null)
                                try {

                                    String fotoId = user.getUid() + "_perfil";
                                    UploadTask uploadTask = storageRef.child(fotoId).putBytes(
                                            ImageHelper.uriToByteArray(new URL(photoPerfil.toString()))
                                    );

                                    newUser.setPhotoPerfil(fotoId);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            firebaseCreateUser(newUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sing In", "createUserWithEmail:failure", task.getException());

                            //int errorCode = task.getException().getLocalizedMessage();
                            String errorCode = ((FirebaseAuthUserCollisionException)task.getException()).getErrorCode();

                            if(errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")){
                                Toast.makeText(LoginActivity.this, "Email já possui cadastro",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Falha no cadastro",
                                        Toast.LENGTH_SHORT).show();
                            }


                            hideProgressDialog();
                        }

                        // ...
                    }
                });

    }


    public void clickSingUp(View v){

        String email = fragmentSingUp.email.getText().toString();
        String nome = fragmentSingUp.nome.getText().toString();
        String password = fragmentSingUp.senha.getText().toString();
        String data = fragmentSingUp.dataNascimento.getText().toString();
        String local = fragmentSingUp.local.getText().toString();



        createUser(email,password,nome,data,local,fragmentSingUp.photoPerfil);

    }

    public void clickSingIn(View v){

        if(!NetworkConnection.isNetworkConnected(this))
            Toast.makeText(this,"Sem conexão com a internet",Toast.LENGTH_SHORT).show();

        else{
            String email = fragmentSingIn.loginEmail.getText().toString();
            String password = fragmentSingIn.loginPassword.getText().toString();

            loginWithEmail(email,password);
        }

    }

    public void firebaseCreateUser(final User user){

        db.collection("users").document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("create", "Usuario criado com sucesso");

                        CurrentUser.getUser(user);
                        toMainActivity();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("create", "Error adding document", e);
                    }
                });

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

    public void toMainActivity(){

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
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
