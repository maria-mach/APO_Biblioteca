package com.apo2.biblioteca.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

/**
 * Utilitario para serializacao e desserializacao de JSON.
 * Utiliza o framework Gson sob as politicas de datas padronizadas para a aplicacao.
 * 
 * @version 1.0
 */
public class JsonConverter {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonSerializer<LocalDate>)
                    (src, typeOfSrc, context) -> new com.google.gson.JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonDeserializer<LocalDate>)
                    (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
            .create();

    /**
     * Converte um objeto Java para uma string no formato JSON.
     * 
     * @param obj Objeto a ser serializado.
     * @return String JSON representativa.
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Converte uma string JSON de volta para um objeto Java de classe correspondente.
     * 
     * @param <T>   Tipo do objeto de retorno.
     * @param json  String em formato JSON.
     * @param clazz Classe destino para mapeamento do objeto.
     * @return Objeto Java instanciado e preenchido.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
