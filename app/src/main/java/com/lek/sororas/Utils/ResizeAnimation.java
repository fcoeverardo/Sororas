package com.lek.sororas.Utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by aluno on 23/01/2017.
 */

public class ResizeAnimation extends Animation {

    private View mView;
    private float mToHeight;
    private float mFromHeight;

    private float mToWidth;
    private float mFromWidth;

    public ResizeAnimation(View v, float fromWidth, float fromHeight, float toWidth, float toHeight) {
        mToHeight = toHeight;
        mToWidth = toWidth;
        mFromHeight = fromHeight;
        mFromWidth = fromWidth;
        mView = v;
        setDuration(300);
    }

    public ResizeAnimation(final View v, double divider,int time) {
        mFromHeight = v.getHeight();
        mFromWidth = v.getWidth();
        mToWidth = v.getWidth();
        mView = v;
        setDuration(time);

        if((v.getTag()).equals("false")){
            mToHeight = (float) (v.getHeight()/divider);
            v.setTag("true");
        }
        else{
            mToHeight = (float) (v.getHeight() * divider);
            v.setTag("false");
        }

        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setClickable(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public ResizeAnimation(final View v,final View arrow, double multiplyer,final int time) {

        mFromHeight = v.getHeight();
        mFromWidth = v.getWidth();
        mToWidth = v.getWidth();
        mView = v;
        setDuration(time);

        if((v.getTag()).equals("false")){
            mToHeight = (float) (v.getHeight() * multiplyer);
            v.setTag("true");
        }
        else{
            mToHeight = (float) (v.getHeight() /multiplyer);
            v.setTag("false");
        }

        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                arrow.setClickable(false);
                arrow.animate().rotation(180).setDuration(time);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                arrow.setClickable(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public ResizeAnimation(final View v,float fromHeight, float toHeight,int time) {
        mFromHeight = v.getHeight();
        mFromWidth = v.getWidth();
        mToWidth = v.getWidth();
        mView = v;
        setDuration(time);

        if((v.getTag()).equals("false")){
            mToHeight = toHeight;
            v.setTag("true");
        }
        else{
            mToHeight = fromHeight;
            v.setTag("false");
        }

        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setClickable(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public ResizeAnimation(View v){
        mFromHeight = v.getHeight();
        mFromWidth = v.getWidth();
        mToWidth = v.getWidth();
        mView = v;
        setDuration(300);

        if((v.getTag()).equals("false")){
            mToHeight = v.getHeight()/3;
            v.setTag("true");
        }
        else{
            mToHeight = v.getHeight() * 3;
            v.setTag("false");
        }


    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {


        float height = (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
        float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
        ViewGroup.LayoutParams p = mView.getLayoutParams();
        p.height = (int) height;
        p.width = (int) width;
        mView.requestLayout();

    }

}

