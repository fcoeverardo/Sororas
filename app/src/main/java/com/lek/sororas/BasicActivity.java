package com.lek.sororas;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lek.sororas.Adapters.AvalicaoesRecyclerView;
import com.lek.sororas.Models.Evaluation;
import com.lek.sororas.Models.User;
import com.lek.sororas.Models.UserEvaluation;
import com.lek.sororas.Utils.CurrentUser;

import java.util.ArrayList;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BasicActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseFirestore db;
    FirebaseStorage storage;
    public StorageReference storageRef;

    ProgressDialog mProgressDialog;

    FirebaseDatabase database;
    public DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFirebase();


    }


    public void initFirebase(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();

        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public User getUserFromFirebase(String id){

        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                        //User user = document.toObject(User.class);
                        CurrentUser.setUser(document.toObject(User.class));

                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                    } else {
                        Log.d("getUser", "No such document");
                    }
                } else {
                    Log.d("getUser", "get failed with ", task.getException());
                }
            }
        });

        return new User();
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

    public void openDialogTermos(View v){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_termos);
//        dialog.setTitle("Title...");

        final TextView confirm = dialog.findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);

    }

    public void loadAvaliacoes(String id, final MaterialRatingBar materialRatingBar, final TextView evaluationCount, final RecyclerView mRecyclerView){

        myRef.child("userevaluation").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChildren()){

                            UserEvaluation userEvaluation = new UserEvaluation((HashMap<String, Object>) dataSnapshot.getValue());
                            setEvaluations(mRecyclerView,userEvaluation.getAvalicaoes());
                            //tabAvaliacoes.setEvaluations(userEvaluation.getAvalicaoes());

                            if(userEvaluation.getMedia()!= null)
                                materialRatingBar.setRating(Float.parseFloat(userEvaluation.getMedia()));


                            evaluationCount.setText("(" + userEvaluation.getAvalicaoes().size() + ")");

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }

    public void setAvaliacoesTexts(String id, final MaterialRatingBar materialRatingBar, final TextView evaluationCount){

        myRef.child("userevaluation").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChildren()){

                            UserEvaluation userEvaluation = new UserEvaluation((HashMap<String, Object>) dataSnapshot.getValue());

                            //tabAvaliacoes.setEvaluations(userEvaluation.getAvalicaoes());

                            if(userEvaluation.getMedia()!= null)
                                materialRatingBar.setRating(Float.parseFloat(userEvaluation.getMedia()));


                            evaluationCount.setText("(" + userEvaluation.getAvalicaoes().size() + ")");

                        }

                        else{
                            materialRatingBar.setRating(0);
                            evaluationCount.setText("(0)");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }

    public void setEvaluations(RecyclerView mRecyclerView, ArrayList<Evaluation> avaliacoes){

        AvalicaoesRecyclerView adapter = new AvalicaoesRecyclerView(this,avaliacoes);
        mRecyclerView.setAdapter(adapter);

    }
}
