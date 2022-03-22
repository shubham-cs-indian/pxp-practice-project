package com.cs.core.technical.rdbms.idto;

/**
 * Data representation of a session tracking - sessionID represents the sessionID
 *
 * @author vallee
 */
public interface ISessionDTO extends ISimpleDTO {

  /**
   * @return the session ID
   */
  public String getSessionID();

  /**
   * @param sessionID overwritten session ID
   */
  public void setSessionID(String sessionID);

  /**
   * @return the login time
   */
  public long getLoginTime();

  /**
   * @param time overwritten login time
   */
  public void setLoginTime(long time);
}
