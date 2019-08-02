package com.qa.restclient;


import com.qa.base.TestBase;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.commons.httpclient.methods.PostMethod;


import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class RestClient extends TestBase {
    final static Logger Log = Logger.getLogger(RestClient.class.getClass());

//get接口带header
public String doGet(String url , String token) throws IOException{
   HttpClient client=new HttpClient();
   String uri=url+"?dataJson=";

    String toke= URLEncoder.encode(token,"UTF-8");
   GetMethod get=new GetMethod(uri+toke);
    get.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
    get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
    get.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
   String body="";
    try{
        //执行postMethod
        int statusCode = client.executeMethod(get);
        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed:" + get.getStatusLine());
        }
        //读取内容
        byte[] responseBody = get.getResponseBody();
        //处理内容
        body = new String(responseBody);
    }catch (HttpException e){
        //发生致命的异常，可能是协议不对或者返回的内容有问题
        System.out.println("Please check your provided http address!");
        body = "";
        e.printStackTrace();
    }catch(IOException e){
        //发生网络异常
        body = "";
        e.printStackTrace();
    }finally{
        //释放连接
        get.releaseConnection();
    }
    System.out.println(body);
    return body;

}
    //post接口 datajson提交参数
    public String doPost(String url, String json)  throws HttpException, IOException{
            String body = "";
            //创建httpclient对象
        HttpClient client=new HttpClient();
        String httpUrl=url;
            //创建post方式请求对象
            PostMethod httpPost = new PostMethod(httpUrl);
            //设置头信息
        httpPost.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        httpPost.addRequestHeader("Content-Type", "application/json");
            //装填参数
        httpPost.setParameter("dataJson",json);

         httpPost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try{
            //执行postMethod
            int statusCode = client.executeMethod(httpPost);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed:" + httpPost.getStatusLine());
            }
            //读取内容
            byte[] responseBody = httpPost.getResponseBody();
            //处理内容
            body = new String(responseBody);
        }catch (HttpException e){
            //发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            body = "";
            e.printStackTrace();
        }catch(IOException e){
            //发生网络异常
            body = "";
            e.printStackTrace();
        }finally{
            //释放连接
            httpPost.releaseConnection();
        }
   //     System.out.println(body);
        return body;

}

    //httpUrlConnection连接方式发送put请求
    static public String doPut(String restfulUrl, String params) {
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpPut requestPut = new HttpPut(restfulUrl);
        requestPut.setHeader("Accept", "application/json, application/xml, text/html, text/*, image/*, */*");
        final List<BasicNameValuePair> putData = new ArrayList<BasicNameValuePair>();
            putData.add(new BasicNameValuePair("dataJson",params));

        if (params != null && !params.isEmpty()) {
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(putData);
                requestPut.setEntity(entity);
            } catch (UnsupportedEncodingException ignore) {
                ignore.printStackTrace();
            }
        }
        String result = "";
        try {
            HttpResponse httpResponse = client.execute(requestPut);
            if (httpResponse == null)
                return result;
            if (httpResponse.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK)
                result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException ignore) {
            ignore.printStackTrace();

        } finally {
            if (client != null)
                client.getConnectionManager().shutdown();
        }
        return result;
    }
}
