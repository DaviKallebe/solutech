package com.example.bruno.myapplication.retrofit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
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
public class Hospedador {

    @NonNull
    @PrimaryKey
    private Integer id_user;
    private Integer id;

    private String primeiroNome;
    private String ultimoNome;
    private String nomeCompleto;
    private String rg;
    private String orgaoEmissor;
    private String cpf;
    private String telefone;
    private String nascimento;

    private Boolean cuidaIdoso;
    private Boolean cuidaFilhote;
    private Boolean cuidaFemea;
    private Boolean cuidaMacho;
    private Boolean cuidaCastrado;
    private Boolean cuidaPequeno;
    private Boolean cuidaGrande;
    private Boolean cuidaExotico;
    private Boolean cuidaCachorro;
    private Boolean cuidaGato;
    private Boolean cuidaMamifero;
    private Boolean cuidaReptil;
    private Boolean cuidaAve;
    private Boolean cuidaPeixe;

    private Integer likes;
    private Integer dislikes;

    private String preferenciaAnimal;
    private Integer quantidadeAnimal;

    private Integer tipoSupervisao;
    private Integer numeroComentario;
    private Integer totalLike;
    private Integer totalPLN;

    private Double preco;
    private Double precoExotico;
    private Double usuarioNota;

    private String descricao;
    private String imagem;
    private String createdAt;
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(@NonNull Integer id_user) {
        this.id_user = id_user;
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

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getCuidaIdoso() {
        return cuidaIdoso;
    }

    public void setCuidaIdoso(Boolean cuidaIdoso) {
        this.cuidaIdoso = cuidaIdoso;
    }

    public Boolean getCuidaFilhote() {
        return cuidaFilhote;
    }

    public void setCuidaFilhote(Boolean cuidaFilhote) {
        this.cuidaFilhote = cuidaFilhote;
    }

    public Boolean getCuidaFemea() {
        return cuidaFemea;
    }

    public void setCuidaFemea(Boolean cuidaFemea) {
        this.cuidaFemea = cuidaFemea;
    }

    public Boolean getCuidaMacho() {
        return cuidaMacho;
    }

    public void setCuidaMacho(Boolean cuidaMacho) {
        this.cuidaMacho = cuidaMacho;
    }

    public Boolean getCuidaCastrado() {
        return cuidaCastrado;
    }

    public void setCuidaCastrado(Boolean cuidaCastrado) {
        this.cuidaCastrado = cuidaCastrado;
    }

    public Boolean getCuidaPequeno() {
        return cuidaPequeno;
    }

    public void setCuidaPequeno(Boolean cuidaPequeno) {
        this.cuidaPequeno = cuidaPequeno;
    }

    public Boolean getCuidaGrande() {
        return cuidaGrande;
    }

    public void setCuidaGrande(Boolean cuidaGrande) {
        this.cuidaGrande = cuidaGrande;
    }

    public Boolean getCuidaExotico() {
        return cuidaExotico;
    }

    public void setCuidaExotico(Boolean cuidaExotico) {
        this.cuidaExotico = cuidaExotico;
    }

    public Boolean getCuidaCachorro() {
        return cuidaCachorro;
    }

    public void setCuidaCachorro(Boolean cuidaCachorro) {
        this.cuidaCachorro = cuidaCachorro;
    }

    public Boolean getCuidaGato() {
        return cuidaGato;
    }

    public void setCuidaGato(Boolean cuidaGato) {
        this.cuidaGato = cuidaGato;
    }

    public Boolean getCuidaMamifero() {
        return cuidaMamifero;
    }

    public void setCuidaMamifero(Boolean cuidaMamifero) {
        this.cuidaMamifero = cuidaMamifero;
    }

    public Boolean getCuidaReptil() {
        return cuidaReptil;
    }

    public void setCuidaReptil(Boolean cuidaReptil) {
        this.cuidaReptil = cuidaReptil;
    }

    public Boolean getCuidaAve() {
        return cuidaAve;
    }

    public void setCuidaAve(Boolean cuidaAve) {
        this.cuidaAve = cuidaAve;
    }

    public Boolean getCuidaPeixe() {
        return cuidaPeixe;
    }

    public void setCuidaPeixe(Boolean cuidaPeixe) {
        this.cuidaPeixe = cuidaPeixe;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public String getPreferenciaAnimal() {
        return preferenciaAnimal;
    }

    public void setPreferenciaAnimal(String preferenciaAnimal) {
        this.preferenciaAnimal = preferenciaAnimal;
    }

    public Integer getQuantidadeAnimal() {
        return quantidadeAnimal;
    }

    public void setQuantidadeAnimal(Integer quantidadeAnimal) {
        this.quantidadeAnimal = quantidadeAnimal;
    }

    public Integer getTipoSupervisao() {
        return tipoSupervisao;
    }

    public void setTipoSupervisao(Integer tipoSupervisao) {
        this.tipoSupervisao = tipoSupervisao;
    }

    public Integer getNumeroComentario() {
        return numeroComentario;
    }

    public void setNumeroComentario(Integer numeroComentario) {
        this.numeroComentario = numeroComentario;
    }

    public Integer getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Integer totalLike) {
        this.totalLike = totalLike;
    }

    public Integer getTotalPLN() {
        return totalPLN;
    }

    public void setTotalPLN(Integer totalPLN) {
        this.totalPLN = totalPLN;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getPrecoExotico() {
        return precoExotico;
    }

    public void setPrecoExotico(Double precoExotico) {
        this.precoExotico = precoExotico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getFullName() {
        return primeiroNome + ' ' + ultimoNome;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Double getUsuarioNota() {
        return usuarioNota;
    }

    public void setUsuarioNota(Double usuarioNota) {
        this.usuarioNota = usuarioNota;
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

    public <T extends Object> void updateFields(T object) {
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

    public HashMap<String, String> getHashMapStringString() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            HashMap<String, String> hashMap = new HashMap<>();

            for (Field field : fields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    Object fieldValue = field.get(this);

                    if (fieldValue != null)
                        hashMap.put(field.getName(), String.valueOf(fieldValue));
                }
            }

            return hashMap;
        } catch (IllegalAccessException e) {
            e.printStackTrace();

            return new HashMap<>();
        }
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

    public <T extends Object> Boolean isEqual(T object) {
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field: fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                try {
                    String name = field.getName();
                    Object value = field.get(this);
                    Field cmp = object.getClass().getField(name);

                    if (cmp == null || !cmp.get(object).equals(value))
                        return false;

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        return true;
    }
}
