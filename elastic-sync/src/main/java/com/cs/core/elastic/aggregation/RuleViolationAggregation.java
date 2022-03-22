package com.cs.core.elastic.aggregation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;

import com.cs.core.elastic.ibuilders.ISearchBuilder.Fields;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IRuleViolationAggregationDTO;

public class RuleViolationAggregation extends BaseAggregation{
  
  public static final String LANGUAGE_INDEPENDENT = "LI";

  @Override
  public AggregationBuilder aggregate(IAggregationRequestDTO request, String localeId)
  {
    IRuleViolationAggregationDTO ruleViolationAggDTO = (IRuleViolationAggregationDTO) request;
    List<String> locales = Arrays.asList(ruleViolationAggDTO.getLocale(), LANGUAGE_INDEPENDENT);

    FiltersAggregationBuilder ruleViolationAgg = new FiltersAggregationBuilder(RuleViolationAggName.violationAgg.name(), 
        new FiltersAggregator.KeyedFilter(RuleViolationAggName.redAgg.name(), QueryBuilders.termsQuery(Fields.red.name(), locales)),
        new FiltersAggregator.KeyedFilter(RuleViolationAggName.orangeAgg.name(), QueryBuilders.termsQuery(Fields.orange.name(), locales)),
        new FiltersAggregator.KeyedFilter(RuleViolationAggName.yellowAgg.name(), QueryBuilders.termsQuery(Fields.yellow.name(), locales)));
        
    ruleViolationAgg.otherBucket(true);
    ruleViolationAgg.otherBucketKey(RuleViolationAggName.greenAgg.name());
    
    return ruleViolationAgg;
  }

  @Override
  public Map<String, Long> parseResult(IAggregationRequestDTO request,
      Map<String, Aggregation> aggs)
  {
    Filters aggregation = (Filters) aggs.get(RuleViolationAggName.violationAgg.name());
    Map<String, Long> colorVScount = new HashMap<>();

    aggregation.getBuckets().forEach(bucket -> colorVScount.put(bucket.getKeyAsString(), bucket.getDocCount()));
    
    return colorVScount;
  }
  
}
