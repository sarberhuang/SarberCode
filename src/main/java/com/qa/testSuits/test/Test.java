package com.qa.testSuits.test;

import okhttp3.*;

public class Test {
  public static void main(String[] args) throws Exception {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
    RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"mobile\"\r\n\r\n13133706290\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
    Request request = new Request.Builder()
            .url("http://192.168.255.207:55099/user/bindMobile")
            .post(body)
            .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
            .addHeader("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NzAwOTUwNTA2NDEsInBheWxvYWQiOiJcIjEzMTMzNzA2MjkyXCIifQ.gW01BsDvKuLXc85pgAiYxcOmUUQ6R7vjLC0zU5BqS7E")
            .addHeader("User-Agent", "PostmanRuntime/7.17.1")
            .addHeader("Accept", "*/*")
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Postman-Token", "eed70914-4327-48f8-8ba1-02ba00adbb4b,e46ded3d-3690-4827-b5e6-22ddf59eaa17")
            .addHeader("Host", "192.168.255.207:55099")
            .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------397151715147174562898763")
            .addHeader("Accept-Encoding", "gzip, deflate")
            .addHeader("Content-Length", "172")
            .addHeader("Connection", "keep-alive")
            .addHeader("cache-control", "no-cache")
            .build();
    Response response = client.newCall(request).execute();
    System.out.println(response);
  }
}
