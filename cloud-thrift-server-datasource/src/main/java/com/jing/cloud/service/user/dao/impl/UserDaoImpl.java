package com.jing.cloud.service.user.dao.impl;

import com.jing.cloud.service.dao.impl.DbDaoImpl;
import com.jing.cloud.service.user.bean.User;
import com.jing.cloud.service.user.dao.UserDao;
import org.springframework.stereotype.Repository;


@Repository
public class UserDaoImpl extends DbDaoImpl<User> implements UserDao{
}
