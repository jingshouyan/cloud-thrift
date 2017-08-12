import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.util.db.Bean4DbUtil;
import com.jing.cloud.service.util.keygen.DefaultKeyGenerator;

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

        ServiceDatasource sd = new ServiceDatasource() ;
        Map<String,Object> map = Bean4DbUtil.Bean2Map(sd);
        System.out.println(map);

        System.out.println(DefaultKeyGenerator.getInstance().generateKey().toString());

        int i = 1;

    }

}
