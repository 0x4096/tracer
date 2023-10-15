package com.github.x4096.tracer.interceptor.mybatis;

import com.github.x4096.tracer.configuration.TracerProperties;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.List;

/**
 * @author 0x4096.peng@gmail.com
 * @date 2023/7/30
 */
@ConditionalOnProperty(prefix = "com.github.x4096.tracer", name = {"global-log-out", "my-sql-log-out"}, matchIfMissing = true)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class})
public class MybatisSqlTracerInterceptorLoader implements InitializingBean {

    @Autowired(required = false)
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Autowired(required = false)
    private TracerProperties tracerProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        MybatisSqlInterceptor interceptor = new MybatisSqlInterceptor(tracerProperties);
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.addInterceptor(interceptor);
        }
    }

}
