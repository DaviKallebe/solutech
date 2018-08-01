package com.example.bruno.myapplication.retrofit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@Entity
public class Logradouro {

    @PrimaryKey
    private Integer id_lugar;
    private String cep;
    private String rua;
    private String estado;
    private String cidade;
    private String bairro;
    private Integer numero;
    private String complemento;
    private String tipo;
    private String logradouro;
    private Double latitude;
    private Double longitude;
    private String descricao;
    private String imagem;
    private Integer id_user;

    private String createdAt;
    private String updatedAt;

    public Integer getId_lugar() {
        return id_lugar;
    }

    public void setId_lugar(Integer id_lugar) {
        this.id_lugar = id_lugar;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
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

    public JSONObject getFieldsJson() {
        JSONObject json = new JSONObject();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field: fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                try {
                    String name = field.getName();
                    Object value = field.get(this);

                    if (value != null)
                        json.put(name, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
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
        String body = this.getFieldsJson().toString();

        if (body != null && !body.equals("")) {
            return RequestBody
                    .create(MediaType.parse("application/json; charset=utf-8"), body);
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
