package cn.ls.config;

import cn.ls.client.LiuAShuoClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @title: LiuAShuoClientConfig
 * @author: liaoshuo
 * @package: cn.ls
 * @date: 2024/5/26 17:20
 * @description:
 */
@Data
@ConfigurationProperties("liuashuo.client")
@Configuration
@ComponentScan
public class LiuAShuoClientConfig {

    private String accessKey;

    private String secretKey;

    private String url;

    @Bean
    public LiuAShuoClient liuAShuoClient(){
        return new LiuAShuoClient(accessKey,secretKey,url);
    }
}
