import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.util.bean.BeanUtil;

import java.util.Map;


public class Test {
    public static void main(String[] args){
        ServiceDatasource db = new ServiceDatasource();
        db.setUrl("123");
        db.setDriver("org");
        db.setPwd("pwd");
        db.setUsername("username");
        db.forCreate();
        Map<String,Object> map = BeanUtil.Obj2Map(db);

        System.out.println(map);
    }
}
