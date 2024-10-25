package be.zsoft.todo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.sqlite.JDBC;
import be.zsoft.todo.utils.Constants;

import javax.sql.DataSource;
import java.nio.file.Paths;

@Configuration
@EnableJpaRepositories(basePackages = {"be.zsoft.todo.repo"})
@EntityScan(basePackages = {"be.zsoft.todo.model"})
public class DatasourceConfig {

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(JDBC.class.getName());
        dataSource.setUrl("jdbc:sqlite:" + getDBPath() + "?date_class=TEXT");
        return dataSource;
    }

    private String getDBPath() {
        return Paths.get(Constants.APPLICATION_DIRECTORY.toString(), "db.db").toString();
    }
}
