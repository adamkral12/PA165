package cz.fi.muni.pa165.secretagency.service.facade;

import cz.fi.muni.pa165.secretagency.dto.DepartmentCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentDTO;
import cz.fi.muni.pa165.secretagency.dto.DepartmentUpdateSpecializationDTO;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.enums.DepartmentSpecialization;
import cz.fi.muni.pa165.secretagency.facade.DepartmentFacade;
import cz.fi.muni.pa165.secretagency.service.BeanMappingService;
import cz.fi.muni.pa165.secretagency.service.DepartmentService;
import cz.fi.muni.pa165.secretagency.service.config.ServiceConfiguration;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ServiceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class DepartmentFacadeTest extends AbstractTestNGSpringContextTests {

    @Autowired
    BeanMappingService beanMappingService;

    @Autowired
    DepartmentFacade departmentFacade;

    @Mock
    DepartmentService departmentService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(departmentFacade, "departmentService", departmentService);
    }

    @Test
    public void getDepartments() {
        Department department = new Department();
        department.setId(3L);
        department.setCountry("test");
        when(departmentService.getAll()).thenReturn(Collections.singletonList(department));
        List<DepartmentDTO> result = departmentFacade.getAllDepartments();
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), department.getId());
        Assert.assertEquals(result.get(0).getCountry(), department.getCountry());
    }

    @Test
    public void getDepartmentById() {
        Department department = new Department();
        department.setId(3L);
        department.setCountry("test");
        when(departmentService.getEntityById(department.getId())).thenReturn(department);
        DepartmentDTO result = departmentFacade.getDepartmentById(department.getId());
        Assert.assertEquals(result.getId(), department.getId());
        Assert.assertEquals(result.getCountry(), department.getCountry());
    }

    @Test
    public void createDepartment() {
        Department department = new Department();
        department.setId(3L);
        department.setCountry("test");
        when(departmentService.save(any())).thenReturn(department);
        DepartmentCreateDTO departmentCreateDTO = new DepartmentCreateDTO();
        departmentCreateDTO.setCountry("test");
        Assert.assertEquals(departmentFacade.createDepartment(departmentCreateDTO), department.getId());
    }

    @Test
    public void changeSpecialization() {
        Department department = new Department();
        department.setId(3L);
        department.setCountry("test");
        department.setSpecialization(DepartmentSpecialization.INTERNATIONAL_RELATIONSHIP);
        when(departmentService.getEntityById(department.getId())).thenReturn(department);
        DepartmentUpdateSpecializationDTO dto = new DepartmentUpdateSpecializationDTO();
        dto.setDepartmentId(department.getId());
        dto.setSpecialization(DepartmentSpecialization.ASSASSINATION);
        departmentFacade.changeSpecialization(dto);
        verify(departmentService).changeSpecialization(department, DepartmentSpecialization.ASSASSINATION);
    }

    @Test
    public void deleteDepartment() {
        departmentFacade.deleteDepartment(12L);
        verify(departmentService).deleteEntityById(12L);
    }

    @Test
    public void getDepartmentsBySpecialization() {
        Department department = new Department();
        department.setId(3L);
        department.setSpecialization(DepartmentSpecialization.INTERNATIONAL_RELATIONSHIP);
        when(departmentService.getDepartmentsBySpecialization(DepartmentSpecialization.ASSASSINATION)).thenReturn(Collections.singletonList(department));
        List<DepartmentDTO> result = departmentFacade.getDepartmentsBySpecialization(DepartmentSpecialization.ASSASSINATION);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), department.getId());
        Assert.assertEquals(result.get(0).getSpecialization(), department.getSpecialization());
    }

    @Test
    public void getDepartmentsByCity() {
        Department department = new Department();
        department.setId(3L);
        department.setCity("test");
        when(departmentService.getDepartmentByCity(department.getCity())).thenReturn(Collections.singletonList(department));
        List<DepartmentDTO> result = departmentFacade.getDepartmentsByCity(department.getCity());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), department.getId());
        Assert.assertEquals(result.get(0).getCity(), department.getCity());
    }

    @Test
    public void getDepartmentsByCountry() {
        Department department = new Department();
        department.setId(3L);
        department.setCountry("test");
        when(departmentService.getDepartmentByCountry(department.getCountry())).thenReturn(Collections.singletonList(department));
        List<DepartmentDTO> result = departmentFacade.getDepartmentsByCountry(department.getCountry());
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), department.getId());
        Assert.assertEquals(result.get(0).getCountry(), department.getCountry());
    }

    @Test
    public void getDepartmentsByArea() {
        Department department = new Department();
        department.setId(3L);
        department.setCountry("test");
        when(departmentService.getDepartmentsInArea(0.0, 0.0, 42.0)).thenReturn(Collections.singletonList(department));
        List<DepartmentDTO> result = departmentFacade.getDepartmentsByArea(0.0, 0.0, 42.0);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), department.getId());
        Assert.assertEquals(result.get(0).getCountry(), department.getCountry());
    }
}
