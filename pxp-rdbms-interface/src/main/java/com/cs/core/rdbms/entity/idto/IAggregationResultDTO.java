package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;

import java.util.Map;

public interface  IAggregationResultDTO {

  /**
   *
   * @return get Code of the property of which value are being aggregated
   */
  public String getCode();

  /**
   *
   * @param code set code of property of which value are being aggregated
   */
  public void setCode(String code);

  /**
   *
   * @return Map of values and their count
   */
  public Map<String, Long> getCount();

  /**
   *
   * @param count map of values and their count
   */
  public void setCount(Map<String, Long> count);

}
