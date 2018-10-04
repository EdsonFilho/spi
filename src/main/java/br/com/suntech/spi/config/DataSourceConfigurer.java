package br.com.suntech.spi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DataSourceConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfigurer.class);

    public static final String MYBATIS_CONFIGURATION = "mybatis-configuration.xml";
    public static final String DATASOURCE_PROPERTIES = "hikari.properties";
    public static final String DATASOURCE_BEAN_NAME = "datasource";
    public static final String SQL_SESSIONFACTORY_BEAN_NAME = "sqlSessionFactory";

    @Bean(name = DATASOURCE_BEAN_NAME)
    public DataSource getDataSource() throws Exception {
        Resource config = new ClassPathResource(DATASOURCE_PROPERTIES);
        Properties props = PropertiesLoaderUtils.loadProperties(config);
        HikariConfig configuration = new HikariConfig(props);
        configuration.setAutoCommit(false);
        HikariDataSource pooledDS = new HikariDataSource(configuration);
        return new TransactionAwareDataSourceProxy(pooledDS);
    }

    @Bean(name = SQL_SESSIONFACTORY_BEAN_NAME) @DependsOn(DATASOURCE_BEAN_NAME)
    public SqlSessionFactory sqlSessionFactory(ApplicationContext context) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIGURATION));
        bean.setDataSource((DataSource) context.getBean(DATASOURCE_BEAN_NAME));
        return bean.getObject();
    }

    @Bean @DependsOn(SQL_SESSIONFACTORY_BEAN_NAME)
    public MapperScannerConfigurer mapperScannerConfigurer() throws Exception {
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("br.com.suntech.spi.mapper");
        return msc;
    }

}