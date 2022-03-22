package com.cs.core.rdbms.tracking.dto;

import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.tracking.idto.ITrackingDTO.TrackingEvent;
import com.cs.core.technical.exception.CSFormatException;
import java.util.Date;
import org.junit.Test;

/**
 * TrackingDTOTests
 *
 * @author umesh.kirdat marked as ignore, please removed it in developement
 *         phase
 */
// @Ignore
public class TrackingDTOTests {
  
  static final String USER_NAME = "hello WORLD";
  static final String USER_ID   = "12345";
  static final long   USER_IID  = 12345;
  
  public TrackingDTOTests()
  {
  }
  
  @Test
  public void test() throws CSFormatException
  {
    UserDTO user = new UserDTO(USER_IID, USER_NAME);
    long now = (new Date()).getTime();
    // long later = now + 3000;
    TrackingDTO sample = new TrackingDTO(user, now, TrackingEvent.CREATE);
    
    String json = sample.toPXON();
    System.out.println(json);
    TrackingDTO sampleRev = new TrackingDTO();
    sampleRev.fromPXON(json);
    assert (sampleRev.getUserName()
        .equals(USER_NAME));
    String jsonRev = sampleRev.toPXON();
    System.out.println(jsonRev);
    assert (jsonRev.equals(json));
  }
}
