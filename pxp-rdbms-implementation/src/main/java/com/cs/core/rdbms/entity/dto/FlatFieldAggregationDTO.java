package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IClassifierAggregationDTO;
import com.cs.core.rdbms.entity.idto.IFlatFieldAggregationDTO;

import java.util.List;

public class FlatFieldAggregationDTO extends BaseAggregationDTO implements IFlatFieldAggregationDTO {

  private String flatField;

  @Override
  public String getFlatField()
  {
    return flatField;
  }

  @Override
  public void setFlatField(String flatField)
  {
    this.flatField = flatField;
  }
}
