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
public interface IEntityTransaction {

  /**
   * A transaction process aimed to be run for entity changes
   *
   * @param currentConn connection to be used by the transaction
   * @param entityDAS entity data services
   * @param eventDAS event data services
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void execute( RDBMSConnection currentConn, BaseEntityDAS entityDAS, EntityEventDAS eventDAS)
          throws CSFormatException, SQLException, RDBMSException;
}
