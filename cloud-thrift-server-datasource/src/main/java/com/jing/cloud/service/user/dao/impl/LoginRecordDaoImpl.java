package com.jing.cloud.service.user.dao.impl;

import com.jing.cloud.service.dao.impl.DbDaoImpl;
import com.jing.cloud.service.user.bean.LoginRecord;
import com.jing.cloud.service.user.dao.LoginRecordDao;
import org.springframework.stereotype.Repository;


@Repository
public class LoginRecordDaoImpl extends DbDaoImpl<LoginRecord> implements LoginRecordDao {
}
