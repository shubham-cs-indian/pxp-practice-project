package com.cs.core.bgprocess.dao;

import com.cs.core.bgprocess.idao.IBGPRelationshipDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class BGPRelationshipDAO implements IBGPRelationshipDAO {
  
  private static IBGPRelationshipDAO bgpRelationshipDAO;
  
  private BGPRelationshipDAO()
  {
  }
  
  public static IBGPRelationshipDAO getInstance()
  {
    if (bgpRelationshipDAO == null) {
      synchronized (BGPRelationshipDAO.class) {
        if (bgpRelationshipDAO == null) {
          bgpRelationshipDAO = new BGPRelationshipDAO();
        }
      }
    }
    return bgpRelationshipDAO;
  }
  
  @Override
  public void deleteContextFromRelationship(Long propertyIid, String sideOneContextId, String sideTwoContextId) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      BGPRelationshipDAS relationshipDAS = new BGPRelationshipDAS(currentConn);
      relationshipDAS.deleteContextFromRelationship(propertyIid, sideOneContextId, sideTwoContextId);
    });
  }
}
