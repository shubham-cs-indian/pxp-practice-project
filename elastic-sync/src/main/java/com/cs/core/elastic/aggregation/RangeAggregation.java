package com.cs.core.elastic.aggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ExtendedStatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedExtendedStats;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.cs.core.elastic.builders.NestedPathBuilder;
import com.cs.core.elastic.builders.SearchBuilder;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.elastic.ibuilders.ISearchBuilder.Fields;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IRangeAggregationDTO;


/**
 * This Class facilitates aggregation for numeric attributes through Range Aggregations.
 * 
 * @author faizan.siddique
 *
 */
public class RangeAggregation extends BaseAggregation {
  
  private final String EXTENDED_STATS = "extendedStats";
  private final String FROM = "from";
  private final String TO = "to";
  
  @Override
  public AggregationBuilder aggregate(IAggregationRequestDTO request, String localeId)
  {
    IRangeAggregationDTO rangeAggDTO = (IRangeAggregationDTO) request;
    IPropertyDTO property = rangeAggDTO.getProperty();
    String propertyCode = property.getCode();
    String attributePath = getAttributePath(propertyCode, true, localeId, rangeAggDTO.getIsTranslatable());
    
    Aggregations extendedStats = getExtendedStatistics(rangeAggDTO, attributePath);
    Set<Map<String, Long>> ranges = prepareRangesFromStats(extendedStats);
   
   RangeAggregationBuilder rangeAggregation = AggregationBuilders.range(RangeAggName.rangeAgg.name()).field(attributePath);
   ranges.forEach(range -> rangeAggregation.addRange(range.get(FROM), range.get(TO)));
   
   NestedAggregationBuilder nestedAggregation = AggregationBuilders.nested(propertyCode + "_" + RangeAggName.nestedRangeAgg, Fields.propertyObjects.name());
   nestedAggregation.subAggregation(rangeAggregation);
  
   return nestedAggregation;
  }

  private Aggregations getExtendedStatistics(IRangeAggregationDTO rangeAggDTO, String attributePath)
  {
    
    ExtendedStatsAggregationBuilder extendedStats = AggregationBuilders.extendedStats(EXTENDED_STATS).field(attributePath);
    NestedAggregationBuilder rangeValuesAgg = AggregationBuilders.nested(RangeAggName.extendedStatsAgg.name(), Fields.propertyObjects.name())
                                             .subAggregation(extendedStats);
    
    SearchSourceBuilder source = new SearchSourceBuilder();
    source.query((BoolQueryBuilder) rangeAggDTO.getBaseQuery());
    source.aggregation(rangeValuesAgg);
    Aggregations result = null;
    
    try {
      result = ElasticServiceDAS.instance().aggregate(source, rangeAggDTO.getIndices().toArray(new String[0]));
    }
    catch (Exception e) {
      RDBMSLogger.instance().warn("Unable to fetch Extended Stats of Range values aggregation due to " + e);
    }
    return result;
  }

  private Set<Map<String, Long>> prepareRangesFromStats(Aggregations extendedStats)
  {
    Set<Map<String, Long>> ranges = new HashSet<>();
    
    if(extendedStats == null) {
      return ranges;
    }
    
    ParsedNested parsedStats = extendedStats.get(RangeAggName.extendedStatsAgg.name());
    ParsedExtendedStats stats = parsedStats.getAggregations().get(EXTENDED_STATS);
    
    Long min = ((Double) stats.getMin()).longValue();
    Long max = ((Double) stats.getMax()).longValue();
    Long avg = ((Double) stats.getAvg()).longValue();
    
    if (min == Double.POSITIVE_INFINITY || max == Double.NEGATIVE_INFINITY) {
      return ranges;
    }
    
    Long standardDeviation = ((Double) stats.getStdDeviation()).longValue();
    
    if (min.equals(max) || Double.isNaN(standardDeviation)) {
      Map<String, Long> range = new HashMap<>();
      range.put(FROM, min);
      range.put(TO, max + 1);
      ranges.add(range);
      return ranges;
    }
    
    Long avgPlusStdDev = avg + standardDeviation;
    Long avgMinusStdDev = avg - standardDeviation;
    
    if (avgMinusStdDev < min) {
      Map<String, Long> range = new HashMap<>();
      range.put(FROM, min);
      range.put(TO, avg);
      ranges.add(range);
    }
    else {
      Map<String, Long> range = new HashMap<>();
      range.put(FROM, min);
      range.put(TO, avgMinusStdDev);
      ranges.add(range);
      
      range = new HashMap<>();
      range.put(FROM, avgMinusStdDev);
      range.put(TO, avg);
      ranges.add(range);
    }
    
    if (avgPlusStdDev > max) {
      Map<String, Long> range = new HashMap<>();
      range.put(FROM, avg);
      range.put(TO, max + 1);
      ranges.add(range);
    }
    else {
      Map<String, Long> range = new HashMap<>();
      range.put(FROM, avg);
      range.put(TO, avgPlusStdDev);
      ranges.add(range);
      
      range = new HashMap<>();
      range.put(FROM, avgPlusStdDev);
      range.put(TO, max + 1);
      ranges.add(range);
    }
    return ranges;
  }
  
  @Override
  public Map<String, Long> parseResult(IAggregationRequestDTO request,
      Map<String, Aggregation> aggs)
  {
    Map<String, Long> rangeVScount = new LinkedHashMap<String, Long>();
    List<String> propertyCodes = new ArrayList<>(aggs.keySet());
    ParsedNested nestedAgg = (ParsedNested) aggs.get(propertyCodes.get(0));
    ParsedRange ranges =  nestedAgg.getAggregations().get(RangeAggName.rangeAgg.name());
    
    ranges.getBuckets().stream().sorted(Collections.reverseOrder((x1, x2) -> ((Long) x1.getDocCount()).compareTo((Long) x2.getDocCount())))
        .forEach(x -> rangeVScount.put(x.getKeyAsString(), x.getDocCount()));
    
    return rangeVScount;
  }
  
  private String getAttributePath(String attributeId, Boolean isNumeric, String localeId, Boolean isTranslatable)
  {
    NestedPathBuilder attributePath = NestedPathBuilder.getPathForAttribute();
    String localeField = isTranslatable ? localeId : SearchBuilder.Fields.independent.name();
    attributePath.append(localeField);
    attributePath.appendAttributeIdForExact(attributeId, isNumeric);
    return attributePath.path();
  }
  
}
