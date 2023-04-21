package org.assessment.the_drone.configuration;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author Muhammed.Ibrahim
 */
@Configuration
@Profile("test")
public class DroneRepositoryTestConfiguration {
    @Primary
    @Bean
    public DataSource dataSource() {
        // Set up a data source for our test
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:dronedb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        
        return dataSource;
    }
}
