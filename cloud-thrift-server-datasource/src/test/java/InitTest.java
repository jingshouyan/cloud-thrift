import com.jing.cloud.service.App;
import com.jing.cloud.service.user.dao.AccountDao;
import com.jing.cloud.service.user.dao.LoginRecordDao;
import com.jing.cloud.service.user.dao.PasswordDao;
import com.jing.cloud.service.user.dao.TicketDao;
import com.jing.cloud.service.user.dao.UserDao;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 29017 on 2017/9/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class InitTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PasswordDao passwordDao;

    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private LoginRecordDao loginRecordDao;

    @Test
    @Order(2)
    public void createTable(){
        userDao.createTable();
        accountDao.createTable();
        passwordDao.createTable();
        ticketDao.createTable();
        loginRecordDao.createTable();
    }

    @Test
    @Order(1)
    public void dropTable(){
        userDao.dropTable();
        accountDao.dropTable();
        passwordDao.dropTable();
        ticketDao.dropTable();
        loginRecordDao.dropTable();
    }
}
