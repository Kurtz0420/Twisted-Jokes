package com.redditjokes.twistedjokes.utils;

import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.redditjokes.twistedjokes.BaseApplication;
import com.redditjokes.twistedjokes.R;

import java.util.Random;

public class ColorsHelper {


    //f78749
    //4abcd9
    //496075
    //c48d81
    //8c76a5
    //ec7e74
    //e1b975
    //9b8271
    //b7556b
    public static int getRandomColor(){
        Random rand = new Random();
        int newrand = rand.nextInt(9);

        switch (newrand){

            case 0:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c1);

            case 1:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c2);

            case 2:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c3);

            case 3:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c4);

            case 4:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c5);

            case 5:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c6);

            case 6:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c7);

            case 7:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c8);

            case 8:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.c9);


            default:
                return ContextCompat.getColor(BaseApplication.getContext(), R.color.textColor);


        }
    }
}
