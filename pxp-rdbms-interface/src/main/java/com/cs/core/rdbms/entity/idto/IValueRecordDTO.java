package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.exception.CSFormatException;

/**
 * A value record is a property record made of: - a locale ID - the row value - the value as expression - the value as number - the value as
 * HTML - context information - tag contexts
 *
 * @author vallee
 */
public interface IValueRecordDTO extends IPropertyRecordDTO {

  /**
   * An additional unique identifier /!\ different than propertyRecordIID as returned and managed by inherited getIID()
   *
   * @return the unique value Record IID
   */
  public long getValueIID();

  /**
   * @return the locale ID or empty for non-locale based value
   */
  public String getLocaleID();

  /**
   * @return the contextual object of this record (which is shared across locale IDs)
   */
  public IContextualDataDTO getContextualObject();

  /**
   * Add a calculation rule (
   *
   * @param calculation the calculation expression
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void addCalculation(String calculation) throws CSFormatException;

  /**
   * @return the current calculation expression
   */
  public String getCalculation();

  /**
   * @return true when the calculation definition has been changed
   */
  public boolean hasChangedCalculation();

  /**
   * @return the value
   */
  public String getValue();

  /**
   * @param value overwritten value
   */
  public void setValue(String value);

  /**
   * @return the value as number
   */
  public double getAsNumber();

  /**
   * @param number overwritten as number
   */
  public void setAsNumber(double number);

  /**
   * @return the value as html
   */
  public String getAsHTML();

  /**
   * @param html overwritten as HTML
   */
  public void setAsHTML(String html);

  /**
   * @return the unit symbol (for price and measurement)
   */
  public String getUnitSymbol();

  /**
   * @param unitSymbol overwritten unit symbol (for price and measurement)
   */
  public void setUnitSymbol(String unitSymbol);
}
