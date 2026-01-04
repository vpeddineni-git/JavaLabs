package com.codewithvenkat.demo.Config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MyDbConfig {

    @Value("${mydblab.datasource.url}")
    private String url;

    @Value("${mydblab.datasource.username}")
    private String username;

    @Value("${mydblab.datasource.password}")
    private String password;

    @Bean(name = "myDbDataSource")
    public DataSource myDbDataSource() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setPoolName("MyDbHikariPool");
        return new HikariDataSource(cfg);
    }

    @Bean(name = "myDbJdbcTemplate")
    public JdbcTemplate myDbJdbcTemplate(DataSource myDbDataSource) {
        return new JdbcTemplate(myDbDataSource);
    }
}
