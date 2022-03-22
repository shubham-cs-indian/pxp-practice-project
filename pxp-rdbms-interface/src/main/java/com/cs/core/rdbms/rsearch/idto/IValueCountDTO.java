package com.cs.core.rdbms.rsearch.idto;

import java.util.Map;

import com.cs.core.rdbms.config.idto.IPropertyDTO;

/**
 * Counts of specific properties for search renderer
 *
 * @author Niraj
 */
public interface IValueCountDTO  {
  
  /**
   * 
   * @return get PropertyDTO of property(either attribute or tag) that qualifies for this DTO.
   */
  public IPropertyDTO getProperty();
  
  /**
   * 
   * @return get value and it's count for a particular property.
   * For Tag value is tagValueCode and for attribute it is actual value.
   */
  public Map<String, Long> getValueVScount();
  
  /**
   * 
   * @param valueVScount : fill the map of multiple value with it's count.
   */
  public void fillValueVScount(Map<String, Long> valueVScount);
  
  /**
   * 
   * @param valueVScount : fill the map of single value with it's count.
   */
  public void fillValueVScount(String value, Long count);
  
  
}
