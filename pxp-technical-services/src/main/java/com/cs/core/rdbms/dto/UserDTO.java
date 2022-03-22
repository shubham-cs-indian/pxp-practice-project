package com.cs.core.rdbms.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author PankajGajjar
 */
public class UserDTO extends RDBMSRootDTO implements IUserDTO, Serializable {

  private String userName = "";

  /**
   * Enabled default constructor
   */
  public UserDTO() {
  }

  /**
   * Value constructor
   *
   * @param userIID
   * @param userName
   */
  public UserDTO(long userIID, String userName) {
    super(userIID);
    this.userName = userName;
  }

  /**
   * Constructor from query result
   *
   * @param parser
   * @throws java.sql.SQLException
   */
  public UserDTO(IResultSetParser parser) throws SQLException {
    super(parser.getLong("userIID"));
    userName = parser.getString("userName");
  }

  public static String getCacheCode(String userName) {
    return String.format("%c:%s", CSEObject.CSEObjectType.User.letter(), userName);
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException {
    CSEObject cse = new CSEObject(CSEObjectType.User);
    if(StringUtils.isNotEmpty(userName))
      userName = "\""+ userName + "\"";
    cse.setCode( userName);
    if ( exportIID )
      cse.setIID(getIID());
    return cse; // returns meta information
  }

  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException {
    CSEObject ocse = (CSEObject) cse;
    userName = userName.replaceAll("\"", "");
    setIID(ocse.getIID());
  }

  @Override
  public long getUserIID() {
    return this.getIID();
  }

  @Override
  public String getUserName()
  {
    return userName;
  }
  
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  @Override
  public int hashCode() {
    return new HashCodeBuilder(3, 5).append(userName)
            .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final UserDTO other = (UserDTO) obj;
    // at least one of the user name or user ID equals
    return !this.userName.isEmpty() && new EqualsBuilder().append(this.userName, other.userName).isEquals();
  }
}
