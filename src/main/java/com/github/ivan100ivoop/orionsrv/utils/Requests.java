package com.github.ivan100ivoop.orionsrv.utils;

import com.github.ivan100ivoop.orionsrv.Main;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Requests {

    private static int CONNECT_TIMEOUT = 5000;
    private static int READ_TIMEOUT = 5000;

    private static String USER_AGENT = "ORIONSRV-Client";

    private static SSLSocketFactory getSocketFactory() throws Exception {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());

        return sc.getSocketFactory();
    }
    private static HttpsURLConnection getClient(String url, boolean verifySSL) throws Exception {
        HttpsURLConnection client = (HttpsURLConnection) new URL(url).openConnection();

        client.setSSLSocketFactory(getSocketFactory());
        client.setDefaultHostnameVerifier((hostname, session) -> verifySSL);

        client.setConnectTimeout(CONNECT_TIMEOUT);
        client.setReadTimeout(READ_TIMEOUT);

        client.setDoInput(true);
        client.setDoOutput(true);

        client.setRequestProperty("User-Agent", USER_AGENT);

        return client;
    }

    private static String sendRequest(String url, String method, String data, Map<String, String> headers){
        try {
            HttpsURLConnection connection = getClient(url, false);
            connection.setRequestMethod(method);

            if(headers != null){
                for (Map.Entry<String, String> header : headers.entrySet()){
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            if (data != null) {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = data.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            connection.connect();
            String content =  readHTTPContent(connection);
            connection.disconnect();

            return content;
        } catch (Exception ex){
            Main.logger.severe(String.format("HTTP Error: %s", ex.getMessage()));
            return null;
        }
    }


    private static String readHTTPContent(HttpsURLConnection connection) throws Exception {
        StringBuilder response = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }

        return response.toString().trim();
    }

    public static String get(String url, Map<String, String> headers) {
        return sendRequest(url, "GET", null, headers);
    }

    public static String post(String url, String data, Map<String, String> headers) {
        return sendRequest(url, "POST", data, headers);
    }

    public static String put(String url, String data, Map<String, String> headers) {
        return sendRequest(url, "PUT", data, headers);
    }

    public static String delete(String url, String data, Map<String, String> headers) {
        return sendRequest(url, "DELETE", data, headers);
    }

    public static String head(String url, Map<String, String> headers) {
        return sendRequest(url, "HEAD", null, headers);
    }

    public static String options(String url, String data, Map<String, String> headers) {
        return sendRequest(url, "OPTIONS", data, headers);
    }

    public static String trace(String url, String data, Map<String, String> headers) {
        return sendRequest(url, "TRACE", data, headers);
    }
}