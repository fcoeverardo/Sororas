package com.lek.sororas.Models;

public class Contato {

    String id;
    String nome;
    String foto64;
    String last;

    public Contato() {

    }

    public Contato(String id, String nome, String foto64) {
        this.id = id;
        this.nome = nome;
        this.foto64 = foto64;
    }

    public Contato(String id, String nome, String foto64, String last) {
        this.id = id;
        this.nome = nome;
        this.foto64 = foto64;
        this.last = last;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto64() {
        return foto64;
    }

    public void setFoto64(String foto64) {
        this.foto64 = foto64;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
