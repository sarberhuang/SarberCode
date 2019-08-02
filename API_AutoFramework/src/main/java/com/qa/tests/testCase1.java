package com.qa.tests;


import com.qa.base.AiwenAssert;
import com.qa.base.Logger;
import com.qa.base.TestBase;
import com.qa.restclient.RestClient;
import com.qa.util.ExcelData;
import com.qa.util.TestUtil;
import jxl.read.biff.BiffException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.HashMap;


public class testCase1 extends TestBase {
    TestBase testBase;
    RestClient restClient;
    //host根url
    String host;
    //Excel路径
    String testCaseExcel;
    //token路径
    String tokenPath;
    //header
    HashMap<String ,String> postHeader = new HashMap<String, String>();
    //token
    HashMap<String,String> loginToken=new HashMap<String, String>();
    //测试永猎excel文件名
    String testcase;
    @BeforeClass
    public void setUp(){
        testBase = new TestBase();
        restClient = new RestClient();
        postHeader.put("Content-Type","application/json");
        //载入配置文件，接口endpoint
        host = prop.getProperty("Host");
        //载入配置文件，文件名字
        testcase="testCase2data";


    }
    @DataProvider(name="postData1")
    public Object[][] Numbers() throws BiffException, IOException{
        ExcelData e=new ExcelData(testcase, "post");
        return e.getExcelData();
    }
    @DataProvider(name="getData1")
    public Object[][] Numbers2() throws BiffException, IOException{
        ExcelData e=new ExcelData(testcase, "get");
        return e.getExcelData();
    }
    @DataProvider(name="putData1")
    public Object[][] putNumbers() throws BiffException, IOException{
        ExcelData e=new ExcelData(testcase, "put");
        return e.getExcelData();
    }

    @Test(dataProvider = "postData1")
    public void login(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        String json=data.get("dataJson");
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
    @Test(dataProvider = "getData1")
    public void get(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        String json=data.get("dataJson");
        String expectval=data.get("expectval");
        boolean result=false;
        String closeableHttpResponse = restClient.doGet(host+apiUrl,json);
        //data.put("actual",closeableHttpResponse);
     AiwenAssert.contains(closeableHttpResponse,expectval);
        // Reporter.log("状态码："+statusCode,true);
        Logger.info("接口地址： "+data);

    }
    @Test(dataProvider = "putData1")
    public void put(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        String json=data.get("dataJson");
        String expectval=data.get("expectval");
        boolean result=false;
        String closeableHttpResponse = restClient.doPut(host+apiUrl,json);
        //data.put("actual",closeableHttpResponse);
        AiwenAssert.contains(closeableHttpResponse,expectval);
        // Reporter.log("状态码："+statusCode,true);
        Logger.info("接口地址： "+data);

    }












    @BeforeClass
    public void endTest(){
        System.out.print("测试结束");
    }

}
