package com.underdusken.kulturekalendar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for downloading and setting a Bitmap to
 * a ImageView. The class has a static cache. The bitmaps
 * are either gotten from the cache, from a file in
 * Context.getCacheDir(), or from the web, with the
 * specified image URL. The URL is what defines an image.
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";
    // Cache for holding all images fetched so far.
    private final static Map<String, Bitmap> cache = new HashMap<String, Bitmap>();

    private final File cacheDir;

    public ImageLoader(Context context) {
        cacheDir = context.getCacheDir();
    }

    /**
     * Sets the Bitmap whose identifier is <code>imageURL</code> as the resource
     * to <code>imageView</code>.
     *
     * @param imageView The <code>ImageView</code> which will hold the image.
     * @param imageURL  The URL to the image resource.
     */
    public void setImageViewResource(final ImageView imageView, final String imageURL) {
        /*
         *      This really ugly block is a part of a hack:
         *      Since we want to load the images off the UI thread we need
         *      to use a AsyncTask. However, UI elements can't be changed
         *      off the UI thread. Therefore, we load the image, inserts it into
         *      the cache, and sets the resource to the imageView.
         *
         *      The onPostExecute method is off the UI thread, and it's created
         *      as an anonymous method so it can have access to both the imageURL and
         *      the imageView parameters.
         */
        new AsyncImageLoader() {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                synchronized (cache) {
                    cache.put(imageURL, bitmap);
                }
                if (imageView == null) {
                    return;
                }
                imageView.setImageBitmap(bitmap);
            }
        }.execute(imageURL);

    }

    /**
     * Its dumb to do this as a seperate method; a better solution would be to
     * make clients create an anonymous class, and override a method, which
     * gets the image in as a parameter.
     *
     * @param imageView
     * @param imageURL
     */
    public void setImageViewResourceAlphaAnimated(final ImageView imageView, final String imageURL) {
        if (Build.VERSION.SDK_INT < 12) {
            setImageViewResource(imageView, imageURL);
            return;
        }
        new AsyncImageLoader() {
            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                synchronized (cache) {
                    cache.put(imageURL, bitmap);
                }
                imageView.setAlpha(0F);
                imageView.setImageBitmap(bitmap);
                imageView.animate().alpha(0.9F).setDuration(1000).start();
            }
        }.execute(imageURL);
    }

    public void downloadImage(String url) {
        // Needs to check if image already is downloaded
        Bitmap b = getBitmapFromFile(String.valueOf(url.hashCode()));
        if (b != null) return;

        b = getBitmapFromWeb(url);
        if (b == null) {
            return;
        }
        saveBitmapToFile(b, String.valueOf(url.hashCode()));
    }

    private class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String imageURL = params[0];
            // The cache
            Bitmap bitmap = cache.get(imageURL);
            if (bitmap != null) {
                Log.d(TAG, "Got bitmap from cache");
                return bitmap;
            }
            // From file
            bitmap = getBitmapFromFile(String.valueOf(imageURL.hashCode()));
            if (bitmap != null) {
                Log.d(TAG, "Got bitmap from file");
                return bitmap;
            }
            // From web
            bitmap = getBitmapFromWeb(imageURL);
            if (bitmap != null) {
                Log.d(TAG, "Got bitmap from web");
                return bitmap;
            }
            return null;
        }
    }

    /**
     * Loads a bitmap from cacheDir.
     *
     * @param fileName the name of the file containing the bitmap.
     * @return the Bitmap in the file.
     */
    private Bitmap getBitmapFromFile(String fileName) {
        File file = new File(cacheDir, fileName);
        try {
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private Bitmap getBitmapFromWeb(String url) {
        InputStream is = null;
        URLConnection connection;
        try {
            URL aURL = new URL(url);
            connection = aURL.openConnection();
            connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
            try {
                is = connection.getInputStream();
            } catch (FileNotFoundException e) {

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        if (bitmap == null) return null;
        saveBitmapToFile(bitmap, String.valueOf(url.hashCode()));
        return bitmap;
    }

    private void saveBitmapToFile(Bitmap b, String name) {
        File f = new File(cacheDir, name);
        try {
            OutputStream os = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
