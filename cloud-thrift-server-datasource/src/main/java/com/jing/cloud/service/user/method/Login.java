package com.jing.cloud.service.user.method;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.method.AbstractMethod;
import com.jing.cloud.service.method.Method;
import com.jing.cloud.service.user.bean.Account;
import com.jing.cloud.service.user.bean.LoginRecord;
import com.jing.cloud.service.user.bean.Password;
import com.jing.cloud.service.user.bean.Ticket;
import com.jing.cloud.service.user.constant.UserConstant;
import com.jing.cloud.service.user.constant.UserErrCode;
import com.jing.cloud.service.user.dao.AccountDao;
import com.jing.cloud.service.user.dao.LoginRecordDao;
import com.jing.cloud.service.user.dao.PasswordDao;
import com.jing.cloud.service.user.dao.TicketDao;
import com.jing.cloud.service.user.dao.UserDao;
import com.jing.cloud.service.user.method.param.LoginBean;
import com.jing.cloud.service.util.bean.StrFormat;
import com.jing.cloud.service.util.db.Compare;
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
    @Autowired
    private LoginRecordDao loginRecordDao;

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
        Password pw = pws.get(0);

        if (pw.getFrozenBefore()>System.currentTimeMillis()){
            //冻结的账号
            return RspUtil.error(UserErrCode.PASSWORD_INVALID);
        }
        boolean pass = BCrypt.checkpw(loginBean.getPw(),pw.getPassword());
        if(!pass){
            Map<String,Object> map = invaildPwHandler(pw);
            return RspUtil.error(UserErrCode.PASSWORD_INVALID,map);
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
                LoginRecord loginRecord = new LoginRecord();
                loginRecord.setLogoutAt(System.currentTimeMillis());
                loginRecord.setLogoutType(UserConstant.LOGOUT_TYPE_KECK);
                loginRecord.forUpdate();
                Compare cTicketId = new Compare();
                cTicketId.setIn(ticketIds);
                condition.put("ticketId",cTicketId);
                condition.put("userId",account.getUserId());
                loginRecordDao.update(loginRecord,condition);
                ticketDao.delete4List(ticketIds);
            }
            Ticket ticket = new Ticket();
            ticket.setUserId(account.getUserId());
            ticket.setDeviceType(loginBean.getDeviceType());
            ticket.setDeviceInfo(loginBean.getDeviceInfo());
            ticket.setMac(loginBean.getMac());
            ticket.setLang(loginBean.getLang());
            ticket.forCreate();
            ticketDao.insert(ticket);
            LoginRecord loginRecord = createByTicket(ticket);
            loginRecordDao.insert(loginRecord);
            return RspUtil.success(ticket);
        }
    }


    public Map<String,Object> invaildPwHandler(Password pw){
        Map<String,Object> map = Maps.newHashMap();
        int wrongNumber = pw.getWrongNumber();
        wrongNumber++;
        map.put("wrongNumber",wrongNumber);
        pw.setWrongNumber(wrongNumber);
        if(wrongNumber>=UserConstant.PW_WRONG_TIME_LOCK){
            long frozenBefore = System.currentTimeMillis()+UserConstant.PW_LOCK_MS;
            map.put("frozenBefore",frozenBefore);
            pw.setFrozenBefore(frozenBefore);
        }
        pw.forUpdate();
        passwordDao.update(pw);
        return map;
    }


    private LoginRecord createByTicket(Ticket ticket){
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setTicketId(ticket.getId());
        loginRecord.setUserId(ticket.getUserId());
        loginRecord.setDeviceType(ticket.getDeviceType());
        loginRecord.setDeviceInfo(ticket.getDeviceInfo());
        loginRecord.setLang(ticket.getLang());
        loginRecord.setMac(ticket.getMac());
        loginRecord.setLoginAt(System.currentTimeMillis());
        loginRecord.forCreate();
        return loginRecord;
    }

}
