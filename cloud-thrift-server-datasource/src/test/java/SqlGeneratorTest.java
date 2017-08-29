import com.google.common.collect.Lists;
import com.jing.cloud.service.bean.ServiceTable;
import com.jing.cloud.service.util.db.SqlPrepared;
import com.jing.cloud.service.util.db.sql.generator.SqlGenerator4Mysql;

import java.util.List;

/**
 * Created by 29017 on 2017/8/29.
 */
public class SqlGeneratorTest {
    static SqlGenerator4Mysql<ServiceTable> sqlGenerator = new SqlGenerator4Mysql<>(ServiceTable.class);

    public static void main(String[] args) {
        List<ServiceTable> serviceTableList = Lists.newArrayList();
        ServiceTable st = new ServiceTable();
        st.setServiceId(123L);
        st.setLogicName("user");
        st.setDataSourceSharding(2);
        st.setDataSourceShardingKey("id");
        st.setTableSharding(5);
        st.setTableShardingKey("id");
        st.forCreate();
        serviceTableList.add(st);
        SqlPrepared sqlPrepared = sqlGenerator.batchInsert(serviceTableList);
        System.out.println(sqlPrepared.getSql());
        System.out.println(sqlPrepared.getParams());
        sqlPrepared = sqlGenerator.batchUpdate(st,null);
        System.out.println(sqlPrepared.getSql());
        System.out.println(sqlPrepared.getParams());

    }
}
