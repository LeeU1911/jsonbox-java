package org.leeu1911;

public abstract class JsonBoxObject extends ApiResource {
    public static <T> T create(Object object, Class<T> clazz) {
        String jsonObject = GSON.toJson(object);
        return GSON.fromJson(request("POST", jsonObject), clazz);
    }

    public static String findById(String recordId){
        return "";
    }

    public static String findAll(String collectionName){
        return "";
    }

    public static String update(String object) {
        return "";
    }

    public static void delete(String recordId) {

    }
}
