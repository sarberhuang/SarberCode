//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



package com.qa.base;


import com.qa.util.ExcelUtils;

public class AiwenAssert {
    protected ExcelUtils excelUtils;
    public AiwenAssert() {
    }
    //ok
    public static void areEqual(Object expectedValue, Object actualValue) throws Exception {
        areEqual(expectedValue, actualValue, "");
    }
    //ok
    public static void areEqual(Object expectedValue, Object actualValue, String desc) throws Exception {
        if (expectedValue.equals(actualValue)) {
            Logger.info(expectedValue.toString(), actualValue.toString(), "二者相同![断言:AreEqual] " + desc);
        } else {
            Logger.error(expectedValue.toString(), actualValue.toString(), "二者不相同![断言:AreEqual] " + desc);
            throw new Exception("预期结果:" + expectedValue + " not AreEqual 实际结果:" + actualValue);
        }
    }
    //输出失败信息，但是放过程序，但是返回值为boolean类型
    public static boolean isEqual(Object expectedValue, Object actualValue) {
        return isEqual(expectedValue, actualValue, "");
    }

    public static boolean isEqual(Object expectedValue, Object actualValue, String desc) {
        boolean result;
        if (expectedValue.equals(actualValue)) {
            result = true;
        } else {
            result = false;
            Logger.error(expectedValue.toString(), actualValue.toString(), "二者不相同![断言:IsEqual] " + desc);
        }

        return result;
    }

    public static void areNotEqual(Object expectedValue, Object actualValue) throws Exception {
        areNotEqual(expectedValue, actualValue, "");
    }

    public static void areNotEqual(Object expectedValue, Object actualValue, String desc) throws Exception {
        if (!expectedValue.equals(actualValue)) {
            Logger.info(expectedValue.toString(), actualValue.toString(), "二者不相同![断言:AreNotEqual] " + desc);
        } else {
            Logger.error(expectedValue.toString(), actualValue.toString(), "二者相同![断言:AreNotEqual] " + desc);
            throw new Exception("预期结果:" + expectedValue + " AreEqual 实际结果:" + actualValue);
        }
    }

    public static void contains(String actualValue, String expectedValue) throws Exception {
        if (actualValue.contains(expectedValue)) {
            Logger.info(expectedValue.toString(), actualValue.toString(), "[断言:Contains]");
            //写入结果为pass（调用写入操作）
        } else {
            Logger.error(expectedValue.toString(), actualValue.toString(), " [断言:Contains]");
            throw new Exception("预期结果:" + expectedValue + "\n not Contains 实际结果:" + actualValue);
            //写入失败结果和抛出的Expection（调用写入操作）
        }
    }

    public static void contains(String actualValue, String expectedValue, String desc) throws Exception {
        if (actualValue.contains(expectedValue)) {
            Logger.info(expectedValue, actualValue, "[断言:Contains] __" + desc);
        } else {
            Logger.error(expectedValue, actualValue, " [断言:Contains] __" + desc);
            throw new Exception("实际结果:" + actualValue + " not Contains 预期结果:" + expectedValue);
        }
    }
    //输出失败信息，但是放过程序，但是返回值为boolean类型
    public static boolean isContains(String actualValue, String expectedValue) throws Exception{
        return isContains(actualValue, expectedValue, "");

    }

    public static boolean isContains (String actualValue, String expectedValue, String desc)throws Exception {
        boolean result;
        if (actualValue.contains(expectedValue)) {
            result = true;
        } else {
            result = false;
            throw new Exception("实际结果:" + actualValue + "\n not Contains 预期结果:" + expectedValue);
            //Logger.error(expectedValue, actualValue, "[断言:Contains] " + desc);
        }

        return result;
    }

    public static void isNull(Object actualValue) throws Exception {
        if (actualValue != null && 0 != actualValue.toString().length()) {
            Logger.error(" 空 ", actualValue.toString(), " 不为空![断言:IsNull]");
            throw new Exception("实际结果:" + actualValue + " is not null! ");
        } else {
            Logger.info(" 空 ", "", " 为空![断言:IsNull]");
        }
    }

    public static boolean isNullBool(Object actualValue) {
        boolean result;
        if (actualValue != null && 0 != actualValue.toString().length()) {
            result = false;
            Logger.error(" 空 ", actualValue.toString(), " 不为空![断言:IsNull]");
        } else {
            result = true;
        }

        return result;
    }

    public static void isNotNull(Object actualValue) throws Exception {
        if (actualValue != null && 0 != actualValue.toString().length()) {
            Logger.info(" 非空 ", actualValue.toString(), " 不为空![断言:IsNotNull]");
        } else {
            Logger.error(" 空 ", "", " 为空![断言:IsNotNull]");
            throw new Exception("实际结果:" + actualValue + " is null! ");
        }
    }

    public static boolean isNotNullBool(Object actualValue) {
        boolean result;
        if (actualValue != null && 0 != actualValue.toString().length()) {
            result = true;
        } else {
            result = false;
            Logger.error(" 空 ", "", " 为空![断言:IsNotNull]");
        }

        return result;
    }

    public static void isTrue(boolean actualValue) throws Exception {
        isTrue(actualValue, "");
    }

    public static void isTrue(boolean actualValue, String desc) throws Exception {
        if (actualValue) {
            Logger.info(" 真 ", Boolean.toString(actualValue), " 为真![断言:IsTrue]" + desc);
        } else {
            Logger.error(" 不为真 ", Boolean.toString(actualValue), " 不为真![断言:IsTrue]" + desc);
            throw new Exception("实际结果:" + actualValue + " is not true! ");
        }
    }

    public static void isFalse(boolean actualValue) throws Exception {
        if (!actualValue) {
            Logger.info(" 假 ", Boolean.toString(actualValue), " 为假![断言:IsTrue]");
        } else {
            Logger.error(" 不为假 ", Boolean.toString(actualValue), " 不为假![断言:IsFalse]");
            throw new Exception("实际结果:" + actualValue + " is true! ");
        }
    }

    public static void fail(String message) throws Exception {
        Logger.error(message);
        throw new Exception("验证失败 | " + message);
    }

    public static void fail(boolean isTestDataNotSupport, String message) throws Exception {
        if (isTestDataNotSupport) {
            Logger.info("测试数据不支持| " + message);
            throw new Exception("测试数据不支持");
        } else {
            Logger.error(message);
            throw new Exception("验证失败 | " + message);
        }
    }
}
