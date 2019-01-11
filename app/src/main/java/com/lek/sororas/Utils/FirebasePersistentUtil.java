package com.lek.sororas.Utils;


import com.google.firebase.database.FirebaseDatabase;

public class FirebasePersistentUtil {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase enablePersistence() {

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            //mDatabase.setPersistenceEnabled(true);
            // ...
        }

        return mDatabase;

    }
}
