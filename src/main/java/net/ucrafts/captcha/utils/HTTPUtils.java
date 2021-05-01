package net.ucrafts.captcha.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtils
{

    public static JSONObject get(String uri)
    {
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setReadTimeout(2000);

            if (httpURLConnection.getResponseCode() != 200) {
                throw new RuntimeException("Method response code not 200");
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String response;

            while ((response = bufferedReader.readLine()) != null) {
                stringBuffer.append(response);
            }

            bufferedReader.close();

            response = stringBuffer.toString();

            try {
                return new JSONObject(response);
            } catch (Throwable e) {
                throw new RuntimeException("Incorrect json response");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error get captcha code");
        }
    }
}
