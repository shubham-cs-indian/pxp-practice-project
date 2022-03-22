package com.cs.core.rdbms.iapp;

import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * This is the entry point to the RDBMS component with full application services. The correct initialization sequence for PGSQL is:
 * Class.forName( "com.cs.core.rdbms.postgres.RDBMSDriver"); RDBMSDriverManager.registerDriver( new
 * com.cs.core.rdbms.postgres.RDBMSDriver()); RDBMSAppDriverManager.registerDriver( new RDBMSAppDriver());
 * <p>
 * The initialization sequence for ORACLE is: Class.forName( "com.cs.core.rdbms.oracle.RDBMSDriver"); RDBMSDriverManager.registerDriver( new
 * com.cs.core.rdbms.oracle.RDBMSDriver()); RDBMSAppDriverManager.registerDriver( new RDBMSAppDriver());
 *
 * @author vallee
 */
public final class RDBMSAppDriverManager {

  private static IRDBMSAppDriver driver = null;

  /**
   * Register the driver to use according to the targeted RDBMS (PGSQL or ORACLE)
   *
   * @param newDriver is the new driver
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public synchronized static void registerDriver(IRDBMSAppDriver newDriver) throws RDBMSException {
    if (driver != null) {
      throw new RDBMSException(0, "Multiple drivers initialization",
              "registerDriver cannot be called many times");
    }
    driver = newDriver;
  }

  /**
   * @return the registered driver
   */
  public static IRDBMSAppDriver getDriver() {
    return driver;
  }
}
