package cn.ls.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

import static cn.ls.util.SignUtils.genSign;

/**
 * @title: LiuAShuoClient
 * @author: liaoshuo
 * @package: cn.ls.client
 * @date: 2024/5/26 17:27
 * @description:
 */
public class LiuAShuoClient {
    private final String url;

    private final String accessKey;

    private final String secretKey;



    public LiuAShuoClient(String accessKey, String secretKey, String url) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.url = url;
    }

    public String ByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.get(url, paramMap);
    }

    public String ByPost(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post(url, paramMap);
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("secretKey", secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(body, secretKey));
        return hashMap;
    }

    public String ByRestfulPost(Object obj) {
        String json = JSONUtil.toJsonStr(obj);
        return HttpRequest.post(url)
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute()
                .body();
    }

}
