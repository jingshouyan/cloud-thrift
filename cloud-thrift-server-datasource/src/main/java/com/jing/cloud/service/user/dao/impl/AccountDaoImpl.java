package com.jing.cloud.service.user.dao.impl;

import com.jing.cloud.service.dao.impl.DbDaoImpl;
import com.jing.cloud.service.user.bean.Account;
import com.jing.cloud.service.user.dao.AccountDao;
import org.springframework.stereotype.Repository;

/**
 * Created by 29017 on 2017/9/4.
 */
@Repository
public class AccountDaoImpl extends DbDaoImpl<Account> implements AccountDao {
}
