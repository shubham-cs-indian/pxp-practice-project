package com.cs.core.elastic.aggregation;

import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IClassifierAggregationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyAggregationDTO;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.util.Map;

public class FlatFieldAggregation  extends BaseAggregation {

  @Override
  public AggregationBuilder aggregate(IAggregationRequestDTO request, String localeId)
  {
   return null;
  }

  @Override public Map<String, Long> parseResult(IAggregationRequestDTO request, Map<String, Aggregation> aggs)
  {
    return null;
  }
}
