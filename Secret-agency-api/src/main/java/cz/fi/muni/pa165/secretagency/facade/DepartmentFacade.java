package cz.fi.muni.pa165.secretagency.facade;

import cz.fi.muni.pa165.secretagency.dto.DepartmentCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentUpdateSpecializationDTO;
import cz.fi.muni.pa165.secretagency.enums.DepartmentSpecialization;

import java.util.List;

/**
 * Facade for department.
 *
 * @author Milos Silhar (433614)
 */
public interface DepartmentFacade {

    /**
     * Gets all departments
     * @return all departments
     */
    List<DepartmentDTO> getAllDepartments();

    /**
     * Gets department with given id
     * @param id id of department to find
     * @return department with given id
     */
    DepartmentDTO getDepartmentById(Long id);

    /**
     * Creates new department
     * @return id of new department
     */
    Long createDepartment(DepartmentCreateDTO departmentCreateDTO);

    /**
     * Changes specialization of department
     * @param departmentUpdateSpecializationDTO data about specialization change
     */
    void changeSpecialization(DepartmentUpdateSpecializationDTO departmentUpdateSpecializationDTO);

    /**
     * Deletes department with diven id
     * @param departmentId id of department to deleted
     */
    void deleteDepartment(Long departmentId);

    /**
     * Gets all departmens with given specialization
     * @param specialization specialization of departments to find
     * @return Departments with given specialization
     */
    List<DepartmentDTO> getDepartmentsBySpecialization(DepartmentSpecialization specialization);

    /**
     * Gets all departments with given city
     * @param city city from where find departments
     * @return Departments from given city
     */
    List<DepartmentDTO> getDepartmentsByCity(String city);

    /**
     * Gets all departments with given country
     * @param country country from where find departments
     * @return Departments from given country
     */
    List<DepartmentDTO> getDepartmentsByCountry(String country);

    /**
     * Gets all departments located in 0 - maxDistance from origin point on the earth
     * @param latitude latitude of the origin point
     * @param longitude longitude of the origin point
     * @param maxDistance maximum distance from origin point
     * @return Departments located in 0 - maxDistance from origin point
     */
    List<DepartmentDTO> getDepartmentsByArea(Double latitude, Double longitude, Double maxDistance);
}
