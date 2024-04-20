package com.ls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ls.model.entity.Post;


public interface PostService extends IService<Post> {

    /**
     * 校验
     *
     * @param post
     * @param add 是否为创建校验
     */
    void validPost(Post post, boolean add);
}
