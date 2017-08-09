import com.google.common.base.Preconditions;
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
        boolean setKey = false;
        Preconditions.checkArgument(setKey,"key value is not set!");

        System.out.println(map);
    }
}
