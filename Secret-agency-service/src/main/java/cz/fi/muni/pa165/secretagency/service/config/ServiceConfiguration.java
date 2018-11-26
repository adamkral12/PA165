package cz.fi.muni.pa165.secretagency.service.config;

import cz.fi.muni.pa165.secretagency.SecretAgencyPersistenceApplicationContext;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
@ComponentScan(basePackages = {"cz.fi.muni.pa165.secretagency"})
public class ServiceConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // with loose mapping, mapper is able to map nested classes
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
