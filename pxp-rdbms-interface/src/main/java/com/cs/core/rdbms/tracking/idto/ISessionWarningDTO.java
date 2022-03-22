package com.cs.core.rdbms.tracking.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;
import com.cs.core.technical.rdbms.idto.ISessionDTO;

/**
 * Data representation of a warning that occurs during a user session sessionID refers to the session ID warningNo is the warning sequential
 * number in the session (sessionID + warningNo is primary key) loginTime the session logging time warningType warningMessage
 *
 * @author vallee
 */
public interface ISessionWarningDTO extends ISessionDTO, IRootDTO {

  /**
   * @return the warning number
   */
  public int getWarningNo();

  ;
  
  /**
   * @param warningNo
   *          overwritten warning number
   */
  public void setWarningNo(int warningNo);

  /**
   * @return the warning type
   */
  public WarningType getWarningType();

  /**
   * @param warningType overwritten warning type
   */
  public void setWarningType(WarningType warningType);

  /**
   * @return the warning message
   */
  public String getWarningMessage();

  /**
   * @param warningMessage overwritten warning message
   */
  public void setWarningMessage(String warningMessage);

  public enum WarningType {

    UNDEFINED, MESSAGE, MINOR, MAJOR, CRITICAL;

    private static final WarningType[] values = values();

    public static WarningType valueOf(int ordinal) {
      return values[ordinal];
    }
  }
}
