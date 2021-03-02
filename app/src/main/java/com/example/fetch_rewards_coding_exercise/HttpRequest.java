package com.example.fetch_rewards_coding_exercise;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {
    public String makeServiceCall(String website) {
        String response = null;
        try {
            URL url = new URL(website);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String convertStreamToString(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
