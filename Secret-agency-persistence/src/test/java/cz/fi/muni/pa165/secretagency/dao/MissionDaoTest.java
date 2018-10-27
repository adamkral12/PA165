package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.SecretAgencyPersistenceApplicationContext;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.time.LocalDate;
import java.util.Date;

@ContextConfiguration(classes= SecretAgencyPersistenceApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class MissionDaoTest extends AbstractTestNGSpringContextTests {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private MissionDao missionDao;

    private Mission m1;
    private Date date1;
    private Date date2;
    private Double latitude;
    private Double longitude;

    @BeforeClass
    public  void createMissions() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        this.latitude = 1d;
        this.longitude = 2d;
        String date2 = "2016-08-15";
        LocalDate localDate2 = LocalDate.parse(date2);
        m1 = new Mission();
        m1.setStarted(localDate2);
        m1.setLatitude(this.latitude);
        m1.setLongitude(this.longitude);
        m1.setMissionType(MissionTypeEnum.ASSASSINATION);
        em.persist(m1);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void getOrdersCreatedBetween() {
        String date = "2016-08-14";
        String date2 = "2016-08-16";
        LocalDate localDate1 = LocalDate.parse(date);
        LocalDate localDate2 = LocalDate.parse(date2);
        Assert.assertEquals(missionDao.getMissionsStartedInInterval(localDate1, localDate2).size(), 1);

        Assert.assertEquals(missionDao.getActiveMissions().size(), 1);

        System.out.print(this.latitude);
        System.out.print(this.longitude);
        System.out.print(m1.getLatitude());
        System.out.print(this.longitude);
        Assert.assertEquals(missionDao.getMissionsInPlace(this.latitude, this.longitude).size(), 1);

        Assert.assertEquals(missionDao.getMissionsWithType(MissionTypeEnum.ASSASSINATION).size(), 1);
    }
}
