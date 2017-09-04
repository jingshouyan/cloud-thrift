import com.jing.cloud.service.App;
import com.jing.cloud.service.config.ServConf;
import com.jing.cloud.service.user.dao.impl.UserDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 29017 on 2017/7/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)

public class UtestUser {
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
    private UserDaoImpl userDaoImpl;


    private static final ExecutorService executor = Executors.newFixedThreadPool(200);


    @Test
    public void dbTest()throws Exception{
        showtime();
//        Page<User> page = new Page<>();
//        page.setPage(1);
//        page.setPageSize(200);
//        page = userDaoImpl.query(null,page);
//        final CountDownLatch cdl = new CountDownLatch(page.getTotalPage());
//        showtime();
//        while(true){
//            page = userDaoImpl.query(null,page);
//            final List<User> users = page.getList();
//            executor.submit(new Runnable() {
//                @Override
//                public void run() {
////                    try{
////                        Thread.sleep(10000L);
////                        System.out.println(Thread.currentThread().getName() +"sleep 10000ms");
////                    }catch (Exception e){}
//
//                    for (User user: users) {
//                        String ext = user.getEntExtend();
//                        JSONArray ja = JSON.parseArray(ext);
//                        JSONObject jo = ja.getJSONObject(0);
//                        String account = jo.getString("username");
//                        if (account==null) continue;
//                        user.setSchool(account);
//                        user.setName(null);
//                        user.setEntExtend(null);
//                        userDaoImpl.update(user);
//                    }
//                    cdl.countDown();
//                }
//            });
//
//            if(page.getPage()==page.getTotalPage()){
//                break;
//            }
//            page.setPage(page.getPage()+1);
//        }
//        cdl.await();
        showtime();
    }
    private static long time = System.currentTimeMillis();
    private static void showtime(){
        long t2 = System.currentTimeMillis();
        System.out.println(t2-time);
        time = t2;
    }


}
