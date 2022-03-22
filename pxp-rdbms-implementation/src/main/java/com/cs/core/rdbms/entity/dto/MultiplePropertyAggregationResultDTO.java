package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IMultiplePropertyAggregationResultDTO;
import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;

import java.util.ArrayList;
import java.util.List;

public class MultiplePropertyAggregationResultDTO implements IMultiplePropertyAggregationResultDTO {

  private List<IValueCountDTO> counts = new ArrayList<>();

  @Override
  public List<IValueCountDTO> getCounts()
  {
    return counts;
  }

  @Override
  public void setCounts(List<IValueCountDTO> counts)
  {
    this.counts = counts;
  }
}
