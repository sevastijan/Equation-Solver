package pl.kurs.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
public class BeansConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean createEMF(JpaVendorAdapter adapter, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

        emf.setJpaVendorAdapter(adapter);
        emf.setPersistenceUnitName("equationSolverPersistenceUnit");
        emf.setPackagesToScan("pl.kurs.models");
        emf.setDataSource(dataSource);

        return emf;
    }

    @Profile({"prod", "!prod & !dev"})
    @Bean
    public HibernateJpaVendorAdapter createMySQLAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        adapter.setGenerateDdl(true);

        return adapter;
    }

    @Profile({"prod", "!prod & !dev"})
    @Bean
    public BasicDataSource createDataSourceMySQL() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/equation-solver?useSSL=false&serverTimezone=CET");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setInitialSize(5);

        return dataSource;
    }

    @Profile("dev")
    @Bean
    public HibernateJpaVendorAdapter createAdapterH2() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.H2);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);

        return adapter;
    }

    @Profile("dev")
    @Bean
    public BasicDataSource createDataSourceH2() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:mem:testdb");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setDriverClassName("org.h2.Driver");
        ds.setInitialSize(5);
        return ds;
    }

}
