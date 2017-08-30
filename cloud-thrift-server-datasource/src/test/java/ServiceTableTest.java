import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jing.cloud.service.App;
import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.bean.ServiceTable;
import com.jing.cloud.service.config.ServConf;
import com.jing.cloud.service.dao.impl.ServiceDatasourceDaoImpl;
import com.jing.cloud.service.dao.impl.ServiceTableDaoImpl;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.OrderBy;
import com.jing.cloud.service.util.db.Page;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 29017 on 2017/7/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)

public class ServiceTableTest {
    private static final String SERVICE_NAME = "service.name";
    private static final String LOG_ROOT_LEVEL = "log.root.level";
    private static final String LOG_ROOT_PATH = "log.root.path";
    static{
        System.setProperty(SERVICE_NAME, ServConf.getString(SERVICE_NAME));
        System.setProperty(LOG_ROOT_LEVEL,ServConf.getString(LOG_ROOT_LEVEL));
        System.setProperty(LOG_ROOT_LEVEL,"INFO");
        System.setProperty(LOG_ROOT_PATH,ServConf.getString(LOG_ROOT_PATH));
    }


    @Autowired
    private ServiceTableDaoImpl dao;

    private static List<Long> ids  = new ArrayList<>();

    @Before
    public void createTable(){
        dao.createTable();
    }


    @Test
    public void batchInsert(){
        int j = 1;
        ServiceTable serviceTable = new ServiceTable();
//        serviceTable.setId((long)j);
        serviceTable.setServiceId(123L);
        serviceTable.setLogicName("table"+j);
        serviceTable.setTableShardingKey("id");
        serviceTable.setDataSourceSharding(j);
        serviceTable.setDataSourceShardingKey("you");
        serviceTable.setTableSharding(j%5+1);
        serviceTable.forCreate();
        dao.insert(serviceTable);
        List<ServiceTable> stl = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            ServiceTable st = new ServiceTable();
            st.setId((long)i);
            st.setServiceId(123L);
            st.setLogicName("table"+i);
            st.setTableShardingKey("id");
            st.setDataSourceSharding(i);
            st.setDataSourceShardingKey("you");
            st.setTableSharding(i%5+1);
            st.forCreate();
            stl.add(st);
        }
//        dao.insert(stl);
        Page<ServiceTable> page = new Page<>();
        page.setPage(1);
        page.setPageSize(15);
        List<OrderBy> orderBies = new ArrayList<>();
        orderBies.add(new OrderBy("id",true));
        orderBies.add(new OrderBy("logicName",false));
        page.setOrderBies(orderBies);
        Map<String,Object> condition = Maps.newHashMap();
        Compare id = new Compare();
        List<Long> idIn = new ArrayList<>();
        for(long k = 0;k<100;k++){
            idIn.add(k);
        }

        id.setIn(idIn);
        List<Long> idNotIn = new ArrayList<>();
        for(long k = 50;k<150;k++){
            idNotIn.add(k);
        }
//        id.setNotIn(idNotIn);
        id.setGt(20);
        id.setGte(22);
        id.setLt(55);
        id.setLte(56);
        id.setEmpty(false);
        id.setNe(7);
        id.setLike("%1%");
        condition.put("id",id);
        page = dao.query(condition,page);
        page.getTotalPage();
//        Assert.assertEquals(100,page.getTotalCount());
//        Assert.assertEquals(7,page.getTotalPage());
        for (ServiceTable st:
             page.getList()) {
            Assert.assertTrue(st.getLogicName().startsWith("table"));

        }
    }

    @After
    public void dropTable(){
        dao.dropTable();
    }

}
