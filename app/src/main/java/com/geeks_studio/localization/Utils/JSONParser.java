package com.geeks_studio.localization.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Classic on 26/9/2016.
 */
public class JSONParser {

    static JSONObject jobj=null;
    static String json="";
    public JSONParser() {

    }
    public JSONObject JSONparserread(String url1){
        URL url = null;
        try {
            url = new URL(url1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line =null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
                json = sb.toString();
                try {
                    jobj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobj;
    }

}
