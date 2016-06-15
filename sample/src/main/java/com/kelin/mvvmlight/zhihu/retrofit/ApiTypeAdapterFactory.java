package com.kelin.mvvmlight.zhihu.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by dingzhihu on 15/5/7.
 */
public class ApiTypeAdapterFactory implements TypeAdapterFactory {
    private String dataElementName;

    public ApiTypeAdapterFactory(String dataElementName) {
        this.dataElementName = dataElementName;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementTypeAdapter = gson.getAdapter(JsonElement.class);


        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementTypeAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("status")) {
                        int status = jsonObject.get("status").getAsInt();
                        String message = jsonObject.get("message").getAsString();
                        if (status == 0) {
                            //do nothing
                        } else {
                            throw new ApiException(status, message);
                        }
                    }
                    if (jsonObject.has(dataElementName)) {
                        jsonElement = jsonObject.get(dataElementName);
                    }
                }
                return delegate.fromJsonTree(jsonElement);
            }

        }.nullSafe();
    }
}
