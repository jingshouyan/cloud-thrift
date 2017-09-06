package com.jing.cloud.service.user.method;

import com.google.common.collect.Maps;
import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.method.AbstractMethod;
import com.jing.cloud.service.method.Method;
import com.jing.cloud.service.user.bean.Account;
import com.jing.cloud.service.user.bean.Password;
import com.jing.cloud.service.user.bean.User;
import com.jing.cloud.service.user.constant.UserConstant;
import com.jing.cloud.service.user.constant.UserErrCode;
import com.jing.cloud.service.user.dao.AccountDao;
import com.jing.cloud.service.user.dao.PasswordDao;
import com.jing.cloud.service.user.dao.UserDao;
import com.jing.cloud.service.user.method.param.User4Reg;
import com.jing.cloud.util.RspUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 29017 on 2017/9/4.
 */
@Component
public class RegisterUser extends AbstractMethod<User4Reg> implements Method<User4Reg>{

    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PasswordDao passwordDao;

    @Override
    public Rsp call(User4Reg user4Reg) throws Exception{
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("account",user4Reg.getAccount());
        condition.put("type",user4Reg.getAccountType());
        List<Account> accounts = accountDao.query(condition);
        if(accounts.isEmpty()){
            User user = new User();
            user.setNickname(user4Reg.getNickname());
            user.setSex(user4Reg.getSex());
            user.setState(UserConstant.USER_STATE_OK);
            user.forCreate();
            userDao.insert(user);
            Account account = new Account();
            account.setUserId(user.getId());
            account.setType(user4Reg.getAccountType());
            account.setAccount(user4Reg.getAccount());
            account.forCreate();
            accountDao.insert(account);
            Password password = new Password();
            password.setUserId(user.getId());
            String pw = BCrypt.hashpw(user4Reg.getPassword(),BCrypt.gensalt());
            password.setPassword(pw);
            password.setWrongNumber(0);
            password.setFrozenBefore(0L);
            password.forCreate();
            passwordDao.insert(password);
            return RspUtil.success();
        }
        return RspUtil.error(UserErrCode.ACCOUNT_EXIST);
    }
}
