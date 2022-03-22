package com.cs.core.rdbms.testutil;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.printer.QuickPrinter;
import com.cs.core.rdbms.app.RDBMSAppDriver;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.iapp.IRDBMSAppDriver;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.io.File;
import java.util.Random;
import org.junit.Before;

public abstract class AbstractRDBMSDriverTests extends QuickPrinter {

  public static boolean IsPGSQLTestRun = true;
  // static = common to every test case
  protected static String sessionID = "session#" + new Random().nextInt();
  protected static IRDBMSAppDriver driver = null;
  protected static ILocaleCatalogDAO localeCatalogDao = null;
  protected static IUserSessionDAO userSession = null;
  protected static IUserSessionDTO session = null;
  protected static ILocaleCatalogDTO localeCatalogDto = null;
  /**
   * Define the variable in which DB All test cases should be run, This variable can be populated base on below gradle
   * command gradle test - true set gradle oracletest - false
   */
  protected static Boolean isDriverRegisterd = false;

  static {
    // Check if file exists then run test cases with oracle driver or else run
    // test cases with pgsql driver
    try {
      File oraFile = new File("./src/test/oracle.selector");
      boolean exists = oraFile.exists();
      if (exists) {
        IsPGSQLTestRun = false;
      }
    } catch (Exception e) {
      System.out.println("Oracle file not found");
      IsPGSQLTestRun = true;
    }
  }

  static void registerDriver() throws ClassNotFoundException {
    try {
      if (IsPGSQLTestRun) {
        println("/!\\ POSTGRES used for test\n");
        Class.forName("com.cs.core.rdbms.postgres.RDBMSDriver");
        RDBMSDriverManager.registerDriver(new com.cs.core.rdbms.postgres.RDBMSDriver());
      } else {
        System.out.println("/!\\ ORACLE used for test\n");
        Class.forName("com.cs.core.rdbms.oracle.RDBMSDriver");
        RDBMSDriverManager.registerDriver(new com.cs.core.rdbms.oracle.RDBMSDriver());
      }
      isDriverRegisterd = true;
      driver = new RDBMSAppDriver();
      RDBMSAppDriverManager.registerDriver(driver);
      RDBMSLogger.instance()
              .debug("Test Driver initialized on %s", driver.getDatabaseVendor());
    } catch (RDBMSException ex) {
      ex.printStackTrace();
    }
  }

  @Before
  public synchronized void init() throws RDBMSException {
    if (!isDriverRegisterd) {
      try {
        registerDriver();
      } catch (ClassNotFoundException ex) {
        throw new RDBMSException(0, "CONFIG ERROR", ex);
      }
    }
    if (userSession == null) {
      userSession = driver.newUserSessionDAO();
      session = userSession.openSession("Rahul Duldul", sessionID);
      localeCatalogDto = userSession.newLocaleCatalogDTO("de_DE", "pim", IStandardConfig.STANDARD_ORGANIZATION_CODE);
      // Get Locale CatelogDAO Interface
      localeCatalogDao = userSession.openLocaleCatalog(session, localeCatalogDto);
      RDBMSLogger.instance()
              .debug("Abstract Test Driver initialized with session: %s", session.getSessionID());
    }
  }
}
