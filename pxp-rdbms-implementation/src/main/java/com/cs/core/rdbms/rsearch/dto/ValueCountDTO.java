package com.cs.core.rdbms.rsearch.dto;

import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;
import java.util.HashMap;
import java.util.Map;

public class ValueCountDTO implements IValueCountDTO {
  
  IPropertyDTO      property;
  Map<String, Long> valueVScount = new HashMap<>();;
  
  public ValueCountDTO(IPropertyDTO property)
  {
    this.property = property;
  }
  
  public IPropertyDTO getProperty()
  {
    if (property == null) {
      property = new PropertyDTO();
    }
    return property;
  }
  
  public Map<String, Long> getValueVScount()
  {
    if (valueVScount == null) {
      valueVScount = new HashMap<>();
    }
    return valueVScount;
  }
  
  public void fillValueVScount(Map<String, Long> valueVScount)
  {
    this.valueVScount.putAll(valueVScount);
  }
  
  public void fillValueVScount(String value, Long count)
  {
    valueVScount.put(value, count);
  }

}
