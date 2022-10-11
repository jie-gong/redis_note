package com.gong.redis_note.service;


import com.gong.redis_note.model.Student;
import com.gong.redis_note.util.MD5Util;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.DigestException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 公杰
 * @Project: JavaLaity
 * @Pcakage: com.gong.redis_note.service.ShortUrlGenerator
 * @Date: 2022年10月10日 11:22
 * @Description:将一个字符串使用md5生成一个32位的签名串，分为四段，每段八个字节
 */
public class ShortUrlGenerator {
    private static final String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9","0",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    //单独转换MD5
    public static String onlyOne(String str) {
        String s = MD5Util.MD5Encode(str, "");
        return s;
    }


    /*
    一个长连接转换为4个短key
     */
    public static String[] shortUrl(String url) {
        String key = "";
        //对地址进行md5
        String sMD5EncryptResult = DigestUtils.md5Hex(key + url);
        System.out.println("直接加密数据" + sMD5EncryptResult);
        String hex = sMD5EncryptResult;
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            //先取出8位字符串，md5 32位，被切割成4组，每组八个字符
            String sTempSubstring = hex.substring(i * 8, i * 8 + 8);

            //先转换为16进制，然后用0x3FFFFFF进行位与运算，目前是格式化截取前三十位
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubstring, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                //0x0000003D代表什么意思？他的10进制是61，61代表了chars长度62从0到61的坐标
                //0x0000003D & l进行位与运算，就是格式化为六位，即61内的数字
                //保证了index绝对是61内的值
                long index = 0x0000003D & lHexLong;
                outChars += chars[(int) index];
                //每次循环按位移5位，因为30位的二进制，分6次循环，即每次右移5位
                lHexLong = lHexLong >> 5;
            }
            resUrl[i] = outChars;
        }
        return resUrl;
    }


    public static void main(String[] args) {
        String s = onlyOne("张蓝芳");
        System.out.println(s);
        //长链接
        String Url = "dhuahdjkhaskjdhajkshdkjashjdaks";
        //转换为短链接的6位码，返回四个短连接
        String[] strings = shortUrl(Url);
        for (int i = 0; i < strings.length; i++) {
            //任何一个都可以作为短链接码
            System.out.println(strings[i]);
        }

    }


}
