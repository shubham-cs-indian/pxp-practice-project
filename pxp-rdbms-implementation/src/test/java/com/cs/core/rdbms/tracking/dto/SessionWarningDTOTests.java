package com.cs.core.rdbms.tracking.dto;

import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.tracking.idto.ISessionWarningDTO;
import com.cs.core.technical.exception.CSFormatException;
import java.util.Date;
import org.junit.Ignore;
import org.junit.Test;

/**
 * SessionWarningDTOTests
 *
 * @author PankajGajjar marked as ignore, please removed it in developement
 *         phase
 */
@Ignore
public class SessionWarningDTOTests {
  
  static final String SESSION_ID = "65465464";
  
  public SessionWarningDTOTests()
  {
  }
  
  @Test
  public void test() throws CSFormatException
  {
    
    String warningMessage = "Warning message";
    ISessionWarningDTO.WarningType warningType = ISessionWarningDTO.WarningType.UNDEFINED;
    int warningNo = -1;
    long now = (new Date()).getTime();
    SessionDTO session = new SessionDTO(SESSION_ID, now);
    
    SessionWarningDTO sample = new SessionWarningDTO(session, warningMessage, warningType,
        warningNo);
    
    String json = sample.toPXON();
    System.out.println(json);
    SessionWarningDTO sampleRev = new SessionWarningDTO();
    sampleRev.fromPXON(json);
    assert (sampleRev.getSessionID()
        .equals(SESSION_ID));
    String jsonRev = sampleRev.toPXON();
    System.out.println(jsonRev);
    assert (jsonRev.equals(json));
  }
}
