package com.example.bruno.myapplication.retrofit;

public class RouteList {
    public static Integer port = 3000;
    public static String host = "localhost";
    public static String baseUrl = "http://%s:%d/%s";
    public static String normalLogin = "login";

    public static String getBaseUrl() {
        return String.format("http://%s:%d", host, port);
    }

    public static String doNormalLogin() {
        return String.format(baseUrl, host, port, normalLogin);
    }
}
