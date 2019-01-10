package com.lek.sororas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.lek.sororas.Adapters.AdapterChatRecycleView;
import com.lek.sororas.Models.Message;
import com.lek.sororas.Models.User;
import com.lek.sororas.Utils.CurrentUser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ChatActivity extends BasicActivity {

    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> users = new ArrayList<>();
    Context context;
    EditText messageET;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;

    ImageView foto;
    TextView contactNameTv;
    String contactId;
    String userId;
    String photo;
    ArrayList<Message> messages = new ArrayList<>();

    ImageView defaultPhoto;
    User contactUser;

    ConstraintLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        findViews();

        Bundle b = this.getIntent().getExtras();
        contactId = b.getString("id");

        loadUser();
//        contactNameTv.setText(b.getString("nome"));
//
//        photo = b.getString("foto");
//        Glide.with(this).load(StringToBitMap(photo)).into(foto);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //checkLogin();
        //auth.setDisplayName


    }


    public void findViews(){

        container = findViewById(R.id.container);

        contactNameTv = findViewById(R.id.contactName);
        recyclerView = findViewById(R.id.recyclerview);
        foto = findViewById(R.id.perfil_photo2);

        defaultPhoto = findViewById(R.id.imageView21);

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);

        messageET = findViewById(R.id.messageET);
        messageET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                recyclerView.scrollToPosition(messages.size() - 1);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        loadMessage();

    }

    public void clickBack(View v){

        finish();
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);

            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public void checkLogin(){

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);

            finish();

        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
            //displayChatMessages();
        }
    }

    public void loadUser(){

        DocumentReference docRef = db.collection("users").document(contactId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                        contactUser = document.toObject(User.class);
                        setContatcPhoto();

                        contactNameTv.setText(contactUser.getNome());


                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                    } else {
                        Log.d("getUser", "No such document");
                    }
                } else {
                    Log.d("getUser", "get failed with ", task.getException());
                }
            }
        });



    }

    public void setContatcPhoto(){

        storageRef = FirebaseStorage.getInstance().getReference();
        String id = contactId + "_perfil";
        storageRef.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                //ImageView photo = .findViewById(R.id.perfil_photo);
                if(uri != null){

                    Glide.with(getApplicationContext()).load(uri).into(foto);
                    defaultPhoto.setVisibility(View.INVISIBLE);
                }
                else
                    defaultPhoto.setVisibility(View.VISIBLE);

                container.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("getUser", "DocumentSnapshot data: ");

            }
        });

    }

    public void loadMessage(){

        myRef.child("messages").child(userId).child(contactId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Toast.makeText(getApplicationContext(),"Atualizando",Toast.LENGTH_SHORT).show();
                messages.clear();

                if(dataSnapshot.hasChildren()){

                    for (DataSnapshot child: dataSnapshot.getChildren()) {

                        Message message = new Message((HashMap<String, String>) child.getValue());
                        messages.add(message);
                    }

                    mAdapter = new AdapterChatRecycleView(messages,getApplicationContext(),userId);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.scrollToPosition(messages.size() - 1);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void sendMessage(View v){

        Message message = new Message(messageET.getText().toString(), Calendar.getInstance().getTime().toString(),userId,contactId);

        myRef.child("messages").child(userId).child(contactId).push().setValue(message);
        myRef.child("contats").child(userId).child(contactId).child("last").setValue(message.getText());

        myRef.child("messages").child(contactId).child(userId).push().setValue(message);
        myRef.child("contats").child(contactId).child(userId).child("last").setValue(message.getText());

        messageET.setText("");

        //fecha teclado
    }

    public String getUserId(){
        return userId;
    }

}