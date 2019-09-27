package com.qa.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;


import org.testng.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestUtil {
    //出去内容里面的空格，换行，制表 符号
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    //获取状态码
    public static int getStatusCode(String response){
        JSONObject obj= (JSONObject) JSONObject.parse(response);
        obj.get("message").toString();
        String state=obj.get("status").toString();
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(state);
        int StatusCode =Integer.parseInt(m.replaceAll(""));
      return StatusCode;
    }

    //解析json
    public static String getValueByJPath(JSONObject responseJson,String jpth){
        Object obj=responseJson;
        for(String s:jpth.split("/")){
            if(!s.isEmpty()){
                if(!(s.contains("[") || s.contains("]"))) {
                    obj = ((JSONObject) obj).get(s);
                }else if(s.contains("[") || s.contains("]")) {
                    obj =((JSONArray)((JSONObject)obj).get(s.split("\\[")[0])).get(Integer.parseInt(s.split("\\[")[1].replaceAll("]", "")));
                }
            }
        }
        return obj.toString();

    }

//获取登陆token
public static  String getToken(String resp,String jsonPath) throws  Exception{
    ReadContext ctx = JsonPath.parse(resp);
    String Token="";
    try{
     Token = ctx.read(jsonPath);
        if(null == Token||"".equals(Token))
        {            new Exception("token不存在");        }
    }catch (PathNotFoundException e){
        e.printStackTrace();
        Assert.fail("配置文件config里面配置的Token路径不对，请根据当前登陆接口返回的response正确配置好路径结构,\n\t\t或者在相关excel表格里配置好token");
    }

      return Token;

}

}