package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.dao.MissionDao;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import cz.fi.muni.pa165.secretagency.service.exceptions.MissionServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of service access to the {@link Mission} entity.
 *
 * @author Adam Skurla (487588)
 */
@Service
@Transactional
public class MissionServiceImpl extends GenericServiceImpl<Mission, MissionDao> implements MissionService {

    @Override
    public List<Mission> getMissionsWithType(MissionTypeEnum type) {
        if (type == null) {
            throw new NullPointerException("Mission type is null");
        }
        return getDao().getMissionsWithType(type);
    }

    @Override
    public List<Mission> getMissionsStartedInInterval(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new NullPointerException("Mission start date or end date is null");
        }
        if (end.isAfter(start)) {
            throw new MissionServiceException("End date is before start date");
        }
        return getDao().getMissionsStartedInInterval(start, end);
    }

    @Override
    public List<Mission> getMissionsInPlace(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new NullPointerException("Latitude or longitude is null");
        }
        return getDao().getMissionsInPlace(latitude, longitude);
    }

    @Override
    public List<Mission> getActiveMissions() {
        return getDao().getActiveMissions();
    }
}
