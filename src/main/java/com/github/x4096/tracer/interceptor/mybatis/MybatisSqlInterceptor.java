package com.github.x4096.tracer.interceptor.mybatis;

import com.github.x4096.tracer.configuration.TracerMySqlProperties;
import com.github.x4096.tracer.configuration.TracerProperties;
import com.github.x4096.tracer.interceptor.TracingMethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.time.Clock;
import java.util.*;
import java.util.regex.Matcher;

/**
 * mybatis sql 拦截器 包含增删改查 sql 打印
 *
 * @author 0x4096.peng@gmail.com
 * @date 2023/7/30
 * @readme
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        }
)
public class MybatisSqlInterceptor extends TracingMethodInterceptor implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MybatisSqlInterceptor(TracerProperties tracerProperties) {
        super(tracerProperties);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args == null || args.length < 2) {
            return invocation.proceed();
        }

        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        BoundSql boundSql = ms.getBoundSql(parameter);

        TracerMySqlProperties mySqlProperties = tracerProperties.getMySqlProperties();
        if (Objects.isNull(mySqlProperties)) {
            return invocation.proceed();
        }

        long start = 0;
        if (mySqlProperties.isMySqlExecuteTimeLogOut()) {
            start = Clock.systemUTC().millis();
        }

        try {
            return invocation.proceed();
        } finally {
            if (mySqlProperties.isMySqlLogOut()) {
                String sql = getSql(ms, boundSql);
                long end;
                if (mySqlProperties.isMySqlExecuteTimeLogOut()) {
                    end = Clock.systemUTC().millis() - start;
                    logger.info("sql: {}, executeTime(millisecond): {}", sql, end);
                } else {
                    logger.info("sql: {}", sql);
                }
            }
        }

    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    private static String getSql(MappedStatement ms, BoundSql boundSql) {
        Configuration configuration = ms.getConfiguration();
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(sql.length());
        str.append(ms.getId());
        str.append(": ");
        str.append(sql);
        return str.toString();
    }

    private static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (!parameterMappings.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration
                    .getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?",
                        Matcher.quoteReplacement(getParameterValue(parameterObject)));

            } else {
                MetaObject metaObject = configuration
                        .newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql
                                .getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(
                    DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }

}
