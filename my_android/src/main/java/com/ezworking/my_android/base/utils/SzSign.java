package com.ezworking.my_android.base.utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by dujiande on 2016/9/29.
 * sign 签名生成工具类
 */
public class SzSign {

    public static String createSign(JSONObject jsonObject){
        String sign = "";
        try{
            //排序
            ArrayList<String> keysList = new ArrayList<String>();
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()){
                String key = it.next();
                keysList.add(key);
            }
          /*  Collections.sort(keysList, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    String s1 = (String)o1;
                    String s2 = (String)o2;
                    return s1.compareTo(s2);
                }
            })*/;
            String[] toBeStored = keysList.toArray(new String[keysList.size()]);
            Arrays.sort(toBeStored,String.CASE_INSENSITIVE_ORDER);
            String signStr = "{";
            for (String istr : toBeStored) {
                signStr = signStr + istr + ":" + jsonObject.get(istr);
            }
            signStr = signStr + "}";

//            String jsonStr = "{";
//            for(int i=0;i<keysList.size();i++){
//                String key = keysList.get(i);
//                if(i == 0){
//                    jsonStr +=  key +":"+jsonObject.get(key).toString();
//                }else{
//                    jsonStr +=  ","+key +":"+jsonObject.get(key).toString();
//                }
//            }
//            jsonStr +="}";
            String jsonStr = signStr;
            //去掉逗号，单引号，双引号，空格
            jsonStr = jsonStr.replace("\n", "");
            jsonStr = jsonStr.replace("'", "");
            jsonStr = jsonStr.replace("\"", "");
            jsonStr = jsonStr.replace(",", "");
            jsonStr = jsonStr.replace(" ", "");
            String step1str = jsonStr;
            //生成随机数字
            int strLength = step1str.length();
            int randNo = (int)((strLength-2) * Math.random());
            randNo = randNo + 2;
            //生成signStr
            String signStr2 = step1str.substring(strLength/randNo);
            //生成sign
            sign = MD5.getMessageDigest(signStr2.getBytes())+randNo;

        }catch (Exception e){
            e.printStackTrace();
        }

        return sign;
    }
}
