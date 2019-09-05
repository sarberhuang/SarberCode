package com.qa.tests.test;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class Test {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "E:\\IntlOnline\\CucumberAutomation\\webDrivers\\chromedriver.exe");
     driver= new ChromeDriver();
    baseUrl = "https://www.wenwo.com/index/home";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @org.testng.annotations.Test
  public void test() throws Exception {
    driver.get("https://www.wenwo.com/main/home");
    driver.findElement(By.id("loginTel")).click();
    driver.findElement(By.id("loginTel")).clear();
    driver.findElement(By.id("loginTel")).sendKeys("13133706292");
    driver.findElement(By.id("loginPwd")).clear();
    driver.findElement(By.id("loginPwd")).sendKeys("123456");
    driver.findElement(By.id("login")).submit();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='去领取'])[1]/following::a[1]")).click();
    driver.findElement(By.linkText("文章发表")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='《爱问医生内容协议》'])[1]/following::a[1]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='发布成功'])[1]/following::div[3]")).click();
    driver.findElement(By.id("pspContent-text")).clear();
    driver.findElement(By.id("pspContent-text")).sendKeys("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
    driver.findElement(By.linkText("下一步")).click();
    driver.findElement(By.id("deptName")).click();
    driver.findElement(By.linkText("五官科")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='全科'])[1]/following::li[1]")).click();
    driver.findElement(By.linkText("眼科")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='文章标签'])[1]/following::div[2]")).click();
    driver.findElement(By.id("5ce27232bf00833d34cfa5a2")).click();
    driver.findElement(By.id("5ce27232bf00833d34cfa5a1")).click();
    driver.findElement(By.id("5ce27232bf00833d34cfa5a0")).click();
    driver.findElement(By.id("5ce27232bf00833d34cfa59f")).click();
    driver.findElement(By.id("5ce27232bf00833d34cfa59e")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='中浆'])[1]/following::b[1]")).click();
    driver.findElement(By.linkText("切换文章类型")).click();
    driver.findElement(By.linkText("确定设置")).click();
    driver.findElement(By.id("next")).click();
    driver.findElement(By.id("date")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='日'])[1]/following::td[26]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='今日'])[1]/following::span[15]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='取消'])[3]/following::span[11]")).click();
    driver.findElement(By.id("next")).click();
    driver.findElement(By.id("next")).click();
    driver.findElement(By.id("prev")).click();
    // ERROR: Caught exception [unknown command [clear]]
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
