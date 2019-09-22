package org.leeu1911;

public abstract class JsonBoxObject extends ApiResource {
    public static String create(String jsonObject) {
        String result = request("POST", jsonObject);
        return result;
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
