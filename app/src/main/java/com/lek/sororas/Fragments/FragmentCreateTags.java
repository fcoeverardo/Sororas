package com.lek.sororas.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lek.sororas.CreateActivity;
import com.lek.sororas.R;
import com.lek.sororas.Utils.DictTags;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class FragmentCreateTags extends android.support.v4.app.Fragment {


    private View view;
    Context context;
    CreateActivity main;
    Button advanceBtn;
    ListView listView;
    TagContainerLayout mTagContainerLayout;
    TagContainerLayout mTagContainerSelec;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.create_tags, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (CreateActivity) context;

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.previusPage(v);
            }
        });

        advanceBtn = view.findViewById(R.id.advanceBtn);
        advanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tags = "";

                for(int i = 0; i<mTagContainerSelec.getChildCount();i++){

                    tags = tags + "#"+mTagContainerSelec.getTagText(i);

                }

                main.anuncio.setTags(tags);
                main.nextPage(v);


            }
        });

        mTagContainerSelec = view.findViewById(R.id.tagcontainerSelec);
        mTagContainerSelec.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

                mTagContainerSelec.removeTag(position);
                if(mTagContainerSelec.getChildCount()== 0)
                    advanceBtn.setVisibility(View.GONE);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        mTagContainerLayout = (TagContainerLayout) view.findViewById(R.id.tagcontainerLayout);



        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

                if(mTagContainerSelec.getChildCount() < 3){
                    mTagContainerSelec.addTag(text);
                }

                else if(mTagContainerSelec.getChildCount() == 3){

                    mTagContainerSelec.removeTag(2);
                        mTagContainerSelec.addTag(text);
                     }

                     advanceBtn.setVisibility(View.VISIBLE);

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        return view;
    }


    public void setTags(){

        DictTags tags = new DictTags();
        mTagContainerLayout.setTags(getResources().getStringArray(tags.getArray(main.anuncio.getCategoria())));

    }

}