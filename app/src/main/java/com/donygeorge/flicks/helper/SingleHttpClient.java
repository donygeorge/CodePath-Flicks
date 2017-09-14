package com.donygeorge.flicks.helper;

import okhttp3.OkHttpClient;

public class SingleHttpClient extends OkHttpClient {
    private static SingleHttpClient instance = null;

    public static SingleHttpClient getInstance() {
        if (instance == null) {
            synchronized (SingleHttpClient.class) {
                if (instance == null) {
                    instance = new SingleHttpClient();
                }
            }
        }
        return instance;
    }
}
