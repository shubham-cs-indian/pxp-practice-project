package com.cs.core.rdbms.tracking.dao;

import com.cs.core.json.JSONBuilder;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.tracking.idao.IObjectTrackingDAO;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

/**
 * ObjectTrackingDAOTests
 *
 * @author PankajGajjar
 */
public class ObjectTrackingDAOTests extends AbstractRDBMSDriverTests {
  
  IObjectTrackingDAO objectTrackingDAO = null;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
    objectTrackingDAO = driver.newObjectTrackingDAO();
  }
  
  @Test
  public void createObjectTracking() throws RDBMSException
  {
    printTestTitle("createObjectTracking");
    long baseEntityIID = 100001;
    long classifierIID = 4000;
    long userIID = 6001; // user name is Rahul Duldul
    String comment = "{"
        + JSONBuilder.newJSONField("comment", "TestcreateObjectTracking Tests ", true) + "}";
    
    objectTrackingDAO.createObjectTracking(baseEntityIID, classifierIID, userIID, comment);
  }
}
