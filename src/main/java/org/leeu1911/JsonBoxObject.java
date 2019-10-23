package org.leeu1911;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class JsonBoxObject extends ApiResource {
    public static <T> T create(Object object, Class<T> clazz) {
        String jsonObject = GSON.toJson(object);
        HttpResponse response = request("POST", jsonObject);
        if (response.getStatusCode() > 299) {
            return null;
        }
        return GSON.fromJson(response.getResponseBody(), clazz);
    }

    public static <T> T create(String collection, Object object, Class<T> clazz) {
        String jsonObject = GSON.toJson(object);
        HttpResponse response = requestWithPath(collection, "POST", jsonObject);
        if (response.getStatusCode() > 299) {
            return null;
        }
        return GSON.fromJson(response.getResponseBody(), clazz);
    }

    public static <T> T findById(String recordId, Class<T> clazz) {
        HttpResponse response = get(recordId, null);
        if (response.getStatusCode() > 299) {
            return null;
        }
        return GSON.fromJson(response.getResponseBody(), clazz);
    }

    public static <T> List<T> findAll(String collectionName) {
        HttpResponse response = get(collectionName, null);
        return parseHttpResponse(response);
    }

    public static <T> List<T> findAll(String collectionName, String orderBy, String orderDirection) {
        String sort = createSortParameter(orderBy, orderDirection);
        queryParameters = new LinkedHashMap<String, Object>();
        queryParameters.put("sort", sort);

        HttpResponse response = get(collectionName, queryParameters);
        return parseHttpResponse(response);
    }

    public static <T> List<T> findAll(String collectionName, int page, int size) {
        int skip = createSkipParameter(page, size);
        queryParameters = new LinkedHashMap<String, Object>();
        queryParameters.put("skip", skip);
        queryParameters.put("limit", size);

        HttpResponse response = get(collectionName, queryParameters);
        return parseHttpResponse(response);
    }

    public static <T> List<T> findAll(String collectionName, String orderBy, String orderDirection, int page, int size) {
        String sort = createSortParameter(orderBy, orderDirection);
        int skip = createSkipParameter(page, size);
        queryParameters = new LinkedHashMap<String, Object>();
        queryParameters.put("sort", sort);
        queryParameters.put("skip", skip);
        queryParameters.put("limit", size);

        HttpResponse response = get(collectionName, queryParameters);
        return parseHttpResponse(response);
    }

    public static boolean update(String recordId, Object object) {
        String jsonObject = GSON.toJson(object);
        HttpResponse response = put(recordId, jsonObject);
        return response.getStatusCode() < 300;
    }

    public static boolean deleteById(String recordId) {
        HttpResponse response = delete(recordId);
        return response.getStatusCode() < 300;
    }
}
