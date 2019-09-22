package org.leeu1911;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public abstract class ApiResource {
    public static final Gson GSON = createGson();

    private static Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    static HttpResponse request(String requestMethod, String requestBody) {
        assert JsonBox.getApiBase() != null;
        assert JsonBox.boxId != null;
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId;
        return request(uri, requestMethod, requestBody);
    }

    static HttpResponse requestWithPath(String path, String requestMethod, String requestBody) {
        assert JsonBox.getApiBase() != null;
        assert JsonBox.boxId != null;
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId + "/" + path;
        return request(uri, requestMethod, requestBody);
    }

    private static HttpResponse request(String uri, String requestMethod, String requestBody) {
        HttpURLConnection connection;
        try {
            URL url = new URL(uri);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            if (requestBody != null) {
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(requestBody);
                osw.flush();
                osw.close();
                os.close();
            }
            connection.connect();

            int status = connection.getResponseCode();
            BufferedInputStream bis;
            String result;
            if (status > 299) {
                bis = new BufferedInputStream(connection.getErrorStream());
            } else {
                bis = new BufferedInputStream(connection.getInputStream());
            }
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int byteResult = bis.read();
            while (byteResult != -1) {
                buf.write((byte) byteResult);
                byteResult = bis.read();
            }
            result = buf.toString();
            connection.disconnect();
            return new HttpResponse(status, result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpResponse(-1, "");
    }

    static HttpResponse get(String id) {
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId + "/" + id;
        return request(uri, "GET", null);
    }

    static HttpResponse put(String id, String requestBody){
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId + "/" + id;
        return request(uri, "PUT", requestBody);
    }

    static HttpResponse delete(String id) {
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId + "/" + id;
        return request(uri, "DELETE", null);
    }

    static class HttpResponse {
        private int statusCode;

        public int getStatusCode() {
            return this.statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        private String responseBody;

        public String getResponseBody() {
            return this.responseBody;
        }

        public void setResponseBody(String responseBody) {
            this.responseBody = responseBody;
        }

        public HttpResponse(int statusCode, String responseBody) {
            this.statusCode = statusCode;
            this.responseBody = responseBody;
        }
    }
}
