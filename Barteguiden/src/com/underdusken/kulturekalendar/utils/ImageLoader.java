package com.underdusken.kulturekalendar.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.*;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Stack;

public class ImageLoader {

    private static final String STORE_PLACE = "/Android/data/com.underdusken.kulturekalendar";

    //private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
    private HashMap<String, SoftReference<Bitmap>> cache=new HashMap<String, SoftReference<Bitmap>>();

    private File cacheDir;

    public ImageLoader(Context context) {
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory() + STORE_PLACE + "/cache");
        }else{
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }


    public void DisplayImage(String url, Activity activity, ImageView imageView, int resourceDraw) {
        if(!url.equalsIgnoreCase("")){
        if (cache.containsKey(url)){
            //imageView.setImageBitmap(cache.get(url));
            SoftReference<Bitmap> softRef = cache.get(url);
            Bitmap bitmap = softRef.get();
            if(bitmap==null){
                queuePhoto(url, activity, imageView);
                imageView.setImageResource(resourceDraw);
            }else{
                imageView.setImageBitmap(softRef.get());
                //TODO Maybe check that get reference already NULL =)
            }
        }else{

            if(url!=null ){
                queuePhoto(url, activity, imageView);

                String filename = String.valueOf(url.hashCode());
                File f = new File(cacheDir, filename);

                //from SD cache
                Bitmap b = decodeFile(f);
                if (b != null){
                    imageView.setImageBitmap(b);
                    return;
                }

                if(url!=null )
                    queuePhoto(url, activity, imageView);
            }



            if(resourceDraw!=-1)
                imageView.setImageResource(resourceDraw);
        }}else{
            if(resourceDraw!=-1)
                imageView.setImageResource(resourceDraw);
        }
    }

    private void queuePhoto(String url, Activity activity, ImageView imageView) {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
        photosQueue.Clean(imageView);
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        synchronized (photosQueue.photosToLoad) {
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        //start thread if it's not started yet
        if (photoLoaderThread.getState() == Thread.State.NEW)
            photoLoaderThread.start();
    }

    private InputStream getInputStreamFromUrl(String url) {
        InputStream is = null;
        URLConnection connection = null;
        try {
            URL aURL = new URL(url);
            connection = aURL.openConnection();
            connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
            is = connection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    private Bitmap getBitmap(String url) {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        if(url==null)
            return null;
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);

        //from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        //from web
        InputStream is = null;
        try {
            Bitmap bitmap = null;
            is = getInputStreamFromUrl(url);
            OutputStream os = new FileOutputStream(f);
            CopyStream(is, os);
            os.close();
            is.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            if(is!=null)
                return BitmapFactory.decodeStream(is);
            return null;
        }
    }

    public void exit(){
        photoLoaderThread.interrupt();
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
        } catch (Exception e){}
        return null;
    }

    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    PhotosQueue photosQueue = new PhotosQueue();

    public void stopThread() {
        photoLoaderThread.interrupt();
    }

    //stores list of photos to download
    class PhotosQueue {
        private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

        //removes all instances of this ImageView
        public void Clean(ImageView image) {
            for (int j = 0; j < photosToLoad.size(); ) {
                ImageView iv = null;
                try{
                    iv = photosToLoad.get(j).imageView;
                }catch (Exception e){
                    break;
                }
                if (iv == image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }

    class PhotosLoader extends Thread {
        public void run() {
            try {
                while (true) {
                    //thread waits until there are any images to load in the queue
                    if (photosQueue.photosToLoad.size() == 0)
                        synchronized (photosQueue.photosToLoad) {
                            photosQueue.photosToLoad.wait();
                        }
                    if (photosQueue.photosToLoad.size() != 0) {
                        PhotoToLoad photoToLoad;
                        synchronized (photosQueue.photosToLoad) {
                            photoToLoad = photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp = getBitmap(photoToLoad.url);
                        if(bmp==null)
                            continue;

                        cache.put(photoToLoad.url, new SoftReference<Bitmap>(bmp));
                        Object tag = photoToLoad.imageView.getTag();

                        if (tag!=null && ((String)tag).equals(photoToLoad.url)) {
                            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a = (Activity) photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if (Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }

    PhotosLoader photoLoaderThread = new PhotosLoader();

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;

        public BitmapDisplayer(Bitmap b, ImageView i) {
            bitmap = b;
            imageView = i;
        }

        public void run() {
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
/*            else
                imageView.setImageResource(stub_id);*/
        }
    }

    public void clearCache() {
        //clear memory cache
        cache.clear();

        //clear SD cache
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }

}

