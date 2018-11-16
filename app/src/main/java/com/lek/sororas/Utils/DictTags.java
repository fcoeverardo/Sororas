package com.lek.sororas.Utils;

import com.lek.sororas.R;

import java.util.HashMap;

public class DictTags {

    public HashMap<String,Integer> dict;

    public DictTags(){

        dict = new HashMap<>();

        dict.put("Artesanato", R.array.artesanato);
        dict.put("Moda", R.array.moda);
        dict.put("Serviços Gerais", R.array.servicos);
        dict.put("Veículos", R.array.veiculos);
        dict.put("Beleza", R.array.beleza);


    }

    public Integer getArray(String categoria){

        return dict.get(categoria);

    }
}
