package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.SecretAgencyPersistenceApplicationContext;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@ContextConfiguration(classes= SecretAgencyPersistenceApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class MissionDaoTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MissionDao missionDao;

    private Mission m1;
    private Date date1;
    private Date date2;

    @BeforeClass
    public void createMissions(){
        String date = "2016-08-17";
        LocalDate localDate1 = LocalDate.parse(date);
        String date2 = "2016-08-15";
        LocalDate localDate2 = LocalDate.parse(date2);
        m1 = new Mission();
        m1.setEnded(localDate1);
        m1.setStarted(localDate2);

    }

    @Test
    public void getOrdersCreatedBetween() {
        String date = "2016-08-16";
        LocalDate localDate1 = LocalDate.parse(date);
        LocalDate localDate2 = LocalDate.parse(date);
        Assert.assertEquals(missionDao.getMissionsInInterval(localDate1, localDate2).size(), 0);
    }
}
