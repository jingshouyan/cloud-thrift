package com.jing.cloud.service.method;

import com.google.common.collect.Maps;
import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.bean.ServiceBean;
import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.bean.ServiceTable;
import com.jing.cloud.service.dao.impl.ServiceBeanDaoImpl;
import com.jing.cloud.service.dao.impl.ServiceDatasourceDaoImpl;
import com.jing.cloud.service.dao.impl.ServiceTableDaoImpl;
import com.jing.cloud.service.method.param.SerInfo;
import com.jing.cloud.util.RspUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 29017 on 2017/8/17.
 */
@Component
public class GetDbInfo extends AbstractMethod<SerInfo> {

    @Autowired
    ServiceBeanDaoImpl serviceBeanDao;
    @Autowired
    ServiceDatasourceDaoImpl serviceDatasourceDao;
    @Autowired
    ServiceTableDaoImpl serviceTableDao;

    @Override
    public Rsp call(SerInfo serInfo) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("serviceName",serInfo.getServiceName());
        List<ServiceBean> serviceBeanList = serviceBeanDao.query(param);
        if(null == serviceBeanList||serviceBeanList.isEmpty()){
            return RspUtil.error(888);
        }
        ServiceBean serviceBean = null;
        for(ServiceBean sb :serviceBeanList){
            if(sb.getVersion().contains(serInfo.getVersion())){
                serviceBean = sb;
                break;
            }
        }
        if(null==serviceBean){
            return RspUtil.error(888);
        }
        param.clear();
        param.put("serviceId",serviceBean.getId());
        List<ServiceDatasource> serviceDatasourceList = serviceDatasourceDao.query(param);
        List<ServiceTable> serviceTableList = serviceTableDao.query(param);
        serviceBean.setDs(serviceDatasourceList);
        serviceBean.setTables(serviceTableList);
        return RspUtil.success(serviceBean);
    }
}
