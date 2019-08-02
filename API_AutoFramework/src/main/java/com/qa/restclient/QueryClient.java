package com.qa.restclient;

import com.alibaba.fastjson.JSONObject;
import com.qa.base.TestBase;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.helper.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class QueryClient extends TestBase {
    /**
     * HTTP发送post、put请求，带header、body的方法，获取结果
     *
     * @param url
     * @return
     * @author yswKnight
     */
    public String doPost(String url, String json)  throws HttpException, IOException{
        String body = "";
        //创建httpclient对象
        HttpClient client=new HttpClient();
        //参数真伪json格式传参
        Map<String,Object> paramsMap=JSONObject.parseObject(json,Map.class);
        //创建post方式请求对象
        PostMethod httpPost = new PostMethod(url);
        Header header=new Header();
        header.setName("Content-Type");
        header.setValue("text/html;charset=UTF-8");
        httpPost.setRequestHeader(header);
        httpPost.addParameter("requestBody",json);
        httpPost.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        //设置头信息
       // httpPost.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        httpPost.setRequestHeader("Content-Type", "application/json");
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

    public static String httpRequest(String url,String json) {
        JSONObject contentMap=JSONObject.parseObject(json);
        Map<String, String> headerMap=new HashMap<String, String>();
        headerMap.put("Content-Type","application/json; charset=utf-8");
        PostMethod postMethod=new PostMethod(url);

        String result = "";
        try {
            URL restURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) restURL.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type","application/json; charset=utf-8");


            connection.setDoInput(true);
            connection.setDoOutput(true);
            Iterator headerIterator = headerMap.entrySet().iterator();          //循环增加header
            while (headerIterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) headerIterator.next();
                connection.setRequestProperty(elem.getKey(), elem.getValue());
            }

            OutputStreamWriter outer = null;
            outer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            outer.write(contentMap.toString());
            outer.flush();
            outer.close();
            InputStream ips = connection.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
                buffer.append("\r\n");
            }
            in.close();
            ips.close();
            connection.disconnect();
            //得到结果
            result = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}