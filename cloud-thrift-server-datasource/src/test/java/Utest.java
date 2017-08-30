import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.config.ServConf;
import com.jing.cloud.service.App;
import com.jing.cloud.service.dao.impl.ServiceDatasourceDaoImpl;
import com.jing.cloud.service.dao.impl.ServiceTableDaoImpl;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.Page;
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

public class Utest {
    private static final String SERVICE_NAME = "service.name";
    private static final String LOG_ROOT_LEVEL = "log.root.level";
    private static final String LOG_ROOT_PATH = "log.root.path";
    static{

        System.setProperty(SERVICE_NAME, ServConf.getString(SERVICE_NAME));
        System.setProperty(LOG_ROOT_LEVEL,ServConf.getString(LOG_ROOT_LEVEL));
        System.setProperty(LOG_ROOT_LEVEL,"debug");
        System.setProperty(LOG_ROOT_PATH,ServConf.getString(LOG_ROOT_PATH));
    }

    @Autowired
    private ServiceDatasourceDaoImpl serviceDatasourceDaoImpl;

    @Autowired
    private ServiceTableDaoImpl serviceTableDaoImpl;

    private static List<Long> ids  = new ArrayList<>();
    @Test
    public void dbTest(){
//        insertTest();
//        updateTest();

//        batchupdateTest();
//        batchInsertTest();
//        batchDeleteTest();

        page();
//
//            page.setPage(page.getPage()+1);


    }

    public void page(){
        Page<ServiceDatasource> page = new Page<>();
        page.setPage(1);
        page.setPageSize(10);
        Map<String,Object> map = new HashMap<>();
        Compare compare = new Compare();
        compare.setGt(123L);
        map.put("createdAt",compare);

        Compare compare2 = new Compare();
        compare2.setLike("%user%nam%");

        Compare compare3 = new Compare();
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        ids.add(4L);
        ids.add(5L);
        compare3.setIn(ids);

        List<Long> ids2 = new ArrayList<>();
        ids2.add(3L);
        ids2.add(4L);
        compare3.setNotIn(ids2);
//        map.put("id",compare3);

            page = serviceDatasourceDaoImpl.query(map,page);
            System.out.println(page);
    }



    public void batchupdateTest(){
        ServiceDatasource ds = new ServiceDatasource();
        ds.setId(11L);
        ds.setUsername("akdkf");
        ds.forUpdate();
        Map<String,Object> condition = new HashMap<>();
        condition.put("username","username");
        serviceDatasourceDaoImpl.update(ds,condition);
    }

    public void updateTest(){
        for(long id :ids) {
            ServiceDatasource ds = serviceDatasourceDaoImpl.find(id);

            ds.setDriver("abc"+id);
            ds.forUpdate();
            serviceDatasourceDaoImpl.update(ds);
            System.out.println(ds);


            System.out.println(ds);
        }

    }

    public void batchInsertTest(){
        List<ServiceDatasource> list = new ArrayList<>();
        for(long i=0;i<100;i++){
            ServiceDatasource db = new ServiceDatasource();
            db.setUrl("123");
            db.setDriver("org");
            db.setPwd("pwd");
            db.setUsername("username"+i);
            db.forCreate();
            list.add(db);
        }
        int a = serviceDatasourceDaoImpl.insert(list);
        System.out.println(a);
    }

    public void insertTest(){
        for(long i=0;i<2;i++) {
            ServiceDatasource db = new ServiceDatasource();
            db.setUrl("123");
            db.setDriver("org");
            db.setPwd("pwd");
            db.setUsername("username");
            db.forCreate();
//            db.setId(i);
            serviceDatasourceDaoImpl.insert(db);
            ids.add(db.getId());
            System.out.println(db);
        }
    }

    public void batchDeleteTest(){
        Map<String,Object> condition = new HashMap<>();
        condition.put("username","username");
        serviceDatasourceDaoImpl.delete4Batch(condition);
    }

}
