package com.example.bruno.myapplication.retrofit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

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
public class Pet {

    @NonNull
    @PrimaryKey
    private Integer id_pet;
    @NonNull
    private Integer id_user;
    private String nome;
    private Integer idade;
    private String especie;
    private String raca;
    private Double tamanho;
    private Double peso;
    private Integer sexo;
    private Boolean vacinado;
    private Boolean castrado;
    private String imagem;
    private String outros;
    private String createdAt;
    private String updatedAt;

    @Ignore
    private Bitmap image;

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    @NonNull
    public Integer getId_pet() {
        return id_pet;
    }

    public void setId_pet(@NonNull Integer id_pet) {
        this.id_pet = id_pet;
    }

    @NonNull
    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(@NonNull Integer id_user) {
        this.id_user = id_user;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Double getTamanho() {
        return tamanho;
    }

    public void setTamanho(Double tamanho) {
        this.tamanho = tamanho;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getSexo() {
        return sexo;
    }

    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }

    public Boolean getVacinado() {
        return vacinado;
    }

    public void setVacinado(Boolean vacinado) {
        this.vacinado = vacinado;
    }

    public Boolean getCastrado() {
        return castrado;
    }

    public void setCastrado(Boolean castrado) {
        this.castrado = castrado;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getOutros() {
        return outros;
    }

    public void setOutros(String outros) {
        this.outros = outros;
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

    public String getSexoAsString() {
        return this.sexo == 1 ? "Macho" : "Fêmea";
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
