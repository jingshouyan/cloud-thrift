package com.jing.cloud.service.user.dao.impl;

import com.jing.cloud.service.dao.impl.DbDaoImpl;
import com.jing.cloud.service.user.bean.Ticket;
import com.jing.cloud.service.user.dao.TicketDao;
import org.springframework.stereotype.Repository;


@Repository
public class TicketDaoImpl extends DbDaoImpl<Ticket> implements TicketDao {
}
