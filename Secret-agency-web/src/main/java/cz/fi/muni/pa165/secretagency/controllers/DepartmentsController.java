package cz.fi.muni.pa165.secretagency.controllers;

import cz.fi.muni.pa165.secretagency.ApiUris;
import cz.fi.muni.pa165.secretagency.dto.DepartmentCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentUpdateSpecializationDTO;
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
     * curl -i -X GET http://localhost:8080/pa165/rest/departments
     * @return DepartmentDTO
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<DepartmentDTO> getDepartments() {
        logger.debug("rest getDepartments()");
        return departmentFacade.getAllDepartments();
    }

    /**
     *
     * Get Department by identifier id curl -i -X GET
     * curl -i -X GET http://localhost:8080/pa165/rest/departments/1
     *
     * @param id identifier for a department
     * @return DepartmentDTO
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final DepartmentDTO getDepartment(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getDepartment({})", id);

        try {
            return departmentFacade.getDepartmentById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     curl -X POST -i -H "Content-Type: application/json" --data \
     '{ "city": "Ostrava", "country": "Czech Republic","longitude": 123213.32,"latitude": 123.231,"specialization": "ASSASSINATION"}'\
     http://localhost:8080/pa165/rest/departments/
     *
     * @param department department to be created
     * @return DepartmentDTO created department
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final DepartmentDTO createDepartment(@RequestBody DepartmentCreateDTO department) throws Exception {

        logger.debug("rest createDepartment()");

        Long id = departmentFacade.createDepartment(department);
        return departmentFacade.getDepartmentById(id);
    }

    /**
     curl -X PUT -i -H "Content-Type: application/json" --data \
     '{ "departmentId":1 ,"specialization": "ASSASSINATION"}'\
     http://localhost:8080/pa165/rest/departments/specializations/
     *
     * @param specialization department specialization
     * @return DepartmentDTO updated department
     * @throws Exception
     */
    @RequestMapping(value = "/specializations", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final DepartmentDTO changeDepartmentSpecialization(@RequestBody DepartmentUpdateSpecializationDTO specialization) throws Exception {

        logger.debug("rest update Department specialization()");

        try {
            departmentFacade.changeSpecialization(specialization);
            return departmentFacade.getDepartmentById(specialization.getDepartmentId());
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }
}
