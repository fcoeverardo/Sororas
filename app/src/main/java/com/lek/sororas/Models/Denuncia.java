package com.lek.sororas.Models;

public class Denuncia {

    String id;
    String comment;

    public Denuncia() {

    }

    public Denuncia(String id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
