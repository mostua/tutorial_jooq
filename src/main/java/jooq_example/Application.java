package jooq_example;

import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
@Configuration
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Autowired
    private org.jooq.Configuration configuration;

    @Bean(name = "dbConfiguration")
    public org.jooq.Configuration getConfiguration() {
        return new DefaultConfiguration().set(getConnection()).set(SQLDialect.POSTGRES);
    }

    @Bean
    public TelephoneBookManager getTelephoneBookManager() {
        return new TelephoneBookManagerImpl(configuration);
    }

    @Bean
    public Connection getConnection() {
        String userName = "postgres";
        String password = "yuPbb226yqDa5UlEaTme";
        String url = "jdbc:postgresql://localhost:5432/jooq_tutorial";
        try {
            return DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            throw new RuntimeException("Cannot connect to database");
        }
    }

}
