package com.naomi.nail_dash;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {
        public String retrieveUrl(String url) throws IOException {
            String urlData ="";
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;

            try {
                URL getUrl = new URL(url);
                httpURLConnection=(HttpURLConnection) getUrl.openConnection();
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();

                String line = "";
                while((line = br.readLine()) != null)
                {
                    sb.append(line);
                }

                urlData = sb.toString();
                br.close();

            } catch (
                    MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {

                httpURLConnection.disconnect();
            }
            Log.d("DownloadURL","Returning data= "+ urlData);

            return urlData;
        }
    }

