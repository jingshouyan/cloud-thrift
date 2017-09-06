import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.mindrot.jbcrypt.BCrypt;

public class Test {
    public static void main(String[] args){

        String a = "111";
//        a.h
        for (int i = 5; i < 25; i++) {
            System.out.println("-------------------");
            showtime();
            String pwd = BCrypt.hashpw("abc",BCrypt.gensalt(i));
            System.out.println(pwd);
            showtime();
            System.out.println(BCrypt.checkpw("abc",pwd));
            System.out.println("-------------------");
//            User user = new User();
////            user.setId(DefaultKeyGenerator.getInstance().generateKey().longValue());
//            user.setUsername("zhangsan_"+String.format("%08d",i));
//            user.setBirthday("2017/07/01");
//            user.forCreate();
//            Bean4DbUtil.encryptOrDecryptBean(user,true);
//            System.out.println(user);
//            Bean4DbUtil.encryptOrDecryptBean(user,false);
//            System.out.println(user);
        }
//        String sss = String.class.getSimpleName();
//
//        System.out.println(sss);
//
//        sss = Integer.class.getSimpleName();
//
//        System.out.println(sss);
//        sss = int.class.getSimpleName();
//
//        System.out.println(sss);
//        sss = String.class.getSimpleName();
//
//        System.out.println(sss);
//
//        sss = Bean4DbUtil.createTableSql(User.class);
//        System.out.println(sss);
//
//        sss = Bean4DbUtil.createTableSql(ServiceBean.class);
//        System.out.println(sss);
//
//        sss = Bean4DbUtil.createTableSql(ServiceDatasource.class);
//        System.out.println(sss);
//
//        sss = Bean4DbUtil.createTableSql(ServiceTable.class);
//        System.out.println(sss);
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

//        Page<ServiceBean> page = new Page<>();
//        page.setPage(1);
//        page.setPageSize(10);
//        Map<String,Object> m2 = new HashMap<>();
//        Compare compare = new Compare();
//        compare.setGt(123L);
//        m2.put("createdAt",compare);
//        m2.put("version","222");
//        page.addOrderBy(OrderBy.newInstance("id"));
//        System.out.println(page);
//        int k=10000;
//        showtime();
//        String s = "";
//        for (int i = 0; i < k; i++) {
//            String a = "a";
//            s+=a;
//        }
//        showtime();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < k; i++) {
//            String a = "a";
//            sb.append(a);
//        }
//        showtime();
//
//        String a = String.format("%02d",2);
//        System.out.println(a);
//
//        for (int i = 0; i < 10000; i++) {
//
//        System.out.println(DefaultKeyGenerator.getInstance().generateKey().longValue());


//        }
    }

    private static long time = System.currentTimeMillis();
    private static void showtime(){
        long t2 = System.currentTimeMillis();
        System.out.println(t2-time);
        time = t2;
    }
}
