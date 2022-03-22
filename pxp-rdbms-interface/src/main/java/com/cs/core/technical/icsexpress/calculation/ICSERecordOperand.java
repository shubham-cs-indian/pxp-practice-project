package com.cs.core.technical.icsexpress.calculation;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;

/**
 * An operand that consists in a record definition
 *
 * @author vallee
 */
public interface ICSERecordOperand extends ICSECalculationNode {

  /**
   * @return the holder identify of the record (same as coupling)
   */
  public ICSECouplingSource getSource();

  /**
   * @return the property identify of the record
   */
  public ICSEProperty getProperty();

  /**
   * @return any field extension
   */
  public PropertyField getPropertyField();

  /**
   * @return true when this record has been resolved
   */
  public boolean isResolved();

  /**
   * @param required if required throws an exception in case of non-resolution
   * @return the dependency signature of this record (i.e. baseentityIID:propertyIID)
   * @throws com.cs.core.technical.exception.CSFormatException when the record has not been resolved
   */
  public String getDependencyNodeID(boolean required) throws CSFormatException;

  public enum PropertyField {
    NONE, html, unit, number, length, complement, range;
  }
}
