package com.cs.core.rdbms.entity.idto;

import java.util.List;

public interface IClassifierAggregationDTO extends IAggregationRequestDTO{

 public List<String> getClassifierIds();
 public void setClassifierIds(List<String> classifierIds);

}
