package com.example.jfinal_blog.dao.impl;

import com.example.jfinal_blog.client.BlogMessage;
import com.example.jfinal_blog.client.User;
import com.example.jfinal_blog.dao.DataHandler;

/**
 * 
* 类描述： 离线数据操作接口
* 创建者： rain
* 项目名称： Blog
* 创建时间： 2013年12月15日 下午10:48:32
* 版本号： v1.0
 */
public class OfflineDataHandler implements DataHandler {

    @Override
    public BlogMessage onLogin(User user) {
	return null;
    }

}
