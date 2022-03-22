/*package com.cs.pxpui.unittest.base.configuration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cs.core.runtime.strategy.db.DatabaseConnection;
import com.cs.pxpui.unittest.base.constants.UnitTestConstants;

// To load from resource folder
// @ContextConfiguration(locations = { "/applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { UnitTestConstants.FILE_PATH_APPLICATION_CONTEXT_XML })
public class BaseConfigurationTest {

  @Autowired
  protected ApplicationContext applicationContext;

  static int                   runOnce = 0;

  @BeforeClass
  public static void setUp()
  {
    // IF we have to make implicit connection
  }

  @Before
  public void setup() throws Exception
  {
    // If we have to perform something for each test case
  }

   Just add the Classes to execute with this test
   * Please extends the same class otherwise unable to access the bean
   * Please make sure child class also called it parent class test method


  @Test
  public void run() throws Exception
  {
    if (runOnce == 0) {
      runOnce++;
      Result result = JUnitCore.runClasses(UnitTestConstants.TEST_CLASS_ARRAY);
      for (Failure failure : result.getFailures()) {
        System.out.println(failure.toString());
      }
      System.out.println("Final Result of Test: " + result.wasSuccessful());
    }
  }

  @After
  public void after() throws Exception
  {
    DatabaseConnection.commit();
  }

  @AfterClass
  public static void setDown() throws Exception
  {
    DatabaseConnection.closeConnection();
    System.out.println("-----> Closing Base Configuration Loading <-----");
  }

}
*/
