package com.jing.cloud.service.user.method;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.method.AbstractMethod;
import com.jing.cloud.service.method.Method;
import com.jing.cloud.service.user.bean.Account;
import com.jing.cloud.service.user.bean.Password;
import com.jing.cloud.service.user.bean.Ticket;
import com.jing.cloud.service.user.constant.UserConstant;
import com.jing.cloud.service.user.constant.UserErrCode;
import com.jing.cloud.service.user.dao.AccountDao;
import com.jing.cloud.service.user.dao.PasswordDao;
import com.jing.cloud.service.user.dao.TicketDao;
import com.jing.cloud.service.user.dao.UserDao;
import com.jing.cloud.service.user.method.param.LoginBean;
import com.jing.cloud.service.util.bean.StrFormat;
import com.jing.cloud.util.RspUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 29017 on 2017/9/5.
 */
@Component
public class Login extends AbstractMethod<LoginBean> implements Method<LoginBean>{

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PasswordDao passwordDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TicketDao ticketDao;

    @Override
    public Rsp call(LoginBean loginBean) throws Exception{
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("account",loginBean.getAccount());
        condition.put("type",loginBean.getAccountType());
        List<Account> accounts = accountDao.query(condition);
        if(accounts.isEmpty()){
            return RspUtil.error(UserErrCode.ACCOUNT_NOT_FOUND);
        }
        Account account = accounts.get(0);
        condition.clear();
        condition.put("userId",account.getUserId());
        List<Password> pws = passwordDao.query(condition);
        if(pws.isEmpty()){
            return RspUtil.error(UserErrCode.PASSWORD_NOT_SET);
        }
        boolean pass = BCrypt.checkpw(loginBean.getPw(),pws.get(0).getPassword());
        if(!pass){
            return RspUtil.error(UserErrCode.PASSWORD_NOT_SET);
        }
        //使用userId进行加锁
        synchronized (String.valueOf(account.getUserId()).intern()){
            condition.clear();
            condition.put("userId",account.getUserId());
            condition.put("deviceType",loginBean.getDeviceType());
            List<Ticket> tickets = ticketDao.query(condition);
            List<Object> ticketIds = Lists.newArrayList();
            if(!tickets.isEmpty()){
                for (Ticket t: tickets) {
                    ticketIds.add(t.getId());
                    // TODO: 2017/9/5  下线通知
                }
                ticketDao.delete4List(ticketIds);
            }
            Ticket ticket = new Ticket();
            ticket.setUserId(account.getUserId());
            ticket.setDeviceType(loginBean.getDeviceType());
            ticket.setDeviceInfo(loginBean.getDeviceInfo());
            ticket.setMac(loginBean.getMac());
            ticket.setLang(loginBean.getLang());
            ticket.setTicket(StrFormat.uuid());
            ticket.forCreate();
            ticketDao.insert(ticket);
            return RspUtil.success(ticket);
        }
    }


}
