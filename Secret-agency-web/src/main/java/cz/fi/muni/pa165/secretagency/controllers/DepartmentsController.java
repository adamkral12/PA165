package cz.fi.muni.pa165.secretagency.controllers;

import cz.fi.muni.pa165.secretagency.ApiUris;
import cz.fi.muni.pa165.secretagency.dto.DepartmentCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentUpdateSpecializationDTO;
import cz.fi.muni.pa165.secretagency.enums.DepartmentSpecialization;
import cz.fi.muni.pa165.secretagency.exceptions.InvalidRequestRemainingAgentsInDepartment;
import cz.fi.muni.pa165.secretagency.exceptions.ResourceNotFoundException;
import cz.fi.muni.pa165.secretagency.facade.DepartmentFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Adam Kral <433328>
 * Date: 12/11/18
 * Time: 4:36 PM
 */
@RestController
@RequestMapping(ApiUris.ROOT_URI_REST + ApiUris.ROOT_URI_DEPARTMENTS)
public class DepartmentsController {
    final static Logger logger = LoggerFactory.getLogger(DepartmentsController.class);

    @Autowired
    private DepartmentFacade departmentFacade;

    /**
     * Get list of Departments
     * @return DepartmentDTO
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<DepartmentDTO> getDepartments() {
        logger.debug("rest getDepartments()");
        return departmentFacade.getAllDepartments();
    }

    /**
     *
     * Get Department by identifier id
     * @param id identifier for a department
     * @return DepartmentDTO
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final DepartmentDTO getDepartment(@PathVariable("id") Long id) throws ResourceNotFoundException {
        logger.debug("rest getDepartment({})", id);

        try {
            return departmentFacade.getDepartmentById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * TODO: ALL agents need to be deleted when deleting department, not sure how to behave in this case
     * @param id department id
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public final void deleteDepartment(@PathVariable("id") Long id) throws ResourceNotFoundException, InvalidRequestRemainingAgentsInDepartment {
        logger.debug("rest deleteDepartment({})", id);
        try {
            DepartmentDTO department = departmentFacade.getDepartmentById(id);

            if (!department.getAgentIds().isEmpty()) {
                throw new InvalidRequestRemainingAgentsInDepartment();
            }
            departmentFacade.deleteDepartment(id);
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * @param department department to be created
     * @return DepartmentDTO created department
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final DepartmentDTO createDepartment(@RequestBody DepartmentCreateDTO department) {

        logger.debug("rest createDepartment({})", department);

        Long id = departmentFacade.createDepartment(department);
        return departmentFacade.getDepartmentById(id);
    }

    /**
     * @param specialization department specialization
     * @return DepartmentDTO updated department
     */
    @RequestMapping(value = "/specializations", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final DepartmentDTO changeDepartmentSpecialization(
            @RequestBody DepartmentUpdateSpecializationDTO specialization
    ) throws ResourceNotFoundException {

        logger.debug("rest update Department specialization({})", specialization);

        try {
            departmentFacade.changeSpecialization(specialization);
            return departmentFacade.getDepartmentById(specialization.getDepartmentId());
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * @return All possible department specializations
     */
    @RequestMapping(value = "/specializations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final DepartmentSpecialization[] getDepartmentSpecializations() {
        logger.debug("rest get Department specializations()");
        return departmentFacade.getSpecializations();
    }

    /**
     * @return All departments with given specialization
     */
    @RequestMapping(
            value = "/specialization/{specialization}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public final List<DepartmentDTO> getDepartmentsBySpecialization(
            @PathVariable("specialization") DepartmentSpecialization specialization
    ) {
        logger.debug("rest get Department by specialization({})", specialization);
        return departmentFacade.getDepartmentsBySpecialization(specialization);
    }

    /**
     * @return get departments by city
     */
    @RequestMapping(value = "/city/{city}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<DepartmentDTO> getDepartmentsByCity(
            @PathVariable("city") String city
    ) {
        logger.debug("rest get Department by city({})", city);
        return departmentFacade.getDepartmentsByCity(city);
    }

    /**
     * @return get departments by country
     */
    @RequestMapping(value = "/country/{country}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<DepartmentDTO> getDepartmentsByCountry(
            @PathVariable("country") String country
    ) {
        logger.debug("rest get Department by country({})", country);
        return departmentFacade.getDepartmentsByCountry(country);
    }

    /**
     * @return get departments by city area and distance
     */
    @RequestMapping(value = "/area/{latitude}/{longitude}/{distance}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<DepartmentDTO> getDepartmentsByArea(
            @PathVariable("latitude") Double latitude,
            @PathVariable("longitude") Double longitude,
            @PathVariable("distance") Double distance
    ) {
        logger.debug("rest get Department by area(latitude {}, longitude {}, distance {})", latitude, longitude, distance);
        return departmentFacade.getDepartmentsByArea(latitude, longitude, distance);
    }
}
