import com.jing.cloud.service.App;
import com.jing.cloud.service.bean.ServiceBean;
import com.jing.cloud.service.config.ServConf;
import com.jing.cloud.service.dao.impl.ServiceBeanDaoImpl;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.OrderBy;
import com.jing.cloud.service.util.db.Page;
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

public class UtestServiceBean {
    private static final String SERVICE_NAME = "service.name";
    private static final String LOG_ROOT_LEVEL = "log.root.level";
    private static final String LOG_ROOT_PATH = "log.root.path";
    static{

        System.setProperty(SERVICE_NAME, ServConf.getString(SERVICE_NAME));
        System.setProperty(LOG_ROOT_LEVEL,ServConf.getString(LOG_ROOT_LEVEL));
        System.setProperty(LOG_ROOT_LEVEL,"info");
        System.setProperty(LOG_ROOT_PATH,ServConf.getString(LOG_ROOT_PATH));
    }

    @Autowired
    private ServiceBeanDaoImpl serviceBeanDao;




    private static List<Long> ids  = new ArrayList<>();

    @Before
    public void create(){
        serviceBeanDao.createTable();
    }

    @Test
    public void dbTest(){
        insertTest();
        updateTest();

//        batchupdateTest();
//        batchInsertTest();
//        batchDeleteTest();

        page();
        deleteTest();
//
//            page.setPage(page.getPage()+1);


    }

    public void page(){
        Page<ServiceBean> page = new Page<>();
        page.setPage(1);
        page.setPageSize(10);
        Map<String,Object> map = new HashMap<>();
        Compare compare = new Compare();
        compare.setGt(123L);
        map.put("createdAt",compare);
        map.put("version","222");
        page.addOrderBy(OrderBy.newInstance("id"));
        page.addOrderBy(OrderBy.newInstance("id"));
        page = serviceBeanDao.query(map,page);
        System.out.println(page);
    }



    public void batchupdateTest(){
        ServiceBean sb = new ServiceBean();
        sb.forUpdate();
        serviceBeanDao.update(sb,null);
    }

    public void updateTest(){
        for(long id :ids) {
            ServiceBean ds = serviceBeanDao.find(id);
            ds.setServiceName("sksjsf"+id);
            ds.forUpdate();
            serviceBeanDao.update(ds);
            System.out.println(ds);


            System.out.println(ds);
        }

    }


    public void deleteTest(){
        for(long id :ids) {
            serviceBeanDao.delete(id);
        }
    }

    public void batchInsertTest(){
//        List<ServiceDatasource> list = new ArrayList<>();
//        for(long i=0;i<100;i++){
//            ServiceDatasource db = new ServiceDatasource();
//            db.setUrl("123");
//            db.setDriver("org");
//            db.setPwd("pwd");
//            db.setUsername("username"+i);
//            db.forCreate();
//            list.add(db);
//        }
//        int a = serviceDatasourceDaoImpl.insert(list);
//        System.out.println(a);
    }

    public void insertTest(){
        for(long i=0;i<2;i++) {
            ServiceBean db = new ServiceBean();
            db.setServiceName("ddd");
            db.setVersion("222");
            db.forCreate();
//            db.setId(i);
            serviceBeanDao.insert(db);
            ids.add(db.getId());
            System.out.println(db);
        }
    }

    public void batchDeleteTest(){
//        Map<String,Object> condition = new HashMap<>();
//        condition.put("username","username");
//        serviceDatasourceDaoImpl.delete4Batch(condition);
    }

}
