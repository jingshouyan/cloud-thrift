import com.jing.cloud.service.App;
import com.jing.cloud.service.Req;
import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.config.ServConf;
import com.jing.cloud.service.user.method.Login;
import com.jing.cloud.service.user.method.RegisterUser;
import com.jing.cloud.service.user.method.param.LoginBean;
import com.jing.cloud.service.user.method.param.User4Reg;
import com.jing.cloud.service.util.keygen.DefaultKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by 29017 on 2017/9/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@Slf4j
public class LoginTest {
    private static final String LOG_ROOT_LEVEL = "log.root.level";
    static{
        System.setProperty(LOG_ROOT_LEVEL, ServConf.getString(LOG_ROOT_LEVEL));
    }

    private static final Executor exe = Executors.newFixedThreadPool(20);

    @Autowired
    private Login login;

    @org.junit.Test
    public void login() throws Exception{
        int j =10000;
        final CountDownLatch c = new CountDownLatch(j);
        for (int i = 0; i < j; i++) {
            final  int k = i;
            exe.execute(new Runnable() {
                @Override
                public void run() {
                    try{

                        long start = System.currentTimeMillis();

                        LoginBean lgb = new LoginBean();
                        lgb.setLang("zh_cn");
                        lgb.setDeviceType(1);
                        lgb.setMac("dd:dd");
                        lgb.setAccount("kkk"+k);
                        lgb.setDeviceInfo("an-www");
                        lgb.setPw("sdfkj1");
                        lgb.setAccountType(1);
                        Rsp r = login.call(lgb);
                        long end = System.currentTimeMillis();
                        log.info("{},use {}ms",r,end-start);
                    }catch (Exception e){

                    }
                    c.countDown();
                }
            });

        }
        c.await();
    }

}
