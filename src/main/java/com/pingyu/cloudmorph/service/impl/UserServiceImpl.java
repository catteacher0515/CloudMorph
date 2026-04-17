package com.pingyu.cloudmorph.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.pingyu.cloudmorph.model.entity.User;
import com.pingyu.cloudmorph.mapper.UserMapper;
import com.pingyu.cloudmorph.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author pingyu
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

}
