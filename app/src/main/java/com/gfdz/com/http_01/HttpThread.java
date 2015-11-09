package com.gfdz.com.http_01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private ImageView imageView;

    public HttpThread( String url, Handler handler, WebView webView) {
        this.url = url;
        this.handler = handler;
        this.webView = webView;
    }
    public HttpThread( String url, Handler handler, ImageView imageView) {
        this.url = url;
        this.handler = handler;
        this.imageView = imageView;
    }

    @Override
    public void run() {
        try {
            URL httpUrl=new URL(url);
            HttpURLConnection conn= (HttpURLConnection) httpUrl.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            FileOutputStream out = null;
            File downloadFile=null;
           /* final StringBuffer sb=new StringBuffer();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            while ((str=reader.readLine())!=null){
                sb.append(str);

            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadData(sb.toString(), "text/html;charset=utf-8", null);
                }
            });*/
            InputStream in=conn.getInputStream();

             if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File parent=Environment.getExternalStorageDirectory();
                downloadFile=new File(parent,String.valueOf(System.currentTimeMillis()));
                 out=new FileOutputStream(downloadFile);
             }
            byte[]b=new byte[2*1024];
            int len;
            if(out!=null){
                while ((len=in.read(b))!=-1){
                    out.write(b,0,len);
                }

            }
            final Bitmap bitmap= BitmapFactory.decodeFile(downloadFile.getAbsolutePath());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
