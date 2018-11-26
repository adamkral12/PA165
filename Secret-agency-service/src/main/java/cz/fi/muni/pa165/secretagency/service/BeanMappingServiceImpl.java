package cz.fi.muni.pa165.secretagency.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service implementation for mapping between entities and DTOs taken from example project.
 */
@Service
public class BeanMappingServiceImpl implements BeanMappingService {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public  <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass) {
        List<T> mappedCollection = new ArrayList<>();
        for (Object object : objects) {
            mappedCollection.add(modelMapper.map(object, mapToClass));
        }
        return mappedCollection;
    }

    @Override
    public <T> Set<T> mapToSet(Collection<?> objects, Class<T> mapToClass) {
        Set<T> mappedSet = new HashSet<>();
        for (Object o : objects) {
            mappedSet.add(modelMapper.map(objects, mapToClass));
        }
        return mappedSet;
    }

    @Override
    public  <T> T mapTo(Object u, Class<T> mapToClass)
    {
        return modelMapper.map(u,mapToClass);
    }

    public ModelMapper getMapper(){
        return modelMapper;
    }
}
