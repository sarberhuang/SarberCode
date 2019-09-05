package com.qa.tests.test;


import com.alibaba.fastjson.JSONObject;
import com.qa.base.AiwenAssert;
import com.qa.base.Logger;
import com.qa.base.TestBase;
import com.qa.restclient.BodyClient;
import com.qa.util.ExcelData;
import com.qa.util.TestUtil;
import jxl.read.biff.BiffException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class testCase3 extends TestBase {
    TestBase testBase;
    BodyClient restClient;
    CloseableHttpResponse closeableHttpResponse;
    //host根url
    String host;
    //Excel路径
    String testCaseExcel;
    //header
    HashMap<String ,String> postHeader = new HashMap<String, String>();
    @BeforeClass
    public void setUp(){
        testBase = new TestBase();
        restClient = new BodyClient();
        postHeader.put("Content-Type","application/json");
        //载入配置文件，接口endpoint
        host = prop.getProperty("Host");
        //载入配置文件，post接口参数
        testCaseExcel="video";//55099爱问SaaS社区C端H5服务--接口文档

    }

    public  void load() throws  Exception{

    }






    @DataProvider(name="loadData")
    public Object[][] loadaccount() throws BiffException, IOException{
        ExcelData e=new ExcelData(testCaseExcel, "load");
        return e.getExcelData();
    }
    @DataProvider(name="postData")
    public Object[][] Numbers() throws BiffException, IOException{
        //ExcelData e=new ExcelData("testCase1data", "post");
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
        String json=data.get("dataJson");
        String expectval=data.get("expectval");

        //将登录请求对象序列化成json对象
        //发送登录请求
      String  closeableHttpResponse = BodyClient.postByJson(host+apiUrl,json);
        //从返回结果中获取状态码
        //data.put("actual",closeableHttpResponse);
        AiwenAssert.contains(TestUtil.replaceBlank(closeableHttpResponse),TestUtil.replaceBlank(expectval));
        // Reporter.log("状态码："+statusCode,true);
        Logger.info("接口地址： "+data);
    }

    @Test(dataProvider = "getData")
    public void get(HashMap<String, String> data) throws Exception {
        String apiUrl=data.get("apiName");
        String json=data.get("dataJson");
        String expectval=data.get("expectval");
        Map<String,String> query=new HashMap<String, String>();
        //将下面参数形式的字符串分割封装添加到map集合里
        // cid: 123
        //pnum: 123123
        //records: 123
        String[] paramter = json.split("\\n");
      for(int i=0;i<paramter.length;i++){  //将输入的参数传入到query数组里
          String key,value="";
          String[] paramters = paramter[i].split(":");
              key=paramters[0];
              value=paramters[1];
          query.put(key,value);
      }
        //发送登录请求
        String  closeableHttpResponse = BodyClient.get(host+apiUrl,query);
        //从返回结果中获取状态码
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
