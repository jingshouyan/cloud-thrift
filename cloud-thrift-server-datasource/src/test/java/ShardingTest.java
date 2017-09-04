import com.jing.cloud.service.App;
import com.jing.cloud.service.config.ServConf;
import com.jing.cloud.service.dao.impl.ServiceBeanDaoImpl;
import com.jing.cloud.service.user.dao.impl.UserDaoImpl;
import org.junit.After;
import org.junit.Before;
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
public class ShardingTest {
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
    private UserDaoImpl userDao;

    @Autowired
    private ServiceBeanDaoImpl serviceBeanDao;


    @Before
    public void createTable(){
        int i =userDao.createTable();
        System.out.println(i);
        serviceBeanDao.createTable();
    }

    @After
    public void dropTable(){
        int i =userDao.dropTable();
        System.out.println(i);
        serviceBeanDao.dropTable();
    }

    @Test
    public void dbTest(){
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setName("zhangsan_"+String.format("%08d",i));
//            user.setEntExtend("2017/07/01");
////            user.forCreate();
//            userDao.insert(user);
//        }
//        User u = new User();
//        u.setRealname(123+"");
//        userDao.update(u,null);
//         u = userDao.find(111L);
//        u=userDao.find(107994870215344130L);
//        List<User> users = userDao.query(null);
//        System.out.println(users.size());
//        for (User user:users
//             ) {
//            System.out.println(user);
//
//        }
    }

    public void serviceBeanTest(){
//        serviceBeanDao.insert();
    }




}
