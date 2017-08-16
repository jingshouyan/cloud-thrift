import com.jing.cloud.service.bean.ServiceBean;
import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.util.db.Bean4DbUtil;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.OrderBy;
import com.jing.cloud.service.util.db.Page;
import com.jing.cloud.service.util.keygen.DefaultKeyGenerator;

import java.util.HashMap;
import java.util.Map;


public class Test {
    public static void main(String[] args){
//        Set<String> keySet = Sets.newHashSet();
//        String key = "abc";
////        keySet.add(keyColumn);
////        keySet.add("abc");
//        keySet.add(new String("abc"));
//        System.out.println(keySet.contains(key));
//        Preconditions.checkArgument(keySet.contains(key),
//                String.format("dos not contains field [%s]",key));

//        ServiceDatasource sd = new ServiceDatasource() ;
//        Map<String,Object> map = Bean4DbUtil.Bean2Map(sd);
//        System.out.println(map);
//
//        System.out.println(DefaultKeyGenerator.getInstance().generateKey().toString());
//
//        int i = 1;
//
//        for (int j = 0; j <0 ; j++) {
//            System.out.println(111);
//        }

        Page<ServiceBean> page = new Page<>();
        page.setPage(1);
        page.setPageSize(10);
        Map<String,Object> m2 = new HashMap<>();
        Compare compare = new Compare();
        compare.setGt(123L);
        m2.put("createdAt",compare);
        m2.put("version","222");
        page.addOrderBy(OrderBy.newInstance("id"));
        System.out.println(page);
    }

}
