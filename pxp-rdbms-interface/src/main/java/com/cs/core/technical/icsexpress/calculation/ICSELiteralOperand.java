package com.cs.core.technical.icsexpress.calculation;

import java.util.List;

import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEList;

/**
 * @author vallee
 */
public interface ICSELiteralOperand extends ICSECalculationNode {

  /**
   * @return the literal type of this operand
   */
  public LiteralType getLiteralType();

  /**
   * @return the value as String (same as toString)
   */
  public default String asString() {
    return toString();
  }

  /**
   * @return the value as integer
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public long asInteger() throws CSFormatException;

  /**
   * @return the value as double
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public double asDouble() throws CSFormatException;

  /**
   * @return the operand interpretation as boolean
   * @throws CSFormatException
   */
  public boolean asBoolean() throws CSFormatException;

  /**
   * @return the operand interpreted as list of String values
   * @throws CSFormatException
   */
  public List<String> asList() throws CSFormatException;

  /**
   * @return the operand interpreted as list of tag values
   * @throws CSFormatException
   */
  public ICSEList asTagValueList() throws CSFormatException;

  /**
   * @return true when this operand cannot be resolved
   */
  public boolean isUndefined();

}
