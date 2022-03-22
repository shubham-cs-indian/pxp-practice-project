package com.cs.core.rdbms.dto;

import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.idto.ISessionDTO;
import java.sql.SQLException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Session Data Transfer Object
 *
 * @author PankajGajjar
 */
public class SessionDTO extends SimpleDTO implements ISessionDTO {

  private final String SESSION_ID = PXONTag.session.toReadOnlyTag();
  private final String LOGIN_TIME = PXONTag.login.toReadOnlyTag();
  private String sessionID = "";
  private long loginTime = 0;

  /**
   * Enabled default constructor
   */
  public SessionDTO() {
  }

  /**
   * Value constructor
   *
   * @param sessionID
   * @param loginTime
   */
  public SessionDTO(String sessionID, long loginTime) {
    this.sessionID = sessionID;
    this.loginTime = loginTime;
  }

  /**
   * Constructor from query result
   *
   * @param parser
   * @throws SQLException
   */
  public SessionDTO(IResultSetParser parser) throws SQLException {
    this.sessionID = parser.getString("sessionID");
    this.loginTime = parser.getDateTime("loginTime");
  }

  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException {
    sessionID = parser.getString(SESSION_ID);
    loginTime = parser.getLong(LOGIN_TIME);
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(SESSION_ID, sessionID),
            JSONBuilder.newJSONField(LOGIN_TIME, loginTime));
  }

  @Override
  public String getSessionID() {
    return sessionID;
  }

  @Override
  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  @Override
  public long getLoginTime() {
    return loginTime;
  }

  @Override
  public void setLoginTime(long time) {
    this.loginTime = time;
  }

  @Override
  public boolean equals(Object other) {
    if (!super.equals(other)) {
      return false;
    }
    return new EqualsBuilder().append(this.sessionID,((SessionDTO) other).sessionID).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(3, 5).append(sessionID)
            .build();
  }
}
