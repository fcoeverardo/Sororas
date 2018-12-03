package com.lek.sororas.Adapters;

import java.util.ArrayList;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {

    ArrayList<String> anuncios;

    public MainSliderAdapter(ArrayList<String> anuncios){

        this.anuncios = anuncios;
    }


    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide(anuncios.get(0));
                break;
            case 1:
                viewHolder.bindImageSlide(anuncios.get(1));
                break;
            case 2:
                viewHolder.bindImageSlide(anuncios.get(2));
                break;
            case 3:
                viewHolder.bindImageSlide(anuncios.get(3));
                break;
            case 4:
                viewHolder.bindImageSlide(anuncios.get(4));
                break;

        }
    }
}
