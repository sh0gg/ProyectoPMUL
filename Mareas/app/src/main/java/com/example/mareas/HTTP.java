package com.example.mareas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HTTP {
    static public String getUrlContents(String strURL) {
        StringBuilder str = new StringBuilder();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.11", 3128));
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(strURL).openConnection(proxy);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea;
            while ((linea = in.readLine()) != null) {
                str.append(linea);
            }
        } catch (IOException e) {
            return "Excepcion al abrir la URL";
        }
        return str.toString();
    }
}