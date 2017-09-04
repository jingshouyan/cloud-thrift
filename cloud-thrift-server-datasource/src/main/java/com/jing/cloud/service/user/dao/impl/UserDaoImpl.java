package com.jing.cloud.service.user.dao.impl;

import com.jing.cloud.service.dao.impl.DbDaoImpl;
import com.jing.cloud.service.user.bean.User;
import com.jing.cloud.service.user.dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * Created by 29017 on 2017/8/22.
 */
@Repository
public class UserDaoImpl extends DbDaoImpl<User> implements UserDao{
}
