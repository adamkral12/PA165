package cz.fi.muni.pa165.secretagency.service.config;

import cz.fi.muni.pa165.secretagency.SecretAgencyPersistenceApplicationContext;
import cz.fi.muni.pa165.secretagency.service.AgentServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Author: Adam Kral <433328>
 * Date: 11/19/18
 * Time: 2:59 PM
 */
@Configuration
@Import(SecretAgencyPersistenceApplicationContext.class)
@ComponentScan(basePackageClasses={AgentServiceImpl.class})
public class ServiceConfiguration {

    @Bean
    public Mapper dozer(){
        return new DozerBeanMapper();
    }

}
