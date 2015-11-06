package com.gfdz.com.http_01;

import android.os.Handler;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Administrator on 2015/11/6.
 */
public class HttpThread extends Thread{
    private  String url;
    private WebView webView;
    private Handler handler;

    public HttpThread( String url, Handler handler, WebView webView) {
        this.url = url;
        this.handler = handler;
        this.webView = webView;
    }

    @Override
    public void run() {
        try {
            URL httpUrl=new URL(url);
            HttpURLConnection conn= (HttpURLConnection) httpUrl.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            final StringBuffer sb=new StringBuffer();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            while ((str=reader.readLine())!=null){
                sb.append(str);

            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                 webView.loadData(sb.toString(),"text/html;charset=utf-8",null);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
