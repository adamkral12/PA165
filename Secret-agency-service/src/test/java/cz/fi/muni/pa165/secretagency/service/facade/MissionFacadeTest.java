package cz.fi.muni.pa165.secretagency.service.facade;

import cz.fi.muni.pa165.secretagency.dto.MissionCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.MissionDTO;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import cz.fi.muni.pa165.secretagency.facade.MissionFacade;
import cz.fi.muni.pa165.secretagency.service.BeanMappingService;
import cz.fi.muni.pa165.secretagency.service.MissionService;
import cz.fi.muni.pa165.secretagency.service.config.ServiceConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for mission facade.
 * @author Adam Skurla (487588)
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MissionFacadeTest extends AbstractTestNGSpringContextTests {

    @Spy
    @Autowired
    private BeanMappingService beanMappingService;

    @Mock
    private MissionService missionService;

    @InjectMocks
    private MissionFacade missionFacade = new MissionFacadeImpl();

    private Mission mission1;
    private Mission mission2;

    @BeforeMethod
    public void setEntities() {
        mission1 = new Mission();
        mission1.setLatitude(14.315424);
        mission1.setLongitude(108.339537);
        mission1.setStarted(LocalDate.of(1952, 1, 1));
        mission1.setEnded(LocalDate.of(1975, 4, 30));
        mission1.setMissionType(MissionTypeEnum.ESPIONAGE);

        mission2 = new Mission();
        mission2.setLatitude(34.543896);
        mission2.setLongitude(69.160652);
        mission2.setStarted(LocalDate.of(2001, 5, 5));
        mission2.setMissionType(MissionTypeEnum.ASSASSINATION);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getByIdTest() {
        when(missionService.getEntityById(mission1.getId())).thenReturn(mission1);
        MissionDTO missionDTO = missionFacade.getMissionById(mission1.getId());
        Assert.assertEquals(mission1, beanMappingService.mapTo(missionDTO, Mission.class));
    }

    @Test
    public void getAllTest() {
        when(missionService.getAll()).thenReturn(Arrays.asList(mission1, mission2));
        List<MissionDTO> missionDTOS = missionFacade.getAllMissions();
        List<Mission> missions = beanMappingService.mapTo(missionDTOS, Mission.class);
        Assert.assertEquals(missions.size(), 2);
        Assert.assertTrue(missions.contains(mission1));
        Assert.assertTrue(missions.contains(mission2));
    }

    @Test
    public void createTest() {
        MissionCreateDTO missionCreateDTO = beanMappingService.mapTo(mission1, MissionCreateDTO.class);
        when(missionService.save(mission1)).thenReturn(mission1);
        Assert.assertEquals(mission1.getId(), missionFacade.createMission(missionCreateDTO));
    }

    @Test
    public void deleteTest() {
        missionFacade.deleteMission(mission1.getId());
        verify(missionService).deleteEntityById(mission1.getId());
    }

    @Test
    public void getMissionsWithTypeTest() {
        when(missionService.getMissionsWithType(mission1.getMissionType())).thenReturn(Collections.singletonList(mission1));
        List<MissionDTO> missionDTOS = missionFacade.getMissionsWithType(mission1.getMissionType());
        List<Mission> missions = beanMappingService.mapTo(missionDTOS, Mission.class);

        Assert.assertEquals(missions.size(), 1);
        Assert.assertTrue(missions.contains(mission1));
    }

    @Test
    public void getMissionsStartedInIntervalTest() {
        when(missionService.getMissionsStartedInInterval(mission1.getStarted(), mission1.getEnded())).thenReturn(Collections.singletonList(mission1));
        List<MissionDTO> missionDTOS = missionFacade.getMissionsStartedInInterval(mission1.getStarted(), mission1.getEnded());
        Assert.assertEquals(beanMappingService.mapTo(missionDTOS, Mission.class), Collections.singletonList(mission1));
    }

    @Test
    public void getMissionsInPlaceTest() {
        when(missionService.getMissionsInPlace(mission1.getLatitude(), mission1.getLongitude())).thenReturn(Collections.singletonList(mission1));
        List<MissionDTO> missionDTOS = missionFacade.getMissionsInPlace(mission1.getLatitude(), mission1.getLongitude());
        Assert.assertEquals(beanMappingService.mapTo(missionDTOS, Mission.class), Collections.singletonList(mission1));
    }

    @Test
    public void getActiveMissionsTest() {
        when(missionService.getActiveMissions()).thenReturn(Collections.singletonList(mission2));
        List<MissionDTO> missionDTOS = missionFacade.getActiveMissions();
        Assert.assertEquals(beanMappingService.mapTo(missionDTOS, Mission.class), Collections.singletonList(mission2));
    }
}
