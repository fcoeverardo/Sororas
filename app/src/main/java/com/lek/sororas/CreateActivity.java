package com.lek.sororas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lek.sororas.Fragments.FragmentCreateAddPhoto;
import com.lek.sororas.Fragments.FragmentCreateCategoty;
import com.lek.sororas.Fragments.FragmentCreateDescription;
import com.lek.sororas.Fragments.FragmentCreateReview;
import com.lek.sororas.Fragments.FragmentCreateTags;
import com.lek.sororas.Fragments.FragmentCreateTitle;
import com.lek.sororas.Fragments.FragmentCreateType;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Utils.CurrentUser;
import com.lek.sororas.Utils.FirebaseHelper;


import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;

    ConstraintLayout addPhotoLayout;
    Button photoAdvanceBtn;

    ArrayList<ImageView> photos;
    ArrayList<ImageView> deleteBtns;
    Dialog dialog;


    FragmentCreateType fragmentCreateType;
    FragmentCreateAddPhoto fragmentCreateAddPhoto;
    FragmentCreateTitle fragmentCreateTitle;
    FragmentCreateDescription fragmentCreateDescription;
    FragmentCreateCategoty fragmentCreateCategoty;
    public FragmentCreateTags fragmentCreateTags;
    FragmentCreateReview fragmentCreateReview;

    public ArrayList<Uri> images;
    ViewPager viewPager;
    public Anuncio anuncio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();

        FirebaseHelper.firebaseInit(db,storage,storageRef,auth);

        images = new ArrayList<>();
        photos = new ArrayList<>();
        deleteBtns = new ArrayList<>();

        anuncio = new Anuncio();

        viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(0);
        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), 7);
        viewPager.setAdapter(adapter);

        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        findViews();


    }

    public void findViews(){


    }

    public void nextPage(View v){

        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    public void previusPage(View v){

        if(viewPager.getCurrentItem() != 0)
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        else{
            finish();
        }
    }


    public void salvarAnuncio(View v){

        anuncio.setProprietaria(FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.collection("advertisement").document(CurrentUser.getUser().getId())
                .set(anuncio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("create", "Anuncio criado com sucesso");

                        Toast.makeText(getApplicationContext(),"Anuncio criado com sucesso",Toast.LENGTH_SHORT).show();
                        //CurrentUser.setUser(user);
                       // toMainActivity();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("create", "Error Criando usiario");
                        Toast.makeText(getApplicationContext(),"Falha na criação do anuncio",Toast.LENGTH_SHORT).show();

                    }
                });
        finish();

    }

    @Override
    public void onBackPressed() {

        if(viewPager.getCurrentItem() != 0)
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        else{
            finish();
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

            switch (position){

                case 0:
                    fragmentCreateType = new FragmentCreateType();
                    return fragmentCreateType;
                case 1:
                    fragmentCreateAddPhoto = new FragmentCreateAddPhoto();
                    return fragmentCreateAddPhoto;

                case 2:
                    fragmentCreateTitle = new FragmentCreateTitle();
                    return fragmentCreateTitle;

                case 3:
                    fragmentCreateDescription = new FragmentCreateDescription();
                    return fragmentCreateDescription;

                case 4:
                    fragmentCreateCategoty = new FragmentCreateCategoty();
                    return fragmentCreateCategoty;

                case 5:
                    fragmentCreateTags = new FragmentCreateTags();
                    return fragmentCreateTags;

                case 6:
                    fragmentCreateReview = new FragmentCreateReview();
                    return fragmentCreateReview;
                default:
                    return null;
            }

        }
        @Override
        public CharSequence getPageTitle(int position) {

            //String[] tabNames = getResources().getStringArray(R.array.tabsNames);
            return "Wololo";
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
