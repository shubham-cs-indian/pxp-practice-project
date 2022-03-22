package com.cs.core.rdbms.tracking.dao;

import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Test;

/**
 * UserSessionDAOTests
 *
 * @author PankajGajjar
 */
public class UserSessionDAOTests extends AbstractRDBMSDriverTests {
  
  @Test
  public void test1_OpenSession() throws RDBMSException, CSFormatException
  {
    printTestTitle("openSession");
    // initilise data - this sample data is available in test-data folder.
    String userName = "Rahul Duldul";
    // test Caller
    String testSessionID = sessionID + "#TEST";
    IUserSessionDTO userSessionDTO = userSession.openSession(userName, testSessionID);
    printJSON(userSessionDTO);
    // true test cases
    assert (userSessionDTO != null);
    assert (userSessionDTO.getSessionID() == testSessionID);
    assert (((UserSessionDTO) userSessionDTO).getUserIID() != 0L);
  }
  
  @Test
  public void test2_CloseSession() throws RDBMSException
  {
    printTestTitle("closeSession");
    LogoutType logoutType = LogoutType.NORMAL;
    System.out.println("Closing session " + sessionID);
    userSession.closeSession(sessionID, logoutType, "logout normal");
  }
  
  @Test
  public void test3_shutdownSessions() throws RDBMSException
  {
    printTestTitle("shutdownSessions");
    userSession.shutdownSessions();
  }
}
