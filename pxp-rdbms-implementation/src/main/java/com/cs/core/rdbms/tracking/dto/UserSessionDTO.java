package com.cs.core.rdbms.tracking.dto;

import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * UserSession Data Transfer Object
 *
 * @author PankajGajjar
 */
public class UserSessionDTO extends SimpleDTO implements IUserSessionDTO, Serializable {
  
  private static final long   serialVersionUID = 1L;
  
  private static final String USER        = PXONTag.user.toReadOnlyCSETag();
  private static final String SESSION     = PXONTag.session.toReadOnlyCSETag();
  private static final String LOGOUT_TYPE = PXONTag.logout.toReadOnlyTag();
  private static final String REMARKS     = PXONTag.text.toReadOnlyTag();
  private static final String LOGOUT_TIME = PXONTag.logouttype.toReadOnlyTag();
  // NO need of internal key in that case
  private UserDTO             user        = new UserDTO();
  private SessionDTO          session     = new SessionDTO();
  private LogoutType          logoutType  = LogoutType.UNDEFINED;
  private long                logoutTime  = 0;
  private String              remarks = "";
  private String              transactionId;
  
  /**
   * Enabled default constructor
   */
  public UserSessionDTO()
  {
  }
  
  /**
   * Value constructor
   *
   * @param session
   * @param user
   * @param logoutType
   * @param logoutTime
   * @param remarks
   */
  public UserSessionDTO(SessionDTO session, UserDTO user, LogoutType logoutType, long logoutTime,
      String remarks)
  {
    this.session = session;
    this.user = user;
    this.logoutType = logoutType;
    this.logoutTime = logoutTime;
    this.remarks = remarks;
  }
  
  /**
   * Constructor from query result
   *
   * @param parser
   * @throws java.sql.SQLException
   */
  public UserSessionDTO(IResultSetParser parser) throws SQLException
  {
    this.session = new SessionDTO(parser);
    this.user = new UserDTO(parser);
  }
  
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    user.fromCSExpressID(parser.getCSEElement(USER));
    session.fromJSON(parser.getJSONParser(SESSION));
    this.logoutType = parser.getEnum(LogoutType.class, LOGOUT_TYPE);
    this.logoutTime = parser.getLong(LOGOUT_TIME);
    this.remarks = parser.getString(REMARKS);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(USER, user.toCSExpressID()),
        JSONBuilder.newJSONField(SESSION, session.toJSONBuffer()),
        JSONBuilder.newJSONField(LOGOUT_TYPE, logoutType),
        JSONBuilder.newJSONField(LOGOUT_TIME, logoutTime),
        JSONBuilder.newJSONField(REMARKS, remarks, true));
  }
  
  @Override
  public long getUserIID()
  {
    return user.getUserIID();
  }
  
  @Override
  public String getUserName()
  {
    return user.getUserName();
  }
  
  @Override
  public String getSessionID()
  {
    return session.getSessionID();
  }
  
  @Override
  public void setSessionID(String sessionID)
  {
    session.setSessionID(sessionID);
  }
  
  @Override
  public long getLoginTime()
  {
    return session.getLoginTime();
  }
  
  @Override
  public void setLoginTime(long time)
  {
    session.setLoginTime(time);
  }
  
  @Override
  public LogoutType getLogoutType()
  {
    return logoutType;
  }
  
  @Override
  public void setLogoutType(LogoutType logoutType)
  {
    this.logoutType = logoutType;
  }
  
  @Override
  public long getLogoutTime()
  {
    return logoutTime;
  }
  
  @Override
  public void setLogoutTime(long time)
  {
    this.logoutTime = time;
  }
  
  @Override
  public String getRemarks()
  {
    return remarks;
  }
  
  @Override
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  
  /**
   * @return true if the session content is valid
   */
  public boolean isNull()
  {
    return session.getSessionID()
        .isEmpty() || user.isNull();
  }

  @Override
  public String getTransactionID()
  {
    return transactionId;
  }

  @Override
  public void setTransactionId(String transactionID)
  {
    this.transactionId = transactionID;
  }
}
