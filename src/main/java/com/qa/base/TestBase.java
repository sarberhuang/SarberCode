package com.qa.base;
import org.junit.Test;

import java.io.*;
import java.util.Properties;

public class TestBase {
    //这个类作为所有接口请求的父类，加载读取properties文件
    public Properties prop;
    //构造函数
    public TestBase(){
        try{
            prop = new Properties();
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/com/qa/config/config.properties");
            prop.load(fis);
        }catch(FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }
    }

    public  void writeData(String path,String key,String value) throws IOException{

        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/com/qa/config/config.properties");
        prop.load(fis);
        fis.close();
        OutputStream fos=new FileOutputStream(System.getProperty("user.dir")+"/src/main/java/com/qa/config/config.properties");
        prop.setProperty(key,value);
        prop.store(fos,"Update '" + key + "' value");
        fos.close();
    }
}
