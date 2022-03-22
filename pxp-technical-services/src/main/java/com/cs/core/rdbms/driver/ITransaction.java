package com.cs.core.rdbms.driver;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;

/**
 * A transactional process aimed to be run by RDBMSConnectionManager
 *
 * @author vallee
 */
public interface ITransaction {

  /**
   * A simple transaction process aimed to be run by RDBMSConnectionManager
   *
   * @param currentConn connection to be used by the transaction
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void execute( RDBMSConnection currentConn)
          throws CSFormatException, SQLException, RDBMSException;
}
