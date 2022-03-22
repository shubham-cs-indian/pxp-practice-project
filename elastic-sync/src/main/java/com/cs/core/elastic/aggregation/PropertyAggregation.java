package com.cs.core.elastic.aggregation;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import com.cs.core.elastic.builders.NestedPathBuilder;
import com.cs.core.elastic.builders.SearchBuilder;
import com.cs.core.elastic.ibuilders.ISearchBuilder.Fields;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IPropertyAggregationDTO;

public class PropertyAggregation extends BaseAggregation {

  public AggregationBuilder aggregate(IAggregationRequestDTO request, String localeId)
  {
    IPropertyAggregationDTO propertyAggDTO = (IPropertyAggregationDTO) request;
    IPropertyDTO property = propertyAggDTO.getProperty();
    NestedAggregationBuilder nestedAggregation = AggregationBuilders.nested(property.getCode() + "_" + PropertyAggName.nestedAgg.name(), Fields.propertyObjects.name());

    if (property.getSuperType().equals(IPropertyDTO.SuperType.ATTRIBUTE)) {
      aggsForAttribute(localeId, propertyAggDTO, property, nestedAggregation);
    }
    else if (property.getSuperType().equals(IPropertyDTO.SuperType.TAGS)) {
      aggsForTag(propertyAggDTO, property, nestedAggregation);
    }
    return nestedAggregation;
  }

  @Override public Map<String, Long> parseResult(IAggregationRequestDTO request, Map<String, Aggregation> aggs)
  {
    IPropertyAggregationDTO propertyAggDTO = (IPropertyAggregationDTO) request;
    IPropertyDTO property = propertyAggDTO.getProperty();
    ParsedNested nestedAgg = (ParsedNested) aggs.get(property.getCode() + "_" + PropertyAggName.nestedAgg.name());
    ParsedTerms propertyAgg = nestedAgg.getAggregations().get(property.getCode());
    if(propertyAgg == null){
      ParsedFilter bucketSearch = nestedAgg.getAggregations().get(PropertyAggName.bucketSearch.name());
      propertyAgg = bucketSearch.getAggregations().get(property.getCode());
    }

    Map<String, Long> count = new LinkedHashMap<String, Long>();
    
    propertyAgg.getBuckets().stream()
        .sorted(Collections.reverseOrder((x1, x2) -> ((Long) x1.getDocCount()).compareTo((Long) x2.getDocCount()))).forEach(bucket -> {
          String key = bucket.getKeyAsString();
          if (property.getPropertyType().equals(PropertyType.DATE)) {
            key = getFunctionToTransform(property).apply(bucket.getKeyAsNumber());
          }
          count.put(key, bucket.getDocCount());
        });
    
    return count;
  }

  private Function<Number, String> getFunctionToTransform(IPropertyDTO property)
  {
      return  keyAsNumber -> new Date(new Timestamp(keyAsNumber.longValue()).getTime()).toString();
  }

  private void aggsForTag(IPropertyAggregationDTO propertyAggDTO, IPropertyDTO property, NestedAggregationBuilder nestedAggregation)
  {
    TermsAggregationBuilder tagAggregation = AggregationBuilders.terms(property.getCode())
        .field(NestedPathBuilder.getPathForTag(property.getCode()))
        .size(propertyAggDTO.getSize());
    nestedAggregation.subAggregation(tagAggregation);
  }

  private void aggForAttributes(IPropertyAggregationDTO propertyAggDTO, NestedAggregationBuilder nestedAggregation, String attributePath,
      IPropertyDTO property)
  {
    TermsAggregationBuilder attributeAggs = AggregationBuilders.terms(property.getCode())
        .field(attributePath)
        .size(propertyAggDTO.getSize());

    String bucketSearch = propertyAggDTO.getBucketSearch();
    if (!StringUtils.isEmpty(bucketSearch)) {
      FilterAggregationBuilder bucketSearchAggs = AggregationBuilders.filter(PropertyAggName.bucketSearch.name(),
          QueryBuilders.wildcardQuery(attributePath,"*" + bucketSearch + "*"));
      bucketSearchAggs.subAggregation(attributeAggs);
      nestedAggregation.subAggregation(bucketSearchAggs);
    }
    else {
      if(!property.isNumeric()) {
        attributeAggs.includeExclude(new IncludeExclude(null, ""));
      }
      nestedAggregation.subAggregation(attributeAggs);
    }
  }

  private String getAttributePath(String attributeId, Boolean isNumeric, String localeId, Boolean isTranslatable)
  {
    NestedPathBuilder attributePath = NestedPathBuilder.getPathForAttribute();
    String localeField = isTranslatable ? localeId : SearchBuilder.Fields.independent.name();
    attributePath.append(localeField);
    attributePath.appendAttributeIdForExact(attributeId, isNumeric);
    return attributePath.path();
  }

  private void aggsForAttribute(String localeId, IPropertyAggregationDTO propertyAggDTO, IPropertyDTO property,
      NestedAggregationBuilder nestedAggregation)
  {
    String attributePath = getAttributePath(property.getPropertyCode(), property.isNumeric(), localeId, propertyAggDTO.getIsTranslatable());
    aggForAttributes(propertyAggDTO, nestedAggregation, attributePath, property);
  }
}
