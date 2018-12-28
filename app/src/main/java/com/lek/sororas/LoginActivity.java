package com.lek.sororas;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.lek.sororas.Fragments.FragmentSingIn;
import com.lek.sororas.Fragments.FragmentSingUp;
import com.lek.sororas.Models.User;
import com.lek.sororas.Utils.CurrentUser;
import com.lek.sororas.Utils.ImageHelper;
import com.lek.sororas.Utils.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends BasicActivity {

    private static final int RC_SIGN_IN = 0;
    private static final int RC_SIGN_UP = 1;

    private static final int NO_PROVIDER = 0;
    private static final int FACEBOOK_PROVIDER = 1;
    private static final int GOOGLE_PROVIDER = 2;

    private GoogleSignInClient mGoogleSignInClient;

    //private FusedLocationProviderClient mFusedLocationClient;

    TabLayout tabLayout;
    ViewPager viewPager;

    String id;
    int code = 0;


    FragmentSingIn fragmentSingIn;
    FragmentSingUp fragmentSingUp;

    CallbackManager callbackManager;
    ProfileTracker profileTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0)
                    fragmentSingUp.showPassordFields();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);

        setCustomFont("roboto_medium.ttf");

        callbackManager = CallbackManager.Factory.create();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {


                // App code
            }
        };

    }

    @Override
    protected void onStart(){
        super.onStart();

        mGoogleSignInClient.signOut();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleSignInResult(task);

//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//        else if (requestCode == RC_SIGN_UP) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignUpResult(task);
//        }
    }

    public void clickGoogleLogin(View v){

        code = GOOGLE_PROVIDER;

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

                            DocumentReference docRef = db.collection("users").document(user.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                                            User user = document.toObject(User.class);
                                            CurrentUser.setUser(user);

                                            finish();

                                            Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                                        } else {

                                            viewPager.setCurrentItem(1);
                                            hideProgressDialog();
                                            Log.d("getUser", "No such document");
                                        }
                                    } else {
                                        Log.d("getUser", "get failed with ", task.getException());
                                    }
                                }
                            });

                            //toMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void firebaseAuthWithFacebook(AccessToken token){

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            //getUser
                            DocumentReference docRef = db.collection("users").document(user.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                                            User user = document.toObject(User.class);
                                            CurrentUser.setUser(user);

                                            Log.d("getUser", "DocumentSnapshot data: " + document.getData());
                                            finish();

                                        } else {

                                            viewPager.setCurrentItem(1);
                                            hideProgressDialog();

                                            Log.d("getUser", "No such document");
                                        }
                                    } else {
                                        Log.d("getUser", "get failed with ", task.getException());
                                    }
                                }
                            });
                            //ver se tem data de nascimento e local
                            //passa pra singup fragment


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Falha no Login",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            //account.
            fragmentSingUp.writeInformations(account);
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
            viewPager.setCurrentItem(1);
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

    public void clickLoginfacebook(View v){

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));

        code = FACEBOOK_PROVIDER;

        final int code = Integer.parseInt(v.getTag().toString());

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        String id = "";
                                        try {

                                            fragmentSingUp.writeInformations(object);

                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        Log.i("login", "Login facebook sucess");
                                        // Application code
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                        Log.i("login", "Login facebook sucess");


                        if(code == 0){

                            firebaseAuthWithFacebook(loginResult.getAccessToken());

                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.i("login", "Login facebook fail");
                    }
                });

    }

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


                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithEmail:failure", task.getException());

                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Log.e("create", e.getMessage());
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(),"Senha incorreta",Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
                                Log.e("create", e.getMessage());
                            } catch(FirebaseAuthUserCollisionException e) {

                                Log.e("create", e.getMessage());
                            } catch(Exception e) {
                                Log.e("create", e.getMessage());
                            }

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
                                            ImageHelper.urlToByteArray(new URL(photoPerfil.toString()))
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

                                if(mAuth.getCurrentUser() == null){
                                    Toast.makeText(LoginActivity.this, "Email já possui cadastro",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "já possui cadastro",
                                            Toast.LENGTH_SHORT).show();
                                }


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

        User newUser = new User();
        switch (code){
            case NO_PROVIDER:
                createUser(email,password,nome,data,local,fragmentSingUp.photoUri);
                return;

            case FACEBOOK_PROVIDER:
                newUser = new User(nome,email,data,local);
                newUser.setId(mAuth.getUid());

                if(fragmentSingUp.photoUrl != null)
                    try {

                        String fotoId = mAuth.getUid() + "_perfil";
                        storageRef.child(fotoId).putBytes(
                                ImageHelper.urlToByteArray(fragmentSingUp.photoUrl)
                        );

                        newUser.setPhotoPerfil(fotoId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    firebaseCreateUser(newUser);
                //createUser(email,password,nome,data,local,fragmentSingUp.photoUrl);
                return;

            case GOOGLE_PROVIDER:

                newUser = new User(nome,email,data,local);
                newUser.setId(mAuth.getUid());

                if(fragmentSingUp.photoUri != null)
                    try {

                        String fotoId = mAuth.getUid() + "_perfil";
                        storageRef.child(fotoId).putBytes(
                                ImageHelper.urlToByteArray(new URL(fragmentSingUp.photoUri.toString()))
                        );

                        newUser.setPhotoPerfil(fotoId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                firebaseCreateUser(newUser);
                return;
        }

    }

    public void clickSingIn(View v){

        showProgressDialog();

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

                        CurrentUser.setUser(user);
                        finish();

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
