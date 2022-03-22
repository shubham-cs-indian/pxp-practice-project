package com.cs.core.elastic.builders;

import java.util.Arrays;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.cs.core.elastic.aggregation.RuleViolationAggregation;
import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.elastic.ibuilders.ISearchBuilder.Fields;
import com.cs.core.rdbms.entity.idto.ISearchDTO;

public class RuleViolationQueryBuilder extends QueryBuilder {
  
  protected RuleViolationQueryBuilder(ISearchDTO searchDTO, BoolQueryBuilder query)
  {
    super(searchDTO, query);
  }
  
  public IQueryBuilder ruleViolationFilters()
  {
    
    List<String> ruleViolationFilters = searchDTO.getRuleViolationFilters();
    if (ruleViolationFilters.isEmpty()) {
      return this;
    }
    
    BoolQueryBuilder ruleViolationQuery = new BoolQueryBuilder();
    query.filter(ruleViolationQuery);
    
    List<String> locales = Arrays.asList(searchDTO.getLocaleId(), RuleViolationAggregation.LANGUAGE_INDEPENDENT);
    
    if (ruleViolationFilters.contains("green")) {
      
      if (!ruleViolationFilters.contains(Fields.red.name())) {
        ruleViolationQuery.mustNot(QueryBuilders.termsQuery(Fields.red.name(), locales));
      }
      if (!ruleViolationFilters.contains(Fields.yellow.name())) {
        ruleViolationQuery.mustNot(QueryBuilders.termsQuery(Fields.yellow.name(), locales));
      }
      if (!ruleViolationFilters.contains(Fields.orange.name())) {
        ruleViolationQuery.mustNot(QueryBuilders.termsQuery(Fields.orange.name(), locales));
      }
    }
    
    else {
      
      if (ruleViolationFilters.contains(Fields.red.name())) {
        ruleViolationQuery.should(QueryBuilders.termsQuery(Fields.red.name(), locales));
      }
      if (ruleViolationFilters.contains(Fields.yellow.name())) {
        ruleViolationQuery.should(QueryBuilders.termsQuery(Fields.yellow.name(), locales));
      }
      if (ruleViolationFilters.contains(Fields.orange.name())) {
        ruleViolationQuery.should(QueryBuilders.termsQuery(Fields.orange.name(), locales));
      }
    }
    
    return this;
  }
  
}
