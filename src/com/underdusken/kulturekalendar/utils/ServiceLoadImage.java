package com.underdusken.kulturekalendar.utils;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 10/31/12
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.widget.ImageView;

public class ServiceLoadImage {

    private Activity activity = null;
    private ImageLoader imageLoader = null;

     public ServiceLoadImage(Activity activity){
        this.activity = activity;
        imageLoader = new ImageLoader(activity);
    }

    // Loading Image to ImageView
    public void loadImage(String urlImage, ImageView imageView, int resourceDraw){
        imageView.setTag(urlImage);
        imageLoader.DisplayImage(urlImage, activity, imageView, resourceDraw);
    }

    public void exit(){
        imageLoader.exit();
    }


}
