package com.lek.sororas.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lek.sororas.Models.User;
import com.lek.sororas.R;

public class FirebaseHelper {

    static FirebaseFirestore db;
    static FirebaseStorage storage;
    static StorageReference storageRef;

    static User user;

    public static void firebaseDbInit(){

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();

        db.setFirestoreSettings(settings);
    }

    public static User getUserById(String id){

        firebaseDbInit();

        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                        user = document.toObject(User.class);
                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                    } else {
                        Log.d("getUser", "No such document");
                    }
                } else {
                    Log.d("getUser", "get failed with ", task.getException());
                }
            }
        });


        return user;

    }


    public static void setPhotoInImageView(Context context, String id, ImageView imageView){

        storageRef = FirebaseStorage.getInstance().getReference().child(id + "_peril");

        Glide.with(context)
                .load(storageRef)
                .into(imageView);


    }

}
