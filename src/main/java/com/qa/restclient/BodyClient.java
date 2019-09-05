package com.qa.restclient;



import com.alibaba.fastjson.JSONObject;
import com.qa.base.TestBase;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;


public class BodyClient extends TestBase {
    final static Logger Log = Logger.getLogger(BodyClient.class.getClass());
    private static String mImageType = "multipart/form-data";
    public static OkHttpClient mClient;
    //post Json提交格式
    public static String postByJson(String url, String json) {
        String result = null;
        //建立Okhttp
        if (mClient == null) {
            mClient = getUnsafeOkHttpClient();
        }
        MediaType mediaType = MediaType.parse("application/json");
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody =RequestBody.create(mediaType, json);
        com.qa.base.Logger.info("此时的请求参数：\n"+json+"\n");
        Request request;
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

    //post dataJson提交格式
    public static String postBydataJson(String url, String json) {
        String result = null;
        //建立Okhttp
        if (mClient == null) {
            mClient = getUnsafeOkHttpClient();
        }
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody =RequestBody.create(mediaType,"data\\="+json);
        com.qa.base.Logger.info("此时的请求参数：\n"+json+"\n");
        Request request;
        if(json.contains("token")){  //有的请求头会带token，这里加个判断，如果传参的json要求带token，自动将token所带的属性值赋给属性头
            String token=JSONObject.parseObject(json).getString("token");
            request = new Request.Builder().url(url)//请求的url
                    .post(requestBody)
                    .addHeader("token", token)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
        }else{
            request = new Request.Builder().url(url)//请求的url
                    .post(requestBody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
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
    public static String postByForm(String url, Map<String, String> querys) {
        // 建立OKhttp
        if (mClient == null) {
            mClient = getUnsafeOkHttpClient();
        }

        Builder builder = new Builder();
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

        Request request = new Request.Builder().url(url).post(formBody).build();
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
    private static String buildUrl(String url, Map<String, String> querys) {
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
    public static String get(String url, Map<String, String> querys) {

        String result = null;
        // 组装参数
        String newUrl = buildUrl(url, querys);
        // 建立OKhttp
        if (mClient == null) {
            mClient = getUnsafeOkHttpClient();
        }

        Request request = new Request.Builder().url(newUrl).build();

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
