package com.cs.core.technical.irdbms;

import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * This is the entry point to manage RDBMS technical services The correct initialization sequence for PGSQL is: Class.forName(
 * "com.cs.core.rdbms.postgres.RDBMSDriver"); RDBMSDriverManager.registerDriver( new com.cs.core.rdbms.postgres.RDBMSDriver());
 * IUserLoggingDAO loggingDAO = RDBMSAppDriverManager.getDriver().newUserLoggingDAO();
 * <p>
 * The initialization sequence for ORACLE is: Class.forName( "com.cs.core.rdbms.oracle.RDBMSDriver"); RDBMSAppDriverManager.registerDriver(
 * new com.cs.core.rdbms.oracle.RDBMSDriver()); IUserLoggingDAO loggingDAO = RDBMSAppDriverManager.getDriver().newUserLoggingDAO();
 *
 * @author vallee
 */
public final class RDBMSDriverManager {

  private static IRDBMSDriver driver = null;

  /**
   * Register the driver to use according to the targeted RDBMS (PGSQL or ORACLE)
   *
   * @param newDriver is the new driver
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public synchronized static void registerDriver(IRDBMSDriver newDriver) throws RDBMSException {
    if (driver != null) {
      throw new RDBMSException(0, "Multiple drivers initialization",
              "registerDriver cannot be called many times");
    }
    driver = newDriver;
  }

  /**
   * @return the registered driver
   */
  public static IRDBMSDriver getDriver() {
    return driver;
  }
}
