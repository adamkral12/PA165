package cz.fi.muni.pa165.secretagency.service.facade;

import cz.fi.muni.pa165.secretagency.dto.MissionCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.MissionDTO;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import cz.fi.muni.pa165.secretagency.facade.MissionFacade;
import cz.fi.muni.pa165.secretagency.service.BeanMappingService;
import cz.fi.muni.pa165.secretagency.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Mission facade.
 * @author Adam Skurla (487588)
 */
@Service
@Transactional
public class MissionFacadeImpl implements MissionFacade {
    @Autowired
    private BeanMappingService beanMappingService;

    @Autowired
    private MissionService missionService;

    @Override
    public List<MissionDTO> getAllMissions() {
        return beanMappingService.mapTo(missionService.getAll(), MissionDTO.class);
    }

    @Override
    public MissionDTO getMissionById(Long id) {
        return beanMappingService.mapTo(missionService.getEntityById(id), MissionDTO.class);
    }

    @Override
    public Long createMission(MissionCreateDTO mission) {
        Mission mappedMission = beanMappingService.mapTo(mission, Mission.class);
        Mission savedMission = missionService.save(mappedMission);
        return savedMission.getId();
    }

    @Override
    public void deleteMission(Long missionId) {
        missionService.deleteEntityById(missionId);
    }

    @Override
    public List<MissionDTO> getMissionsWithType(MissionTypeEnum type) {
        return beanMappingService.mapTo(missionService.getMissionsWithType(type), MissionDTO.class);
    }

    @Override
    public List<MissionDTO> getMissionsStartedInInterval(LocalDate start, LocalDate end) {
        return beanMappingService.mapTo(missionService.getMissionsStartedInInterval(start, end), MissionDTO.class);
    }

    @Override
    public List<MissionDTO> getMissionsInPlace(Double latitude, Double longitude) {
        return beanMappingService.mapTo(missionService.getMissionsInPlace(latitude, longitude), MissionDTO.class);
    }

    @Override
    public List<MissionDTO> getActiveMissions() {
        return beanMappingService.mapTo(missionService.getActiveMissions(), MissionDTO.class);
    }
}
