package com.sheygam.java_221_04_11_18;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpProvider {
    private static final String BASE_URL = "https://contacts-telran.herokuapp.com";
    private OkHttpClient client;
    private MediaType JSON;
    private Gson gson;
    private static final HttpProvider ourInstance = new HttpProvider();

    public static HttpProvider getInstance() {
        return ourInstance;
    }

    private HttpProvider() {
//        client = new OkHttpClient();
        client = new OkHttpClient.Builder()
                .readTimeout(15,TimeUnit.SECONDS)
                .connectTimeout(15,TimeUnit.SECONDS)
                .build();

        JSON = MediaType.parse("application/json; charset=utf-8");
        gson = new Gson();
    }

    public TokenModel registration(String email, String password) throws Exception {
        AuthModel authModel = new AuthModel(email,password);
        String json = gson.toJson(authModel);
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(BASE_URL +"/api/registration")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseJson = responseBody.string();
                TokenModel model = gson.fromJson(responseJson,TokenModel.class);
                return model;
            }else{
                throw new Exception("Something was wrong! try again!");
            }
        }else if(response.code() == 409){
            String responseJson = response.body().string();
            ErrorResponseModel error = gson.fromJson(responseJson,ErrorResponseModel.class);
            throw new Exception(error.getMessage());
        }else{
            String responseJson = response.body().string();
            Log.d("MY_TAG", "registration error: " + responseJson);
            throw new Exception("Server error! Call to support!");
        }
    }

    public TokenModel login(String email, String password) throws Exception {
        AuthModel auth = new AuthModel(email,password);
        String body = gson.toJson(auth);
        URL url = new URL(BASE_URL+"/api/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Authorization","toke");
        connection.setRequestProperty("Content-type","application/json");

//        connection.connect();
        OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write(body);
        writer.flush();
        writer.close();

        if(connection.getResponseCode() >= 200 && connection.getResponseCode() < 300){
            String line;
            String str = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line = reader.readLine()) != null){
                str += line;
            }
            reader.close();
            TokenModel tokenModel = gson.fromJson(str,TokenModel.class);
            return tokenModel;
        }else{
            String line;
            String str = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while((line = reader.readLine()) != null){
                str += line;
            }
            reader.close();
            ErrorResponseModel model = gson.fromJson(str,ErrorResponseModel.class);
            Log.d("MY_TAG", "login error: " + model.toString());
            throw new Exception(model.getMessage());
        }
    }
}
