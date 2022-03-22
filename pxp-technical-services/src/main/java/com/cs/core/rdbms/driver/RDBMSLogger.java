package com.cs.core.rdbms.driver;

import com.cs.core.rdbms.tracking.idao.ILoggerDAO;
import com.cs.core.services.CSLogger;
import com.cs.core.technical.exception.CSInitializationException;

/**
 * Singleton to access logger functions in the RDBMS component
 *
 * @author vallee
 */
public final class RDBMSLogger extends CSLogger implements ILoggerDAO {

  // Singleton initialization
  private static RDBMSLogger instance = null;

  private RDBMSLogger() throws CSInitializationException {
    super();
  }

  /**
   * @return unique instance of properties
   */
  public static RDBMSLogger instance() {
    if (instance == null) {
      try {
        instance = new RDBMSLogger();
      } catch (CSInitializationException ex) {
        System.out.println("---------  Exception occured while initializing RDBMS Logger  " + ex.getMessage());
        ex.printStackTrace(System.err);
        System.exit(1); // TODO: when run from an application server is that the
        // correct way to abort?
      }
    }
    return instance;
  }
}
