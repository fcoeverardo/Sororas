package com.lek.sororas.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class UserEvaluation {

    String media;
    ArrayList<Evaluation> avalicaoes;

    public UserEvaluation(){

    }

    public UserEvaluation(String media) {
        this.media = media;
    }

    public UserEvaluation(HashMap<String,Object> map) {
        this.media = (String) map.get("media");

        avalicaoes = new ArrayList<>();

        HashMap<String,Object> aux = (HashMap<String, Object>) map.get("evaluations");

        for(String key : aux.keySet()){

//            Evaluation evaluation = new Evaluation((HashMap<String,String>) aux.get(key));
//            avalicaoes.add(evaluation);

        }

        //Evaluation evaluation = new Evaluation((HashMap<String,String>) map.get("avaliacoes"));

    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public ArrayList<Evaluation> getAvalicaoes() {
        return avalicaoes;
    }

    public void setAvalicaoes(ArrayList<Evaluation> avalicaoes) {
        this.avalicaoes = avalicaoes;
    }
}
