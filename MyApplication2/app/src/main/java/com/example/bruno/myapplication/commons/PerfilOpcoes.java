package com.example.bruno.myapplication.commons;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerfilOpcoes<T extends Object> {
    private String name;
    private String field;
    private T value;
    private Integer typeField;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Integer getTypeField() {
        return typeField;
    }

    public void setTypeField(Integer typeField) {
        this.typeField = typeField;
    }

    public PerfilOpcoes(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public PerfilOpcoes(String name, String field, T value, Integer typeField) {
        this.name = name;
        this.field = field;
        this.value = value;
        this.typeField = typeField;
    }

    public String getValueAsString() {
        if (value == null)
            return null;

        if (typeField == null)
            return (String)value;

        if (typeField == 1 || typeField == 5 || typeField == 6)
            return (String)value;
        if (typeField == 2)
            return Integer.toString((Integer)value);
        if (typeField == 3)
            return Double.toString((Double)value);
        if (typeField == 4) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            Log.d("MyDate " + name, (String)value);

            try {
                return DateFormat.getDateInstance().format(format.parse((String)value));
            } catch (ParseException e) {
                Log.e("DATEPARSEERROR", e.getMessage());
            }
        }

        return null;
    }
}
