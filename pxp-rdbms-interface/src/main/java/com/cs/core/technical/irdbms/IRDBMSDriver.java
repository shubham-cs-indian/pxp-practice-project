package com.cs.core.technical.irdbms;

import com.cs.core.rdbms.tracking.idao.ILoggerDAO;
import com.cs.core.technical.icsexpress.ICSEParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * The RDBMS driver is the factory entry point for DAOs From there, only 2 possibilities: - creating a new user session and starting
 * transactions from this DAO - creating a tracking interface for simple action logs
 *
 * @author vallee
 */
public interface IRDBMSDriver {

  /**
   * @return the database vendor name
   */
  public String getDatabaseVendor();

  /**
   * @return the database vendor class name
   */
  public String getDriverClassName();

  /**
   * @return a new CSExpression parser
   */
  public ICSEParser newParser();

  /**
   * @return an Logger to access log services
   */
  public ILoggerDAO newLoggerDAO();

  /**
   * Handle any shutdown procedures to finalize properly the component
   *
   * @throws RDBMSException
   */
  public void shutdown() throws RDBMSException;
}
