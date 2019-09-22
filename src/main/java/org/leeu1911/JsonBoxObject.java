package org.leeu1911;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public static <T> T findById(String recordId, Class<T> clazz){
        HttpResponse response = get(recordId);
        if (response.getStatusCode() > 299) {
            return null;
        }
        return GSON.fromJson(response.getResponseBody(), clazz);
    }

    public static <T> List<T> findAll(String collectionName){
        HttpResponse response = get(collectionName);
        if (response.getStatusCode() > 299) {
            return null;
        }
        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        return GSON.fromJson(response.getResponseBody(), listType);
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
