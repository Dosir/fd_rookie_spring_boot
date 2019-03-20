package com.fd.rookie.spring.boot.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

@Component
@Intercepts( {
        @Signature(method = "query", type = Executor.class, args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class }),
        @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class, Integer.class }) })
public class MyInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        System.out.println("Invocation.proceed()");
        return result;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }

}

/**
 * 代码说明：
 1）首先看setProperties方法，这个方法在Configuration初始化当前的Interceptor时就会执行，这里不做任何操作。
 2）其次看plugin方法中我们是用的Plugin的逻辑来实现Mybatis的逻辑的。
 3）接着看MyInterceptor类上我们用@Intercepts标记了这是一个Interceptor，然后在@Intercepts中定义了两个@Signature，即两个拦截点。
 第一个@Signature我们定义了该Interceptor将拦截Executor接口中参数类型为MappedStatement、Object、RowBounds和ResultHandler的query方法；
 第二个@Signature我们定义了该Interceptor将拦截StatementHandler中参数类型为Connection的prepare方法。
 4）最后再来看下Intercept方法，这里我们人只是简单的打印例如一句话，然后调用Invocation的proceed方法，是当前方法正常的调用。
 5）注册拦截器：只需要在实现的拦截器类上加上 @Component 注解即可。
 */
