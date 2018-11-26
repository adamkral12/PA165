package cz.fi.muni.pa165.secretagency.service.facade;

import cz.fi.muni.pa165.secretagency.dto.DepartmentCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentUpdateSpecializationDTO;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.enums.DepartmentSpecialization;
import cz.fi.muni.pa165.secretagency.facade.DepartmentFacade;
import cz.fi.muni.pa165.secretagency.service.BeanMappingService;
import cz.fi.muni.pa165.secretagency.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of facade interface of the department entity
 *
 * @author Milos Silhar (433614)
 */
@Service
@Transactional
public class DepartmentFacadeImpl implements DepartmentFacade {

    @Autowired
    private BeanMappingService beanMappingService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return beanMappingService.mapTo(departmentService.getAll(), DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        return beanMappingService.mapTo(departmentService.getEntityById(id), DepartmentDTO.class);
    }

    @Override
    public Long createDepartment(DepartmentCreateDTO departmentCreateDTO) {
        Department mappedDepartment = beanMappingService.mapTo(departmentCreateDTO, Department.class);
        Department newDepartment = departmentService.save(mappedDepartment);
        return newDepartment.getId();
    }

    @Override
    public void changeSpecialization(DepartmentUpdateSpecializationDTO dto) {
        departmentService.changeSpecialization(departmentService.getEntityById(dto.getDepartmentId()), dto.getSpecialization());
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        departmentService.deleteEntityById(departmentId);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsBySpecialization(DepartmentSpecialization specialization) {
        return beanMappingService.mapTo(departmentService.getDepartmentsBySpecialization(specialization), DepartmentDTO.class);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByCity(String city) {
        return beanMappingService.mapTo(departmentService.getDepartmentByCity(city), DepartmentDTO.class);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByCountry(String country) {
        return beanMappingService.mapTo(departmentService.getDepartmentByCountry(country), DepartmentDTO.class);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByArea(Double latitude, Double longitude, Double maxDistance) {
        return beanMappingService.mapTo(
                departmentService.getDepartmentsInArea(latitude, longitude, maxDistance),
                DepartmentDTO.class);
    }
}
