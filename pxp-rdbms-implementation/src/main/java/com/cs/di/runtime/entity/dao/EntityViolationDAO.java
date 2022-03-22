package com.cs.di.runtime.entity.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;

public class EntityViolationDAO  implements IEntityViolationDAO{
  
  RDBMSAbstractDriver driver;
  private static final String Q_VIOLATION_CHECK_QUERY = "SELECT COUNT(qualityflag) AS violationCount FROM pxp.baseentityqualityrulelink where baseentityiid =  ? and qualityflag = ?";
  
  public boolean checkViolation( Long baseentityiid, int violationCode)
  {
    Boolean[] violationExists = new Boolean[1];
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
            PreparedStatement statement = currentConn.prepareStatement(Q_VIOLATION_CHECK_QUERY);
            statement.setLong(1, baseentityiid);
            statement.setLong(2, violationCode);
            ResultSet executeQuery = statement.executeQuery();
            if (executeQuery.next()) {
              violationExists[0] = executeQuery.getInt("violationCount") != 0;
            }
          });
    }
    catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      RDBMSLogger.instance().exception(e1);
    }
    return violationExists[0];
  }
}

