package com.lek.sororas.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lek.sororas.LoginActivity;
import com.lek.sororas.R;
import com.lek.sororas.LoginActivity;
import com.lek.sororas.Utils.DateInputMask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static android.content.Context.WIFI_SERVICE;

public class FragmentSingUp extends Fragment {

    private FusedLocationProviderClient mFusedLocationClient;


    private View view;
    Context context;
    LoginActivity main;

    public EditText nome, email, senha, local, dataNascimento;
    public Uri photoPerfil;
    public URL photoUrl;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    String city = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.layout_singup, container, false);
        } catch (InflateException e) {

        }


        context = inflater.getContext();

        main = (LoginActivity) context;

        findViews();


        //URL url = new URL("http://api.ipstack.com/134.201.250.155?access_key=b697082b731c3b8c4cff59209aa3ec92")
        //new checkLocal().execute();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        return view;
    }

    public void findViews(){

        nome = view.findViewById(R.id.createname);
        email = view.findViewById(R.id.createEmail);
        senha = view.findViewById(R.id.createsenha);

        dataNascimento = view.findViewById(R.id.createdate);
        new DateInputMask(dataNascimento);

        local = view.findViewById(R.id.createlocal);


        local.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                    verifyPermission();
            }
        });

//        dataNascimento.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DialogFragment newFragment = new MyDatePickerFragment();
//                ((MyDatePickerFragment) newFragment).setDisplay(dataNascimento);
//                newFragment.show(main.getSupportFragmentManager(), "date picker");
//
//            }
//        });

    }

    public void writeInformations(GoogleSignInAccount account){

        email.setText(account.getEmail());
        nome.setText(account.getDisplayName());

        photoPerfil = account.getPhotoUrl();

    }

    public void writeInformations(JSONObject object) throws JSONException, MalformedURLException {

        email.setText(object.get("email").toString());
        nome.setText(object.get("name").toString());

        String id = object.get("id").toString();
        photoUrl = new URL("https://graph.facebook.com/" + id + "/picture?type=large");

        Log.i("TEste","Wololo");

    }

    @Override
    public void onResume() {

        super.onResume();
        //this.onCreate(null);
    }



    public void verifyPermission(){

        if (ContextCompat.checkSelfPermission(main,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(main,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }else{

           getCity();

        }
    }


    public void getCity(){

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            Geocoder gcd = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses.size() > 0) {

                                    city = addresses.get(0).getLocality();
                                    local.setText(city);
                                    local.setSelection(local.getText().length());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   getCity();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }


    class checkLocal extends AsyncTask<Void, Void, String> {




        @Override
        protected String doInBackground(Void... params) {
            WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
            DhcpInfo info = wm.getDhcpInfo();
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL("http://api.ipstack.com/" + ip + "?access_key=" + getString(R.string.api_localizacao_key));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String linha;
                StringBuffer buffer = new StringBuffer();
                while((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n");
                }
                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String dados) {

           Log.i("webservice",dados);
            // Faça alguma coisa com os dados
        }
    }

}
