package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.entity.Mission;

/**
 * Class implementing database operations on entity Mission
 *
 * @author Adam Kral (433328)
 */
public class MissionDaoImpl extends GenericDaoImpl<Mission> {
    public MissionDaoImpl() {
        super(Mission.class);
    }
}
