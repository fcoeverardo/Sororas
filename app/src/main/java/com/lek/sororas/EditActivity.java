package com.lek.sororas;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lek.sororas.Models.User;
import com.lek.sororas.Utils.CurrentUser;

public class EditActivity extends BasicActivity {

    TextView nometv,senhatv,cidadetv,datatv,emailtv;
    ViewFlipper viewFlipper;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        user = CurrentUser.getUser();

        findViews();

    }

    public void findViews(){

        nometv = findViewById(R.id.nometv);
        senhatv = findViewById(R.id.senhatv);
        cidadetv = findViewById(R.id.cidadetv);
        datatv = findViewById(R.id.datatv);
        emailtv = findViewById(R.id.emailtv);

        viewFlipper = findViewById(R.id.viewFlipper);

        setInfos();
    }


    public void setInfos(){

        nometv.setText(user.getNome());
        //senhatv.setText(user.getNome());
        cidadetv.setText(user.getCidade());
        datatv.setText(user.getNascimento());
        emailtv.setText(user.getEmail());

    }


    public void clickInfo(View v){

        flipperNext();
    }

    public void clickBackArrow(View v){

        if(viewFlipper.getDisplayedChild() == 1){
            flipperBack();
        }
        else
            finish();
    }

    public void flipperNext() {

        //int index = Integer.parseInt(v.getTag() + "");

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));

        viewFlipper.showNext();
        //viewFlipper.setDisplayedChild(1);
    }

    public void flipperBack() {


        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));

        viewFlipper.showPrevious();
        //viewFlipper.setDisplayedChild(0);
    }

    @Override
    public void onBackPressed() {

        if(viewFlipper.getDisplayedChild() == 1){
            flipperBack();
        }
        else
            finish();

    }



}
