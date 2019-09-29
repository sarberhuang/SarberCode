package com.qa.restclient;


import com.alibaba.fastjson.JSONObject;

import okhttp3.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.commons.httpclient.methods.PostMethod;


import javax.net.ssl.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RestClient  {
    final static Logger Log = Logger.getLogger(RestClient.class.getClass());
    private static String mImageType = "multipart/form-data";
    public static OkHttpClient mClient;
//get接口带header
    public String doGetByDataJson(String url , String json) throws IOException{
        HttpClient client=new HttpClient();
        //拼接请求头
        String uri=url+"?dataJson=";
        String param= URLEncoder.encode(json,"UTF-8");
        GetMethod get=new GetMethod(uri+param);
        com.qa.base.Logger.info("此时的请求参数：\n"+json+"\n");
        String logintoken="";
        logintoken= JSONObject.parseObject(json).getString("token");
        get.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        get.addRequestHeader("token",logintoken);
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

        return body;

    }
    //post接口 datajson提交参数
    public String doPostByDataJson(String url, String json)  throws HttpException, IOException{
        String body = "";
        //创建httpclient对象
        HttpClient client=new HttpClient();
        String httpUrl=url;
        //创建post方式请求对象
        PostMethod httpPost = new PostMethod(httpUrl);
        //设置头信息
        String logintoken="";
        if(json.contains("token")){
            logintoken=JSONObject.parseObject(json).getString("token");}
        httpPost.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        httpPost.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.addRequestHeader("token", logintoken);
        //装填参数
        httpPost.setParameter("dataJson",json);
        com.qa.base.Logger.info("此时的请求参数：\n"+json+"\n");
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

    //okhttp method
    public static String postByJson(String url, String json) {
        String result = null;
        //建立Okhttp
        if (mClient == null) {
            mClient = getUnsafeOkHttpClient();
        }
        MediaType mediaType;     //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody;    //请求参数body形式
        Request request;            //请求体
        mediaType= MediaType.parse("application/json");
        requestBody=RequestBody.create(mediaType, json);
        com.qa.base.Logger.info("此时的请求参数：\n"+json+"\n");
        if(json.contains("token")){  //有的请求头会带token，这里加个判断，如果传参的json要求带token，自动将token所带的属性值赋给属性头
            String token=JSONObject.parseObject(json).getString("token");
            request = new Request.Builder().url(url)//请求的url
                    .post(requestBody)
                    .addHeader("token", token)
                    .build();
        }else{
            request = new Request.Builder().url(url)//请求的url
                    .post(requestBody)

                    .build();
        }
        try {
            //执行post请求，返回result字符串
            Response response = mClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            result = "failed";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * post 提交表单方式  目前还用不上，暂时先放这
     * */
    public static String postByForm(String url, Map<String, String> querys) throws  Exception{
        // 建立OKhttp
        if (mClient == null) {
            mClient = getUnsafeOkHttpClient();
        }
        String token;
        token=querys.get("token");
        querys.remove("token");
        FormBody.Builder builder = new FormBody.Builder();
        try {
            if (null != querys) {
                for (Map.Entry<String, String> query : querys.entrySet()) {
                    if (!StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                        builder.add(query.getKey(), URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).addHeader("token", token).post(formBody).build();
        String result = "";
        try {
            Response response = mClient.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 如果需要上传其他参数自己添加设置
     *            上传的图片的全路径
     * @throws Exception
     */
    // public static Map<String, String> PostImage(String imgUrl) throws Exception {
    // 	logger.info("请求图片生成url:{}", imgUrl);
    // 	// 建立OKhttp
    // 	if (mClient == null) {
    // 		mClient = getUnsafeOkHttpClient();
    // 	}
    // 	Map<String, String> map = new HashMap<>();
    // 	try {
    // 		//从网络上获取图片bytes
    // 		Request urlRequest = new Request.Builder().url(imgUrl).get().addHeader("Content-Type", "text/plain")
    // 						.addHeader("Cache-Control", "no-cache")
    // 						.addHeader("Postman-Token", UUID.randomUUID().toString()).build();
    // 		Response urlResponse = mClient.newCall(urlRequest).execute();
    // 		RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), urlResponse.body().bytes());
    //
    // 		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
    // 						// 可以根据自己的接口需求在这里添加上传的参数
    // 						.addFormDataPart("image", "", fileBody).addFormDataPart("imagetype", mImageType).build();
    // 		//"http://192.168.1.240:51001"
    // 		Request request = new Request.Builder().url(ConfigMap.get(AppParamConstant.SAASOSURL) + "/api/image/save")
    // 						.post(requestBody).build();
    //
    // 		//上传图片到阿里云
    // 		logger.info("上传图片到阿里云url:{}", imgUrl);
    // 		Response response;
    // 		response = mClient.newCall(request).execute();
    // 		String jsonString = response.body().string();
    // 		if (!response.isSuccessful()) {
    // 			// 上传失败
    // 			map.put("code", "failed");
    // 		} else {
    // 			JSONObject jsonObject = JSONObject.parseObject(jsonString);
    // 			if (jsonObject != null) {
    // 				map.put("code", "success");
    // 				map.put("id", jsonObject.getString("id"));
    // 				map.put("url", jsonObject.getString("url"));
    // 			}
    // 		}
    //
    // 	} catch (Exception e) {
    // 		logger.info("======上传异常====" + e.getMessage());
    // 		map.put("code", "failed");
    // 	}
    // 	return map;
    // }

    //拼接URL和get的参数
    public static String buildUrl(String url, Map<String, String> querys) {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(url);
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!org.apache.commons.lang.StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        try {
                            sbQuery.append(query.getValue());
                            //sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }
    //query get传参方式
    public static String getByJson(String url, Map<String, String> querys) {

        String result = null;
        // 组装参数
        String newUrl = buildUrl(url, querys);
        // 建立OKhttp
        if (mClient == null) {
            mClient = getUnsafeOkHttpClient();
        }

        Request request = new Request.Builder().url(newUrl)
                    .get()
                                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = mClient.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //添加信任所有证书/忽略证书验证
    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
