package com.qa.tests.test;

import com.alibaba.fastjson.JSONObject;
import com.qa.base.AiwenAssert;
import com.qa.base.Logger;
import com.qa.base.TestBase;
import com.qa.restclient.BodyClient;
import com.qa.restclient.RestClient;
import com.qa.util.CreatData;
import com.qa.util.ExcelData;
import com.qa.util.TestUtil;
import jxl.read.biff.BiffException;
import org.junit.Before;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;


public class testCase2 extends TestBase {
    TestBase testBase;
    RestClient restClient;
    //host根url
    String host;            //
    //Excel路径
    String testCaseExcel="testCase1data.xls";
    String tokenPath;   //token在登陆成功之后相应的response
    String loginApi;        //登陆接口
   String loginToken;           //  登陆之后产生的ntoke
   String loginParameter;//登陆参数
    String method;      //登陆的请求方式
    //header
    HashMap<String ,String> postHeader = new HashMap<String, String>();
    @BeforeClass
    public void setUp() throws Exception {
        testBase = new TestBase();
        restClient = new RestClient();
        postHeader.put("Content-Type","application/json");
        System.out.println(getConfigDatas());//获取excel里面的配置host 登陆api 登陆账号 以及登陆的方式
        Map<String,String> data =(Map<String,String>)getConfigDatas()[0][0];
        Iterator iterator=data.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry=(Map.Entry)iterator.next();
            String key=(String) entry.getKey();
            if (key.equals("apiName")) {
                loginApi = (String) entry.getValue();  //将config里面的值 赋给loginApi
            }
            else if (key.equals("dataJson")) {
                loginParameter = (String) entry.getValue();  //将config里面的值 赋给loginParameter
            }
            else  if (key.equals("Host")) {
                host = (String) entry.getValue();    //将config里面的值 赋给host
            }
            else  if (key.equals("Method")) {
                method = (String) entry.getValue();
            }
            else if(key.equals("token")){
                loginToken=(String) entry.getValue();
            }
        }
        if(loginToken==null ||loginToken.equals("")){
            System.out.println("使用登陆参数方法进行获取token");
            //拿到登陆里面token数据的位置，生成全局变量token
            tokenPath = prop.getProperty("token");
            System.out.println("只运行一次登陆，并获取登陆token");
            login();
        }else{
            loginToken=loginToken;
            System.out.println("直接读取表里的token值设为logintoken的参数值");
        }
    }

    public Object[][] getConfigDatas() throws BiffException, IOException{
        ExcelData e=new ExcelData(testCaseExcel, "config");
        return e.getExcelData();
    }
    @DataProvider(name="postData")
    public Object[][] Numbers() throws BiffException, IOException{
        ExcelData e=new ExcelData(testCaseExcel, "post");
        return e.getExcelData();
    }
    @DataProvider(name="getData")
    public Object[][] Numbers2() throws BiffException, IOException{
        ExcelData e=new ExcelData(testCaseExcel, "get");
        return e.getExcelData();
    }
    @DataProvider(name="putData")
    public Object[][] putNumbers() throws BiffException, IOException{
        ExcelData e=new ExcelData(testCaseExcel, "put");
        return e.getExcelData();
    }
    //@Test(dataProvider = "loadData")
    public void login() throws Exception {
        String closeableHttpResponse="";
        //发送登录请求
        if(loginApi.contains("get")){
            //爱问B端 get请求登陆方式
            closeableHttpResponse=restClient.doGet(host+loginApi,loginParameter);
        }else{
            //爱问后台post请求登陆方式
         closeableHttpResponse=restClient.doPost(host+loginApi,loginParameter);}
        loginToken=TestUtil.getToken(closeableHttpResponse,tokenPath);
        System.out.println("登陆token:\n"+loginToken);
        //从返回结果中获取状态码
       String tokens =JSONObject.parseObject(JSONObject.parseObject(closeableHttpResponse).getString("data")).getString("token");


    }
    @Test(dataProvider = "postData")
    public void post(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        Logger.output("接口名称： "+apiUrl);
        String json=data.get("dataJson");
        if(JSONObject.parseObject(json).containsKey("token")) {
            json = String.valueOf(JSONObject.parseObject(json).fluentPut("token", loginToken)); //将token拿到，传给全局变量logintoken,再去替换一些需要token传参的value
        }
        json = CreatData.initdata(json,"true");
        String expectval=data.get("expectval");
        boolean result=false;
        //使用构造函数将传入的用户名密码初始化成登录请求参数
        // getParameters tokens=new getParameters(json);
        //将登录请求对象序列化成json对象
        //发送登录请求

        String closeableHttpResponse = restClient.doPost(host+apiUrl,json);
        data.put("actual",closeableHttpResponse);
        //从返回结果中获取状态码
        //int statusCode = TestUtil.getStatusCode(closeableHttpResponse);
//        if(AiwenAssert.isContains(expectval,closeableHttpResponse)){
//            result=true;
//            data.put("result",String.valueOf(result));
//        }
//        else{
//            String e=String.valueOf(AiwenAssert.isContains(closeableHttpResponse,expectval));
//            data.put("result",String.valueOf(result));
//            //throw new Exception("实际结果:" + closeableHttpResponse + " is not null! ");
//            data.put("Exception", e);
//        }
        // Reporter.log("状态码："+statusCode,true);
        AiwenAssert.contains(closeableHttpResponse,expectval);

    }

    @Test(dataProvider = "getData")
    public void get(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        Logger.output("接口名称： "+apiUrl);
        String json=data.get("dataJson");
        if(JSONObject.parseObject(json).containsKey("token")) {
            json = String.valueOf(JSONObject.parseObject(json).fluentPut("token", loginToken)); //将token拿到，传给全局变量logintoken,再去替换一些需要token传参的value
        }
        json = CreatData.initdata(json,"true");
        //将token拿到，传给全局变量logintoken,再去替换一些需要token传参的value
        String expectval=data.get("expectval");
        boolean result=false;
        String closeableHttpResponse = restClient.doGet(host+apiUrl,json);
        //data.put("actual",closeableHttpResponse);
        AiwenAssert.contains(closeableHttpResponse,expectval);
        // Reporter.log("状态码："+statusCode,true);

    }



    @Test(dataProvider = "putData")
    public void put(HashMap<String, String> data) throws Exception {
        String apiUrl = data.get("apiName");
        String json = data.get("dataJson");
        if(JSONObject.parseObject(json).containsKey("token")) {
            json = String.valueOf(JSONObject.parseObject(json).fluentPut("token", loginToken)); //将token拿到，传给全局变量logintoken,再去替换一些需要token传参的value
        }
        String expectval = data.get("expectval");
        boolean result = false;
        String closeableHttpResponse = restClient.doPut(host + apiUrl, json);
        //data.put("actual",closeableHttpResponse);
        AiwenAssert.contains(closeableHttpResponse, expectval);
        // Reporter.log("状态码："+statusCode,true);
        Logger.info("接口地址： " + data);
    }

    @BeforeClass
    public void endTest(){
        System.out.print("测试结束");
    }

}
