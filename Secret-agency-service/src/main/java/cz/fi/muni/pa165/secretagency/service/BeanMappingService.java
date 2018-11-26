package cz.fi.muni.pa165.secretagency.service;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Service for mapping between entities and DTOs taken from example project.

 */
public interface BeanMappingService {

    <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);
    <T> Set<T> mapToSet(Collection<?> objects, Class<T> mapToClass);

    <T> T mapTo(Object u, Class<T> mapToClass);
    ModelMapper getMapper();
}