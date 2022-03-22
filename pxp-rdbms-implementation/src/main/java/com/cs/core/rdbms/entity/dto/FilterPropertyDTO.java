package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IFilterDTO;
import com.cs.core.rdbms.entity.idto.IFilterDTO.ValueType;
import com.cs.core.rdbms.entity.idto.IFilterDTO.FilterType;
import com.cs.core.rdbms.entity.idto.IFilterPropertyDTO;

import java.util.ArrayList;
import java.util.List;

public class FilterPropertyDTO implements IFilterPropertyDTO {

  private String          propertyCode;
  private PropertyType    propertyType;
  private List<IFilterDTO> filters = new ArrayList<>();

  FilterPropertyDTO(String propertyCode, PropertyType propertyType)
  {
    this.propertyCode = propertyCode;
    this.propertyType = propertyType;
  }

  @Override
  public void addFilter(FilterType filterType, ValueType valueType, Object value){
    filters.add(new FilterDTO(filterType, valueType, value ));
  }

  @Override
  public String getPropertyCode()
  {
    return propertyCode;
  }

  @Override
  public PropertyType getPropertyType()
  {
    return propertyType;
  }

  @Override
  public List<IFilterDTO> getFilters()
  {
    return filters;
  }
}
