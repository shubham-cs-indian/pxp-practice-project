package com.cs.core.elastic.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.elasticsearch.search.aggregations.AggregationBuilder;

import com.cs.core.elastic.aggregation.BaseAggregation;
import com.cs.core.elastic.aggregation.ClassifierAggregation;
import com.cs.core.elastic.aggregation.FlatFieldAggregation;
import com.cs.core.elastic.aggregation.PropertyAggregation;
import com.cs.core.elastic.aggregation.RangeAggregation;
import com.cs.core.elastic.aggregation.RuleViolationAggregation;
import com.cs.core.elastic.ibuilders.IAggregator;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO.AggregationType;

public class Aggregator implements IAggregator {

  private final IAggregationRequestDTO request;
  private       AggregationBuilder     aggregationBuilder;
  private final String                 localeId;

  public Aggregator(IAggregationRequestDTO request, String localeId)
  {
    this.request = request;
    this.localeId = localeId;
  }

  public AggregationBuilder getAggregationBuilder()
  {
    return aggregationBuilder;
  }


  public IAggregationRequestDTO getRequest()
  {
    return request;
  }

  @Override
  public IAggregator aggregate()
  {
    aggregationBuilder = Aggregation.getAggregation(request.getAggregationType()).aggregate(request, localeId);
    return this;
  }

  public Map<String, Long> parseResult(Map<String,org.elasticsearch.search.aggregations.Aggregation> aggs)
  {
    return Aggregation.getAggregation(request.getAggregationType()).parseResult(request, aggs);
  }

  public enum Aggregation {
    ClassifierAggregation(new ClassifierAggregation(), AggregationType.byClass, AggregationType.byTaxonomy),
    PropertyAggregation(new PropertyAggregation(), AggregationType.byProperty),
    FlatFieldAggregation(new FlatFieldAggregation(), AggregationType.byFlatField),
    RuleViolationAggregation(new RuleViolationAggregation(), AggregationType.byRuleViolation),
    RangeAggregation(new RangeAggregation(), AggregationType.byRange);

    private final List<AggregationType> types = new ArrayList<>();
    private final BaseAggregation       aggregation;

    Aggregation(BaseAggregation agg, AggregationType... aggregationTypes)
    {
      types.addAll(Arrays.asList(aggregationTypes));
      this.aggregation = agg;
    }

    public List<AggregationType> getTypes()
    {
      return types;
    }

    public BaseAggregation getAggregation()
    {
      return aggregation;
    }

    public static BaseAggregation getAggregation(AggregationType aggregationType)
    {
      Optional<Aggregation> optional = Arrays.stream(Aggregation.values())
          .filter(x -> x.getTypes().contains(aggregationType)).findFirst();
      if (optional.isPresent()) {
        return optional.get().getAggregation();
      }
      return null;
    }
  }
}
