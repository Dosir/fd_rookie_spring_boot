package com.fd.rookie.spring.boot.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现数据源切换的功能就是自定义一个类扩展AbstractRoutingDataSource抽象类，
 * 其实该相当于数据源DataSource的路由中介，可以实现在项目运行时根据相应key值切换到对应的数据源DataSource上
 */
public class DynamicDataSource extends AbstractRoutingDataSource{
    static final Map<DatabaseType, List<String>> METHOD_TYPE_MAP = new HashMap<>();

    /**
     * spring在开始进行数据库操作时会通过这个方法来决定使用哪个数据库
     * @return
     */
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        DatabaseType type = DatabaseContextHolder.getDatabaseType();
        logger.info("====================dataSource ==========" + type);
        return type;
    }

    void setMethodType(DatabaseType type, String content) {
        List<String> list = Arrays.asList(content.split(","));
        METHOD_TYPE_MAP.put(type, list);
    }
}
