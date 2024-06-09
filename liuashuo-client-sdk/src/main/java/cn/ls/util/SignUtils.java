package cn.ls.util;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @title: SignUtils
 * @author: liaoshuo
 * @package: cn.ls.util
 * @date: 2024/5/26 17:24
 * @description:
 */
public class SignUtils {

    /**
     * 生成签名
     *
     * @param body      身体
     * @param secretKey 密钥
     * @return {@link String}
     */
    public static String genSign(String body,String secretKey){
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + "-" + secretKey;
        return digester.digestHex(content);
    }
}
