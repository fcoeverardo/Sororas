package com.lek.sororas.Models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Anuncio {

    String titulo;
    String tipo;
    String descricao;
    String categoria;
    DocumentReference proprietaria;
    String tags;
    ArrayList<String> fotos;

    public Anuncio() {
        this.titulo = "";
        this.tipo = "";
        this.descricao = "";
        this.categoria = "";
        this.fotos = new ArrayList<>();
    }


    public Anuncio(String titulo, String tipo, String descricao, String categoria, DocumentReference proprietaria, ArrayList<String> fotos) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.proprietaria = proprietaria;
        this.fotos = fotos;
    }

    public Anuncio(String titulo, String tipo, String descricao, String categoria, DocumentReference proprietaria, String tags, ArrayList<String> fotos) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.proprietaria = proprietaria;
        this.tags = tags;
        this.fotos = fotos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ArrayList<String> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<String> fotos) {
        this.fotos = fotos;
    }

    public DocumentReference getProprietaria() {
        return proprietaria;
    }

    public void setProprietaria(DocumentReference proprietaria) {
        this.proprietaria = proprietaria;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAllText(){

        String text = titulo + tipo + descricao +categoria + proprietaria + tags;
        text = text.toLowerCase();
        text = text.replaceAll(" ","");

        return text;
    }
}
