package com.underdusken.kulturekalendar.network;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 10/12/12
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OnLoadListener {
    public void onLoadError(String url, int errorCode, String data);

    public void onLoad(String url, String data);
}