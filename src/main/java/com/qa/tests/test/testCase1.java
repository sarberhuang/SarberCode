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
        // testCaseExcel="wenwo-cloud-biz-manager";//5400swagger-bootstrap-ui RESTful APIs
        testCaseExcel="wenwo-cloud-doctor-consult.xls";//5300爱问SaaS社区B端服务biz--接口文档
        //拿到登陆里面token数据的位置，生成全局变量token

        System.out.println("此方法case不需要登陆token");

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

    @Test(dataProvider = "postData")
    public void post(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        Logger.output("接口名称： "+apiUrl);
        String json=data.get("dataJson");
        if(JSONObject.parseObject(json).containsKey("token")) {
            json = String.valueOf(JSONObject.parseObject(json).fluentPut("token", loginToken)); //将token拿到，传给全局变量logintoken,再去替换一些需要token传参的value
        }
        json = CreatData.initdata(json, "true");
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
        json = CreatData.initdata(json, "true");
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
