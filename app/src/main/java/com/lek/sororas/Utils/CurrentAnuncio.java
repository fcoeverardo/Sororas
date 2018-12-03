package com.lek.sororas.Utils;

import com.lek.sororas.Models.Anuncio;
import com.lek.sororas.Models.Anuncio;

public class CurrentAnuncio {

    private static Anuncio currentAnuncio = null;

    public static Anuncio setAnuncio(Anuncio anuncio){

        currentAnuncio = anuncio;

        return currentAnuncio;
    }

    public static Anuncio getAnuncio(){

        if(currentAnuncio == null){

            currentAnuncio = new Anuncio();
        }

        return currentAnuncio;
    }

}
