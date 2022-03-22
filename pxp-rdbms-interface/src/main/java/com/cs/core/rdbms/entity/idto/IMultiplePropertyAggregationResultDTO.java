package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;

import java.util.List;

public interface IMultiplePropertyAggregationResultDTO {

  List<IValueCountDTO> getCounts();

  void setCounts(List<IValueCountDTO> counts);
}
