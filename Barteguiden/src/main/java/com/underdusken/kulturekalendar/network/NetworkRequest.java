package com.underdusken.kulturekalendar.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class NetworkRequest extends AsyncTask<Void, Void, String> {

    private static Context context = null;

    private OnLoadListener onLoadListener = null;

    DefaultHttpClient httpClient;
    HttpContext localContext;
    private String ret;

    HttpResponse response = null;
    HttpPost httpPost = null;
    HttpGet httpGet = null;

    // data for async load
    private String asyncUrl;
    private ArrayList<NameValuePair> asyncData;
    private String asyncContentType;
    private String asyncBaseAuth;
    private HttpStatusCode asyncStatusCode;

    private HttpStatusCode errorStatusCode = new HttpStatusCode();

    public static void setActivity(Context context) {
        NetworkRequest.context = context;
    }
/*}
    public void noInternet() {
        if (context != null) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.internet_connection_error, Toast.LENGTH_SHORT).show();
                }
            });
        }*/


    public NetworkRequest() {
        HttpParams myParams = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(myParams, 120000);
        HttpConnectionParams.setSoTimeout(myParams, 120000);
        httpClient = new DefaultHttpClient(myParams);
        localContext = new BasicHttpContext();
    }

    public void clearCookies() {
        httpClient.getCookieStore().clear();
    }

    public void abort() {
        try {
            if (httpClient != null) {
                System.out.println("Abort.");
                httpPost.abort();
            }
        } catch (Exception e) {
            System.out.println("Your App Name Here" + e);
        }
    }

    public NetworkRequest setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;

        return this;
    }

    public NetworkRequest setAsyncPost(String url, ArrayList<NameValuePair> data, String contentType, String baseAuth, HttpStatusCode statusCode) {
        asyncUrl = url;
        asyncData = data;
        asyncContentType = contentType;
        asyncBaseAuth = baseAuth;
        asyncStatusCode = statusCode;

        return this;
    }

    public NetworkRequest setAsyncPost(String url, ArrayList<NameValuePair> data, HttpStatusCode statusCode) {
        asyncUrl = url;
        asyncData = data;
        asyncStatusCode = statusCode;

        return this;
    }

    public NetworkRequest setAsyncPost(String url, ArrayList<NameValuePair> data, String baseAuth, HttpStatusCode statusCode) {
        asyncUrl = url;
        asyncData = data;
        asyncBaseAuth = baseAuth;
        asyncStatusCode = statusCode;

        return this;
    }

    public String sendPost(String url, ArrayList<NameValuePair> data, HttpStatusCode statusCode) {
        return sendPost(url, data, null, statusCode);
    }


    public String sendJSONPost(String url, ArrayList<NameValuePair> data, HttpStatusCode statusCode) {
        return sendPost(url, data, "application/json", statusCode);
    }


    public String sendPost(String url, ArrayList<NameValuePair> data, String contentType, HttpStatusCode statusCode) {
        ret = null;

        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

        httpPost = new HttpPost(url);
        response = null;

        StringEntity tmp = null;

        Log.d("Your App Name Here", "Setting httpPost headers");

        httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
        httpPost.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");

        if (contentType != null) {
            httpPost.setHeader("Content-Type", contentType);
        } else {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }

        if (data != null) {

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            Log.d("Your App Name Here", url + "?" + data);
        } else {
            Log.d("Your App Name Here", url);
        }

        try {
            response = httpClient.execute(httpPost, localContext);

            if (response != null) {
                errorStatusCode.setStatusCode(response.getStatusLine().getStatusCode());
                if (statusCode != null)
                    statusCode.setStatusCode(response.getStatusLine().getStatusCode());

                ret = EntityUtils.toString(response.getEntity());
            }
        } catch (ConnectTimeoutException e) {
            errorStatusCode.setStatusCode(1);
        } catch (SocketTimeoutException e) {
            errorStatusCode.setStatusCode(1);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String resultString = "";
        if (ret == null) {
            /*if (!haveNetworkConnection()) {
                noInternet();
            }*/
            return null;
        }
        try {
            resultString = URLDecoder.decode(ret, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            resultString = null;
        }
        return resultString;
    }


    // Testing for internet connection
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public String sendGet(String url) {
        httpGet = new HttpGet(url);

        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e) {
            Log.e("Your App Name Here", e.getMessage());
        }

        //int status = response.getStatusLine().getStatusCode();

        // we assume that the response body contains the error message
        try {
            ret = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            Log.e("Your App Name Here", e.getMessage());
        }

        String result = null;
        try {
            result = new StringEntity(ret, HTTP.UTF_8).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return result;
    }

    public static String convertStreamToString(InputStream is) {
        if(is==null)
            return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String result = sb.toString();
        return result;
    }

    public String getInputStreamFromUrl(String url) {
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

        String result = convertStreamToString(is);
        return result;

    }

    public InputStream getHttpStream(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception e) {
            throw new IOException("Error connecting");
        } // end try-catch

        return in;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = sendPost(asyncUrl, asyncData, asyncContentType, errorStatusCode);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (onLoadListener != null)
            if (result == null) {
                onLoadListener.onLoadError(result, errorStatusCode.getStatusCode(), null);
            } else {
                if (errorStatusCode.getStatusCode() == 1) {
                    onLoadListener.onLoadError("Connection is absent", errorStatusCode.getStatusCode(), "Connection is absent");
                }
                if (errorStatusCode.getStatusCode() != 200)
                    onLoadListener.onLoadError(result, errorStatusCode.getStatusCode(), result);
                else
                    onLoadListener.onLoad(result, (String) result);
            }
        super.onPostExecute(result);
    }
}
