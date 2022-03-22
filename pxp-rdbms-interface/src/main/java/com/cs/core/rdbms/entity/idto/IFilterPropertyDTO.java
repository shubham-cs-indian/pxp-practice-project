package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

import java.io.Serializable;
import java.util.List;

public interface IFilterPropertyDTO extends Serializable {

  /**
   * Add a specific filter for particulat property.
   * @param filterType
   * @param valueType
   * @param value
   */
  public void addFilter(IFilterDTO.FilterType filterType, IFilterDTO.ValueType valueType, Object value);

  String getPropertyCode();

  IPropertyDTO.PropertyType getPropertyType();

  List<IFilterDTO> getFilters();
}
