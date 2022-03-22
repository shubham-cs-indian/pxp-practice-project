package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.technical.exception.CSFormatException;

/**
 * The Builder interface to construct IValueRecordDTO
 * 
 * @author janak
 */
public interface IValueRecordDTOBuilder extends IPropertyRecordDTOBuilder<IValueRecordDTOBuilder> {
  
  /**
   * @param valueIID the value identifier
   * @return IValueRecordDTOBuilder
   */
  public IValueRecordDTOBuilder valueIID(long valueIID);

  /**
   * @param contextDTO the context referred by this object or empty context object
   * @return IValueRecordDTOBuilder
   */
  public IValueRecordDTOBuilder contextDTO(IContextDTO contextDTO);

  /**
   * @param number overwritten as number
   * @return IValueRecordDTOBuilder
   */
  public IValueRecordDTOBuilder asNumber(double number);

  /**
   * @param html overwritten as HTML
   * @return IValueRecordDTOBuilder
   */
  public IValueRecordDTOBuilder asHTML(String html);

  /**
   * @param localeID the locale ID or empty for non-locale based value
   * @return IValueRecordDTOBuilder
   */
  public IValueRecordDTOBuilder localeID(String localeID);

  /**
   * @param unitSymbol overwritten unit symbol (for price and measurement)
   * @return IValueRecordDTOBuilder
   */
  public IValueRecordDTOBuilder unitSymbol(String unitSymbol);

  /**
   * Add a calculation rule
   *
   * @param calculation the calculation expression
   * @return IValueRecordDTOBuilder
   * @throws CSFormatException in case of format error
   */
  public IValueRecordDTOBuilder addCalculation(String calculation) throws CSFormatException;

  /**
   * factory method to build IValueRecordDTO object
   *
   * @return IValueRecordDTO
   */
  @Override
  public IValueRecordDTO build();

}

