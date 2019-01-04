package com.lek.sororas.Models;


import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class User {

    String nome;
    String email;
    String nascimento;
    String cidade;
    String id;

    ArrayList<String> anunciosIds;
    ArrayList<DocumentReference> favoritosIds;


    String photoPerfil;
    String photoBanner;

    public Uri perfilPhoto;
    public Uri bannerPhoto;

    public User() {

    }

    public User(String nome, String email, String nascimento, String cidade) {
        this.nome = nome;
        this.email = email;
        this.nascimento = nascimento;
        this.cidade = cidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public ArrayList<String> getAnunciosIds() {
        return anunciosIds;
    }

    public void setAnunciosIds(ArrayList<String> anunciosIds) {
        this.anunciosIds = anunciosIds;
    }

    public ArrayList<DocumentReference> getFavoritosIds() {
        return favoritosIds;
    }

    public void setFavoritosIds(ArrayList<DocumentReference> favoritosIds) {
        this.favoritosIds = favoritosIds;
    }

    public String getPhotoPerfil() {
        return photoPerfil;
    }

    public void setPhotoPerfil(String photoPerfil) {
        this.photoPerfil = photoPerfil;
    }

    public String getPhotoBanner() {
        return photoBanner;
    }

    public void setPhotoBanner(String photoBanner) {
        this.photoBanner = photoBanner;
    }

    //perfil;
}
