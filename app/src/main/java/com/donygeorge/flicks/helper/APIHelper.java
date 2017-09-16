package com.donygeorge.flicks.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class APIHelper {

    public static interface APICallback {
        abstract void onSuccess(JSONObject object);
        abstract void onFailure();
    }

    private static void queryObject(String baseURL, HashMap<String, String> params, final APICallback apiCallback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
        urlBuilder.addQueryParameter("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed");
        if (params != null) {
            for (String key : params.keySet()) {
                urlBuilder.addQueryParameter(key, params.get(key));
            }
        }
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        SingleHttpClient.getInstance().newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        apiCallback.onFailure();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            apiCallback.onFailure();
                        }

                        String responseData = response.body().string();
                        try {
                            JSONObject json = new JSONObject(responseData);
                            apiCallback.onSuccess(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            apiCallback.onFailure();
                        }
                    }
                });
    }


    public static void queryMovies(int page, final APICallback apiCallback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", "" + page);
        queryObject("https://api.themoviedb.org/3/movie/now_playing", map, apiCallback);
    }

    public static void queryVideos(int id, final APICallback apiCallback) {
        queryObject("https://api.themoviedb.org/3/movie/" + id + "/videos", null, apiCallback);
    }
}
