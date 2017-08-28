import com.alibaba.fastjson.JSON;
import com.jing.cloud.service.bean.ServiceBean;

/**
 * Created by 29017 on 2017/8/26.
 */
public class JSONTest {
    public static void main(String[] args) {
        String json = "{\n" +
                "    \"shardingShowSql\": true,\n" +
                "    \"shardingMetricsEnable\": true,\n" +
                "    \"shardingmetricsMillisPeriod\": 300,\n" +
                "    \"createdAt\": 1503403632544,\n" +
                "    \"ds\": [\n" +
                "        {\n" +
                "            \"createdAt\": 1503403632611,\n" +
                "            \"driver\": \"com.mysql.jdbc.Driver\",\n" +
                "            \"id\": 106845836876447740,\n" +
                "            \"initialSize\": 5,\n" +
                "            \"maxActive\": 30,\n" +
                "            \"minIdle\": 5,\n" +
                "            \"name\": \"user_00\",\n" +
                "            \"pwd\": \"le\",\n" +
                "            \"serviceId\": 106845836599623680,\n" +
                "            \"testWhileIdle\": false,\n" +
                "            \"updatedAt\": 1503403632611,\n" +
                "            \"url\": \"jdbc:mysql://127.0.0.1:3306/user_00?useUnicode=true&characterEncoding=utf8\",\n" +
                "            \"username\": \"jing\",\n" +
                "            \"validationQuery\": \"select 1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"createdAt\": 1503403632643,\n" +
                "            \"driver\": \"com.mysql.jdbc.Driver\",\n" +
                "            \"id\": 106845837010665470,\n" +
                "            \"initialSize\": 5,\n" +
                "            \"maxActive\": 30,\n" +
                "            \"minIdle\": 5,\n" +
                "            \"name\": \"user_01\",\n" +
                "            \"pwd\": \"le\",\n" +
                "            \"serviceId\": 106845836599623680,\n" +
                "            \"testWhileIdle\": true,\n" +
                "            \"updatedAt\": 1503403632643,\n" +
                "            \"url\": \"jdbc:mysql://127.0.0.1:3306/user_01?useUnicode=true&characterEncoding=utf8\",\n" +
                "            \"username\": \"jing\",\n" +
                "            \"validationQuery\": \"select 1\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"id\": 106845836599623680,\n" +
                "    \"serviceName\": \"user\",\n" +
                "    \"tables\": [\n" +
                "        {\n" +
                "            \"createdAt\": 1503403632704,\n" +
                "            \"dataSourceSharding\": 2,\n" +
                "            \"dataSourceShardingKey\": \"id\",\n" +
                "            \"id\": 106845837266518020,\n" +
                "            \"logicName\": \"user\",\n" +
                "            \"serviceId\": 106845836599623680,\n" +
                "            \"tableSharding\": 5,\n" +
                "            \"tableShardingKey\": \"id\",\n" +
                "            \"updatedAt\": 1503403632704\n" +
                "        }\n" +
                "    ],\n" +
                "    \"updatedAt\": 1503403632544,\n" +
                "    \"version\": \"v1.0.0\"\n" +
                "}";
        showtime();
        for (int i = 0; i <1000000; i++) {
            JSON.parseObject(json, ServiceBean.class);
        }
        showtime();
    }

    private static long time = System.currentTimeMillis();
    private static void showtime(){
        long t2 = System.currentTimeMillis();
        System.out.println(t2-time);
        time = t2;
    }
}
