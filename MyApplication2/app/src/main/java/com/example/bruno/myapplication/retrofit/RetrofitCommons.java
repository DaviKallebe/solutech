package com.example.bruno.myapplication.retrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RetrofitCommons {

    public JSONObject getFieldsJson() throws IllegalAccessException, JSONException {
        JSONObject json = new JSONObject();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field: fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                String name = field.getName();
                Object value = field.get(this);

                if (value != null)
                    json.put(name, value);
            }
        }

        return json;
    }

    public RequestBody generateRequestBody() {
        try {
            return RequestBody
                    .create(MediaType.parse("application/json; charset=utf-8"),
                            this.getFieldsJson().toString());

        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }

        return null;
    }

    public void updateFields(Hospedador hospedador) {
        try {
            Field[] fields = hospedador.getClass().getDeclaredFields();

            for (Field field : fields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    Object fieldValue = field.get(hospedador);

                    if (fieldValue != null)
                        field.set(this, fieldValue);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



}
