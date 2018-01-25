package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by yanhui on 2018/1/24.
 */
public interface IUserService {


    ServerResponse<User> login(String username, String password);
}



