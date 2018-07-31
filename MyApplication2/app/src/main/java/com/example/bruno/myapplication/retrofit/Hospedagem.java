package com.example.bruno.myapplication.retrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Hospedagem {

    private Integer id;
    private Integer id_user_pedinte;
    private Integer id_user_hospedador;

    private String dataInicio;
    private String dataFim;
    private String id_pets;
    private Integer status;
    private Double nota;
    private String comentario;
    private String createdAt;
    private String updatedAt;


    private String primeiroNome;
    private String ultimoNome;
    private String imagem;

    private List<Pet> pets;

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_user_pedinte() {
        return id_user_pedinte;
    }

    public void setId_user_pedinte(Integer id_user_pedinte) {
        this.id_user_pedinte = id_user_pedinte;
    }

    public Integer getId_user_hospedador() {
        return id_user_hospedador;
    }

    public void setId_user_hospedador(Integer id_user_hospedador) {
        this.id_user_hospedador = id_user_hospedador;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getId_pets() {
        return id_pets;
    }

    public void setId_pets(String id_pets) {
        this.id_pets = id_pets;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public String getNomeCompleto() {
        return primeiroNome + ' ' + ultimoNome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

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

    public void setFieldsByJson(JSONObject json) {
        if (json == null)
            return;

        try {
            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field : fields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    if (json.has(field.getName()) && !json.isNull(field.getName()))
                        field.set(this, json.get(field.getName()));
                }
            }
        }catch (JSONException e1) {
            e1.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
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

    public void updateFields(Class object) {
        if (this.getClass().equals(object.getClass())){
            try {
                Field[] fields = object.getClass().getDeclaredFields();

                for (Field field : fields) {
                    if (Modifier.isPrivate(field.getModifiers())) {
                        Object fieldValue = field.get(object);

                        if (fieldValue != null)
                            field.set(this, fieldValue);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private RequestBody createPartFromObject(Object object) {
        if (object instanceof String)
            return RequestBody.create(
                    okhttp3.MultipartBody.FORM, (String)object);
        if (object instanceof Integer)
            return RequestBody.create(
                    MultipartBody.FORM, Integer.toString((Integer)object));
        if (object instanceof Date) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            return RequestBody.create(
                    okhttp3.MultipartBody.FORM, df.format((Date)object));
        }
        if (object instanceof Double)
            return RequestBody.create(
                    okhttp3.MultipartBody.FORM, Double.toString((Double)object));
        if (object instanceof Boolean)
            return RequestBody.create(
                    okhttp3.MultipartBody.FORM, Boolean.toString((Boolean)object));

        return null;
    }

    public HashMap<String, RequestBody> getHashMapStringRequestBody() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            HashMap<String, RequestBody> hashMap = new HashMap<>();

            for (Field field : fields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    Object fieldValue = field.get(this);

                    if (fieldValue != null) {
                        RequestBody requestBody = createPartFromObject(fieldValue);

                        if (requestBody != null)
                            hashMap.put(field.getName(), requestBody);
                    }
                }
            }

            return hashMap;
        } catch (IllegalAccessException e) {
            e.printStackTrace();

            return new HashMap<>();
        }
    }
}
