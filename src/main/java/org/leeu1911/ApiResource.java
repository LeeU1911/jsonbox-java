package org.leeu1911;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ApiResource {
    public static final Gson GSON = createGson();
    public static HashMap<String, Object> queryParameters;

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

    static HttpResponse get(String id, HashMap<String, Object> queryParameters) {
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId + "/" + id;
        if (queryParameters != null) {
            uri = uri + "?" + getQueryParameters(queryParameters);
        }
        return request(uri, "GET", null);
    }

    static HttpResponse get(String filterQuery) {
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId + filterQuery;
        return request(uri, "GET", null);
    }

    static HttpResponse put(String id, String requestBody) {
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId + "/" + id;
        return request(uri, "PUT", requestBody);
    }

    static HttpResponse delete(String id) {
        String uri = JsonBox.getApiBase() + "/" + JsonBox.boxId + "/" + id;
        return request(uri, "DELETE", null);
    }

    public static String getQueryParameters(HashMap<String, Object> queryParameters) {
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, Object> parameter : queryParameters.entrySet()) {
            if (data.length() != 0) {
                data.append('&');
            }
            try {
                data.append(URLEncoder.encode(parameter.getKey(), "UTF-8"));
                data.append('=');
                data.append(URLEncoder.encode(String.valueOf(parameter.getValue()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return data.toString();
    }

    public static <T> List<T> parseHttpResponse(HttpResponse response) {
        if (response.getStatusCode() > 299) {
            return null;
        }
        Type listType = new TypeToken<ArrayList<T>>() {}.getType();
        return GSON.fromJson(response.getResponseBody(), listType);
    }

    public static String createSortParameter(String orderBy, String orderDirection) {
        if (orderBy == null) {
            throw new IllegalArgumentException("OrderBy parameter must be non-null");
        }
        if (orderDirection == null) {
            throw new IllegalArgumentException("OrderDirection parameter must be non-null");
        }
        if (!"ASC".equals(orderDirection) && !"DESC".equals(orderDirection)) {
            throw new IllegalArgumentException("OrderDirection parameter must be ASC or DESC, received " + orderDirection);
        }

        if (orderDirection.equals("DESC")) {
            orderBy = "-" + orderBy;
        }
        return orderBy;
    }

    public static int createSkipParameter(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page parameter must be non-negative, received " + page);
        }
        if (size < 0) {
            throw new IllegalArgumentException("Size parameter must be non-negative, received " + size);
        }
        return page * size;
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
