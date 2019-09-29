package com.qa.testSuits.datajson;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.qa.base.AiwenAssert;
import com.qa.base.Logger;
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
import java.util.Iterator;
import java.util.Map;


public class testdataJsonTrue  {
    RestClient restClient;
    String host;
    String testCaseExcel ; //j接口数据文件名
    String tokenPath;   //token在登陆成功之后相应的response
    String loginApi;        //登陆接口
    String loginToken;           //  登陆之后产生的ntoke
    String loginParameter;//登陆参数
    String method;      //登陆的请求方式
    @BeforeClass
    public void setUp() throws Exception {
        restClient = new RestClient();
        testCaseExcel= initExcelPath.initExcelPath();//初始化文件名
        System.out.println(getConfigDatas());//获取excel里面的配置host 登陆api 登陆账号 以及登陆的方式
        Map<String, String> data = (Map<String, String>) getConfigDatas()[0][0];
        Iterator iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
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
            //采取读excel里面参数里的tokenPath 不再取prop 文件里面的参数
            else if(key.equals("tokenPath")){
                System.out.print("读取到token的路径为:");
                tokenPath=(String) entry.getValue();
            }
        }
        if(loginToken==null ||loginToken.equals("")){
            System.out.println("使用登陆参数方法进行获取token");
            //拿到登陆里面token数据的位置，生成全局变量token
              System.out.println("只运行一次登陆，并获取登陆token");
            login();
        }else{
            loginToken=loginToken;
            System.out.println("直接用登陆token形式登陆成功！");
            System.out.println("直接读取表里的token值设为logintoken的参数值");
        }

    }

    @Test(dataProvider = "postData")
    public void post(HashMap<String, String> data) throws Exception {
        String apiUrl = data.get("apiName");String caseName = data.get("用例场景");
        Logger.output("用例场景： " + caseName); Logger.output("接口名称： " + apiUrl);
        String json = data.get("dataJson");
        String expectval = data.get("expectval");
        String closeableHttpResponse="";
        try {
            json = CreatData.initdata(json, "true");
            json = String.valueOf(JSONObject.parseObject(json).fluentPut("token", loginToken)); //将token拿到，传给全局变量logintoken,再去替换一些需要token传参的value
            //添加token字段
            closeableHttpResponse =restClient.doPostByDataJson(host + apiUrl, json);
            AiwenAssert.contains(closeableHttpResponse, expectval);
        }catch (JSONException e){
            System.out.println("Swagge复制到过来或者传入的参数形式不能转成Json格式的");
        e.printStackTrace();
        }
    }
    @Test(dataProvider = "getData")
    public void get(HashMap<String, String> data) throws Exception {
        String apiUrl = data.get("apiName");String caseName = data.get("用例场景");
        Logger.output("用例场景： " + caseName); Logger.output("接口名称： " + apiUrl);
        String json = data.get("dataJson");
        String expectval = data.get("expectval");
        String closeableHttpResponse="";
        try {
            if (JSONObject.parseObject(json).containsKey("token")) {
                json = String.valueOf(JSONObject.parseObject(json).fluentPut("token", loginToken)); //将token拿到，传给全局变量logintoken,再去替换一些需要token传参的value
            }
            json = CreatData.initdata(json, "true");//前端不该后端改
            //将token拿到，传给全局变量logintoken,再去替换一些需要token传参的value
            restClient.doGetByDataJson(host + apiUrl, json);
            AiwenAssert.contains(closeableHttpResponse, expectval);
        } catch (JSONException e) {
            System.out.println("Swagge复制到过来或者传入的参数形式不能转成Json格式的");
            e.printStackTrace();
        }
    }

    //数据提供开始
    public Object[][] getConfigDatas() throws BiffException, IOException {
        ExcelData e = new ExcelData(testCaseExcel, "config");
        return e.getExcelData();
    }

    @DataProvider(name = "postData")
    public Object[][] Numbers() throws BiffException, IOException {
        ExcelData e = new ExcelData(testCaseExcel, "post");
        return e.getExcelData();
    }

    @DataProvider(name = "getData")
    public Object[][] Numbers2() throws BiffException, IOException {
        ExcelData e = new ExcelData(testCaseExcel, "get");
        return e.getExcelData();
    }

    @DataProvider(name = "putData")
    public Object[][] putNumbers() throws BiffException, IOException {
        ExcelData e = new ExcelData(testCaseExcel, "put");
        return e.getExcelData();
    }

    public void login() throws Exception {
        String closeableHttpResponse = "";
        //发送登录请求
        if (loginApi.contains("get")) {
            //爱问B端 get请求登陆方式
            closeableHttpResponse = restClient.doGetByDataJson(host + loginApi, loginParameter);
        } else {
            //爱问后台post请求登陆方式
            closeableHttpResponse = restClient.doPostByDataJson(host + loginApi, loginParameter);
        }
        loginToken = TestUtil.getToken(closeableHttpResponse, tokenPath);
        System.out.println("用登陆参数形式登陆成功！");
        System.out.println("登陆token:\n" + loginToken);
    }



    @BeforeClass
    public void endTest() {
        System.out.print("测试结束");
    }

}
