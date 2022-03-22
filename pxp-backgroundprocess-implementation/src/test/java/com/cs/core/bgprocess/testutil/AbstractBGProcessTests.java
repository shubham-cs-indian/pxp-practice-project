package com.cs.core.bgprocess.testutil;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.rdbms.app.RDBMSAppDriver;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import static com.cs.core.printer.QuickPrinter.printf;
import static com.cs.core.printer.QuickPrinter.println;

/**
 * @author vallee
 */
public abstract class AbstractBGProcessTests extends BGProcessApplication {
  
  private static final String        TEST_PROPERTIES_PATH = "./src/test/bgprocess.properties";
  
  protected static String            sessionID            = "session#" + new Random().nextInt();
  
  protected static ILocaleCatalogDAO localeCatalogDao     = null;
  protected static IUserSessionDAO   userSession          = null;
  protected static IUserSessionDTO   session              = null;
  protected static ILocaleCatalogDTO localeCatalogDto     = null;
  protected static Boolean           isDriverRegisterd    = false;
  public static boolean              IsPGSQLTestRun       = true;
  
  @Before
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    initialize();
    super.init();
    super.init(TEST_PROPERTIES_PATH);
    if (userSession == null) {
      userSession = driver.newUserSessionDAO();
      session = userSession.openSession("Rahul Duldul", sessionID);
      localeCatalogDto = userSession.newLocaleCatalogDTO("en_US", "pim", IStandardConfig.STANDARD_ORGANIZATION_CODE);
      // Get Locale CatelogDAO Interface
      localeCatalogDao = userSession.openLocaleCatalog(session, localeCatalogDto);
      RDBMSLogger.instance().debug("Abstract Test Driver initialized with session: %s", session.getSessionID());
    }
  }
  
  static {
    // Check if file exists then run test cases with oracle driver or else run test cases with pgsql driver
    try {
      File oraFile = new File("./src/test/oracle.selector");
      boolean exists = oraFile.exists();
      if (exists) {
        IsPGSQLTestRun = false;
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().info("Oracle file not found");
      IsPGSQLTestRun = true;
    }
  }
  
  static void registerDriver() throws ClassNotFoundException
  {
    try {
      if (IsPGSQLTestRun) {
        Class.forName("com.cs.core.rdbms.postgres.RDBMSDriver");
        RDBMSDriverManager.registerDriver(new com.cs.core.rdbms.postgres.RDBMSDriver());
      }
      else {
        RDBMSLogger.instance().info("/!\\ ORACLE used for test\n");
        Class.forName("com.cs.core.rdbms.oracle.RDBMSDriver");
        RDBMSDriverManager.registerDriver(new com.cs.core.rdbms.oracle.RDBMSDriver());
      }
      isDriverRegisterd = true;
      driver = new RDBMSAppDriver();
      RDBMSAppDriverManager.registerDriver(driver);
      RDBMSLogger.instance()
          .debug("Test Driver initialized on %s", driver.getDatabaseVendor());
    }
    catch (RDBMSException ex) {
      RDBMSLogger.instance().exception(ex);
    }
  }
  
  protected void displayLogContent( long jobIID) throws RDBMSException, IOException {
    File loadJobLogFile = BGPDriverDAO.instance().loadJobLogFile(jobIID);
    println("Execution log from: " + loadJobLogFile.getName());
    Files.lines( loadJobLogFile.toPath()).forEachOrdered( 
      ln -> {printf("\t>%s\n", ln);});

  }
}
