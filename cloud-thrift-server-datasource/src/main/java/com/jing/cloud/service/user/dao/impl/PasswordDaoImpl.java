package com.jing.cloud.service.user.dao.impl;

import com.jing.cloud.service.dao.impl.DbDaoImpl;
import com.jing.cloud.service.user.bean.Password;
import com.jing.cloud.service.user.dao.PasswordDao;
import org.springframework.stereotype.Repository;


@Repository
public class PasswordDaoImpl extends DbDaoImpl<Password> implements PasswordDao {
}
