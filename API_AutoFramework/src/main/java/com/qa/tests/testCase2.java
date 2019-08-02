package com.qa.tests;

import com.alibaba.fastjson.JSONObject;
import com.qa.base.AiwenAssert;
import com.qa.base.Logger;
import com.qa.base.TestBase;
import com.qa.restclient.RestClient;
import com.qa.util.ExcelData;
import com.qa.util.TestUtil;
import jxl.read.biff.BiffException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;



public class testCase2 extends TestBase {
    TestBase testBase;
    RestClient restClient;
    //host根url
    String host;
    //Excel路径
    String testCaseExcel;
    String tokenPath;
   String loginToken;
    //header
    HashMap<String ,String> postHeader = new HashMap<String, String>();
    @BeforeClass
    public void setUp() throws Exception {
        testBase = new TestBase();
        restClient = new RestClient();
        postHeader.put("Content-Type","application/json");
        //载入配置文件，接口endpoint
        host = prop.getProperty("Host");
        //载入配置文件，post接口参数
        testCaseExcel=prop.getProperty("testCase1data");
        //
tokenPath=prop.getProperty("token");
        System.out.println("只运行一次");
        //login();
    }
    @DataProvider(name="loadData")
    public Object[][] loadaccount() throws BiffException, IOException{
        ExcelData e=new ExcelData("testCase1data", "load");
        return e.getExcelData();
    }
    @DataProvider(name="postData")
    public Object[][] Numbers() throws BiffException, IOException{
        ExcelData e=new ExcelData("testCase1data", "post");
        return e.getExcelData();
    }
    @DataProvider(name="getData")
    public Object[][] Numbers2() throws BiffException, IOException{
        ExcelData e=new ExcelData("testCase1data", "get");
        return e.getExcelData();
    }
    @DataProvider(name="putData")
    public Object[][] putNumbers() throws BiffException, IOException{
        ExcelData e=new ExcelData("testCase1data", "put");
        return e.getExcelData();
    }
    //@Test(dataProvider = "loadData")
    public void login() throws Exception {
        //String apiUrl=data.get("apiName");
       // String json=data.get("dataJson");
        String apiUrl=prop.getProperty("loginApi");
        String json=prop.getProperty("loginParameter");
        //发送登录请求
        String closeableHttpResponse = restClient.doPost(host+apiUrl,json);
        loginToken=TestUtil.getToken(closeableHttpResponse,tokenPath);
        //从返回结果中获取状态码
       String tokens =JSONObject.parseObject(JSONObject.parseObject(closeableHttpResponse).getString("data")).getString("token");


    }
    @Test(dataProvider = "postData")
    public void post(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        String json=data.get("dataJson");
        if(JSONObject.parseObject(json).containsKey("token")) {
            json = String.valueOf(JSONObject.parseObject(json).fluentPut("token", loginToken)); //将tokenz带入到传输当中
        }
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
        if(AiwenAssert.isContains(closeableHttpResponse,expectval)){
            result=true;
            data.put("result",String.valueOf(result));
        }
        else{
            String e=String.valueOf(AiwenAssert.isContains(closeableHttpResponse,expectval));
            data.put("result",String.valueOf(result));
            //throw new Exception("实际结果:" + closeableHttpResponse + " is not null! ");
            data.put("Exception", e);
        }
        // Reporter.log("状态码："+statusCode,true);
        Logger.info("接口地址： "+data);

    }

    @Test(dataProvider = "getData")
    public void get(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        String json=data.get("dataJson");
        JSONObject.parseObject(json).fluentPut("token",loginToken);//将tokenz带入到传输当中
        String expectval=data.get("expectval");
        boolean result=false;
        String closeableHttpResponse = restClient.doGet(host+apiUrl,json);
        //data.put("actual",closeableHttpResponse);
        AiwenAssert.contains(closeableHttpResponse,expectval);
        // Reporter.log("状态码："+statusCode,true);
        Logger.info("接口地址： "+data);

    }
    @Test(dataProvider = "putData")
    public void put(HashMap<String, String> data) throws Exception {
        String apiUrl = data.get("apiName");
        String json = data.get("dataJson");
        JSONObject.parseObject(json).fluentPut("token",loginToken);//将tokenz带入到传输当中
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
