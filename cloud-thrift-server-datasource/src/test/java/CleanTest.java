import com.jing.cloud.service.App;
import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.bean.ServiceBean;
import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.bean.ServiceTable;
import com.jing.cloud.service.config.ServConf;
import com.jing.cloud.service.dao.impl.ServiceBeanDaoImpl;
import com.jing.cloud.service.dao.impl.ServiceDatasourceDaoImpl;
import com.jing.cloud.service.dao.impl.ServiceTableDaoImpl;
import com.jing.cloud.service.method.GetDbInfo;
import com.jing.cloud.service.method.param.SerInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 29017 on 2017/7/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class CleanTest {
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
    private ServiceDatasourceDaoImpl serviceDatasourceDao;

    @Autowired
    private ServiceTableDaoImpl serviceTableDao;

    @Autowired
    private ServiceBeanDaoImpl serviceBeanDao;

    @Autowired
    private GetDbInfo getDbInfo;

    @Test
    public void dbTest(){
        clean();

        init();

        getinfo();

    }

    public void getinfo(){
        try{
            SerInfo s = new SerInfo();
            s.setServiceName("user");
            s.setVersion("v1.0.0");
            Rsp rsp = getDbInfo.call(s);
            System.out.println(rsp);
        }catch (Exception e){

        }

    }

    public void init(){
        ServiceBean sb = new ServiceBean();
        sb.setServiceName("user");
        sb.setVersion("v1.0.0");
        sb.forCreate();
        serviceBeanDao.insert(sb);
        ServiceDatasource sd = new ServiceDatasource();
        sd.setServiceId(sb.getId());
        sd.setDriver("com.mysql.jdbc.Driver");
        sd.setUrl("jdbc:mysql://127.0.0.1:3306/user_00?useUnicode=true&characterEncoding=utf8");
        sd.setName("user_00");
        sd.setUsername("jing");
        sd.setPwd("le");
        sd.setTestWhileIdle(false);
        sd.setValidationQuery("select 1");
        sd.setInitialSize(5);
        sd.setMinIdle(5);
        sd.setMaxActive(30);
        sd.forCreate();
        serviceDatasourceDao.insert(sd);
        ServiceDatasource sd1 = new ServiceDatasource();
        sd1.setServiceId(sb.getId());
        sd1.setDriver("com.mysql.jdbc.Driver");
        sd1.setUrl("jdbc:mysql://127.0.0.1:3306/user_01?useUnicode=true&characterEncoding=utf8");
        sd1.setName("user_01");
        sd1.setUsername("jing");
        sd1.setPwd("le");
        sd1.setTestWhileIdle(true);
        sd1.setValidationQuery("select 1");
        sd1.setInitialSize(5);
        sd1.setMinIdle(5);
        sd1.setMaxActive(30);
        sd1.forCreate();
        serviceDatasourceDao.insert(sd1);
        ServiceTable st = new ServiceTable();
        st.setServiceId(sb.getId());
        st.setLogicName("user");
        st.setDataSourceSharding(2);
        st.setDataSourceShardingKey("id");
        st.setTableSharding(5);
        st.setTableShardingKey("id");
        st.forCreate();
        serviceTableDao.insert(st);
    }

    public void clean(){
        serviceBeanDao.delete4Batch(null);
        serviceDatasourceDao.delete4Batch(null);
        serviceTableDao.delete4Batch(null);
    }



}
