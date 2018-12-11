package cz.fi.muni.pa165.secretagency.controllers;

import cz.fi.muni.pa165.secretagency.ApiUris;
import cz.fi.muni.pa165.secretagency.dto.DepartmentDTO;
import cz.fi.muni.pa165.secretagency.facade.DepartmentFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: Adam Kral <433328>
 * Date: 12/11/18
 * Time: 4:36 PM
 */
@RestController
@RequestMapping(ApiUris.ROOT_URI_DEPARTMENTS)
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
    public final List<DepartmentDTO> getProducts() {
        logger.debug("rest getDepartments()");
        return departmentFacade.getAllDepartments();
    }
}
