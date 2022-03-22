package com.cs.core.elastic.aggregation;

import java.util.Map;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;

public abstract class BaseAggregation {

  public abstract AggregationBuilder aggregate(IAggregationRequestDTO request, String localeId);

  public abstract Map<String, Long> parseResult(IAggregationRequestDTO request, Map<String, Aggregation> aggs);

  public enum PropertyAggName {
    nestedAgg, propertyAgg, bucketSearch;
  }

  enum ClassifierAggName {
    classifierAgg, classifierFilterAgg;
  }
  
  public enum RuleViolationAggName
  {
    redAgg, yellowAgg, orangeAgg, greenAgg, violationAgg;
  }
  
  public enum RangeAggName
  {
    rangeAgg, nestedRangeAgg, extendedStatsAgg;
  }
}
