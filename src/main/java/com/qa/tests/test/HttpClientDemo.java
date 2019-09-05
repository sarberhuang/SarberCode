package com.qa.tests.test;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public class HttpClientDemo {
    public static void main(String[] args) throws Exception {
        getResoucesByLoginCookies();
    }
    private  static  void  getResoucesByLoginCookies()throws Exception{
        HttpClientDemo demo=new HttpClientDemo();
        String loginTel="13133706292";
        String loginPwd="123456";
        String freeLogin="false";
        String urlLogin="https://www.wenwo.com/index/checkDoctorLogin?phoneNumber=" + loginTel + "&password=" + loginPwd;
        String urlAfter="https://www.wenwo.com/patient/myPatient";
        DefaultHttpClient client=new DefaultHttpClient(new PoolingClientConnectionManager());
        HttpPost post = new HttpPost(urlLogin);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        CookieStore cookieStore = client.getCookieStore();
        client.setCookieStore(cookieStore);

        HttpGet get = new HttpGet(urlAfter);
        response = client.execute(get);
        entity = response.getEntity();

    }
}
