package com.cs.core.rdbms.tracking.idto;

import com.cs.core.technical.rdbms.idto.ISessionDTO;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Data representation of a user session - sessionID represents the sessionID - userID - loginTime - logoutType - logoutTime - remarks
 *
 * @author vallee
 */
public interface IUserSessionDTO extends ISessionDTO {

  /**
   * @param prefix
   * @return a unique session ID
   */
  public static String createUniqueSessionID(String prefix) {
    LocalDateTime now = LocalDateTime.now();
    return String.format("%s%d%d%06d", prefix, now.getMonthValue(), now.getDayOfMonth(),
            new Random().nextInt(1000000));
  }

  ;
  
  /**
   * @return the user IID of the session
   */
  public long getUserIID();

  /**
   * @return the user name of the session
   */
  public String getUserName();

  /**
   * @return the logout type
   */
  public LogoutType getLogoutType();

  /**
   * @param logoutType overwritten logout type
   */
  public void setLogoutType(LogoutType logoutType);

  /**
   * @return the logout time
   */
  public long getLogoutTime();

  /**
   * @param time overwritten logout time
   */
  public void setLogoutTime(long time);

  /**
   * @return the remarks
   */
  public String getRemarks();

  /**
   * @param remarks overwritten remarks
   */
  public void setRemarks(String remarks);

  public String getTransactionID();
  public void setTransactionId(String transactionID);

  /**
   * Constant for Logout type
   */
  public enum LogoutType {

    UNDEFINED, NORMAL, TIMEOUT, KILLED, SHUTDOWN;

    private static final LogoutType[] values = values();

    public static LogoutType valueOf(int ordinal) {
      return values[ordinal];
    }

  }
}
