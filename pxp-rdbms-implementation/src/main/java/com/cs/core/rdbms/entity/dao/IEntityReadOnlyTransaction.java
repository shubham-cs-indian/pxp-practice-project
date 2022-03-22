package com.cs.core.rdbms.entity.dao;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;

/**
 * A transactional process aimed to be run by RDBMSConnectionManager
 *
 * @author vallee
 */
public interface IEntityReadOnlyTransaction {

  /**
   * A transaction process aimed to be run for entity data access
   *
   * @param currentConn connection to be used by the transaction
   * @param entityDAS entity data services
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void execute( RDBMSConnection currentConn, BaseEntityDAS entityDAS)
          throws CSFormatException, SQLException, RDBMSException;
}
