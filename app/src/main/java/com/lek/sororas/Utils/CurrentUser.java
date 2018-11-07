package com.lek.sororas.Utils;

import com.lek.sororas.Models.User;

public class CurrentUser {

    private static User currentUser = null;

    public static User getUser(User user){

        if(currentUser == null){

            currentUser = user;
        }

        return currentUser;
    }

    public static User getUser(){

        if(currentUser == null){

            currentUser = new User();
        }

        return currentUser;
    }

}
