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

    static String request(String requestMethod, String requestBody) {
        assert JsonBox.getApiBase() != null;
        assert JsonBox.boxId != null;
        HttpURLConnection connection;
        try {
            URL url = new URL(JsonBox.getApiBase() + "/" + JsonBox.boxId);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(requestBody);
            osw.flush();
            osw.close();
            os.close();
            connection.connect();

            int status = connection.getResponseCode();
            BufferedInputStream bis;
            String result;
            if (status > 299) {
                bis = new BufferedInputStream(connection.getErrorStream());
            }else {
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
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
