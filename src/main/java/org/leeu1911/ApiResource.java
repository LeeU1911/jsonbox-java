package org.leeu1911;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class ApiResource {
    public static final Gson GSON = createGson();
    private static Gson createGson() {
        GsonBuilder builder =
                new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        return builder.create();
    }

    static String request(String requestMethod, String requestBody){
        return "";
    }
}
