package com.cs.core.elastic.aggregation;

import com.cs.core.elastic.ibuilders.ISearchBuilder.Fields;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO.AggregationType;
import com.cs.core.rdbms.entity.idto.IClassifierAggregationDTO;
import org.elasticsearch.client.ml.job.results.Bucket;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassifierAggregation extends BaseAggregation {

  public enum ClassifierAggregationType {
    Class( AggregationType.byClass, Fields.classIds),
    Taxonomy( AggregationType.byTaxonomy, Fields.taxonomyIds);

    AggregationType type;
    Fields          field;

    ClassifierAggregationType( AggregationType type, Fields field)
    {
      this.type = type;
      this.field = field;
    }

    public static ClassifierAggregationType getClassifierAggregationType(AggregationType type)
    {
      return type.equals(AggregationType.byClass) ? Class : Taxonomy;
    }
  }

  @Override
  public AggregationBuilder aggregate(IAggregationRequestDTO request, String localeId)
  {
    IClassifierAggregationDTO classifierAggRequest = (IClassifierAggregationDTO) request;
    ClassifierAggregationType type = ClassifierAggregationType.getClassifierAggregationType(
        classifierAggRequest.getAggregationType());

    List<String> classifierIds = classifierAggRequest.getClassifierIds();
    if(classifierIds.isEmpty()) {
      return null;
    }
  
    TermsAggregationBuilder classifierAggregation = AggregationBuilders
        .terms(ClassifierAggName.classifierAgg.name())
        .field(type.field.name())
        .includeExclude(new IncludeExclude(classifierIds.toArray(new String[0]), new String[] {}))
        .size(classifierAggRequest.getSize());

    FilterAggregationBuilder classifierFilterAggregation = AggregationBuilders
        .filter(ClassifierAggName.classifierFilterAgg.name(),
            QueryBuilders.termsQuery(type.field.name(), classifierIds))
        .subAggregation(classifierAggregation);

    return classifierFilterAggregation;
  }

  @Override
  public Map<String, Long> parseResult(IAggregationRequestDTO request, Map<String, Aggregation> aggs)
  {

    ParsedFilter filterAggs = (ParsedFilter) aggs.get(ClassifierAggName.classifierFilterAgg.name());
    ParsedStringTerms classifierAggs = filterAggs.getAggregations().get(ClassifierAggName.classifierAgg.name());

    Map<String, Long> count = new HashMap<>();
    for (Terms.Bucket bucket : classifierAggs.getBuckets()) {
      count.put(bucket.getKey().toString(), bucket.getDocCount());
    }
    return count;
  }
}
