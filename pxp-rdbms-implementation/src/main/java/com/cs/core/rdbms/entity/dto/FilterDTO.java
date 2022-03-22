package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IFilterDTO;
import org.apache.commons.lang3.Range;


public class FilterDTO implements IFilterDTO {

  public FilterType type;
  public ValueType  valueType;
  public Object     value;


  public FilterDTO(FilterType type, ValueType valueType, Object value)
  {
    this.type = type;
    this.valueType = valueType;
    this.value = value;
  }

  @Override
  public FilterType getType()
  {
    return type;
  }

  @Override
  public ValueType getValueType()
  {
    return valueType;
  }

  @Override
  public Object getValue()
  {
    return value;
  }
}
