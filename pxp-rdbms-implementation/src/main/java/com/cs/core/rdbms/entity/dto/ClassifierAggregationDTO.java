package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IClassifierAggregationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyAggregationDTO;

import java.util.ArrayList;
import java.util.List;

public class ClassifierAggregationDTO extends BaseAggregationDTO implements IClassifierAggregationDTO {

private List<String> classifierIds = new ArrayList<>();

  @Override public List<String> getClassifierIds()
  {
    return classifierIds;
  }

  @Override
  public void setClassifierIds(List<String> classifierIds)
  {
    this.classifierIds = classifierIds;
  }
}
