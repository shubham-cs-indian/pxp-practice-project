package com.cs.core.bgprocess.dao;

import java.sql.SQLException;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class BGPRelationshipDAS extends RDBMSDataAccessService {

  public BGPRelationshipDAS(RDBMSConnection connection)
  {
    super(connection);
  }
  
  public void deleteContextFromRelationship(Long propertyIid, String sideOneContextId, String sideTwoContextId) throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_removeContextFromRelationship")
    .setInput(RDBMSAbstractFunction.ParameterType.IID, propertyIid)
    .setInput(RDBMSAbstractFunction.ParameterType.STRING, sideOneContextId)
    .setInput(RDBMSAbstractFunction.ParameterType.STRING, sideTwoContextId)
    .execute();
  }
}
