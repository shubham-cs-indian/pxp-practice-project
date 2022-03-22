package com.cs.core.technical.rdbms.idto;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

/**
 * The root DTO properties for DTO which have a CSE definition
 *
 * @author vallee
 */
public interface IRootDTO extends ISimpleDTO {
  
  /**
   * @param status overwritten status to export $iid specification (default is false)
   */
  public void setExportOfIID( boolean status);

  /**
   * @return true if this is a null DTO (meaning having null identifiers)
   */
  public boolean isNull();

  /**
   * @return the change status of this DTO since its last loading or JSON conversion
   */
  public boolean isChanged();

  /**
   * @param status overwritten status of the data change status
   */
  public void setChanged(boolean status);

  /**
   * update DTO content from an incoming PXON (automatically moves the change status to true)
   *
   * @param json the PXON content of the DTO
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void fromPXON(String json) throws CSFormatException;

  @Override
  public default void fromJSON(String json) throws CSFormatException {
    fromPXON(json);
  }

  /**
   * Used to aggregate multiple DTOs into a single PXON structure
   *
   * @return the JSON representation of this DTO without enclosing brackets
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public StringBuffer toPXONBuffer() throws CSFormatException;

  @Override
  public default StringBuffer toJSONBuffer() throws CSFormatException {
    return toPXONBuffer();
  }

  /**
   * default implementation is to return "{" + toPXONBuffer() + "}"
   *
   * @return the JSON string representation of this DTO
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public default String toPXON() throws CSFormatException {
    return "{" + toPXONBuffer() + "}";
  }

  /**
   * The CS Expression identity of the object represented by this DTO CS Expression identities are involved into: - object tracking and
   * event queue - coupling and calculation formulas - PXON export/import
   *
   * @return the CS Expression
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public ICSEElement toCSExpressID() throws CSFormatException;

  /**
   * Redefine the identify part of this DTO from a CS Expression
   *
   * @param cse a CS Expression identity
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException;
}
