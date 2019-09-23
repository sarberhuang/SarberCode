package com.qa.util;

import com.alibaba.fastjson.JSONObject;
import jxl.read.biff.BiffException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class CreatData {

    public static String initdata(String json, String valuetype) throws BiffException, IOException {
        ExcelData e=new ExcelData("data.xls", valuetype);
        Iterator iterator1 = JSONObject.parseObject(json).keySet().iterator(); //迭代出json里面的所有key
        while(iterator1.hasNext()) {
            String key = (String) iterator1.next();     //一次拿到每一个key
            //for标志
            for (int i = 0; i < e.getExcelData().length; i++) {     //开始遍历字典里面的key
                Map<String, String> map = (Map<String, String>) e.getExcelData()[i][0];
                if(!(map.get("key").equals(key))){
                    continue ;  //跳出for循环
                } //如果这行数据里面的key就跳到下一行
                else {  //如果有相同  那就根据valuetype的类型分别给JSON对应的key传输对应的value的值
                    if (valuetype.equals("null")) {//参数值为null类型
                        if (JSONObject.parseObject(json).containsKey(map.get("key"))) {
                            json = String.valueOf(JSONObject.parseObject(json).fluentPut(map.get("key"), null));
                        }
                    }
                    else {      //参数数据不为null的时候，
                        if (map.get("value").equals(null)) { //只给了变量 没给值得时候，为了防止空指针异常，一律传 “”
                            json = String.valueOf(JSONObject.parseObject(json).fluentPut(map.get("key"),""));
                        } else {//如果有值了，是多少就传多少
                            if(map.get("type").equals("int")){
                                json = String.valueOf(JSONObject.parseObject(json).fluentPut(map.get("key"),Integer.parseInt(map.get("value"))));
                            }else if(map.get("type").equals("List<String>")){//字符串类型的素组暂时只赋值为空
                                List<String> list=new ArrayList<String>();
                                list.add("");
//                              json = String.valueOf(JSONObject.parseObject(json).fluentPut(map.get("key"), Arrays.toString())));
                            }
                            else{
                            json = String.valueOf(JSONObject.parseObject(json).fluentPut(map.get("key"),map.get("value")));
                             }
                        }
                    }
                }

            }//for
        }//while
        return json;
    }


}
