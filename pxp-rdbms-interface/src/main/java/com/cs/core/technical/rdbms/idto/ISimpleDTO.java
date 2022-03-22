package com.cs.core.technical.rdbms.idto;

import java.io.Serializable;

import com.cs.core.technical.exception.CSFormatException;

/**
 * Basic DTO with no CSE identification and not PXON compliant
 *
 * @author vallee
 */
public interface ISimpleDTO extends Serializable {

  /**
   * update DTO content from an incoming JSON
   *
   * @param json the simple JSON content of the DTO
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void fromJSON(String json) throws CSFormatException;

  /**
   * Used to aggregate multiple DTOs into a single JSON/PXON structure
   *
   * @return the JSON representation of this DTO without enclosing brackets
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public StringBuffer toJSONBuffer() throws CSFormatException;

  /**
   * default implementation is to return "{" + toJSONBuffer() + "}"
   *
   * @return the simple JSON string representation of this DTO
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public default String toJSON() throws CSFormatException {
    return "{" + toJSONBuffer() + "}";
  }

}
