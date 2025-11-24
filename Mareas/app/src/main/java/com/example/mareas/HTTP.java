package com.example.mareas;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTP {
    static public String getUrlContents(String strURL) {
        StringBuilder str = new StringBuilder();
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(strURL).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea;
            while ((linea = in.readLine()) != null) {
                str.append(linea);
            }
        } catch (IOException e) {
            return "";
        }
        return str.toString();
    }
}
