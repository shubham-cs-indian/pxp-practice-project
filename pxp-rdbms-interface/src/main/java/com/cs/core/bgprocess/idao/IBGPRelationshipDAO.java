package com.cs.core.bgprocess.idao;

import java.sql.SQLException;

import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author Yash.Waghmare
 *
 */

public interface IBGPRelationshipDAO {
  /**
   * set context to null and remove record from pxp.contextualobject
   * @param propertyIid propertyiid of property in config
   * @param sideOneContextId side 1 context code needs to be removed from relationship 
   * @param sideTwoContextId side 1 context code needs to be removed from relationship
   * @throws RDBMSException
   * @throws SQLException
   */
  public void deleteContextFromRelationship(Long propertyIid, String sideOneContextId, String sideTwoContextId) throws RDBMSException;
  
}
