package com.ls.service.impl;

import cn.ls.client.LiuAShuoClient;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ls.common.ErrorCode;
import com.ls.common.model.entity.InterfaceInfo;
import com.ls.common.model.entity.User;
import com.ls.enums.InterfaceInfoStatuEnum;
import com.ls.exception.BusinessException;
import com.ls.mapper.InterfaceInfoMapper;
import com.ls.model.dto.interfaceinfo.InterfaceinfoInvokeRequest;
import com.ls.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Resource
    private LiuAShuoClient liuAShuoClient;

    @Value("${liuashuo.client.url}")
    private String clientUrl;


    /**
     * 有效接口信息
     *
     * @param interfaceInfo 接口信息
     * @param isAdd         是添加
     */
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean isAdd) {
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();

        // 创建时，所有参数必须非空
        if (isAdd) {
            if (StringUtils.isAnyBlank(name, description, url, requestHeader, responseHeader, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }

        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }


    /**
     * 下线
     *
     * @param id 同上
     * @return {@link Boolean}
     */
    @Override
    public Boolean offlineInterface(long id) {
        // 判断是否存在
        InterfaceInfo interfaceinfoById = this.getById(id);
        if (interfaceinfoById == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        interfaceinfoById.setId(id);
        interfaceinfoById.setStatus(InterfaceInfoStatuEnum.OFFLINE.getCode());
        return this.updateById(interfaceinfoById);
    }

    /**
     * 发布
     *
     * @param id 同上
     * @return {@link Boolean}
     */
    @Override
    public Boolean publishInterface(Long id) {
        // 判断是否存在
        InterfaceInfo interfaceinfoById = this.getById(id);
        if (interfaceinfoById == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User user = new User();
        user.setUserName("aaaaa");
        String post = liuAShuoClient.ByRestfulPost(user);
        if (StringUtils.isBlank(post)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "请求接口失败");
        }
        interfaceinfoById.setId(id);
        interfaceinfoById.setStatus(InterfaceInfoStatuEnum.PUBLISH.getCode());
        return this.updateById(interfaceinfoById);
    }

    /**
     * 调用接口
     *
     * @param interfaceinfoInvokeRequest interfaceinfo 调用请求
     * @param loginUser                  登录用户
     * @return {@link Object}
     */
    @Override
    public Object invokeInterface(InterfaceinfoInvokeRequest interfaceinfoInvokeRequest, User loginUser) {
        Long id = interfaceinfoInvokeRequest.getId();
        String requestParams = interfaceinfoInvokeRequest.getRequestParams();
        InterfaceInfo info = this.getById(id);
        if (info == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (info.getStatus() != InterfaceInfoStatuEnum.PUBLISH.getCode()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口未启用");
        }
        Gson gson = new Gson();
        Object params = gson.fromJson(requestParams, Object.class);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        LiuAShuoClient shuoClient = new LiuAShuoClient(accessKey, secretKey, clientUrl + "/api/user/a");
        return shuoClient.ByRestfulPost(params);
    }
    public static void main(String[] args) {

        //String regex = "http://[^/]+:[0-9]+(/[^/]*)?";
        //String regex = "http://127\\.0\\.0\\.1:9909/(.*)";
        //String regex = "http://127\\.0\\.0\\.1:9909(.*)";
        //String regex = "http://([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}):([0-9]+)(.*)";
        //String regex = "http://(localhost|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})):([0-9]+)(.*)";
        //String url = "http://192.168.127.10:9909/api/user/a";
        String url = "http://localhost:9909/api/user/a";
        //String regex = "http://([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}):([0-9]+)(.*)";
        String regex = "http://(localhost|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})):([0-9]+)(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            System.out.println("IP地址：" + matcher.group(1));
            System.out.println("端口号：" + matcher.group(2));
            System.out.println("请求路径：" + matcher.group(3));
            System.out.println("请求路径：" + matcher.group(4));
        }
    }
}




