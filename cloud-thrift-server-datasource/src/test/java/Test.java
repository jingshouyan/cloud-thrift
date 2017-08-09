import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.jing.cloud.service.util.bean.BeanUtil;

import java.util.Map;
import java.util.Set;


public class Test {
    public static void main(String[] args){
        Set<String> keySet = Sets.newHashSet();
        String key = "abc";
//        keySet.add(key);
//        keySet.add("abc");
        keySet.add(new String("abc"));
        System.out.println(keySet.contains(key));
        Preconditions.checkArgument(keySet.contains(key),
                String.format("dos not contains field [%s]",key));

    }
}
