package cn.ls.filter;

import cn.ls.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    public static final List<String> IP_WAITER_ADDERS = Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        RequestPath path = request.getPath();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String hostString = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        //请求日志
        log.info("请求头；{},", headers);
        log.info("请求地址；{},", hostString);
        log.info("请求路径：{},请求参数:{}", path, queryParams);
        if (!IP_WAITER_ADDERS.contains(hostString)) {
            return Forbidden(response);
        }
        String accessKey = headers.getFirst("accessKey");
        String secretKey = headers.getFirst("secretKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");

        if (nonce != null && Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        //todo 时间和当前时间不能超过 5 分钟
        Long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_TIME = 60 * 5L;
        if (timestamp != null) {
            if ((currentTime - Long.parseLong(timestamp)) >= FIVE_TIME) {
                return Forbidden(response);
            }
        }
        //todo 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign != null && !sign.equals(serverSign)) {
            throw new RuntimeException("无权限");
        }
        //return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
        return handleResponse(exchange, chain);
        //请求转发，调用模拟接口
        //Mono<Void> filter = chain.filter(exchange);
        ////响应日志
        //log.info("响应状态码：{}", response.getStatusCode());
        ////调用成功，接口调用次数 + 1
        //if (response.getStatusCode() == HttpStatus.OK) {
        //
        //} else {
        //    //调用失败，返回一个规范的错误码
        //    return SERVER_ERROR(response);
        //}
        //
        //return filter;
    }


    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    //public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        //try {
                                        //    innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        //} catch (Exception e) {
                                        //    log.error("invokeCount error", e);
                                        //}
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> Forbidden(ServerHttpResponse response) {
        response.setRawStatusCode(HttpStatus.FORBIDDEN.value());
        return response.setComplete();
    }

    public Mono<Void> SERVER_ERROR(ServerHttpResponse response) {
        response.setRawStatusCode(HttpStatus.Series.SERVER_ERROR.value());
        return response.setComplete();
    }
}