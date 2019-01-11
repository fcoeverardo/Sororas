package com.lek.sororas;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
    ConstraintLayout containerChange;
    int count=0;

    EditText createNome,createEmail,createCidade,createdate,createsenha,createNewsenha,createconfirmsenha;

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

        createEmail = findViewById(R.id.createEmail);
        createNome = findViewById(R.id.createNome);
        createCidade = findViewById(R.id.createCidade);
        createsenha = findViewById(R.id.createsenha);
        createNewsenha = findViewById(R.id.confirmNewesenha);
        createconfirmsenha = findViewById(R.id.confirmCreatesenha2);
        createdate = findViewById(R.id.createdate);

        viewFlipper = findViewById(R.id.viewFlipper);

        containerChange = findViewById(R.id.containerChange);

        setInfos();

    }


    public void setInfos(){

        nometv.setText(user.getNome());
        //senhatv.setText(user.getNome());
        cidadetv.setText(user.getCidade());
        datatv.setText(user.getNascimento());
        emailtv.setText(user.getEmail());

        createEmail.setText(user.getEmail());
        createNome.setText(user.getNome());
        createCidade.setText(user.getCidade());
        createdate.setText(user.getNascimento());

    }


    public void clickInfo(View v){

        String i = (String) v.getTag();

        count = Integer.parseInt(i);

        ConstraintLayout item = ((ConstraintLayout) containerChange.getChildAt(count));
        item.setVisibility(View.VISIBLE);

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


        ConstraintLayout item = ((ConstraintLayout) containerChange.getChildAt(count));
        item.setVisibility(View.GONE);

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


    public void updateUser(View v){


        switch (count){
            case 0:
                String email = createEmail.getText().toString();
                nometv.setText(email);
                user.setEmail(email);

                break;

            case 1:
                String nome = createNome.getText().toString();
                nometv.setText(nome);
                user.setNome(nome);

                break;
            case 2:
                String cidade = createCidade.getText().toString();
                nometv.setText(cidade);
                user.setNome(cidade);

                break;
            case 3:


                break;
            case 4:

                String data = createdate.getText().toString();
                nometv.setText(data);
                user.setNascimento(data);
                break;

        }

        user.setBannerPhoto(null);
        user.setPerfilPhoto(null);

        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user);
        CurrentUser.setUser(user);

        Toast.makeText(this,"Seu dados foram atualizados",Toast.LENGTH_SHORT).show();
        flipperBack();
        hideKeyboard(this);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}
