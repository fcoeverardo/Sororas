package com.lek.sororas;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lek.sororas.Fragments.FragmentCreateAddPhoto;
import com.lek.sororas.Fragments.FragmentCreateCategoty;
import com.lek.sororas.Fragments.FragmentCreateDescription;
import com.lek.sororas.Fragments.FragmentCreateReview;
import com.lek.sororas.Fragments.FragmentCreateTags;
import com.lek.sororas.Fragments.FragmentCreateTitle;
import com.lek.sororas.Fragments.FragmentCreateType;
import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.User;
import com.lek.sororas.Utils.CurrentUser;
import com.lek.sororas.Utils.FirebaseHelper;
import com.lek.sororas.Utils.ImageHelper;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateActivity extends BasicActivity {


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

        //FirebaseHelper.firebaseInit(db,storage,storageRef,auth);

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

        showProgressDialog();

        anuncio.setProprietaria(db.collection("users").document(mAuth.getCurrentUser().getUid()));
        anuncio.setData(Calendar.getInstance().getTime());

        DocumentReference ref = db.collection("advertisement").document();

        final String anuncioId = ref.getId();
        anuncio.setId(anuncioId);
        try {
            savePhotos(anuncioId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePhotos(String anuncioId) throws IOException {

        for(int i = 0; i < images.size(); i++){

            final String fotoId = anuncioId + "_foto" + i;

            final int j = i;

            fragmentCreateAddPhoto.photos.get(i).setDrawingCacheEnabled(true);
            fragmentCreateAddPhoto.photos.get(i).buildDrawingCache();

            final Bitmap bitmap = ((BitmapDrawable) fragmentCreateAddPhoto.photos.get(i).getDrawable()).getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageRef.child(fotoId).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.w("create", "Error Criando usuario");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    anuncio.getFotos().add(fotoId);
                    DocumentReference ref = db.collection("advertisement").document();
                    ArrayList<String> ids = CurrentUser.getUser().getAnunciosIds();

                    if(ids == null)
                        ids = new ArrayList<>();

                    ids.add(ref.getId());

                    User user = CurrentUser.getUser();
                    user.setAnunciosIds(ids);

                    CurrentUser.setUser(user);
                    db.collection("users").document(mAuth.getCurrentUser().getUid()).set( CurrentUser.getUser());

                    if(j == images.size()-1){

                        ref.set(anuncio)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("create", "Anuncio criado com sucesso");

                                        Toast.makeText(getApplicationContext(),"Anuncio criado com sucesso",Toast.LENGTH_SHORT).show();
                                        hideProgressDialog();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("create", "Error Criando usuario");
                                        Toast.makeText(getApplicationContext(),"Falha na criação do anuncio",Toast.LENGTH_SHORT).show();
                                        hideProgressDialog();
                                    }
                                });
                    }

                }
            });
        }
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

            String[] tabNames = getResources().getStringArray(R.array.tabsNames);
            return tabNames[position];
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
