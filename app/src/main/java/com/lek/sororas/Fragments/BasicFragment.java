package com.lek.sororas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lek.sororas.MainActivity;
import com.lek.sororas.R;

public class BasicFragment extends android.support.v4.app.Fragment {

    View view;
    Context context;
    MainActivity main;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;

    public void initFirebase(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();

        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }


}
